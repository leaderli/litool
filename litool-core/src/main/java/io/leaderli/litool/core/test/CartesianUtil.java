package io.leaderli.litool.core.test;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.PrimitiveEnum;
import io.leaderli.litool.core.type.ReflectUtil;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/8/19 8:10 AM
 */
public class CartesianUtil {


    private static <T> Object[] cartesian(Class<T> type, AnnotatedElement annotatedElement, CartesianContext context) {

        return ReflectUtil.findAnnotationsWithMark(annotatedElement, Valuable.class)
                .filter(an -> {

                    Class<? extends CartesianFunction<?, ?>> value = an.annotationType().getAnnotation(Valuable.class).value();
                    if (value == ObjectCartesian.class) {
                        return true;
                    }
                    Class<?> cartesianFunction2GenericType = ReflectUtil.getGenericInterfacesType(value, CartesianFunction.class, 1).get();
                    return cartesianFunction2GenericType == ClassUtil.primitiveToWrapper(type);
                })
                .first()
                .map(an -> {

                            if (an instanceof ObjectValues) {

                                return new CartesianObject<>(type, field -> CartesianUtil.cartesian(field, context)).cartesian().toArray();
                            }
                            return context.provideByValuable(an);
                        }

                )
                .or(cartesian(type))
                .get();


    }

    public static <T> Object[] cartesian(Parameter parameter, CartesianContext context) {
        return cartesian(parameter.getType(), parameter, context);
    }

    public static <T> Object[] cartesian(Field field, CartesianContext context) {
        return cartesian(field.getType(), field, context);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] cartesian(Class<T> cls) {

        PrimitiveEnum primitive = PrimitiveEnum.get(cls);
        Object def = primitive.def;
        Object arr = Array.newInstance(ClassUtil.primitiveToWrapper(cls), 1);
        Array.set(arr, 0, def);
        return (T[]) arr;
    }


    private static Object convertToType(Object item, Class<?> type) {

        if (ClassUtil._instanceof(item, type)) {
            return item;
        }
        if (item instanceof Double) {

            PrimitiveEnum primitiveEnum = PrimitiveEnum.get(type);
            if (PrimitiveEnum.isNumber(primitiveEnum)) {
                return ClassUtil.castNumber((Double) item, primitiveEnum);
            }
        }
        return null;

    }

    public static <T> Lira<T> cartesianByTemplate(Class<T> cls, Map<String, Object> template) {

        CartesianObject<T> cartesianObject = new CartesianObject<>(cls, field -> {
            Object val = template.get(field.getName());
            if (val == null) {
                return null;
            }

            Class<?> type = field.getType();
            Object[] result = null;
            if (val instanceof Iterable) {
                Object[] objects = Lira.of((Iterable<?>) val).map(item -> convertToType(item, type)).toArray();
                if (objects.length > 0) {
                    result = objects;
                }
            } else if (val.getClass().isArray()) {
                Object[] objects = Lira.of(ClassUtil.toArray(val)).map(item -> convertToType(item, type)).toArray();
                if (objects.length > 0) {
                    result = objects;
                }
            } else {
                Object single = convertToType(val, type);
                if (single != null) {
                    result = new Object[]{single};
                }
            }
            return result;
        });
        return cartesianObject.cartesian();


    }
}
