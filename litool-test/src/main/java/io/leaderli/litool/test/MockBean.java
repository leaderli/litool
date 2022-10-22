package io.leaderli.litool.test;

import io.leaderli.litool.core.type.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MockBean<T> {

    private final LiTypeToken<T> typeToken;
    private final ConstructorConstructor constructorConstructor;
    private final Map<LiTypeToken<?>, Object> cache;

    private MockBean(LiTypeToken<T> typeToken, ConstructorConstructor constructorConstructor, Map<LiTypeToken<?>, Object> cache) {
        this.typeToken = typeToken;
        this.constructorConstructor = constructorConstructor;
        this.cache = cache;

        for (PrimitiveEnum primitive : PrimitiveEnum.PRIMITIVES) {
            cache.put(LiTypeToken.of(primitive.primitive), primitive.zero_value);
            cache.put(LiTypeToken.of(primitive.wrapper), primitive.zero_value);
        }
    }

    public static <T> MockBean<T> instance(Class<T> cls) {
        return instance(cls, new LinkedHashMap<>());
    }

    public static <T> MockBean<T> instance(Type type, LinkedHashMap<Type, InstanceCreator<?>> instanceCreators) {
        LiTypeToken<T> token = LiTypeToken.of(type);
        return instance(token, instanceCreators);
    }

    public static <T> MockBean<T> instance(LiTypeToken<T> token, LinkedHashMap<Type, InstanceCreator<?>> instanceCreators) {

        instanceCreators.put(String.class, t -> "");
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        return new MockBean<>(token, constructorConstructor, new HashMap<>());
    }

    public static <T> MockBean<T> instance(Type type) {
        return instance(type, new LinkedHashMap<>());
    }

    public static <T> MockBean<T> instance(LiTypeToken<T> token) {
        return instance(token, new LinkedHashMap<>());
    }

    /**
     * return a instance of type, if the instance class is same as type  invoke pojo set get
     *
     * @param type the type
     * @return a instanceof type
     * @see MockBean#create()
     * @see LiMock#runGetSet(Type, Object)
     */
    public static Object mockBean(Type type) {

        Object obj = instance(type, LiMock.instanceCreators).create();

        LiMock.runGetSet(type, obj);

        return obj;
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


        T instance = (T) ReflectUtil.newInstance(rawType).get();
        if (instance == null) {
            for (Object value : cache.values()) {
                if (value != null && ClassUtil.isAssignableFromOrIsWrapper(rawType, value.getClass())) {
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

        for (Field field : ReflectUtil.getFields(rawType)) {
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
                LiTypeToken<Object> fieldTypeToken = LiTypeToken.of(fieldType);
                fieldValue = new MockBean<>(fieldTypeToken, constructorConstructor, cache).create();
            }
            ReflectUtil.setFieldValue(instance, field, fieldValue);
        }

        LiTypeToken<Object> superTypeToken = LiTypeToken.of(TypeUtil.resolve(typeToken.getType(), rawType.getGenericSuperclass()));
        populate(instance, superTypeToken);
    }

}
