package io.leaderli.litool.core.type;

/**
 * @author leaderli
 * @since 2022/6/17
 */
public class LiPrimitive {


    public static final boolean BOOLEAN = PrimitiveValue.BOOLEAN;
    public static final byte BYTE = PrimitiveValue.BYTE;
    public static final char CHAR = PrimitiveValue.CHAR;
    public static final double DOUBLE = PrimitiveValue.DOUBLE;
    public static final float FLOAT = PrimitiveValue.FLOAT;
    public static final int INT = PrimitiveValue.INT;
    public static final long LONG = PrimitiveValue.LONG;
    public static final short SHORT = PrimitiveValue.SHORT;
    private static final LiTypeMap ZERO_VALUE = new LiTypeMap();

    static {
        ZERO_VALUE.put(Boolean.class, BOOLEAN);
        ZERO_VALUE.put(Byte.class, BYTE);
        ZERO_VALUE.put(Character.class, CHAR);
        ZERO_VALUE.put(Double.class, DOUBLE);
        ZERO_VALUE.put(Float.class, FLOAT);
        ZERO_VALUE.put(Integer.class, INT);
        ZERO_VALUE.put(Long.class, LONG);
        ZERO_VALUE.put(Short.class, SHORT);
        ZERO_VALUE.put(Void.class, null);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> type) {
        type = (Class<T>) LiClassUtil.primitiveToWrapper(type);
        return ZERO_VALUE.get(type).get();
    }


    @SuppressWarnings("all")
    private static class PrimitiveValue {
        public static int INT;
        public static char CHAR;
        public static byte BYTE;
        public static long LONG;
        public static boolean BOOLEAN;
        public static double DOUBLE;
        public static float FLOAT;
        public static short SHORT;
    }


}
