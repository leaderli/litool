package io.leaderli.litool.core.type;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/8/17 6:53 PM
 */
public enum PrimitiveEnum {
    BYTE(PrimitiveValue.BYTE, Byte.TYPE, Byte.class),
    BOOLEAN(PrimitiveValue.BOOLEAN, Boolean.TYPE, Boolean.class),
    CHAR(PrimitiveValue.CHAR, Character.TYPE, Character.class),
    DOUBLE(PrimitiveValue.DOUBLE, Double.TYPE, Double.class),
    FLOAT(PrimitiveValue.FLOAT, Float.TYPE, Float.class),
    LONG(PrimitiveValue.LONG, Long.TYPE, Long.class),
    INT(PrimitiveValue.INT, Integer.TYPE, Integer.class),
    SHORT(PrimitiveValue.SHORT, Short.TYPE, Short.class),
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
     * 零值，默认值
     */
    public final Object def;
    public final Class<?> primitive;
    public final Class<?> wrapper;

    PrimitiveEnum(Object def, Class<?> primitive, Class<?> wrapper) {
        this.def = def;
        this.primitive = primitive;
        this.wrapper = wrapper;
    }

    public static boolean isNumber(Object number) {
        return isNumber(get(number));
    }

    public static boolean isNumber(Class<?> number) {

        return isNumber(get(number));
    }

    public static boolean isNumber(PrimitiveEnum number) {
        return EnumUtil.sameAny(number, BYTE, FLOAT, DOUBLE, LONG, INT, SHORT);
    }

    public static PrimitiveEnum get(Object obj) {
        return get(ClassUtil.getClass(obj));
    }

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


    public static Object[] toWrapperArray(Object obj) {

        if (obj == null) {
            return null;
        }
        if (obj.getClass().isArray()) {

            Class<?> componentType = obj.getClass().getComponentType();
            if (componentType.isPrimitive()) {

                int length = Array.getLength(obj);
                Object[] newArr = (Object[]) Array.newInstance(ClassUtil.primitiveToWrapper(componentType), length);


                for (int i = 0; i < length; i++) {

                    newArr[i] = Array.get(obj, i);
                }

                return newArr;

            }
            return (Object[]) obj;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T def(Class<T> type) {
        return (T) get(type).def;
    }


    @SuppressWarnings("all")
    static class PrimitiveValue {
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
