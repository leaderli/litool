package io.leaderli.litool.core.type;

import io.leaderli.litool.core.util.ObjectsUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/8/17 6:53 PM
 */
public enum PrimitiveEnum {
    BYTE(PrimitiveZeroValue.BYTE, Byte.TYPE, Byte.class),
    BOOLEAN(PrimitiveZeroValue.BOOLEAN, Boolean.TYPE, Boolean.class),
    CHAR(PrimitiveZeroValue.CHAR, Character.TYPE, Character.class),
    DOUBLE(PrimitiveZeroValue.DOUBLE, Double.TYPE, Double.class),
    FLOAT(PrimitiveZeroValue.FLOAT, Float.TYPE, Float.class),
    LONG(PrimitiveZeroValue.LONG, Long.TYPE, Long.class),
    INT(PrimitiveZeroValue.INT, Integer.TYPE, Integer.class),
    SHORT(PrimitiveZeroValue.SHORT, Short.TYPE, Short.class),
    VOID(null, Void.TYPE, Void.class),
    OBJECT(null, null, null);

    static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_MAP = new HashMap<>();
    static final Map<Class<?>, Class<?>> WRAPPER_PRIMITIVE_MAP = new HashMap<>();

    static {

        Arrays.stream(values())
                .filter(e -> e != OBJECT)
                .forEach(tu -> {

                    PRIMITIVE_WRAPPER_MAP.put(tu.primitive, tu.wrapper);
                    WRAPPER_PRIMITIVE_MAP.put(tu.wrapper, tu.primitive);
                });

    }

    /**
     * zero value
     */
    public final Object zero_value;
    public final Class<?> primitive;
    public final Class<?> wrapper;

    PrimitiveEnum(Object zero_value, Class<?> primitive, Class<?> wrapper) {
        this.zero_value = zero_value;
        this.primitive = primitive;
        this.wrapper = wrapper;
    }

    /**
     * @param obj the obj
     * @return is instanceof {@link Number}
     */
    public static boolean isNumber(Object obj) {
        return isNumber(get(obj));
    }

    /**
     * <ul>
     *     <li>{@link  #BYTE}</li>
     *     <li>{@link  #FLOAT}</li>
     *     <li>{@link  #DOUBLE}</li>
     *     <li>{@link  #LONG}</li>
     *     <li>{@link  #INT}</li>
     *     <li>{@link  #SHORT}</li>
     * </ul>
     *
     * @param primitiveEnum the  primitive enum
     * @return the primitive is the number
     */
    public static boolean isNumber(PrimitiveEnum primitiveEnum) {
        return ObjectsUtil.sameAny(primitiveEnum, BYTE, FLOAT, DOUBLE, LONG, INT, SHORT);
    }

    /**
     * Return the primitive enum related to obj
     *
     * @param obj the obj
     * @return the primitive enum related to obj
     * @see #get(Class)
     */
    public static PrimitiveEnum get(Object obj) {
        return get(ClassUtil.getClass(obj));
    }

    /**
     * Return the primitive enum related to cls
     *
     * @param cls the cls
     * @return the primitive enum related to cls
     * @see #get(Class)
     */
    public static PrimitiveEnum get(Class<?> cls) {
        cls = ClassUtil.primitiveToWrapper(cls);


        if (cls == Byte.class) {
            return BYTE;
        }
        if (cls == Boolean.class) {
            return BOOLEAN;
        }
        if (cls == Character.class) {
            return CHAR;
        }
        if (cls == Double.class) {
            return DOUBLE;
        }
        if (cls == Float.class) {
            return FLOAT;
        }
        if (cls == Long.class) {
            return LONG;
        }
        if (cls == Integer.class) {
            return INT;
        }

        if (cls == Short.class) {
            return SHORT;
        }
        if (cls == Void.class) {
            return VOID;
        }

        return OBJECT;
    }

    /**
     * @param cls the class
     * @return the class is one of sub class of  {@link  Number}
     */
    public static boolean isNumber(Class<?> cls) {

        return isNumber(get(cls));
    }

    /**
     * @param type the class
     * @param <T>  the type parameter of class
     * @return the class zero value
     */
    @SuppressWarnings({"unchecked", "java:S1845"})
    public static <T> T zero_value(Class<T> type) {
        return (T) get(type).zero_value;
    }


    @SuppressWarnings("all")
    static class PrimitiveZeroValue {
        public static byte BYTE;
        public static boolean BOOLEAN;
        public static char CHAR;
        public static double DOUBLE;
        public static float FLOAT;
        public static long LONG;
        public static int INT;
        public static short SHORT;
    }
}
