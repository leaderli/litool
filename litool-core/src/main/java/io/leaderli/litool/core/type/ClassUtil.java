package io.leaderli.litool.core.type;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.io.FileNameUtil;
import io.leaderli.litool.core.io.FileUtil;
import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.util.ObjectsUtil;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.*;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author leaderli
 * @since 2022-01-22
 */
public class ClassUtil {


    /**
     * 获取实例的声明类(可能是其超类，不能作为实际类使用)，支持空安全。
     * <p>
     * 声明类可能不是实例的实际类型，可能是其父类，不能作为实际类使用。
     * 例如：
     * <pre>
     * {@code
     *
     * Class<CharSequence> type = ClassUtil.getDeclaringClass("");
     * type == CharSequence // false
     * }
     * </pre>
     *
     * @param instance 实例
     * @param <T>      实例的类类型
     * @return 实例的声明类
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getDeclaringClass(T instance) {
        if (instance == null) {
            return null;
        }
        return (Class<T>) instance.getClass();
    }


    /**
     * 将通配符泛型类型缩小为指定泛型类型
     *
     * @param clazz 通配符泛型类型
     * @param <T>   类型参数，表示类或其父类的类型
     * @return {@code Class<T>} 指定泛型类型
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> narrow(final Class<? extends T> clazz) {

        return (Class<T>) clazz;
    }

    /**
     * 将包装类转换为对应的基本数据类型，如果传入的是基本数据类型或者数组类型，则直接返回。
     *
     * @param wrapperClass 要转换的包装类
     * @return 转换后的基本数据类型
     */
    public static Class<?> wrapperToPrimitive(final Class<?> wrapperClass) {

        if (wrapperClass != null && !wrapperClass.isArray()) {
            Class<?> convertedClass = PrimitiveEnum.WRAPPER_PRIMITIVE_MAP.get(wrapperClass);
            if (convertedClass != null) {
                return convertedClass;
            }
        }
        return wrapperClass;
    }

    /**
     * 将基本数据类型转化为对应的包装类，如果传入的类不是基本数据类型，或者是数组类型，则返回原类。
     * <p>
     * 参数可以为空
     *
     * @param primitiveClass 要转换的类
     * @return 转换后的类
     */
    public static Class<?> primitiveToWrapper(final Class<?> primitiveClass) {

        if (primitiveClass != null && !primitiveClass.isArray()) {
            Class<?> convertedClass = PrimitiveEnum.PRIMITIVE_WRAPPER_MAP.get(primitiveClass);
            if (convertedClass != null) {
                return convertedClass;
            }
        }
        return primitiveClass;

    }

    /**
     * 判断一个类是否为基本类型或者基本类型的包装类
     *
     * @param clazz 要判断的类
     * @return 如果是基本类型或者基本类型的包装类，返回true；否则返回false
     */
    public static boolean isPrimitiveOrWrapper(Class<?> clazz) {

        return PrimitiveEnum.PRIMITIVE_WRAPPER_MAP.containsKey(clazz) || PrimitiveEnum.PRIMITIVE_WRAPPER_MAP.containsValue(clazz);
    }

    /**
     * 返回一个由类实例元素组成的数组类型
     * <pre>
     *     int.class -> int[].class
     *     String.class -> String[].class
     * </pre>
     *
     * @param clazz 类型的 Class 对象
     * @return 由类实例元素组成的数组类型的 Class 对象
     * @throws NullPointerException 当clazz为null
     */
    public static Class<?> getArrayClass(Class<?> clazz) {


        return Array.newInstance(clazz, 0).getClass();
    }


    /**
     * 获取 classpath 下所有 jar 文件的路径
     *
     * @return classpath 下所有 jar 文件的路径列表
     */
    public static List<String> getAllJarFilePaths() {
        return getJavaClassPaths().filter(path -> path.endsWith(FileNameUtil.EXT_JAR)).get();
    }

    /**
     * 获取所有的 Java 类路径，不包括 JRE 相关的路径。
     *
     * @return 所有 Java 类路径的列表
     */
    public static Lira<String> getJavaClassPaths() {
        return Lira.of(System.getProperty("java.class.path").split(System.getProperty("path.separator"))).map(path -> path.replace(File.separatorChar, '/'));
    }


    /**
     * 获取对象 obj 的组件类型，支持空对象
     *
     * @param obj 要获取组件类型的对象
     * @return obj 对象的组件类型，如果 obj 为空则返回 null
     */
    public static Class<?> getComponentType(Object obj) {

        if (obj == null) {
            return null;
        }
        return obj.getClass().getComponentType();
    }


