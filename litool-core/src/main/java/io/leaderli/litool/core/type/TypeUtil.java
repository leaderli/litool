package io.leaderli.litool.core.type;


import io.leaderli.litool.core.meta.Lira;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author leaderli
 * @since 2022/6/27
 */
public class TypeUtil {

    /**
     * the type is unknown
     * <pre>
     * null == type || type instanceof TypeVariable;
     * </pre>
     *
     * @param type the type
     * @return the type is unknown
     */
    public static boolean isUnknown(Type type) {
        return null == type || type instanceof TypeVariable;
    }


    /**
     * Return two TypeVariable typeName is equals
     *
     * @param left  a TypeVariable
     * @param right a TypeVariable
     * @return two TypeVariable typeName is equals
     */
    private static boolean equals(TypeVariable<?> left, TypeVariable<?> right) {
        return Objects.equals(left.getTypeName(), right.getTypeName());
    }


    private static boolean equals(ParameterizedType left, ParameterizedType right) {

        if (left.getRawType() == right.getRawType()) {

            Type[] leftActualTypeArguments = left.getActualTypeArguments();
            Type[] rightActualTypeArguments = right.getActualTypeArguments();
            if (leftActualTypeArguments.length != rightActualTypeArguments.length) {
                return false;
            }

            for (int i = 0; i < leftActualTypeArguments.length; i++) {
                if (!equals(leftActualTypeArguments[i], rightActualTypeArguments[i])) {
                    return false;
                }
            }

            return true;
        }
        return false;
    }

    /**
     * Return two type is equals
     *
     * @param left  type
     * @param right type
     * @return two type is equals
     */
    public static boolean equals(Type left, Type right) {
        if (left instanceof ParameterizedType && right instanceof ParameterizedType) {
            return equals((ParameterizedType) left, (ParameterizedType) right);
        }
        if (left instanceof TypeVariable && right instanceof TypeVariable) {
            return equals((TypeVariable<?>) left, (TypeVariable<?>) right);
        }
        return Objects.equals(left, right);
    }

    /**
     * @param type the type
     * @return the type raw class
     */
    @SuppressWarnings("rawtypes")
    public static Class erase(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            return (Class<?>) pt.getRawType();
        }
        if (type instanceof TypeVariable) {
            TypeVariable<?> tv = (TypeVariable<?>) type;
            Type[] bounds = tv.getBounds();
            return (0 < bounds.length)
                    ? erase(bounds[0])
                    : Object.class;
        }
        if (type instanceof WildcardType) {
            WildcardType wt = (WildcardType) type;
            Type[] bounds = wt.getUpperBounds();
            return (0 < bounds.length)
                    ? erase(bounds[0])
                    : Object.class;
        }
        if (type instanceof GenericArrayType) {
            GenericArrayType gat = (GenericArrayType) type;
            return Array.newInstance(erase(gat.getGenericComponentType()), 0).getClass();
        }
        if (type == null) {
            return null;
        }
        throw new IllegalArgumentException("Unknown Type kind: " + type.getClass());
    }


    private static Type resolve(Type type, Map<TypeVariable<?>, Type> visitedTypeVariables) {

        if (type instanceof ParameterizedType) {

            ParameterizedType parameterizedType = (ParameterizedType) type;
            Class<?> rawType = (Class<?>) parameterizedType.getRawType();
            Type[] actualTypeArguments = Lira.of(parameterizedType.getActualTypeArguments())
                    .map(arg -> resolve(arg, visitedTypeVariables))
                    .toArray(Type.class);
            return LiParameterizedType.make(rawType, parameterizedType.getOwnerType(), actualTypeArguments);
        } else if (type instanceof TypeVariable) {
            return visitedTypeVariables.getOrDefault(type, erase(type));
        } else if (type instanceof Class) {
            return type;
        }

        return erase(type);
    }

    public static <T> LiParameterizedType resolve(Class<?> declare, Class<T> resolve) {
        Objects.requireNonNull(resolve);
        if (resolve.getTypeParameters().length == 0 || resolve == declare) {
            return LiParameterizedType.make(resolve);
        }
        Map<TypeVariable<?>, Type> visitedTypeVariables = new HashMap<>();
        TypeUtil.resolve(declare, declare, resolve, visitedTypeVariables);
        Type[] declareTypeArguments = Lira.of(resolve.getTypeParameters()).map(visitedTypeVariables::get).toArray(Type.class);
        return LiParameterizedType.make(resolve, null, declareTypeArguments);
    }

    /**
     * @param resolving            the resolving type
     * @param raw                  the resolving raw class
     * @param toResolve            the resolve class
     * @param visitedTypeVariables typeVariable and it's actual declare class
     * @return find the resolve class generic typeParameter declare class
     */
    static boolean resolve(Type resolving, Class<?> raw, Class<?> toResolve, Map<TypeVariable<?>, Type> visitedTypeVariables) {

        if (resolving == toResolve) { // found
            return true;
        }
        if (raw == null) {
            // interface should continue resolve by super interfaces
            return !toResolve.isInterface();
        }


        if (resolving instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) resolving;
            TypeVariable<?>[] typeParameters = ((Class<?>) parameterizedType.getRawType()).getTypeParameters();
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            for (int i = 0; i < actualTypeArguments.length; i++) {
                Type actualTypeArgument = actualTypeArguments[i];
                if (actualTypeArgument instanceof TypeVariable) {
                    actualTypeArgument = resolve(actualTypeArgument, visitedTypeVariables);
                } else if (actualTypeArgument instanceof ParameterizedType) {

                    actualTypeArgument = resolve(actualTypeArgument, visitedTypeVariables);
                }
                visitedTypeVariables.put(typeParameters[i], actualTypeArgument);
            }
        }
        if (toResolve.isInterface()) {
            for (Type genericInterface : raw.getGenericInterfaces()) {

                if (resolve(genericInterface, erase(genericInterface), toResolve, visitedTypeVariables)) {
                    return true;
                }
            }

        }
        return resolve(raw.getGenericSuperclass(), raw.getSuperclass(), toResolve, visitedTypeVariables);
    }
}
