package io.leaderli.litool.core.type;

import io.leaderli.litool.core.meta.Lino;

import java.lang.annotation.Repeatable;
import java.lang.reflect.Method;

/**
 * @author leaderli
 * @since 2022/6/26 7:00 AM
 */
public class MethodUtil {

    /**
     * Return the method of lookup obj which have same signature with reference method
     *
     * @param lookup    the look up obj
     * @param reference the method  will use to be compare
     * @return the method of lookup obj which have same signature with reference method
     * @see #getSameSignatureMethod(Class, Method)
     */
    public static Lino<Method> getSameSignatureMethod(Object lookup, Method reference) {

        if (lookup == null) {
            return Lino.none();
        }
        return getSameSignatureMethod(lookup.getClass(), reference);
    }

    /**
     * Return the method of lookup class which have same signature with reference method
     *
     * @param lookup    the look up  class
     * @param reference the method  will use to be compare
     * @return the method of lookup class which have same signature with reference method
     * @see #getSameSignatureMethod(Class, Method)
     */
    public static Lino<Method> getSameSignatureMethod(Class<?> lookup, Method reference) {


        if (lookup == null || reference == null) {
            return Lino.none();
        }
        MethodSignature compare = MethodSignature.non_strict(reference);
        return ReflectUtil.getMethods(lookup)
                .filter(compare::equals)
                .first();
    }


    /**
     * Return the same signature method of lookup class
     *
     * @param lookup the lookup class
     * @return the same signature method of lookup class
     */
    public static Lino<Method> findMethod(Class<?> lookup, MethodSignature signature) {

        MethodScanner methodScanner = new MethodScanner(lookup, true, signature::equals);
        return methodScanner.scan().first();
    }


    /**
     * Return method is not belong to {@link Object}
     *
     * @param method a method
     * @return method is not belong to {@link Object}
     * @see #belongsTo(Method, Class)
     */
    public static boolean notObjectMethod(Method method) {
        return !belongsTo(method, Object.class);
    }

    /**
     * Return method is belong to {@link Object}
     *
     * @param method a method
     * @return method is belong to {@link Object}
     */
    public static boolean belongsTo(Method method, Class<?> cls) {
        return method.getDeclaringClass() == cls;
    }


    /**
     * Return method is  {@link  Repeatable#value()} class's value method
     *
     * @param method a method
     * @return method is  {@link  Repeatable#value()} class's value method
     */
    public static boolean methodOfRepeatableContainer(Method method) {

        if (method == null) {
            return false;
        }
        return method.getName().equals("value")
                && method.getDeclaringClass().isAnnotation()
                && method.getParameterTypes().length == 0
                && method.getReturnType().isArray()
                && method.getReturnType().getComponentType().isAnnotation()
                && method.getReturnType().getComponentType().isAnnotationPresent(Repeatable.class);
    }
}
