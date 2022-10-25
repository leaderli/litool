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
 * The type Type util.
 *
 * @author leaderli
 * @since 2022 /6/27
 */
public class TypeUtil {

    /**
     * The constant EMPTY_TYPE_ARRAY.
     */
    public static final Type[] EMPTY_TYPE_ARRAY = new Type[0];
    /**
     * The constant EMPTY_TYPE_VARIABLE_ARRAY.
     */
    @SuppressWarnings("rawtypes")
    public static final TypeVariable[] EMPTY_TYPE_VARIABLE_ARRAY = new TypeVariable[0];

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
     * Erase class.
     *
     * @param type the type
     * @return the type raw class
     */
    public static Class<?> erase(Type type) {
        if (type instanceof Class<?>) {
            // type is a normal class.
            return (Class<?>) type;

        } else if (type instanceof ParameterizedType) {

            // I'm not exactly sure why getRawType() returns Type instead of Class.
            // Neal isn't either but suspects some pathological case related
            // to nested classes exists.
            Type rawType = ((ParameterizedType) type).getRawType();
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
            throw new IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <" + type + "> is of type " + className);
        }

    }

    private static Type resolveByTypeVariables(Type type, Map<TypeVariable<?>, Type> visitedTypeVariables) {

        if (type instanceof ParameterizedType) {

            ParameterizedType parameterizedType = (ParameterizedType) type;

            if (parameterizedType.getActualTypeArguments().length == 0) {
                return parameterizedType;
            }

            Type ownerType = parameterizedType.getOwnerType();
            Class<?> rawTp = (Class<?>) parameterizedType.getRawType();
            Type[] argType = Lira.of(parameterizedType.getActualTypeArguments())
                    .map(arg -> resolveByTypeVariables(arg, visitedTypeVariables))
                    .toArray(Type.class);

            return ParameterizedTypeImpl.make(ownerType, rawTp, argType);

        } else if (type instanceof TypeVariable) {

            return visitedTypeVariables.getOrDefault(type, erase(type));

        } else if (type instanceof GenericArrayType) {

            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            componentType = resolveByTypeVariables(componentType, visitedTypeVariables);
            return Array.newInstance(erase(componentType), 0).getClass();

        } else if (type instanceof Class) {

            Class<?> cls = (Class<?>) type;
            if (cls.getTypeParameters().length == 0) {
                return type;
            }
            return resolveByTypeVariables(ParameterizedTypeImpl.make(cls), visitedTypeVariables);
        }

        return erase(type);
    }

    /**
     * the generic class type be declared at a context type, the resolving progress will fill {@link  TypeVariable},
     * {@link  GenericArrayType}, {@link  ParameterizedType} undefined type to the defined type. such as {@link  Class},
     * {@link  ParameterizedType} with defined typed actualTypeArguments
     * <p>
     * expand the context typeVariable and fill to the toResolve
     *
     * @param context   the context
     * @param toResolve the toResolve toResolve that has generic typeParameter
     * @return the toResolve with toResolve class with context class typeParameters
     */
    public static Type resolve(Type context, Type toResolve) {

        Objects.requireNonNull(toResolve);
        Class<?> rawType = erase(toResolve);

        Map<TypeVariable<?>, Type> visitedTypeVariables = new HashMap<>();

        expandTypeVariables(context, visitedTypeVariables);

        if (rawType.getTypeParameters().length > 0 && rawType != context) {

            resolve(context, rawType, visitedTypeVariables);

        } else if (toResolve instanceof GenericArrayType) {

            Type componentType = ((GenericArrayType) toResolve).getGenericComponentType();
            componentType = resolve(context, componentType);
            return ClassUtil.getArrayClass(erase(componentType));

        } else if (toResolve instanceof TypeVariable) {

            TypeVariable<?> typeVariable = (TypeVariable<?>) toResolve;
            Type declareRaw = TypeUtil.resolveTypeVariable(context, typeVariable);
            visitedTypeVariables.put(typeVariable, declareRaw);
            return declareRaw;
        }

        Type type = resolveByTypeVariables(toResolve, visitedTypeVariables);
        if (type == rawType && rawType.getTypeParameters().length > 0) {
            return resolveByTypeVariables(ParameterizedTypeImpl.make(rawType), visitedTypeVariables);
        }
        return type;
    }

    /**
     * the generic class type will be declared at actual class that can be new.
     * the resolving progress will replace {@link  TypeVariable} to the {@link  Class},
     * at end, it will return  {@link  ParameterizedType} with the toResolve class and with
     * actual context typeParameters
     *
     * @param <T>       the type parameter of toResolve
     * @param context   the context type
     * @param toResolve the toResolve class that has generic typeParameter
     * @return the LiParameterizedType with toResolve class with context class typeParameters
     */
    public static <T> ParameterizedTypeImpl resolve2Parameterized(Type context, Class<T> toResolve) {
        Type resolve = resolve(context, toResolve);
        return ParameterizedTypeImpl.make(resolve);
    }


