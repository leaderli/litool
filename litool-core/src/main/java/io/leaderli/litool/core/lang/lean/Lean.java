package io.leaderli.litool.core.lang.lean;

import io.leaderli.litool.core.lang.BeanPath;
import io.leaderli.litool.core.text.StringConvert;
import io.leaderli.litool.core.type.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author leaderli
 * @since 2022/9/24 9:39 AM
 */
public class Lean {
    private final List<TypeAdapterFactory> factories = new ArrayList<>();
    private final Map<LiTypeToken<?>, TypeAdapter<?>> typeTokenCache = new ConcurrentHashMap<>();

    public Lean() {

        factories.add(TypeAdapters.INTEGER_FACTORY);
        factories.add(TypeAdapters.STRING_FACTORY);

        factories.add(new TypeAdapterFactory() {
            @SuppressWarnings({"unchecked", "rawtypes"})
            @Override
            public <T> TypeAdapter<T> create(Lean lean, LiTypeToken<T> type) {
                if (!Collection.class.isAssignableFrom(type.getRawType())) {
                    return null;
                }
                Type type1 = type.getType();

                if (type1 instanceof ParameterizedType) {
                    type1 = ((ParameterizedType) type1).getActualTypeArguments()[0];
                }
                return new Adapter(getAdapter(LiTypeToken.of(type1)));

            }
        });

        factories.add(new TypeAdapterFactory() {
            @SuppressWarnings("unchecked")
            @Override
            public <T> TypeAdapter<T> create(Lean lean, LiTypeToken<T> type) {
                return obj -> (T) ReflectUtil.newInstance(type.getRawType())
                        .ifPresent(bean -> populate(bean, obj))
                        .get();
            }
        });
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public <T> TypeAdapter<T> getAdapter(LiTypeToken<T> type) {
        Objects.requireNonNull(type);
        TypeAdapter<?> cached = typeTokenCache.get(type);
        if (cached != null) {
            return (TypeAdapter<T>) cached;
        }

        synchronized (typeTokenCache) {

            for (TypeAdapterFactory factory : this.factories) {
                TypeAdapter<T> candidate = factory.create(this, type);
                if (candidate != null) {
                    typeTokenCache.put(type, candidate);
                    return candidate;
                }
            }
        }

        throw new IllegalArgumentException("Lean cannot handle " + type);


    }

    public void populate(Object bean, Object properties) {

        for (Field field : ReflectUtil.getFields(bean.getClass())) {
            BeanPath.parse(properties, field.getName())
                    .map(value -> parser(value, field.getGenericType()))
                    .ifPresent(v -> ReflectUtil.setFieldValue(bean, field, v));
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T> T parser(Object o, Type parser) {
        for (TypeAdapterFactory factory : factories) {
            LiTypeToken liTypeToken = LiTypeToken.of(parser);
            TypeAdapter<T> typeAdapter = factory.create(this, liTypeToken);
            if (typeAdapter != null) {
                return typeAdapter.read(o);
            }
        }

        return null;


    }

    public static void main(String[] args) {
        System.out.println(ClassScanner.getSubTypesOf("io", Iterable.class));
    }

    private static final class Adapter<E> implements TypeAdapter<Collection<E>> {
        private final TypeAdapter<E> elementTypeAdapter;

        private Adapter(TypeAdapter<E> elementTypeAdapter) {
            this.elementTypeAdapter = elementTypeAdapter;
        }

        @Override
        public Collection<E> read(Object obj) {

            //TODO  ConstructorConstructor
            Collection<E> collection = new ArrayList<>();
            if (obj instanceof Iterable) {

                ((Iterable<?>) obj).forEach(e -> collection.add(elementTypeAdapter.read(e)));
            }

            return collection;
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