    /**
     * 获取类的所有父类和接口，不包括 {@link  Serializable},{@link  Cloneable},{@link  Object}
     * <p>
     * 如果类为原始类型或者数组，则返回{@code new Class[]{}}
     * <p>
     * 如果类实现了Serializable或Cloneable接口，将会被排除
     *
     * @param clazz 要获取父类和接口的类
     * @return 父类和接口的递归
     */
    public static Class<?>[] getSuperTypeAndInterfacesRecursively(Class<?> clazz) {

        Objects.requireNonNull(clazz);
        Set<Class<?>> visit = new LinkedHashSet<>();
        findSuperType(clazz, visit);
        if (clazz.isPrimitive() || clazz.isArray()) {
            return new Class[]{};
        }
        Arrays.asList(Serializable.class, Cloneable.class, Object.class).forEach(visit::remove);
        return visit.toArray(new Class[0]);
    }

    /**
     * 查找一个类的所有超类和接口, 按照其访问顺序排序
     *
     * @param clazz   待查找的类
     * @param visited 已经访问过的类和顺序
     */
    private static void findSuperType(Class<?> clazz, Set<Class<?>> visited) {

        if (clazz == Object.class) {
            return;
        }


        // 按照广度优先搜索的顺序查找超类和接口
        Class<?> superclass = clazz.getSuperclass();

        if (superclass != null && superclass != Object.class) {
            visited.add(superclass);
        }

        Collections.addAll(visited, clazz.getInterfaces());

        if (superclass != null && superclass != Object.class) {
            findSuperType(superclass, visited);
        }
        //查找其所有接口
        for (Class<?> anInterface : clazz.getInterfaces()) {
            findSuperType(anInterface, visited);
        }

    }


    /**
     * Return a specified length array, if the componentType is primitive, will
     * convert to it's wrapper
     *
     * @param componentType the class of  new array elements
     * @param length        the length of new array
     * @param <T>           the type of new array elements
     * @return a specified length array
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] newWrapperArray(Class<? extends T> componentType, int length) {
        return (T[]) Array.newInstance(primitiveToWrapper(componentType), length);
    }


    /**
     * Return return the box value
     *
     * @param origin obj
     * @return return the box value
     */
    public static Object box(Object origin) {
        return origin;
    }

    /**
     * Return the map  cast key and value type and filter the element can not cast
     *
     * @param map       An map object
     * @param keyType   the type of map key can cast to
     * @param valueType the type of map value can cast to
     * @param <K>       the type parameter of  keyType
     * @param <V>       the type parameter of valueType
     * @return the map  cast key and value type and filter the element can not cast
     */
    public static <K, V> Map<K, V> filterCanCast(Map<?, ?> map, Class<? extends K> keyType,
                                                 Class<? extends V> valueType) {

        if (map == null || keyType == null || valueType == null) {
            return new HashMap<>();
        }
        return map.entrySet().stream().map(entry -> {

            K k = cast(entry.getKey(), keyType);
            V v = cast(entry.getValue(), valueType);
            return LiTuple.of(k, v);
        }).filter(LiTuple2::notIncludeNull).collect(Collectors.toMap(tu -> tu._1, tu -> tu._2));
    }

