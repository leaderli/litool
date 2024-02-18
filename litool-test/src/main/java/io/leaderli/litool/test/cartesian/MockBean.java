package io.leaderli.litool.test.cartesian;

import io.leaderli.litool.core.type.*;

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

        for (PrimitiveEnum primitive : PrimitiveEnum.PRIMITIVES) {
            cache.put(LiTypeToken.of(primitive.primitive), primitive.zero_value);
            cache.put(LiTypeToken.of(primitive.wrapper), primitive.zero_value);
        }
    }

    public static <T> MockBean<T> instance(Class<T> cls) {
        return instance(cls, new LinkedHashMap<>());
    }

    public static <T> MockBean<T> instance(Type type, LinkedHashMap<Type, InstanceCreator<?>> instanceCreators) {
        LiTypeToken<T> token = LiTypeToken.ofType(type);
        return instance(token, instanceCreators);
    }

    public static <T> MockBean<T> instance(LiTypeToken<T> token, LinkedHashMap<Type, InstanceCreator<?>> head, LinkedHashMap<Type, InstanceCreator<?>> tail) {

        tail.put(String.class, t -> "");
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(head, tail);
        return new MockBean<>(token, constructorConstructor, new HashMap<>());
    }

    public static <T> MockBean<T> instance(LiTypeToken<T> token, LinkedHashMap<Type, InstanceCreator<?>> tail) {

        LinkedHashMap<Type, InstanceCreator<?>> head = new LinkedHashMap<>();
        head.put(MockMap.class, t -> new MockMap<>());
        head.put(MockList.class, t -> new MockList<>());
        return instance(token, head, tail);
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
     */
    public static Object mockBean(Type type) {

        return instance(type, LiMock.instanceCreators).create();
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
                LiTypeToken<?> fieldTypeToken = LiTypeToken.of(fieldType);
                fieldValue = new MockBean<>(fieldTypeToken, constructorConstructor, cache).create();
            }
            ReflectUtil.setFieldValue(instance, field, fieldValue);
        }

        LiTypeToken<Object> superTypeToken = LiTypeToken.ofType(TypeUtil.resolve(typeToken.getType(), rawType.getGenericSuperclass()));
        populate(instance, superTypeToken);
    }

}
