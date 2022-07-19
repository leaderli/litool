package io.leaderli.litool.core.type;

import io.leaderli.litool.core.bit.BitStatus;
import io.leaderli.litool.core.collection.LiListUtil;
import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.core.util.LiStrUtil;

import java.io.IOException;
import java.lang.reflect.*;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author leaderli
 * @since 2022-01-22
 */
public class LiClassUtil {

    /**
     * 基础类型和其包装类型的映射
     */
    private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_MAP = new HashMap<>();
    private static final Map<Class<?>, Class<?>> WRAPPER_PRIMITIVE_MAP = new HashMap<>();

    static {
        PRIMITIVE_WRAPPER_MAP.put(Boolean.TYPE, Boolean.class);
        PRIMITIVE_WRAPPER_MAP.put(Byte.TYPE, Byte.class);
        PRIMITIVE_WRAPPER_MAP.put(Character.TYPE, Character.class);
        PRIMITIVE_WRAPPER_MAP.put(Short.TYPE, Short.class);
        PRIMITIVE_WRAPPER_MAP.put(Integer.TYPE, Integer.class);
        PRIMITIVE_WRAPPER_MAP.put(Long.TYPE, Long.class);
        PRIMITIVE_WRAPPER_MAP.put(Double.TYPE, Double.class);
        PRIMITIVE_WRAPPER_MAP.put(Float.TYPE, Float.class);
        PRIMITIVE_WRAPPER_MAP.put(Void.TYPE, Void.class);

        PRIMITIVE_WRAPPER_MAP.forEach((k, v) -> WRAPPER_PRIMITIVE_MAP.put(v, k));
    }


    /**
     * @param def 实例
     * @param <T> 实例父类泛型
     * @param <E> 实例泛型
     * @return 实例类型的父类
     */
    @SuppressWarnings("unchecked")
    public static <T, E extends T> Class<T> getClass(E def) {
        Objects.requireNonNull(def, "default value should can not be null");

        return (Class<T>) def.getClass();
    }

    /**
     * @param cls 实际类型
     * @param <T> 泛型，其为实际类型的父类或实际类型
     * @return 返回 {@code Class<T>}
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> narrow(final Class<? extends T> cls) {

        return (Class<T>) cls;
    }

    /**
     * @param cls 需要转换的 class
     * @return 返回其基础类 class ，当类是数组 class 时，返回其基础数组 class ，例如 {@code Integer[].class --> int.class }
     */
    public static Class<?> wrapperToPrimitive(final Class<?> cls) {
        Class<?> convertedClass = null;
        if (cls != null) {

            if (cls.isArray()) {
                convertedClass = getArrayClass(cls.getComponentType());
            } else {

                convertedClass = WRAPPER_PRIMITIVE_MAP.get(cls);
            }
        }
        if (convertedClass != null) {
            return convertedClass;
        }
        return cls;
    }

    /**
     * @param type class 类型
     * @return 获取 class 类 的数组类 class
     */
    public static Class<?> getArrayClass(Class<?> type) {


        return Array.newInstance(type, 0).getClass();
    }

    /**
     * @return classpath 路径下的所有 jar 包
     */
    public static List<String> getAppJars() {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> resources = loader.getResources("META-INF");
            return Collections.list(resources).stream()
                    .filter(url -> "jar".equals(url.getProtocol()))
                    .map(URL::getFile)
                    .map(path -> path.replace("!/META-INF", "")
                            .replaceAll("^[^/]++/", ""))

                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return LiListUtil.emptyList();

    }

    /**
     * @param componentType 数组元素的 class 类型
     * @param length        数组长度
     * @param <T>           数组的泛型，可以为实际 class 的父类
     * @return 返回一个指定长度的数组，因为基础类型无法使用声明为泛型，因此所有 基础类型都会被转换为包装类数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] newArray(Class<? extends T> componentType, int length) {
        return (T[]) Array.newInstance(primitiveToWrapper(componentType), length);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] newArray(Object arr) {
        if (arr == null || arr.getClass().getComponentType() == null) {
            return null;

        }
        int length = Array.getLength(arr);
        Object[] objects = newArray(arr.getClass().getComponentType(), length);

        for (int i = 0; i < length; i++) {
            objects[i] = Array.get(arr, i);
        }

        return (T[]) objects;
    }

    /**
     * @param cls 需要转换的 class
     * @return 返回其包装类 class ，当类是数组 class 时，返回其包装数组 class ，例如 {@code int[].class --> Integer.class }
     */
    public static Class<?> primitiveToWrapper(final Class<?> cls) {
        Class<?> convertedClass = cls;
        if (cls != null) {

            if (cls.isPrimitive()) {
                convertedClass = PRIMITIVE_WRAPPER_MAP.get(cls);
            } else if (cls.isArray()) {
                convertedClass = Array.newInstance(cls.getComponentType(), 0).getClass();

            }
        }
        return convertedClass;
    }

