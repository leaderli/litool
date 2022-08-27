package io.leaderli.litool.dom.sax;

import io.leaderli.litool.core.exception.ExceptionUtil;
import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.text.StringConvert;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.MethodScanner;
import io.leaderli.litool.core.type.ReflectUtil;
import org.xml.sax.Locator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author leaderli
 * @since 2022/7/24
 * <p>
 * 默认的事件处理器，具体 SaxBean 可以重写相关事件，达到更精准的处理
 */
public interface SaxEventHandler {

static void addErrorMsgs(List<String> parseErrorMsgs, boolean success, String error) {
    if (!success) {
        parseErrorMsgs.add(error);
    }
}

default void start(StartEvent startEvent) {


    // 查找set（优先级更高) 或 add 方法 填充属性
    // set 一般用于设置SaxBean， add 一般用于添加 SaxList

    String tag = startEvent.name;

    MethodScanner methodScanner = MethodScanner.of(getClass(), false, method ->

            StringUtils.equalsAnyIgnoreCase(method.getName(), "set" + tag, "add" + tag)
                    && method.getParameterCount() == 1
                    && ClassUtil.isAssignableFromOrIsWrapper(SaxBean.class, method.getParameterTypes()[0]));


    Lino<Method> find = methodScanner.scan()
            .sorted((m1, m2) -> m2.getName().compareTo(m1.getName()))
            .first();

    if (find.present()) {
        Method method = find.get();
        // 使用 set 注入属性应当是唯一的
        if (method.getName().startsWith("set")) {
            methodScanner = MethodScanner.of(getClass(), false, get ->
                    StringUtils.equalsAnyIgnoreCase(get.getName(), "get" + tag)
                            && get.getParameterCount() == 0
                            && ClassUtil.isAssignableFromOrIsWrapper(SaxBean.class, get.getReturnType()));
            methodScanner.scan().first()
                    .ifPresent(get -> LiAssertUtil.assertTrue(ReflectUtil.getMethodValue(get, this).absent(),
                            String.format("%s:%s already inited", getClass().getSimpleName(), tag)));


        }
        // saxBean 都有一个 包含 tag 的构造器
        ReflectUtil.newInstance(method.getParameterTypes()[0]).cast(SaxBean.class).ifPresent(sax -> {
            SaxBeanAdapter saxBeanAdapter = SaxBeanAdapter.of(sax);
            // 成员变量在执行到 end 时可以确保已经加载好，此时通过回调函数再注入到实例中
            saxBeanAdapter.addCallback(() -> {

                try {
                    method.invoke(this, sax);
                } catch (Throwable throwable) {

                    Throwable cause = ExceptionUtil.getCause(throwable);
                    Locator locator = startEvent.locator;
                    saxBeanAdapter.getParseErrorMsgs().add(String.format("%s at line:%d column:%d",
                            cause.getMessage(), locator.getLineNumber(), locator.getColumnNumber()));
                }
            });
            startEvent.setNewSaxBean(saxBeanAdapter);
        });

    }

}


default void attribute(AttributeEvent attributeEvent) {
    // 使用 attribute 的值填充 field 的值

    MethodScanner methodScanner = MethodScanner.of(getClass(), false, method ->

            StringUtils.equalsAnyIgnoreCase(method.getName(), "set" + attributeEvent.name)
                    && method.getParameterCount() == 1);

    methodScanner.scan().first().ifPresent(method -> {

        Class<?> parameterType = method.getParameterTypes()[0];
        String value = attributeEvent.value;
        Lino<?> fieldValue;

        // 原始类型直接转换
        if (StringConvert.support(parameterType)) {

            fieldValue = StringConvert.parser(parameterType, value);
        } else {
            fieldValue = complexField(parameterType, value);
        }

        fieldValue.ifThrowablePresent(val -> method.invoke(this, val));
    });

}


/**
 * @param parameterType 参数类型
 * @param value         参数的字符串值
 * @return 复杂类型， 默认使用 String 参数的构造器，可以重写该方法
 */
default Lino<?> complexField(Class<?> parameterType, String value) {
    return ReflectUtil.newInstance(parameterType, value);
}

default void body(BodyEvent bodyEvent) {

}

default void end(EndEvent endEvent) {

    // 校验是否有成员变量未初始化
    for (Field field : ReflectUtil.getFields(getClass())) {
        ReflectUtil.getFieldValue(this, field).assertNotNone(String.format("%s has no init", field));
    }

    end_check(endEvent.getSaxBeanWrapper().getParseErrorMsgs());
    // 最后向上级元素注入
    endEvent.getSaxBeanWrapper().run();

}

default void end_check(List<String> parseErrorMsgs) {

}


}
