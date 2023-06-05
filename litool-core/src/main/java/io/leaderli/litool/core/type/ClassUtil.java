package io.leaderli.litool.core.type;

import io.leaderli.litool.core.io.FileNameUtil;
import io.leaderli.litool.core.io.FileUtil;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.util.ObjectsUtil;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.*;

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
        if (clazz.isPrimitive() || clazz.isArray()) {
            return new Class[]{};
        }
        Set<Class<?>> visit = new LinkedHashSet<>();
        visitSuperTypes(clazz, visit);
        Arrays.asList(Serializable.class, Cloneable.class, Object.class).forEach(visit::remove);
        return visit.toArray(new Class[0]);
    }

    /**
     * 查找一个类的所有超类和接口, 按照其访问顺序排序
     *
     * @param clazz   待查找的类
     * @param visited 已经访问过的类和顺序
     */
    private static void visitSuperTypes(Class<?> clazz, Set<Class<?>> visited) {

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
            visitSuperTypes(superclass, visited);
        }
        //查找其所有接口
        for (Class<?> anInterface : clazz.getInterfaces()) {
            visitSuperTypes(anInterface, visited);
        }

    }


    /**
     * 创建一个指定长度的数组，如果元素类型是基本类型，将转换为其包装类型
     *
     * @param componentType 新数组元素的类
     * @param length        新数组的长度
     * @param <T>           新数组元素的类型
     * @return 指定长度的新数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] newWrapperArray(Class<? extends T> componentType, int length) {
        return (T[]) Array.newInstance(primitiveToWrapper(componentType), length);
    }


    /**
     * 将参数对象进行装箱操作
     *
     * @param obj 需要进行装箱操作的对象
     * @return 装箱后的对象
     */
    public static Object box(Object obj) {
        return obj;
    }

    /**
     * 将给定的map对象中的键和值的类型转换为指定的类型，并过滤不能转换的元素，可为null的不会被保留
     *
     * @param map       需要转换的map对象
     * @param keyType   map键的类型，可以转换为的类型
     * @param valueType map值的类型，可以转换为的类型
     * @param <K>       keyType的类型参数
     * @param <V>       valueType的类型参数
     * @return 转换后的map对象，键和值的类型均已转换，并过滤了不能转换的元素
     */
    public static <K, V> Map<K, V> filterCanCast(Map<?, ?> map, Class<? extends K> keyType,
                                                 Class<? extends V> valueType) {

        HashMap<K, V> kvHashMap = new HashMap<>();
        if (map == null || keyType == null || valueType == null) {
            return kvHashMap;
        }

        for (Map.Entry<?, ?> entry : map.entrySet()) {

            K k = cast(entry.getKey(), keyType);
            V v = cast(entry.getValue(), valueType);

            if (k != null && v != null) {

                kvHashMap.put(k, v);
            }

        }
        return kvHashMap;
    }

    /**
     * 将对象转换为指定类型，如果无法转换则返回null，如果是原始类型并且不是数组，则返回包装器
     *
     * @param obj      需要转换的对象
     * @param castType 可以转换为的目标类型
     * @param <T>      目标类型的泛型参数
     * @return 转换后的实例
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
     * 判断一个类是否是另一个类的子类或者包装类。如果两个类都是数组类型，因为包装类数组不能强制转换为原始类型数组，
     * <p>
     * 所以需要判断 son 的元素类型是否是 father 的元素类型的子类或者包装类。
     *
     * @param father 父类或包装类
     * @param son    子类或原始类型类
     * @return {@code son} 是否是 {@code father} 的子类或包装类
     */
    public static boolean isAssignableFromOrIsWrapper(Class<?> father, Class<?> son) {

        if (father == null || son == null) {
            return false;
        }


        if (father.isArray()) {

            if (son.isArray()) {

                father = father.getComponentType();
                son = son.getComponentType();
                // 原始类型数组不能强制转换为包装类数组
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
     * 判断一个对象是否是某个类或其包装类的实例，如果son和father都是数组类型，因为包装数组不能强制转换为基本类型，所以需要判断son的component type是否是father的component type的实例。
     *
     * @param son    子类实例
     * @param father 父类或包装类
     * @return {@code son} 是否是 {@code father} 或其包装类的实例
     * @see #isAssignableFromOrIsWrapper(Class, Class)
     */
    public static boolean isInstanceof(Object son, Class<?> father) {

        if (father == null || son == null) {
            return false;
        }

        return isAssignableFromOrIsWrapper(father, son.getClass());

    }

    /**
     * 将double类型的值转换为其他基本数据类型
     * <p>
     * 支持byte、boolean、char、float、double、long、int、short
     *
     * @param doubleValue   double类型的值
     * @param primitiveEnum 基本数据类型枚举
     * @return 转换后的其他基本数据类型值
     */
    public static Object convertDoubleToPrimitive(Double doubleValue, PrimitiveEnum primitiveEnum) {
        switch (primitiveEnum) {
            case BYTE:
                return doubleValue.byteValue();
            case BOOLEAN:
                return doubleValue != 0;
            case CHAR:
                return (char) (double) doubleValue;
            case FLOAT:
                return doubleValue.floatValue();
            case DOUBLE:
            case OBJECT:
                return doubleValue;
            case LONG:
                return doubleValue.longValue();
            case INT:
                return doubleValue.intValue();
            case SHORT:
                return doubleValue.shortValue();
            case VOID:
                return null;
            default:
                throw new IllegalStateException();
        }
    }

    /**
     * @param subclass 子类
     * @param supclass 父类
     * @return 回从 subclass 到 supclass 的类层次结构中的距离，如果 subclass 不是 supclass 的子类，则返回 -1。
     * 如果两个类相同，则返回0
     */
    private static int getClassHierarchyDistance0(Class<?> subclass, Class<?> supclass) {

        if (subclass == supclass) {
            return 0;
        }
        if (subclass == null || supclass == null || !supclass.isAssignableFrom(subclass)) {
            return -1;
        }
        int distance;
        if (supclass.isInterface()) {

            for (Class<?> anInterface : subclass.getInterfaces()) {

                if (supclass.isAssignableFrom(anInterface)) {
                    distance = getClassHierarchyDistance0(anInterface, supclass);
                    if (distance > -1) {
                        distance++;
                    }
                    return distance;
                }
            }
        }
        distance = getClassHierarchyDistance(subclass.getSuperclass(), supclass);
        if (distance > -1) {
            distance++;
        }
        return distance;
    }


    /**
     * @param a 类
     * @param b 类
     * @return 两个类的继承层级，如果两个类不具备继承关系则返回0
     */
    public static int getClassHierarchyDistance(Class<?> a, Class<?> b) {

        if (ObjectsUtil.anyNull(a, b)) {
            return 0;
        }

        if (b.isAssignableFrom(a)) {
            int distance = getClassHierarchyDistance0(a, b);
            return Math.max(distance, 0);
        }
        if (a.isAssignableFrom(b)) {
            int distance = getClassHierarchyDistance0(b, a);
            if (distance < 0) {
                return 0;
            }
            return -distance;
        }
        return 0;

    }

    /**
     * 获取构造方法所属的类。
     *
     * @param constructor 构造方法
     * @param <T>         构造方法的类型
     * @return 构造方法所属的类
     */
    public static <T> Class<T> getDeclaringClass(Constructor<T> constructor) {

        if (constructor == null) {
            return null;
        }
        return constructor.getDeclaringClass();
    }

    /**
     * 获取方法所属的类。
     *
     * @param method 方法
     * @return 构造方法所属的类
     */
    public static Class<?> getDeclaringClass(Method method) {
        if (method == null) {
            return null;
        }
        return method.getDeclaringClass();
    }

    /**
     * 获取字段的声明类
     *
     * @param field 字段
     * @return 字段的声明类
     */
    public static Class<?> getDeclaringClass(Field field) {
        if (field == null) {
            return null;
        }
        return field.getDeclaringClass();
    }

    /**
     * 获取字段的类型
     *
     * @param field 字段
     * @return 获取字段的类型
     */
    public static Class<?> getType(Field field) {
        if (field == null) {
            return null;
        }
        return field.getType();
    }

    /**
     * @param method 方法
     * @return 获取方法的返回类型
     */
    public static Class<?> getType(Method method) {
        if (method == null) {
            return null;
        }
        return method.getReturnType();
    }

    /**
     * 获取一个类的Jar文件
     *
     * @param clazz 需要获取Jar文件的类
     * @return 返回包含该类所在Jar文件的Lino对象
     */
    public static Lino<File> getJarFile(Class<?> clazz) {

        return Lino.of(clazz)
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
