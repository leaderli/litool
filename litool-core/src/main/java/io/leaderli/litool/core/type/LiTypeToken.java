

package io.leaderli.litool.core.type;


import io.leaderli.litool.core.internal.ParameterizedTypeImpl;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 表示一个泛型类型 {@code T}，由于 Java 不支持在运行时直接获取泛型类型，所以使用此类进行传递。
 *
 * <p>例如，要创建 {@code List<String>} 的类型字面量，可以创建一个空的匿名内部类：
 * <p>{@code LiTypeToken<List<String>> list = new LiTypeToken<List<String>>() {};}
 * <p>无法使用此语法创建带有通配符参数的类型字面量，例如 {@code Class<?>} 或 {@code List<? extends CharSequence>}。
 *
 * @param <T> 泛型类型参数
 */
public class LiTypeToken<T> implements ParameterizedType {
    private final Type type;
    private final Class<? super T> rawType;
    private final int hashCode;

    /**
     * 构造一个新的类型字面量。从类型参数派生表示的类。
     * <p>客户端创建一个空的匿名子类。这样做将类型参数嵌入匿名类的类型层次结构中，
     * 因此我们可以在擦除后的运行时重新构建它。
     */
    @SuppressWarnings("unchecked")
    protected LiTypeToken() {
        this.type = getSuperclassTypeParameter(getClass());
        this.rawType = (Class<? super T>) TypeUtil.erase(type);
        this.hashCode = type.hashCode();
    }

    /**
     * 获取给定类的父类的泛型类型参数。
     *
     * @param subclass 给定的子类。
     * @return 规范化形式的父类泛型类型参数。
     */
    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new IllegalArgumentException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return TypeUtil.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    /**
     * 构造一个新的类型字面量。手动构造，不安全。
     *
     * @param type 泛型类型实例
     */
    @SuppressWarnings("unchecked")
    LiTypeToken(Type type) {
        this.type = TypeUtil.canonicalize(Objects.requireNonNull(type));
        this.rawType = (Class<? super T>) TypeUtil.erase(this.type);
        this.hashCode = this.type.hashCode();
    }

    /**
     * 判断一个类型是否可以分配给一个泛型数组类型。
     *
     * @param from 要判断的类型。
     * @param to   泛型数组类型。
     * @return 如果类型可以分配给泛型数组类型，则返回true；否则返回false。
     */
    private static boolean isAssignableFrom(Type from, GenericArrayType to) {

        Type toGenericComponentType = to.getGenericComponentType();
        if (toGenericComponentType instanceof ParameterizedType) {
            Type t = from;
            if (from instanceof GenericArrayType) {
                t = ((GenericArrayType) from).getGenericComponentType();
            } else if (from instanceof Class<?>) {
                Class<?> classType = (Class<?>) from;
                while (classType.isArray()) {
                    classType = classType.getComponentType();
                }
                t = classType;
            }
            return isAssignableFrom(t, (ParameterizedType) toGenericComponentType,
                    new HashMap<>());
        }
        // No generic defined on "to"; therefore, return true and let other
        // checks determine assignability
        return true;
    }

    /**
     * 递归进行类型安全的检查，判断一个类型是否可以分配给一个参数化类型。
     *
     * @param from       要检查的类型。
     * @param to         参数化类型。
     * @param typeVarMap 类型变量映射。
     * @return 如果类型可以分配给参数化类型，则返回true；否则返回false。
     */
    private static boolean isAssignableFrom(Type from, ParameterizedType to,
                                            Map<String, Type> typeVarMap) {

        if (from == null) {
            return false;
        }

        if (to.equals(from)) {
            return true;
        }

        // First figure out the class and any type information.
        Class<?> clazz = TypeUtil.erase(from);
        ParameterizedType ptype = null;
        if (from instanceof ParameterizedType) {
            ptype = (ParameterizedType) from;
        }

        // Load up parameterized variable info if it was parameterized.
        if (ptype != null) {
            Type[] tArgs = ptype.getActualTypeArguments();
            TypeVariable<?>[] tParams = clazz.getTypeParameters();
            for (int i = 0; i < tArgs.length; i++) {
                Type arg = tArgs[i];
                TypeVariable<?> va = tParams[i];
                while (arg instanceof TypeVariable<?>) {
                    TypeVariable<?> v = (TypeVariable<?>) arg;
                    arg = typeVarMap.get(v.getName());
                }
                typeVarMap.put(va.getName(), arg);
            }

            // check if they are equivalent under our current mapping.
            if (typeEquals(ptype, to, typeVarMap)) {
                return true;
            }
        }

        for (Type itype : clazz.getGenericInterfaces()) {
            if (isAssignableFrom(itype, to, new HashMap<>(typeVarMap))) {
                return true;
            }
        }

        // Interfaces didn't work, try the superclass.
        Type sType = clazz.getGenericSuperclass();
        return isAssignableFrom(sType, to, new HashMap<>(typeVarMap));
    }

