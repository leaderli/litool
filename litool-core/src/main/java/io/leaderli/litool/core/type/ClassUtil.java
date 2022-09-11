package io.leaderli.litool.core.type;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.io.FileNameUtil;
import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.core.meta.Lira;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author leaderli
 * @since 2022-01-22
 */
public class ClassUtil {


    /**
     * Return the assumed class of instance , null-safe.
     * <p>
     * the assumed class may not the real class of instance,  it
     * may only the super class of instance, and it can not be used
     * as  assumed class.
     * eg:
     * <pre>
     *  Class&lt;CharSequence> type = ClassUtil.getClass("");
     *  type == CharSequence // false
     * </pre>
     * type cannot be used as CharSequence
     *
     * @param def the instance
     * @param <T> the type of instance class
     * @return the assumed class of instance
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClass(T def) {
        if (def == null) {
            return null;
        }
        return (Class<T>) def.getClass();
    }


    /**
     * narrow wild generic type to  specifier generic type
     *
     * @param cls the class
     * @param <T> the type parameter of class or it's superclass
     * @return {@code Class<T>}
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> narrow(final Class<? extends T> cls) {

        return (Class<T>) cls;
    }

    /**
     * Return primitive class  when class is wrapper class and is not
     * array class, otherwise  return it self.
     *
     * <p>
     * it's null-safe
     *
     * @param cls the class to be convert
     * @return the converted class
     */
    public static Class<?> wrapperToPrimitive(final Class<?> cls) {

        if (cls != null && !cls.isArray()) {
            Class<?> convertedClass = PrimitiveEnum.WRAPPER_PRIMITIVE_MAP.get(cls);
            if (convertedClass != null) {
                return convertedClass;
            }
        }
        return cls;
    }

    /**
     * The type of an array consisting of the elements of instance of the class
     *
     * @param type a class
     * @return type of an array consisting of the elements of
     */
    public static Class<?> getArrayClass(Class<?> type) {


        return Array.newInstance(type, 0).getClass();
    }

    /**
     * @return get all jar file path under classPath
     */
    public static List<String> getAppJars() {

        return getJavaClassPaths().filter(f -> f.endsWith(FileNameUtil.EXT_JAR)).get();

    }

    /**
     * @return get all classPath except jre
     */
    public static Lira<String> getJavaClassPaths() {
        return Lira.of(System.getProperty("java.class.path").split(System.getProperty("path.separator"))).map(path -> path.replace(File.separatorChar, '/'));
    }

    /**
     * Return whether class is primitive or wrapper
     *
     * @param cls the class
     * @return whether class is primitive or wrapper
     */
    public static boolean isPrimitiveOrWrapper(Class<?> cls) {

        return PrimitiveEnum.PRIMITIVE_WRAPPER_MAP.containsKey(cls) || PrimitiveEnum.PRIMITIVE_WRAPPER_MAP.containsValue(cls);
    }

    /**
     * return {@code  null} if obj is not array otherwise cast to array
     * <p>
     * if array's element is primitive, will convert to  wrapper array
     *
     * @param Obj the array that declare as Object
     * @param <T> the type of array
     * @return casted array
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Object Obj) {

        Class<?> componentType = getComponentType(Obj);
        if (componentType == null) {
            return null;
        }
        int length = Array.getLength(Obj);
        T[] objects = (T[]) newWrapperArray(componentType, length);

        for (int i = 0; i < length; i++) {
            objects[i] = (T) Array.get(Obj, i);
        }

        return objects;
    }

    /**
     * Return the componentType of obj class, it's null-safe
     *
     * @param obj the obj
     * @return the componentType of obj class
     */
    public static Class<?> getComponentType(Object obj) {

        if (obj == null) {
            return null;
        }
        return obj.getClass().getComponentType();
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
     * Return wrapper class  when class is primitive class and is not
     * array class, otherwise  return it self.
     *
     * <p>
     * it's null-safe
     *
     * @param cls the class to be convert
     * @return the converted class
     */
    public static Class<?> primitiveToWrapper(final Class<?> cls) {

        if (cls != null && !cls.isArray()) {
            Class<?> convertedClass = PrimitiveEnum.PRIMITIVE_WRAPPER_MAP.get(cls);
            if (convertedClass != null) {
                return convertedClass;
            }
        }
        return cls;

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
     * Return casted instance, if obj can not cast will return {@link  null}
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
     * @see MethodUtil#getSameSignatureMethod(Method, Object)
     */
    public static <T> T addInterface(Class<T> _interface, Object obj) {

        LiAssertUtil.assertTrue(_interface.isInterface(), "only support interface");

        InvocationHandler invocationHandler = (proxy, method, params) ->
                MethodUtil.getSameSignatureMethod(method, obj)
                        .throwable_map(m -> m.invoke(obj, params), Throwable::printStackTrace)
                        .get();
        Object proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{_interface},
                invocationHandler);
        return _interface.cast(proxy);
    }


}
