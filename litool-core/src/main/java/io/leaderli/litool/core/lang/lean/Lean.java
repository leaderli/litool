package io.leaderli.litool.core.lang.lean;

import io.leaderli.litool.core.lang.BeanPath;
import io.leaderli.litool.core.text.StringConvert;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.LiTypeToken;
import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.core.type.TypeUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author leaderli
 * @since 2022/9/24 9:39 AM
 */
public class Lean {

    private final List<TypeAdapterFactory> factories = new ArrayList<>();
    private static final LiTypeToken<?> NULL_KEY_SURROGATE = LiTypeToken.get(Object.class);
    private final Map<LiTypeToken<?>, TypeAdapter<?>> typeTokenCache = new ConcurrentHashMap<>();

    public Lean() {

        factories.add(TypeAdapters.INTEGER_FACTORY);
        factories.add(TypeAdapters.STRING_FACTORY);

//        factories.add(TypeAdapters.INTEGER_FACTORY);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public <T> TypeAdapter<T> getAdapter(LiTypeToken<T> type) {
        TypeAdapter<?> cached = typeTokenCache.get(type == null || type.getRawType() == null ? NULL_KEY_SURROGATE : type);
        if (cached != null) {
            return (TypeAdapter<T>) cached;
        }
        return obj -> (T) ReflectUtil.newInstance(type.getRawType())
                .ifPresent(bean -> populate(bean, obj))
                .get();

    }

    public <T> T parser(Object o, Class<T> parser) {
        for (TypeAdapterFactory factory : factories) {
            TypeAdapter<T> typeAdapter = factory.create(this, LiTypeToken.get(parser));
            if (typeAdapter != null) {
                return typeAdapter.read(o);
            }
        }

        return null;


    }

    public void populate(Object bean, Object properties) {

        for (Field field : ReflectUtil.getFields(bean.getClass())) {
            BeanPath.parse(properties, field.getName())
                    .map(value -> parser(value, field.getType()))
                    .ifPresent(v -> ReflectUtil.setFieldValue(bean, field, v));
        }
    }

    public void populate(Object bean, Field field, Object value) {

        if (value != null) {
            Class<?> type = ClassUtil.getType(field);
            if (ClassUtil.isAssignableFromOrIsWrapper(type, ClassUtil.getClass(value))) {
                System.out.println(TypeUtil.resolve(type, ClassUtil.getClass(value)));

                ReflectUtil.setFieldValue(bean, field, value);
            } else if (type == CharSequence.class || type == String.class) {
                ReflectUtil.setFieldValue(bean, field, value.toString());
            } else if (value instanceof String) {
                StringConvert.parser(type, (String) value)
                        .ifPresent(v -> ReflectUtil.setFieldValue(bean, field, v));
                ReflectUtil.newInstance(type)
                        .filter(fv -> ReflectUtil.setFieldValue(bean, field, fv))
                        .ifPresent(fv -> populate(fv, value));
            }


        }

    }

}