    /**
     * Checks if two parameterized types are exactly equal, under the variable
     * replacement described in the typeVarMap.
     *
     * @param from       -
     * @param to         -
     * @param typeVarMap -
     * @return -
     */
    private static boolean typeEquals(ParameterizedType from,
                                      ParameterizedType to, Map<String, Type> typeVarMap) {
        if (from.getRawType().equals(to.getRawType())) {
            Type[] fromArgs = from.getActualTypeArguments();
            Type[] toArgs = to.getActualTypeArguments();
            for (int i = 0; i < fromArgs.length; i++) {
                if (!matches(fromArgs[i], toArgs[i], typeVarMap)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static AssertionError buildUnexpectedTypeError(Type token, Class<?>... expected) {

        // Build exception message
        StringBuilder exceptionMessage =
                new StringBuilder("Unexpected type. Expected one of: ");
        for (Class<?> clazz : expected) {
            exceptionMessage.append(clazz.getName()).append(", ");
        }
        exceptionMessage.append("but got: ").append(token.getClass().getName())
                .append(", for type token: ").append(token).append('.');

        return new AssertionError(exceptionMessage.toString());
    }

    /**
     * 判断两个类型是否相同或等效于TypeMap中提供的变量映射。
     *
     * @param from    - 原类型
     * @param to      - 目标类型
     * @param typeMap - 类型映射
     * @return - 返回true表示两个类型相同或等效，否则返回false。
     */
    private static boolean matches(Type from, Type to, Map<String, Type> typeMap) {
        return to.equals(from)
                || (from instanceof TypeVariable
                && to.equals(typeMap.get(((TypeVariable<?>) from).getName())));

    }

    /**
     * 获取给定Type实例的类型文本表示。
     *
     * @param <T>  the type parameter
     * @param type - Type实例
     * @return - 表示给定Type实例的LiTypeToken实例
     */
    public static <T> LiTypeToken<T> ofType(Type type) {
        return new LiTypeToken<>(type);
    }

    /**
     * 获取给定Class对象的类型文本表示。
     *
     * @param <T>  the type parameter
     * @param type - Class对象
     * @return - 表示给定Class对象的LiTypeToken实例
     */
    public static <T> LiTypeToken<T> of(Class<T> type) {
        return new LiTypeToken<>(type);
    }

    /**
     * 获取表示所有元素类型均为componentType的数组类型的类型文本表示。
     *
     * @param componentType - 表示数组元素类型的Type实例
     * @return 表示该数组类型的LiTypeToken实例
     */
    public static LiTypeToken<?> getArray(Type componentType) {
        return new LiTypeToken<>(LiTypes.arrayOf(componentType));
    }

    /**
     * 获取该类型的实际类型参数。
     *
     * @return - 表示该类型实际类型参数的Type数组
     */
    @Override
    public final Type[] getActualTypeArguments() {

        if (isParameterizedType()) {
            return ((ParameterizedType) type).getActualTypeArguments();
        }
        return ParameterizedTypeImpl.make(rawType).getActualTypeArguments();
    }

    /**
     * 判断该类型是否是参数化类型。
     *
     * @return - 如果是参数化类型返回true，否则返回false。
     */
    public final boolean isParameterizedType() {
        return type instanceof ParameterizedType;
    }

    /**
     * 获取该类型的原始类型。
     *
     * @return - 表示该类型的原始类型的Class实例
     */
    @Override
    public final Class<? super T> getRawType() {
        return rawType;
    }

    /**
     * 获取该类型的所有者类型。
     *
     * @return - 表示该类型所有者类型的Type实例
     */
    @Override
    public Type getOwnerType() {
        if (isParameterizedType()) {
            return ((ParameterizedType) type).getOwnerType();
        }
        return null;
    }

    /**
     * 获取该类型的Type实例。
     *
     * @return - 表示该类型的Type实例。
     */
    public final Type getType() {
        return type;
    }

    /**
     * 获取该类型的泛型类型。
     *
     * @return - 表示该类型的泛型类型的Class实例
     */
    @SuppressWarnings("unchecked")
    public final Class<T> getGenericType() {
        return (Class<T>) rawType;
    }


    @Override
    public final int hashCode() {
        return this.hashCode;
    }

    @Override
    public final boolean equals(Object o) {
        return o instanceof LiTypeToken<?>
                && TypeUtil.equals(type, ((LiTypeToken<?>) o).type);
    }

    @Override
    public final String toString() {
        return TypeUtil.typeToString(type);
    }
}
