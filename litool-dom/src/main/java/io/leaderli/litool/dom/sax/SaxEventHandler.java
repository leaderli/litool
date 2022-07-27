package io.leaderli.litool.dom.sax;

import io.leaderli.litool.core.function.ThrowableRunner;
import io.leaderli.litool.core.lang.TupleMap;
import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
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

    @SuppressWarnings({"unchecked", "rawtypes"})
    default void start(StartEvent startEvent) {


        // 查找set（优先级更高) 或 add 方法 填充属性

        String methodName = startEvent.name;

        MethodScanner methodScanner = MethodScanner.of(getClass(), false, method ->

                StringUtils.equalsAnyIgnoreCase(method.getName(), "set" + methodName, "add" + methodName)
                        && method.getParameterCount() == 1
                        && ClassUtil.isAssignableFromOrIsWrapper(SaxBean.class, method.getParameterTypes()[0]));

        Lino<LiTuple2<Method, SaxBean>> simpleField = methodScanner.scan()
                .sort((m1, m2) -> m2.getName().compareTo(m1.getName()))
                .first()
                .tuple(m -> ReflectUtil.newInstance(m.getParameterTypes()[0]).cast(SaxBean.class).get())
                .ifPresent(tuple -> {
                    Method method = tuple._1;
                    SaxBean sax = tuple._2;
                    ThrowableRunner call = () -> method.invoke(this, sax);
                    SaxBeanAdapter saxBeanAdapter = SaxBeanAdapter.of(sax, call);
                    startEvent.setNewSaxBean(saxBeanAdapter);
                });
    }





    default void attribute(AttributeEvent attributeEvent) {
        // 使用 attribute 的值填充 field 的值


        Lino<Field> lino = ReflectUtil.getField(this.getClass(), attributeEvent.name);
        if (lino.present()) {
            Field field = lino.get();

            Lino<?> fieldValue;
            String value = attributeEvent.value;

            if (StringConvert.support(field.getType())) {

                fieldValue = StringConvert.parser(field.getType(), value);
            } else {
                fieldValue = ReflectUtil.newInstance(field.getType(), value);
            }
            fieldValue.ifPresent(v -> ReflectUtil.setFieldValue(this, field, v));


        }
    }

    default void body(BodyEvent bodyEvent) {
        String value = bodyEvent.description();
        Lira.of(this.getClass().getFields())
                .filter(field -> ClassUtil.isAssignableFromOrIsWrapper(SaxBody.class, field.getType()))
                .first()
                .ifPresent(field -> {
                            Object fieldValue = ReflectUtil.newInstance(field.getType(), value).get();
                            ReflectUtil.setFieldValue(this, field, fieldValue);
                        }
                );
    }

    default void end(EndEvent endEvent) {

        endEvent.getSaxBeanWrapper().run();
        // 校验是否有成员变量未初始化
        for (Field field : getClass().getFields()) {
            ReflectUtil.getFieldValue(this, field).assertNotNone(String.format("%s has no init", field));

        }
    }


}
