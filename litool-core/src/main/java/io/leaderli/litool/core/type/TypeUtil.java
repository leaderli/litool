package io.leaderli.litool.core.type;


import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.internal.ParameterizedTypeImpl;
import io.leaderli.litool.core.internal.WildcardTypeImpl;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

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
     * <pre>
     * null == type || type instanceof TypeVariable;
     * </pre>
     *
     * @param type 类型
     * @return 类型是未知的
     */
    public static boolean isUnknown(Type type) {
        return null == type || type instanceof TypeVariable;
    }


    /**
     * @param type 类型
     * @return 根据传入的Type类型参数，获取其原始类型Class
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
            Class<?> rawType = (Class<?>) parameterizedType.getRawType();
            Type[] argType = Stream.of(parameterizedType.getActualTypeArguments())
                    .map(arg -> resolveByTypeVariables(arg, visitedTypeVariables))
                    .toArray(Type[]::new);

            return ParameterizedTypeImpl.make(ownerType, rawType, argType);

        }

        if (type instanceof GenericArrayType) {

            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            componentType = resolveByTypeVariables(componentType, visitedTypeVariables);
            return LiTypes.arrayOf(componentType);
//            return Array.newInstance(erase(componentType), 0).getClass();


        }
        if (type instanceof TypeVariable) {


            Type value = visitedTypeVariables.get(type);
            if (value != null) {
                return value;
            }
            // 防止递归调用，先填充值
            visitedTypeVariables.put((TypeVariable<?>) type, TypeUtil.erase(type));
            Type bound = ((TypeVariable<?>) type).getBounds()[0];
            value = resolveByTypeVariables(bound, visitedTypeVariables);
            // 填充更准确的值
            visitedTypeVariables.put((TypeVariable<?>) type, value);

        }

        if (type instanceof Class) {

            Class<?> cls = (Class<?>) type;
            if (cls.getTypeParameters().length == 0) {
                return type;
            }
            return resolveByTypeVariables(ParameterizedTypeImpl.make(cls), visitedTypeVariables);
        }

        return erase(type);
    }

    /**
     * 该方法用于解析包含类型变量的泛型类型，根据上下文类型将类型变量替换为具体类型。其处理过程就是将 {@link  TypeVariable},{@link  GenericArrayType}, {@link  ParameterizedType}
     * 这些不确定的类型，填充为确定的类型
     *
     * @param context   上下文类型,可能包含类型变量
     * @param toResolve 待解析类型,可能包含未知的类型变量
     * @return 解析后的类型, 类型变量被具体类型替换
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
     * 该方法用于解析泛型类型，将TypeVariable替换为Class，最终返回带有实际上下文类型参数的ParameterizedType
     *
     * @param <T>       toResolve的类型参数
     * @param context   上下文类型
     * @param toResolve 需要解析的泛型类型
     * @return 带有toResolve类和上下文类类型参数的ParameterizedTypeImpl
     */
    public static <T> ParameterizedTypeImpl resolve2Parameterized(Type context, Class<T> toResolve) {
        Type resolve = resolve(context, toResolve);
        return ParameterizedTypeImpl.make(resolve);
    }


    /**
     * 解析 TypeVariable 类型
     *
     * @param context      上下文
     * @param typeVariable 待解析的 TypeVariable 类型
     * @return 解析后的类型
     */
    public static Type resolveTypeVariable(Type context, TypeVariable<?> typeVariable) {


        Class<?> declaredByRaw = ClassUtil.getDeclaringClass(typeVariable);

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
        Class<?> erase = erase(typeVariable);
        if (erase.getTypeParameters().length > 0) {

            expandTypeVariables(erase, visitedTypeVariables);
            ParameterizedTypeImpl parameterizedType = resolve2Parameterized(context, erase);
            return resolve(context, erase);
        }
        return erase;

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
     * 将类型变量展开为实际类型，并将其存储在类型变量-类型映射中。
     * 对于 {@link ParameterizedType}，将展开其 {@link ParameterizedType#getRawType()} 的 {@link TypeVariable}。
     * 对于 {@link GenericArrayType}，将展开其 {@link GenericArrayType#getGenericComponentType()}。
     * 对于其他类型，将查找该类型中声明的类型变量，并将其解析为实际类型。
     *
     * @param context              声明了类型变量的类型
     * @param visitedTypeVariables 类型变量-类型映射
     *                             不查找父类型
     */
    public static void expandTypeVariables(Type context, Map<TypeVariable<?>, Type> visitedTypeVariables) {

        if (context instanceof ParameterizedType) {
            expandParameterizedTypeTypeVariables((ParameterizedType) context, visitedTypeVariables);
        } else if (context instanceof GenericArrayType) {
            expandTypeVariables(((GenericArrayType) context).getGenericComponentType(), visitedTypeVariables);
        }
        //仅查找在类上直接声明的
        else {

            Type type = resolveByTypeVariables(context, visitedTypeVariables);
        }

    }


    private static void expandClassTypeVariables(Class<?> clazz, Map<TypeVariable<?>, Type> visitedTypeVariables) {
        if (clazz != null && clazz.getTypeParameters().length > 0) {
            expandParameterizedTypeTypeVariables(ParameterizedTypeImpl.make(clazz), visitedTypeVariables);
        }

    }

    private static void expandParameterizedTypeTypeVariables(ParameterizedType parameterizedType, Map<TypeVariable<?>, Type> visitedTypeVariables) {

        TypeVariable<?>[] typeParameters = ((Class<?>) parameterizedType.getRawType()).getTypeParameters();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

        for (int i = 0; i < actualTypeArguments.length; i++) {
            Type actualTypeArgument = actualTypeArguments[i];
            // expand  args first
            expandTypeVariables(actualTypeArgument, visitedTypeVariables);

            Type resolve = resolveByTypeVariables(actualTypeArgument, visitedTypeVariables);
            visitedTypeVariables.put(typeParameters[i], resolve);
        }
    }

    /**
     * @param type -
     * @return 类型的字符串表示形式
     */
    public static String typeToString(Type type) {
        if (type instanceof Class) {
            return ((Class<?>) type).getName();
        }
        if (type instanceof TypeVariable) {
            return ((TypeVariable<?>) type).getGenericDeclaration() + " " + ((TypeVariable<?>) type).getName();
        }
        return type instanceof Class ? ((Class<?>) type).getName() : type.toString();
    }


    /**
     * 返回一个与给定类型功能上相等但不一定与 {@link Object#equals(Object) Object.equals()} 相等的类型。返回的类型是 {@link java.io.Serializable}。
     *
     * @param type 要处理的类型
     * @return 处理后的类型
     */
    public static Type canonicalize(Type type) {
        if (type instanceof Class) {
            Class<?> c = (Class<?>) type;
            return c.isArray() ? LiTypes.arrayOf(canonicalize(c.getComponentType())) : c;

        } else if (type instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType) type;
            return ParameterizedTypeImpl.make(p);

        } else if (type instanceof GenericArrayType) {
            GenericArrayType g = (GenericArrayType) type;
            return LiTypes.arrayOf(g.getGenericComponentType());

        } else if (type instanceof WildcardType) {
            WildcardType w = (WildcardType) type;
            return new WildcardTypeImpl(w.getUpperBounds(), w.getLowerBounds());

        } else {
            // type is either serializable as-is or unsupported
            return type;
        }
    }


    /**
     * @param a the a
     * @param b the b
     * @return 两个类型是否相等
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
