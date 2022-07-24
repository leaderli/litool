package io.leaderli.litool.dom.sax;

import io.leaderli.litool.core.lang.TupleMap;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StringConvert;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.ReflectUtil;

/**
 * @author leaderli
 * @since 2022/7/24
 * <p>
 * 默认的事件处理器，具体 SaxBean 可以重写相关事件，达到更精准的处理
 */
public interface SaxEventHandler {

    @SuppressWarnings("unchecked")
    default void start(StartEvent startEvent) {


        // 优先查找同名 SaxBean 成员
        ReflectUtil.getField(sax().getClass(), startEvent.name).ifPresent(field -> {

            Class<?> type = field.getType();
            ReflectUtil.newInstance(type)
                    .cast(SaxBean.class)
                    .ifPresent(newFieldBean -> startEvent.setNewSaxBean(SaxBeanAdapter.of(
                                    newFieldBean,
                                    () -> ReflectUtil.setFieldValue(sax(), field, newFieldBean))
                            )
                    );
        });

        // 若未查找到，则查找支持该标签的 SaxList 成员
        if (ClassUtil.isAssignableFromOrIsWrapper(IgnoreSaxBean.class, startEvent.getNewSaxBean().sax.getClass())) {

            Lira.of(sax().getClass().getFields())
                    .map(field -> ReflectUtil.getFieldValue(sax(), field).get())
                    .cast(SaxList.class)
                    .filter(saxList -> saxList.support().getValueByKey(startEvent.name))
                    .first()
                    .ifPresent(saxList -> {

                        TupleMap<String, Class<?>> support = saxList.support();

                        support.getValueByKey(startEvent.name)
                                .map(ReflectUtil::newInstance)
                                .map(Lino::get)
                                .cast(SaxBean.class)
                                .ifPresent(newFieldBean ->
                                        startEvent.setNewSaxBean(SaxBeanAdapter.of(
                                                newFieldBean,
                                                () -> saxList.add(newFieldBean)))
                                );

                    });

        }


    }

    SaxBean sax();

    default void father(SaxBean father) {
    }

    default void attribute(AttributeEvent attributeEvent) {
        // 使用 attribute 的值填充 field 的值
        ReflectUtil.getField(sax().getClass(), attributeEvent.name)
                .ifPresent(field -> {
                    Object fieldValue;
                    String value = attributeEvent.value;
                    if (StringConvert.support(field.getType())) {

                        fieldValue = StringConvert.parser(field.getType(), value).get();
                    } else {
                        fieldValue = ReflectUtil.newInstance(field.getType(), value).get();
                    }

                    ReflectUtil.setFieldValue(sax(), field, fieldValue);

                });
    }

    default void body(BodyEvent bodyEvent) {
        String value = bodyEvent.description();
        Lira.of(sax().getClass().getFields())
                .filter(field -> ClassUtil.isAssignableFromOrIsWrapper(SaxBody.class, field.getType()))
                .first()
                .ifPresent(field -> {
                            Object fieldValue = ReflectUtil.newInstance(field.getType(), value).get();
                            ReflectUtil.setFieldValue(sax(), field, fieldValue);
                        }
                );
    }

    default void end(EndEvent endEvent) {

        endEvent.getSaxBeanWrapper().run();
        // 校验是否有成员变量未初始化
        Lira.of(sax().getClass().getFields())
                .forEach(field ->
                        ReflectUtil.getFieldValue(sax(), field).assertNotNone(String.format("%s has no init", field))
                );

    }


}