    /**
     * @param origin 数据
     * @return 自动装箱
     */
    public static Object box(Object origin) {
        return origin;
    }

    /**
     * @param map       An map object
     * @param keyType   the type of map key can cast to
     * @param valueType the type of map value can cast to
     * @param <K>       the type parameter of  keyType
     * @param <V>       the type parameter of valueType
     * @return the map  cast key and value type and filter the element can not cast
     */
    public static <K, V> Map<K, V> filterCanCast(Map<?, ?> map, Class<? extends K> keyType, Class<? extends V> valueType) {

        if (map == null || keyType == null || valueType == null) {
            return new HashMap<>();
        }
        return map.entrySet().stream()
                .map(entry -> {

                    K k = cast(entry.getKey(), keyType);
                    V v = cast(entry.getValue(), valueType);
                    return LiTuple.of(k, v);
                })
                .filter(LiTuple2::notIncludeNull)
                .collect(Collectors.toMap(
                        tu -> tu._1,
                        tu -> tu._2
                ));
    }

    /**
     * @param obj      实例
     * @param castType 强转后的 class 类型
     * @param <T>      泛型
     * @return 强转后的实例，如果实例无法进行强转将会返回 null，对于基础类型因为泛型的缘故，会返回其包装类型，基础类型数组 还是会直接返回基础类型数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj, Class<T> castType) {

        if (obj == null || castType == null) {
            return null;
        }

        if (isAssignableFromOrIsWrapper(castType, obj.getClass())) {

            return (T) obj;
        }

        return null;
    }

    /**
     * @param father 父类或包装类
     * @param son    子类或包装类
     * @return {@code son} 是否继承或等于或是包装类 {@code father}，需要注意的是 数组类型的基础类型与包装类无法进行互相 cast
     */
    public static boolean isAssignableFromOrIsWrapper(Class<?> father, Class<?> son) {


        if (father != null && son != null) {

            if (father.isArray()) {

                if (son.isArray()) {

                    //对于数组，基础类型的数组无法进行强转,所以这里不能将 基础类型 视作可以继承自其包装类
                    father = father.getComponentType();
                    son = son.getComponentType();
                    if (father.isPrimitive() || son.isPrimitive()) {
                        return father == son;
                    }
                    return father.isAssignableFrom(son);
                }
            } else {

                if (father.isAssignableFrom(son)) {
                    return true;
                }
                return primitiveToWrapper(son) == primitiveToWrapper(father);
            }
        }
        return false;
    }

    /**
     * @param _interface 一个不含泛型的接口
     * @param aop        代理类
     * @param <T>        接口泛型
     * @return 返回一个使用了  _interface 接口的代理类，其实际接口的方法调用是通过 proxy 调用具有相同 signature 的方法使用的
     * @see LiMethodUtil#getSameSignatureMethod(Method, Object)
     * <p>
     * 可能会抛出 io.leaderli.litool.core.exception.LiAssertException
     */
    public static <T> T addInterface(Class<T> _interface, Object aop) {

        BitStatus bitStatus = BitStatus.of(Modifier.class);

        LiAssertUtil.assertTrue(_interface.isInterface(), "only support interface");
        LiAssertUtil.assertTrue(_interface.getTypeParameters().length == 0, "not support interface with generic of :  " + _interface.toGenericString());
        LiAssertUtil.assertTrue(_interface.getInterfaces().length == 0, "not support interface extends other interface:  " + LiStrUtil.join(",", (Object[]) _interface.getInterfaces()));

        InvocationHandler invocationHandler = (proxy, method, params) ->
                LiMethodUtil.getSameSignatureMethod(method, aop)
                        .throwable_map(m -> m.invoke(aop, params), Throwable::printStackTrace)
                        .get();
        return _interface.cast(Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{_interface}, invocationHandler));
    }


}
