package io.leaderli.litool.core.type;


import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.internal.GenericArrayTypeImpl;
import io.leaderli.litool.core.internal.ParameterizedTypeImpl;
import io.leaderli.litool.core.internal.WildcardTypeImpl;
import io.leaderli.litool.core.meta.Lira;

import java.lang.reflect.*;
import java.util.Arrays;
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
     * @param type the type
     * @return the type raw class
     */
    public static Class<?> erase(Type type) {
        if (type instanceof Class<?>) {
            // type is a normal class.
            return (Class<?>) type;

        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            // I'm not exactly sure why getRawType() returns Type instead of Class.
            // Neal isn't either but suspects some pathological case related
            // to nested classes exists.
            Type rawType = parameterizedType.getRawType();
            LiAssertUtil.assertTrue(rawType instanceof Class);
            return (Class<?>) rawType;

        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(erase(componentType), 0).getClass();

        } else if (type instanceof TypeVariable) {
            // we could use the variable's bounds, but that won't work if there are multiple.
            // having a raw type that's more general than necessary is okay
            return erase(((TypeVariable<?>) type).getBounds()[0]);

        } else if (type instanceof WildcardType) {
            return erase(((WildcardType) type).getUpperBounds()[0]);

        } else if (type == null) {
            return null;
        } else {
            String className = type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType, or "
                    + "GenericArrayType, but <" + type + "> is of type " + className);
        }

    }

    private static Type resolve(Type type, Map<TypeVariable<?>, Type> visitedTypeVariables) {

        if (type instanceof ParameterizedType) {

            ParameterizedType parameterizedType = (ParameterizedType) type;
            Class<?> rawType = (Class<?>) parameterizedType.getRawType();
            Type[] actualTypeArguments = Lira.of(parameterizedType.getActualTypeArguments()).map(arg -> resolve(arg, visitedTypeVariables)).toArray(Type.class);
            return ParameterizedTypeImpl.make(parameterizedType.getOwnerType(), rawType, actualTypeArguments);
        } else if (type instanceof TypeVariable) {
            return visitedTypeVariables.getOrDefault(type, erase(type));
        } else if (type instanceof Class) {
            return type;
        }

        return erase(type);
    }

    /**
     * {@code  resolve(declare, erase(declare),resolve}
     *
     * @param declare the declare
     * @param resolve the resolve class that has generic typeParameter
     * @param <T>     the type parameter of resolve
     * @return {@link  #resolve(Type, Class, Class)}
     * @see #erase(Type)
     */
    public static <T> ParameterizedTypeImpl resolve(Type declare, Class<T> resolve) {
        return resolve(declare, erase(declare), resolve);
    }

    /**
     * the generic class type will be declared at actual class that can be new.
     * the resolving progress will replace {@link  TypeVariable} to the {@link  Class},
     * at end, it will return  {@link  ParameterizedType} with the resolve class and with
     * actual declare typeParameters
     *
     * @param declare the declare type
     * @param resolve the resolve class that has generic typeParameter
     * @param rawType the rawType of declare class
     * @param <T>     the type parameter of resolve
     * @return the  LiParameterizedType with resolve class with declare class typeParameters
     */
    public static <T> ParameterizedTypeImpl resolve(Type declare, Class<?> rawType, Class<T> resolve) {
        Objects.requireNonNull(resolve);
        if (resolve.getTypeParameters().length == 0 || resolve == declare) {
            return ParameterizedTypeImpl.make(resolve);
        }
        Map<TypeVariable<?>, Type> visitedTypeVariables = new HashMap<>();
        TypeUtil.resolve(declare, rawType, resolve, visitedTypeVariables);
        Type[] declareTypeArguments = Lira.of(resolve.getTypeParameters()).map(visitedTypeVariables::get).toArray(Type.class);
        return ParameterizedTypeImpl.make(null, resolve, declareTypeArguments);
    }

    private static boolean resolve(Type resolving, Class<?> raw, Class<?> toResolve, Map<TypeVariable<?>, Type> visitedTypeVariables) {

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
                Type resolve = resolve(actualTypeArguments[i], visitedTypeVariables);
                visitedTypeVariables.put(typeParameters[i], resolve);
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

    public static String typeToString(Type type) {
        return type instanceof Class ? ((Class<?>) type).getName() : type.toString();
    }

    public static void checkNotPrimitive(Type type) {
        LiAssertUtil.assertTrue(!(type instanceof Class<?>) || !((Class<?>) type).isPrimitive());
    }

    /**
     * Returns a type that is functionally equal but not necessarily equal
     * according to {@link Object#equals(Object) Object.equals()}. The returned
     * type is {@link java.io.Serializable}.
     */
    public static Type canonicalize(Type type) {
        if (type instanceof Class) {
            Class<?> c = (Class<?>) type;
            return c.isArray() ? new GenericArrayTypeImpl(canonicalize(c.getComponentType())) : c;

        } else if (type instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType) type;
            return ParameterizedTypeImpl.make(p);

        } else if (type instanceof GenericArrayType) {
            GenericArrayType g = (GenericArrayType) type;
            return new GenericArrayTypeImpl(g.getGenericComponentType());

        } else if (type instanceof WildcardType) {
            WildcardType w = (WildcardType) type;
            return new WildcardTypeImpl(w.getUpperBounds(), w.getLowerBounds());

        } else {
            // type is either serializable as-is or unsupported
            return type;
        }
    }


    /**
     * Returns true if {@code a} and {@code b} are equal.
     */
    public static boolean equals(Type a, Type b) {
        if (a == b) {
            // also handles (a == null && b == null)
            return true;

        } else if (a instanceof Class) {
            // Class already specifies equals().
            return a.equals(b);

        } else if (a instanceof ParameterizedType) {
            if (!(b instanceof ParameterizedType)) {
                return false;
            }

            ParameterizedType pa = (ParameterizedType) a;
            ParameterizedType pb = (ParameterizedType) b;
            return Objects.equals(pa.getOwnerType(), pb.getOwnerType())
                    && pa.getRawType().equals(pb.getRawType())
                    && Arrays.equals(pa.getActualTypeArguments(), pb.getActualTypeArguments());

        } else if (a instanceof GenericArrayType) {
            if (!(b instanceof GenericArrayType)) {
                return false;
            }

            GenericArrayType ga = (GenericArrayType) a;
            GenericArrayType gb = (GenericArrayType) b;
            return equals(ga.getGenericComponentType(), gb.getGenericComponentType());

        } else if (a instanceof WildcardType) {
            if (!(b instanceof WildcardType)) {
                return false;
            }

            WildcardType wa = (WildcardType) a;
            WildcardType wb = (WildcardType) b;
            return Arrays.equals(wa.getUpperBounds(), wb.getUpperBounds())
                    && Arrays.equals(wa.getLowerBounds(), wb.getLowerBounds());

        } else if (a instanceof TypeVariable) {
            if (!(b instanceof TypeVariable)) {
                return false;
            }
            TypeVariable<?> va = (TypeVariable<?>) a;
            TypeVariable<?> vb = (TypeVariable<?>) b;
            return va.getGenericDeclaration() == vb.getGenericDeclaration()
                    && va.getName().equals(vb.getName());

        } else {
            // This isn't a type we support. Could be a generic array type, wildcard type, etc.
            return false;
        }
    }
}
