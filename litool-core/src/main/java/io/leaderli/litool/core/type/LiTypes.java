package io.leaderli.litool.core.type;

import io.leaderli.litool.core.internal.GenericArrayTypeImpl;
import io.leaderli.litool.core.internal.ParameterizedTypeImpl;
import io.leaderli.litool.core.internal.WildcardTypeImpl;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;


/**
 * 提供静态方法，用于处理类型。
 */
public final class LiTypes {

    private LiTypes() {
    }

    /**
     * 返回一个新的参数化类型，将 {@code typeArguments} 应用于 {@code rawType} 并由 {@code ownerType} 封装。
     *
     * @param ownerType     所有者类型
     * @param rawType       原始类型
     * @param typeArguments 类型参数
     * @return 可序列化的参数化类型
     */
    public static ParameterizedType newParameterizedTypeWithOwner(
            Type ownerType, Type rawType, Type... typeArguments) {
        return new ParameterizedTypeImpl(ownerType, rawType, typeArguments);
    }

    /**
     * 返回一个元素类型都是 {@code componentType} 的数组类型。
     *
     * @param componentType 组件类型
     * @return 可序列化的泛型数组类型
     */
    public static GenericArrayType arrayOf(Type componentType) {
        return new GenericArrayTypeImpl(componentType);
    }

    /**
     * 返回表示一个未知 {@code bound} 类型的类型。例如，如果 {@code bound} 是 {@code CharSequence.class}，则返回 {@code ? extends CharSequence}。如果 {@code bound} 是 {@code Object.class}，则返回 {@code ?}，它是 {@code ? extends Object} 的简写。
     *
     * @param bound 限定类型
     * @return 通配符类型
     */
    public static WildcardType subtypeOf(Type bound) {
        Type[] upperBounds;
        if (bound instanceof WildcardType) {
            upperBounds = ((WildcardType) bound).getUpperBounds();
        } else {
            upperBounds = new Type[]{bound};
        }
        return new WildcardTypeImpl(upperBounds, TypeUtil.EMPTY_TYPE_ARRAY);
    }

    /**
     * 返回表示 {@code bound} 的未知超类型的类型。例如，
     * 如果 {@code bound} 是 {@code String.class}，
     * 则返回 {@code ? super String}。
     *
     * @param bound 限定类型
     * @return 通配符类型
     */
    public static WildcardType supertypeOf(Type bound) {
        Type[] lowerBounds;
        if (bound instanceof WildcardType) {
            lowerBounds = ((WildcardType) bound).getLowerBounds();
        } else {
            lowerBounds = new Type[]{bound};
        }
        return new WildcardTypeImpl(new Type[]{Object.class}, lowerBounds);
    }


    /**
     * 返回 {@code supertype} 的通用超类型。例如，给定一个类 {@code IntegerSet}，
     * 当超类型为 {@code Set.class} 时，返回 {@code Set<Integer>}，当超类型为
     * {@code Collection.class} 时，返回 {@code Collection<Integer>}。
     *
     * @param context   -
     * @param rawType   -
     * @param toResolve -
     * @return -
     */
    static Type getGenericSupertype(Type context, Class<?> rawType, Class<?> toResolve) {
        if (toResolve == rawType) {
            return context;
        }

        // we skip searching through interfaces if unknown is an interface
        if (toResolve.isInterface()) {
            Class<?>[] interfaces = rawType.getInterfaces();
            for (int i = 0, length = interfaces.length; i < length; i++) {
                if (interfaces[i] == toResolve) {
                    return rawType.getGenericInterfaces()[i];
                } else if (toResolve.isAssignableFrom(interfaces[i])) {
                    return getGenericSupertype(rawType.getGenericInterfaces()[i], interfaces[i], toResolve);
                }
            }
        }

        // check our supertypes
        if (!rawType.isInterface()) {
            while (rawType != Object.class) {
                Class<?> rawSupertype = rawType.getSuperclass();
                if (rawSupertype == toResolve) {
                    return rawType.getGenericSuperclass();
                } else if (toResolve.isAssignableFrom(rawSupertype)) {
                    return getGenericSupertype(rawType.getGenericSuperclass(), rawSupertype, toResolve);
                }
                rawType = rawSupertype;
            }
        }

        // we can't resolve this further
        return toResolve;
    }


    /**
     * 返回此数组类型的组件类型。
     *
     * @param array 数组类型
     * @return 数组组件类型
     * @throws ClassCastException 如果此类型不是数组。
     */
    public static Type getArrayComponentType(Type array) {
        return array instanceof GenericArrayType
                ? ((GenericArrayType) array).getGenericComponentType()
                : ((Class<?>) array).getComponentType();
    }


}

