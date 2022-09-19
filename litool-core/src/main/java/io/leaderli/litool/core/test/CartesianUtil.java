package io.leaderli.litool.core.test;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.PrimitiveEnum;
import io.leaderli.litool.core.type.ReflectUtil;

import java.lang.annotation.Annotation;
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

        return ReflectUtil.findAnnotationsWithMark(annotatedElement, Valuable.class).filter(an -> {
            Class<? extends CartesianFunction<?, ?>> value = an.annotationType().getAnnotation(Valuable.class).value();
            if (value == ObjectCartesian.class) {
                return true;
            }
            Class<?> cartesianFunction2GenericType = ReflectUtil.getDeclareTypeAt(value,
                    CartesianFunction.class, 1).get();
            return cartesianFunction2GenericType == ClassUtil.primitiveToWrapper(type);
        }).first().map(an -> {

                    if (an instanceof ObjectValues) {

                        return new CartesianObject<>(type, field -> CartesianUtil.cartesian(field, context)).cartesian().toArray();
                    }
                    return context.provideByValuable(an);
                }

        ).or(cartesian_single_def(type)).get();


    }

    /**
     * Return the potential values of parameter
     *
     * @param parameter the parameter
     * @param context   the context may be used for {@link  CartesianFunction#apply(Annotation, CartesianContext)}
     * @return the potential values of parameter
     */
    public static Object[] cartesian(Parameter parameter, CartesianContext context) {
        return cartesian(parameter.getType(), parameter, context);
    }

    /**
     * Return the potential values of field
     *
     * @param field   the field
     * @param context the context may be used for {@link  CartesianFunction#apply(Annotation, CartesianContext)}
     * @return the potential values of field
     */
    public static Object[] cartesian(Field field, CartesianContext context) {
        return cartesian(field.getType(), field, context);
    }

    /**
     * Return a one element arr with cls def value
     *
     * @param cls a class
     * @param <T> the type parameter of class
     * @return a one element arr with cls def value
     * @throws NumberFormatException if {@code  cls == null}
     * @see PrimitiveEnum#zero_value
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] cartesian_single_def(Class<T> cls) {

        PrimitiveEnum primitive = PrimitiveEnum.get(cls);
        Object def = primitive.zero_value;
        Object arr = Array.newInstance(ClassUtil.primitiveToWrapper(cls), 1);
        Array.set(arr, 0, def);
        return (T[]) arr;
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
                Object[] objects = Lira.of((Iterable<?>) val).map(item -> convertByType(item, type)).toArray();
                if (objects.length > 0) {
                    result = objects;
                }
            } else if (val.getClass().isArray()) {
                Object[] objects = Lira.of(ClassUtil.toArray(val)).map(item -> convertByType(item, type)).toArray();
                if (objects.length > 0) {
                    result = objects;
                }
            } else {
                Object single = convertByType(val, type);
                if (single != null) {
                    result = new Object[]{single};
                }
            }
            return result;
        });
        return cartesianObject.cartesian();


    }

    /**
     * Return casted obj, if instance's class type is equals, or sub
     * of the type, just {@code  cast}. if instance is Double, return
     * {@link  ClassUtil#castDouble(Double, PrimitiveEnum)}. otherwise
     * return null
     *
     * @param instance the obj to be cast
     * @param type     casted class type
     * @return casted obj
     */
    private static Object convertByType(Object instance, Class<?> type) {

        if (ClassUtil._instanceof(instance, type)) {
            return instance;
        }
        if (instance instanceof Double) {// it's specific used to json number type

            PrimitiveEnum primitiveEnum = PrimitiveEnum.get(type);
            if (PrimitiveEnum.isNumber(primitiveEnum)) {
                return ClassUtil.castDouble((Double) instance, primitiveEnum);
            }
        }
        return null;

    }
}