    /**
     * Resolve type variable type.
     *
     * @param context      the context
     * @param typeVariable the type variable
     * @return the type
     */
    public static Type resolveTypeVariable(Type context, TypeVariable<?> typeVariable) {


        Class<?> declaredByRaw = getDeclaringClass(typeVariable);

        // we can't reduce this further
        if (declaredByRaw == null) {
            return erase(typeVariable);
        }
        Map<TypeVariable<?>, Type> visitedTypeVariables = new HashMap<>();
        resolve(context, declaredByRaw, visitedTypeVariables);
        Type type = visitedTypeVariables.get(typeVariable);
        if (type != null) {
            return type;
        }
        return erase(typeVariable);

    }


    // to recursively upward to find the toResolve class TypeVariables actualClass, don't find the inner class or
    // enclosing class
    private static boolean resolve(Type resolving, Class<?> toResolve,
                                   Map<TypeVariable<?>, Type> visitedTypeVariables) {

        if (resolving == Object.class) {
            return true;
        }
        if (toResolve == Object.class) {
            return false;
        }

        Class<?> raw = erase(resolving);

        // interface should continue resolve by super interfaces
        if (raw == null) {
            return !toResolve.isInterface();
        }
        if (raw == toResolve) { // found
            expandTypeVariables(resolving, visitedTypeVariables);
            return true;
        }
        expandTypeVariables(resolving, visitedTypeVariables);

        for (Type genericInterface : raw.getGenericInterfaces()) {
            if (resolve(genericInterface, toResolve, visitedTypeVariables)) {
                return true;
            }
        }

        // ignore inner class or enclosing class


        return resolve(raw.getGenericSuperclass(), toResolve, visitedTypeVariables);


    }


    /**
     * the declare type have declare the actual {@link  ParameterizedType} of it's {@link  Class#getTypeParameters()}.
     * store the corresponding pair. just expand {@link  TypeVariable} of {@link ParameterizedType#getRawType()}
     * <p>
     * if declare type is  {@link  GenericArrayType}, expand it's {@link GenericArrayType#getGenericComponentType()}
     *
     * @param declare              the type that have declare type
     * @param visitedTypeVariables the typeVariable-type map
     */
    public static void expandTypeVariables(Type declare, Map<TypeVariable<?>, Type> visitedTypeVariables) {

        if (declare instanceof ParameterizedType) {

            ParameterizedType parameterizedType = (ParameterizedType) declare;

            TypeVariable<?>[] typeParameters = ((Class<?>) parameterizedType.getRawType()).getTypeParameters();
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

            for (int i = 0; i < actualTypeArguments.length; i++) {
                Type actualTypeArgument = actualTypeArguments[i];

                Type resolve = resolveByTypeVariables(actualTypeArgument, visitedTypeVariables);
                visitedTypeVariables.put(typeParameters[i], resolve);
                // expand child
                expandTypeVariables(actualTypeArgument, visitedTypeVariables);
            }
        } else if (declare instanceof GenericArrayType) {
            expandTypeVariables(((GenericArrayType) declare).getGenericComponentType(), visitedTypeVariables);
        }
        // ignore the  not directly

    }

    /**
     * Type to string string.
     *
     * @param type the type
     * @return the string
     */
    public static String typeToString(Type type) {
        return type instanceof Class ? ((Class<?>) type).getName() : type.toString();
    }

    /**
     * Check not primitive.
     *
     * @param type the type
     */
    public static void checkNotPrimitive(Type type) {
        LiAssertUtil.assertTrue(!(type instanceof Class<?>) || !((Class<?>) type).isPrimitive());
    }

    /**
     * Returns a type that is functionally equal but not necessarily equal
     * according to {@link Object#equals(Object) Object.equals()}. The returned
     * type is {@link java.io.Serializable}.
     *
     * @param type the type
     * @return the type
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
     * Gets declaring class.
     *
     * @param typeVariable the type variable
     * @return the declaring class
     */
    public static Class<?> getDeclaringClass(TypeVariable<?> typeVariable) {
        GenericDeclaration genericDeclaration = typeVariable.getGenericDeclaration();
        return genericDeclaration instanceof Class ? (Class<?>) genericDeclaration : null;
    }


    /**
     * Returns true if {@code a} and {@code b} are equal.
     *
     * @param a the a
     * @param b the b
     * @return the boolean
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
