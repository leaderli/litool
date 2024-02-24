package io.leaderli.litool.test;

import io.leaderli.litool.core.type.*;
import io.leaderli.litool.test.cartesian.MockList;
import io.leaderli.litool.test.cartesian.MockMap;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * mock a bean, and will recursive set bean field with a default value.
 * <p>
 * if {@link #cache}  contains the {@link LiTypeToken}, just simple return the cache value
 * <ul>
 *     <li>if field is primitive, will set a {@link  PrimitiveEnum#zero_value}</li>
 *     <li>if field is {@link String} , set {@code ""}</li>
 *     <li>if field is array , set a zero length array</li>
 *     <li>if field is {@link  List}, set a empty {@link MockList}</li>
 *     <li>if field is {@link  Map}, set a empty {@link MockMap}</li>
 *     <li>otherwise will try to use {@link  MockBean} to mock a bean</li>
 *  </ul>
 *
 * @param <T> the type of mock bean
 */
public class MockBean<T> {

    private final LiTypeToken<T> typeToken;
    private final ConstructorConstructor constructorConstructor;
    private final Map<LiTypeToken<?>, Object> cache;

    private MockBean(LiTypeToken<T> typeToken, ConstructorConstructor constructorConstructor, Map<LiTypeToken<?>, Object> cache) {
        this.typeToken = typeToken;
        this.constructorConstructor = constructorConstructor;
        this.cache = cache;
    }


    public static <T> T mockBean(Class<T> type) {
        return create(type).build().create();
    }


    public static <T> MockBeanBuilder<T> create(Class<T> clazz) {
        return new MockBeanBuilder<>(LiTypeToken.of(clazz));
    }

    public static <T> MockBeanBuilder<T> create(Type type) {
        return new MockBeanBuilder<>(LiTypeToken.ofType(type));
    }

    public static <T> MockBeanBuilder<T> create(LiTypeToken<T> token) {
        return new MockBeanBuilder<>(LiTypeToken.ofType(token));
    }


    @SuppressWarnings("unchecked")
    public T create() {

        if (cache.containsKey(typeToken)) {
            return (T) cache.get(typeToken);
        }
        ObjectConstructor<T> constructor = constructorConstructor.get(typeToken);
        if (constructor != null) {
            return constructor.get();
        }

        Class<? super T> rawType = typeToken.getRawType();
        if (rawType.isArray()) {
            return (T) Array.newInstance(rawType.getComponentType(), 0);
        }


        T instance = null;
        if (!rawType.isInterface()) {
            instance = (T) ReflectUtil.newInstance(rawType).get();
        }
        if (instance == null) {
            // try to use a cache value that inherit from rawType
            for (Object value : cache.values()) {
                if (value != null && ClassUtil.isAssignableFromOrIsWrapper(rawType, value.getClass())) {
                    cache.put(typeToken, value);
                    return (T) value;
                }
            }
        }
        cache.put(typeToken, instance);
        if (instance != null) {
            populate(instance, typeToken);
        }
        return instance;

    }

    private void populate(Object instance, LiTypeToken<?> typeToken) {
        Class<?> rawType = typeToken.getRawType();
        if (rawType == Object.class) {
            return;
        }

        for (Field field : ReflectUtil.getFields(rawType)
                .filter(f -> !ModifierUtil.isStatic(f))
                .filter(f -> ReflectUtil.getFieldValue(instance, f).absent())) {
            Type genericType = field.getGenericType();

            Type targetType = TypeUtil.resolve(typeToken.getType(), genericType);
            Class<?> fieldType = TypeUtil.erase(targetType);
            Object fieldValue;
            Object zero_value = PrimitiveEnum.get(fieldType).zero_value;
            if (zero_value != null) {
                fieldValue = zero_value;
                ReflectUtil.setFieldValue(instance, field, fieldValue);
                continue;
            }

            if (fieldType.isArray()) {
                fieldValue = Array.newInstance(fieldType.getComponentType(), 0);
            } else {
                LiTypeToken<?> fieldTypeToken = LiTypeToken.of(fieldType);
                fieldValue = new MockBean<>(fieldTypeToken, constructorConstructor, cache).create();
            }
            ReflectUtil.setFieldValue(instance, field, fieldValue);
        }

        LiTypeToken<Object> superTypeToken = LiTypeToken.ofType(TypeUtil.resolve(typeToken.getType(), rawType.getGenericSuperclass()));
        populate(instance, superTypeToken);
    }

    public static class MockBeanBuilder<T> {
        private final LiTypeToken<T> typeToken;
        private final Map<LiTypeToken<?>, Object> cache = new HashMap<>();
        private LinkedHashMap<Type, InstanceCreator<?>> head;
        private LinkedHashMap<Type, InstanceCreator<?>> tail;

        private MockBeanBuilder(LiTypeToken<T> typeToken) {
            this.typeToken = typeToken;
        }


        public MockBeanBuilder<T> head(LinkedHashMap<Type, InstanceCreator<?>> head) {
            this.head = head;
            return this;
        }

        public MockBeanBuilder<T> tail(LinkedHashMap<Type, InstanceCreator<?>> tail) {
            this.tail = tail;
            return this;
        }

        public MockBean<T> build() {
            ConstructorConstructor constructorConstructor = new ConstructorConstructor(head, tail);
            for (PrimitiveEnum primitive : PrimitiveEnum.PRIMITIVES) {
                cache.put(LiTypeToken.of(primitive.primitive), primitive.zero_value);
                cache.put(LiTypeToken.of(primitive.wrapper), primitive.zero_value);
            }
            cache.put(LiTypeToken.of(String.class), "");
            return new MockBean<>(typeToken, constructorConstructor, cache);
        }
    }
}
