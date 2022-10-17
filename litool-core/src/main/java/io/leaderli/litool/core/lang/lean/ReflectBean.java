package io.leaderli.litool.core.lang.lean;

import io.leaderli.litool.core.type.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

import static io.leaderli.litool.core.util.ConsoleUtil.print;

public class ReflectBean<T> {

    public final Set<LiTypeToken<?>> circle_check;
    private final LiTypeToken<T> typeToken;
    private final ConstructorConstructor constructorConstructor;
    private final Map<LiTypeToken<?>, Object> cache;
    private final Lean lean = new Lean();

    private ReflectBean(LiTypeToken<T> typeToken, ConstructorConstructor constructorConstructor, Set<LiTypeToken<?>> circle_check, Map<LiTypeToken<?>, Object> cache) {
        this.typeToken = typeToken;
        this.constructorConstructor = constructorConstructor;
        this.circle_check = circle_check;
        this.cache = cache;
    }


//
//    public ReflectBean(LiTypeToken<T> typeToken) {
//        this(typeToken, new LinkedHashMap<>());
//    }
//
//    public ReflectBean(Class<T> cls) {
//        this(LiTypeToken.of(cls));
//    }

    public static <T> ReflectBean<T> instance(Class<T> cls) {

        LiTypeToken<T> token = LiTypeToken.of(cls);
        LinkedHashMap<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        instanceCreators.put(String.class, t -> "");
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        return new ReflectBean<>(token, constructorConstructor, new HashSet<>(), new HashMap<>());
    }

    @SuppressWarnings("unchecked")
    public T create() {

        Object caching = cache.get(typeToken);
        if (caching != null) {
            return lean.fromBean(caching, typeToken);
        }
        ObjectConstructor<T> constructor = constructorConstructor.get(typeToken);
        T instance = null;
        if (constructor != null) {
            return constructor.get();
        } else {

            Class<? super T> rawType = typeToken.getRawType();
            if (rawType.isArray()) {
                return (T) Array.newInstance(rawType.getComponentType(), 0);
            }

            if (!ModifierUtil.isAbstract(rawType) && !rawType.isInterface()) {
                instance = (T) ReflectUtil.newInstance(rawType).get();
            }
        }
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
//            performField(source, targetType, target, field);
            Class<?> fieldType = TypeUtil.erase(targetType);
            Object fieldValue = null;
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
                ObjectConstructor<Object> objectObjectConstructor = constructorConstructor.get(fieldTypeToken);
                if (objectObjectConstructor != null) {
                    fieldValue = objectObjectConstructor.get();
                } else if (circle_check.add(fieldTypeToken)) {
                    fieldValue = new ReflectBean<>(fieldTypeToken, constructorConstructor, new HashSet<>(circle_check), cache).create();
                }
            }
            print(instance, targetType, fieldValue);
            cache.put(LiTypeToken.of(targetType), fieldValue);
            ReflectUtil.setFieldValue(instance, field, fieldValue);
        }

        LiTypeToken<Object> superTypeToken = LiTypeToken.of(TypeUtil.resolve(typeToken.getType(), rawType.getGenericSuperclass()));
        print(superTypeToken);
        populate(instance, superTypeToken);


    }

}
