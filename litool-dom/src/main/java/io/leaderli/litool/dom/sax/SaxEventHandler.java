package io.leaderli.litool.dom.sax;

import io.leaderli.litool.core.function.ThrowableRunner;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.text.StringConvert;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.MethodScanner;
import io.leaderli.litool.core.type.ReflectUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author leaderli
 * @since 2022/7/24
 * <p>
 * 默认的事件处理器，具体 SaxBean 可以重写相关事件，达到更精准的处理
 */
public interface SaxEventHandler {

    default void start(StartEvent startEvent) {


        // 查找set（优先级更高) 或 add 方法 填充属性
        // set 一般用于设置SaxBean， add 一般用于添加 SaxList

        String methodName = startEvent.name;

        MethodScanner methodScanner = MethodScanner.of(getClass(), false, method ->

                StringUtils.equalsAnyIgnoreCase(method.getName(), "set" + methodName, "add" + methodName)
                        && method.getParameterCount() == 1
                        && ClassUtil.isAssignableFromOrIsWrapper(SaxBean.class, method.getParameterTypes()[0]));


        Lino<Method> find = methodScanner.scan()
                .sort((m1, m2) -> m2.getName().compareTo(m1.getName()))
                .first();

        if (find.present()) {
            Method method = find.get();
            ReflectUtil.newInstance(method.getParameterTypes()[0]).cast(SaxBean.class).ifPresent(sax -> {
                // 成员变量在执行到 end 时可以确保已经加载好，此时通过回调函数再注入到实例中
                ThrowableRunner call = () -> method.invoke(this, sax);
                SaxBeanAdapter saxBeanAdapter = SaxBeanAdapter.of(sax, call);
                startEvent.setNewSaxBean(saxBeanAdapter);
            });

        }

    }


    default void attribute(AttributeEvent attributeEvent) {
        // 使用 attribute 的值填充 field 的值

        MethodScanner methodScanner = MethodScanner.of(getClass(), false, method ->

                StringUtils.equalsAnyIgnoreCase(method.getName(), "set" + attributeEvent.name)
                        && method.getParameterCount() == 1
                        && ClassUtil.isAssignableFromOrIsWrapper(SaxBean.class, method.getParameterTypes()[0]));

        Lino<Field> lino = ReflectUtil.getField(this.getClass(), attributeEvent.name);
        if (lino.present()) {
            Field field = lino.get();

            Lino<?> fieldValue;
            String value = attributeEvent.value;

            // 原始类型直接转换
            if (StringConvert.support(field.getType())) {

                fieldValue = StringConvert.parser(field.getType(), value);
            } else {
                //复杂类型，默认为一个使用 String 参数的构造器
                fieldValue = ReflectUtil.newInstance(field.getType(), value);
            }
            fieldValue.ifPresent(v -> ReflectUtil.setFieldValue(this, field, v));


        }
    }

    default void body(BodyEvent bodyEvent) {

    }

    default void end(EndEvent endEvent) {

        endEvent.getSaxBeanWrapper().run();
        // 校验是否有成员变量未初始化
        for (Field field : getClass().getFields()) {
            ReflectUtil.getFieldValue(this, field).assertNotNone(String.format("%s has no init", field));

        }
    }


}
