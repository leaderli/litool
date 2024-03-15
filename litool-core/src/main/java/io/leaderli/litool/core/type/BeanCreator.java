package io.leaderli.litool.core.type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

/**
 * mock a bean, and will recursive set bean field with a default value.
 * <p>
 * if {@link #cache}  contains the {@link LiTypeToken}, just simple return the cache value
 * <ul>
 *     <li>if field is primitive, will set a {@link  PrimitiveEnum#zero_value}</li>
 *     <li>if field is {@link String} , set {@code ""}</li>
 *     <li>if field is array , set a zero length array</li>
 *     <li>otherwise will try to use {@link  BeanCreator} to mock a bean</li>
 *  </ul>
 *
 * @param <T> the type of mock bean
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class BeanCreator<T> {

    private final LiTypeToken<T> typeToken;
    private final ConstructorConstructor constructorConstructor;
    private final Map<LiTypeToken<?>, Object> cache;

    private final List<FieldValueGetter<?>> fieldValueGetterList;

    private BeanCreator(LiTypeToken<T> typeToken, ConstructorConstructor constructorConstructor, Map<LiTypeToken<?>, Object> cache, List<FieldValueGetter<?>> fieldValueGetterList) {
        this.typeToken = typeToken;
        this.constructorConstructor = constructorConstructor;
        this.cache = cache;
        this.fieldValueGetterList = fieldValueGetterList;
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
        if (!ModifierUtil.isAbstract(rawType)) {
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
            ReflectUtil.setFieldValue(instance, field, createFieldValue(instance, field));
        }

        LiTypeToken<Object> superTypeToken = LiTypeToken.ofType(TypeUtil.resolve(typeToken.getType(), rawType.getGenericSuperclass()));
        populate(instance, superTypeToken);
    }

    private Object createFieldValue(Object instance, Field field) {

        Type genericType = field.getGenericType();
        Type targetType = TypeUtil.resolve(typeToken.getType(), genericType);
        Class<?> fieldType = TypeUtil.erase(targetType);

        for (FieldValueGetter valueGetter : fieldValueGetterList) {
            Object value = valueGetter.getValue(instance, field, fieldType);
            if (value != null && ClassUtil.isAssignableFromOrIsWrapper(fieldType, value.getClass())) {
                return value;
            }
        }
        Object zero_value = PrimitiveEnum.get(fieldType).zero_value;
        if (zero_value != null) {
            return zero_value;
        }
        if (fieldType.isArray()) {
            return Array.newInstance(fieldType.getComponentType(), 0);
        } else {
            LiTypeToken<?> fieldTypeToken = LiTypeToken.of(fieldType);
            return new BeanCreator<>(fieldTypeToken, constructorConstructor, cache, this.fieldValueGetterList).create();
        }
    }

    public static class MockBeanBuilder<T> {
        private final LiTypeToken<T> typeToken;
        private final Map<LiTypeToken<?>, Object> cache = new HashMap<>();
        private final LinkedHashMap<Type, InstanceCreator<?>> head = new LinkedHashMap<>();
        private final LinkedHashMap<Type, InstanceCreator<?>> tail = new LinkedHashMap<>();
        private final List<FieldValueGetter<?>> fieldValueGetterMap = new ArrayList<>();


        private MockBeanBuilder(LiTypeToken<T> typeToken) {
            this.typeToken = typeToken;
        }

        public <R> MockBeanBuilder<R> type(Class<R> clazz) {
            MockBeanBuilder<R> mockBeanBuilder = new MockBeanBuilder<>(LiTypeToken.of(clazz));
            mockBeanBuilder.cache.putAll(cache);
            mockBeanBuilder.head.putAll(head);
            mockBeanBuilder.tail.putAll(tail);
            mockBeanBuilder.fieldValueGetterMap.addAll(fieldValueGetterMap);
            return mockBeanBuilder;
        }

        public <R> MockBeanBuilder<T> head(Class<R> clazz, InstanceCreator<R> instanceCreator) {
            this.head.put(clazz, instanceCreator);
            return this;
        }

        public <R> MockBeanBuilder<T> tail(Class<R> clazz, InstanceCreator<R> instanceCreator) {
            this.tail.put(clazz, instanceCreator);
            return this;
        }

        public <R> MockBeanBuilder<T> cache(Class<R> clazz, R r) {
            this.cache.put(LiTypeToken.of(clazz), r);
            return this;
        }

        public MockBeanBuilder<T> annotated(Class<? extends Annotation> annotated, FieldValueGetter fieldValueGetter) {
            this.fieldValueGetterMap.add((bean, field, fieldType) -> {
                if (field.isAnnotationPresent(annotated)) {
                    return fieldValueGetter.getValue(bean, field, fieldType);
                }
                return null;
            });
            return this;
        }

        public MockBeanBuilder<T> populate(String name, FieldValueGetter fieldValueGetter) {
            this.fieldValueGetterMap.add((bean, field, fieldType) -> {
                if (name.equals(field.getName())) {
                    return fieldValueGetter.getValue(bean, field, fieldType);
                }
                return null;
            });
            return this;
        }

        public MockBeanBuilder<T> populate(String name, Object value) {
            this.fieldValueGetterMap.add((bean, field, fieldType) -> {
                if (name.equals(field.getName())) {
                    return value;
                }
                return null;
            });
            return this;
        }

        public <R> MockBeanBuilder<T> populate(Class<R> type, R value) {
            this.fieldValueGetterMap.add((bean, field, fieldType) -> {
                if (ClassUtil.isInstanceof(value, field.getType())) {
                    return value;
                }
                return null;
            });
            return this;
        }

        public <R> MockBeanBuilder<T> populate(FieldValueGetter fieldValueGetter) {
            this.fieldValueGetterMap.add(fieldValueGetter);
            return this;
        }

        public BeanCreator<T> build() {
            ConstructorConstructor constructorConstructor = new ConstructorConstructor(head, tail);
            for (PrimitiveEnum primitive : PrimitiveEnum.PRIMITIVES) {
                cache.put(LiTypeToken.of(primitive.primitive), primitive.zero_value);
                cache.put(LiTypeToken.of(primitive.wrapper), primitive.zero_value);
            }
            cache.put(LiTypeToken.of(String.class), "");
            return new BeanCreator<>(typeToken, constructorConstructor, cache, fieldValueGetterMap);
        }
    }
}
