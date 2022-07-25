package io.leaderli.litool.dom.sax;

import io.leaderli.litool.core.function.ThrowableRunner;
import io.leaderli.litool.core.lang.TupleMap;
import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StringConvert;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.ReflectUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author leaderli
 * @since 2022/7/24
 * <p>
 * 默认的事件处理器，具体 SaxBean 可以重写相关事件，达到更精准的处理
 */
public interface SaxEventHandler {

    @SuppressWarnings({"unchecked", "rawtypes"})
    default void start(StartEvent startEvent) {


        // 优先查找同名 SaxBean 成员
        //TODO 查找set方法

        Lino<LiTuple2<Method, SaxBean>> simpleField = Lira.of(getClass().getMethods())
                .filter(m -> m.getName().equalsIgnoreCase("set" + startEvent.name) && Modifier.isPublic(m.getModifiers()))
                .filter(m -> m.getParameterCount() == 1 && ClassUtil.isAssignableFromOrIsWrapper(SaxBean.class, m.getParameterTypes()[0]))
                .first()
                .tuple(m -> ReflectUtil.newInstance(m.getParameterTypes()[0]).cast(SaxBean.class).get())
                .ifPresent(tuple -> {
                    Method method = tuple._1;
                    SaxBean sax = tuple._2;
                    ThrowableRunner call = () -> method.invoke(this, sax);
                    SaxBeanAdapter saxBeanAdapter = SaxBeanAdapter.of(sax, call);
                    startEvent.setNewSaxBean(saxBeanAdapter);
                });

        // 若未查找到，则查找支持该标签的 SaxList 成员
        if (simpleField.absent()) {

            Lira.of(this.getClass().getFields())
                    .map(field -> ReflectUtil.getFieldValue(this, field).get())
                    .cast(SaxList.class)
                    .filter(saxList -> saxList.support().getValueByKey(startEvent.name))
                    .first()
                    .tuple(saxList -> toSupportTuple(startEvent, saxList))
                    .ifPresent(tuple -> {
                        SaxList saxList = tuple._1;
                        SaxBean sax = tuple._2;
                        startEvent.setNewSaxBean(SaxBeanAdapter.of(sax, () -> saxList.add(sax)));

                    });

        }


    }


    static SaxBean toSupportTuple(StartEvent startEvent, SaxList<?> saxList) {

        TupleMap<String, ? extends Class<?>> support = saxList.support();

        return support.getValueByKey(startEvent.name)
                .map(ReflectUtil::newInstance)
                .map(Lino::get)
                .cast(SaxBean.class)
                .get();
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
        Lira.of(this.getClass().getFields())
                .forEach(field ->
                        ReflectUtil.getFieldValue(this, field).assertNotNone(String.format("%s has no init", field))
                );

    }


}
