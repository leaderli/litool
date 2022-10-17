package io.leaderli.litool.core.lang.lean;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.type.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.function.Supplier;

import static io.leaderli.litool.core.util.ConsoleUtil.print;

public class ReflectBean<T> {

    public final Set<LiTypeToken<?>> circle_check;
    private final LiTypeToken<T> typeToken;
    private final ConstructorConstructor constructorConstructor;

    private ReflectBean(LiTypeToken<T> typeToken, ConstructorConstructor constructorConstructor, Set<LiTypeToken<?>> circle_check) {
        this.typeToken = typeToken;
        this.constructorConstructor = constructorConstructor;
        this.circle_check = circle_check;
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
        return new ReflectBean<>(token, constructorConstructor, new HashSet<>());
    }

    @SuppressWarnings("unchecked")
    public T create() {

        T instance = (T) ReflectUtil.newInstance(typeToken.getRawType()).get();
        populate(instance, typeToken);
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
            } else if (fieldType.isArray()) {
                fieldValue = Array.newInstance(fieldType.getComponentType(), 0);
            } else {
                LiTypeToken<Object> fieldTypeToken = LiTypeToken.of(fieldType);
                fieldValue = Lino.of(constructorConstructor.get(fieldTypeToken)).map(Supplier::get).get(() -> {
                    if (circle_check.add(fieldTypeToken)) {
                        return new ReflectBean<>(fieldTypeToken, constructorConstructor, circle_check).create();
                    }
                    return null;
                });
            }
            ReflectUtil.setFieldValue(instance, field, fieldValue);
            print(field.getName(), fieldValue);
        }

        LiTypeToken<Object> superTypeToken = LiTypeToken.of(TypeUtil.resolve(typeToken.getType(), rawType.getGenericSuperclass()));
        print(superTypeToken);
        populate(instance, superTypeToken);


    }

}
