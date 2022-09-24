package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.text.StringConvert;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.core.type.TypeUtil;

import java.lang.reflect.Field;

import static io.leaderli.litool.core.util.ConsoleUtil.print;

/**
 * @author leaderli
 * @since 2022/9/24 9:39 AM
 */
public class BeanUtil {


    public static <T> T parser(Object o, Class<T> parser) {

        T instance = ReflectUtil.newInstance(parser).get();
        populate(instance, o);
        return instance;
    }

    public static void populate(Object bean, Object properties) {

        for (Field field : ReflectUtil.getFields(bean.getClass())) {
            BeanPath.parse(properties, field.getName())
                    .ifPresent(value -> {
                        print(field.getName(), value);
                        populate(bean, field, value);
                    });
        }
    }

    public static void populate(Object bean, Field field, Object value) {

        if (value != null) {
            Class<?> type = ClassUtil.getType(field);
            if (ClassUtil.isAssignableFromOrIsWrapper(type, ClassUtil.getClass(value))) {
                System.out.println(TypeUtil.resolve(type, ClassUtil.getClass(value)));

                ReflectUtil.setFieldValue(bean, field, value);
            } else if (type == CharSequence.class || type == String.class) {
                ReflectUtil.setFieldValue(bean, field, value.toString());
            } else if (value instanceof String) {
                StringConvert.parser(type, (String) value).ifPresent(v -> ReflectUtil.setFieldValue(bean, field, v));
            } else {
                ReflectUtil.newInstance(type).filter(fv -> ReflectUtil.setFieldValue(bean, field, fv)).ifPresent(fv -> populate(fv, value));
            }

        }

    }

}