    /**
     * Return casted instance, if obj can not cast will return {@code null}
     * return wrapper  if obj is primitive and not array
     *
     * @param obj      obj
     * @param castType the class that obj can cast
     * @param <T>      the type parameter of  castType
     * @return casted instance
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
     * Return {@code  son} is instanceof or wrapper of {@code  father}. if {@code  son} , {@code  father}
     * is array class, because wrapper array cannot cast to primitive ,so judge  {@code son} componentType
     * is instanceof {@code father} componentType
     *
     * @param father the super class  or wrapper class
     * @param son    the sub class or primitive class
     * @return {@code  son} is instanceof or wrapper of {@code  father}
     */
    public static boolean isAssignableFromOrIsWrapper(Class<?> father, Class<?> son) {

        if (father == null || son == null) {
            return false;
        }


        if (father.isArray()) {

            if (son.isArray()) {

                father = father.getComponentType();
                son = son.getComponentType();
                // the primitive array cannot cast to wrapper array
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
        return false;
    }

    /**
     * Return {@code  son} is instanceof or wrapper of {@code  father}. if {@code  son} , {@code  father}
     * is array class, because wrapper array cannot cast to primitive ,so judge  {@code son} componentType
     * is instanceof {@code father} componentType
     *
     * @param son    the sub instance
     * @param father the super class  or wrapper class
     * @return {@code  son} is instanceof or wrapper of {@code  father}
     */
    public static boolean _instanceof(Object son, Class<?> father) {

        if (father == null || son == null) {
            return false;
        }

        return isAssignableFromOrIsWrapper(father, son.getClass());

    }

    /**
     * Return the primitive value converted  by double value
     * <p>
     * support byte,boolean,char,float,double,long,int,short
     *
     * @param d             a double value
     * @param primitiveEnum a primitive enum
     * @return convert double value to other primitive value
     */
    public static Object castDouble(Double d, PrimitiveEnum primitiveEnum) {
        switch (primitiveEnum) {
            case BYTE:
                return d.byteValue();
            case BOOLEAN:
                return d != 0;
            case CHAR:
                return (char) (double) d;
            case FLOAT:
                return d.floatValue();
            case DOUBLE:
            case OBJECT:
                return d;
            case LONG:
                return d.longValue();
            case INT:
                return d.intValue();
            case SHORT:
                return d.shortValue();
            default:
                throw new IllegalStateException();
        }
    }

    private static int rank(Class<?> sub, Class<?> sup, int rank) {

        if (sub == sup) {
            return rank;
        }
        if (sub == null || sup == null || !sup.isAssignableFrom(sub)) {
            return -1;
        }
        if (sup.isInterface()) {

            Lino<Class<?>> first = Lira.of(sub.getInterfaces()).filter(sup::isAssignableFrom).first();
            if (first.present()) {
                return rank(first.get(), sup, rank + 1);
            }
        }
        return rank(sub.getSuperclass(), sup, rank + 1);
    }

    /**
     * @param sub the sub class
     * @param sup the sup class
     * @param <T> the type of sup class
     * @return the rank of sub to sup, return 0 if sub == sup
     */
    public static <T> int rank(Class<? extends T> sub, Class<T> sup) {


        ObjectsUtil.requireNotNull(sub, sup);

        return rank(sub, sup, 0);
    }

    /**
     * @param sub the sub class
     * @param sup the sup class
     * @return the rank of sub to sup, return 0 if sub == sup or sub,sup is no inheritance relationship
     */
    public static int rank0(Class<?> sub, Class<?> sup) {

        ObjectsUtil.requireNotNull(sub, sup);

        if (sup.isAssignableFrom(sub)) {
            return rank(sub, sup, 0);
        }
        if (sub.isAssignableFrom(sup)) {
            return -rank(sup, sub, 0);
        }
        return 0;

    }

    /**
     * Return  a proxy that add the interface to obj, the interface method will invoke the obj
     * same signature method.
     * <p>
     * when the interface is generic, will invoke generic-erasure  method
     *
     * @param _interface a interface
     * @param obj        the real instance that actually execute method
     * @param <T>        the type of interface
     * @return a instance  declare as interface
     * @throws io.leaderli.litool.core.exception.AssertException if _interface  is not interface
     * @see MethodUtil#getSameSignatureMethod(Object, Method)
     */
    public static <T> T addInterface(Class<T> _interface, Object obj) {

        LiAssertUtil.assertTrue(_interface.isInterface(), "only support interface");

        InvocationHandler invocationHandler = (proxy, method, params) ->
                MethodUtil.getSameSignatureMethod(obj, method)
                        .throwable_map(m -> m.invoke(obj, params), Throwable::printStackTrace)
                        .get();
        Object proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{_interface},
                invocationHandler);
        return _interface.cast(proxy);
    }

    /**
     * @param constructor the constructor
     * @param <T>         the type of constructor
     * @return the declare class of constructor
     */
    public static <T> Class<T> getDeclaringClass(Constructor<T> constructor) {

        if (constructor == null) {
            return null;
        }
        return constructor.getDeclaringClass();
    }

    /**
     * @param method the  method
     * @return the declare class of method
     */
    public static Class<?> getDeclaringClass(Method method) {
        if (method == null) {
            return null;
        }
        return method.getDeclaringClass();
    }

    /**
     * @param field the  field
     * @return the declare class of field
     */
    public static Class<?> getDeclaringClass(Field field) {
        if (field == null) {
            return null;
        }
        return field.getDeclaringClass();
    }

    /**
     * @param field the field
     * @return the type of field
     */
    public static Class<?> getType(Field field) {
        if (field == null) {
            return null;
        }
        return field.getType();
    }

    /**
     * @param method the method
     * @return the type of  method return
     */
    public static Class<?> getType(Method method) {
        if (method == null) {
            return null;
        }
        return method.getReturnType();
    }

    /**
     * @param cls the class
     * @return return the class jar file
     */
    public static Lino<File> getJarFile(Class<?> cls) {

        return Lino.of(cls)
                .map(Class::getProtectionDomain)
                .map(ProtectionDomain::getCodeSource)
                .map(CodeSource::getLocation)
                .filter(l -> FileUtil.FILE_PROTOCOL.equals(l.getProtocol()) && l.toString().endsWith(FileNameUtil.EXT_JAR))
                .map(l -> {

                    try {
                        return new File(l.toURI());
                    } catch (URISyntaxException e) {
                        return new File(l.getPath());
                    }
                });
    }
}
