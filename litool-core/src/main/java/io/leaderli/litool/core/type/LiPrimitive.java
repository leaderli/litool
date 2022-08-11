package io.leaderli.litool.core.type;

import java.lang.reflect.Array;

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
    private static final TypeMap ZERO_VALUE = new TypeMap();

    static {
        ZERO_VALUE.put(boolean.class, BOOLEAN);
        ZERO_VALUE.put(byte.class, BYTE);
        ZERO_VALUE.put(char.class, CHAR);
        ZERO_VALUE.put(double.class, DOUBLE);
        ZERO_VALUE.put(float.class, FLOAT);
        ZERO_VALUE.put(int.class, INT);
        ZERO_VALUE.put(long.class, LONG);
        ZERO_VALUE.put(short.class, SHORT);

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
    public static <T> T def(Class<T> type) {
        type = (Class<T>) ClassUtil.primitiveToWrapper(type);
        return ZERO_VALUE.get(type).get();
    }


    public static Byte[] toWrapperArray(byte[] arr) {

        Byte[] result = (Byte[]) Array.newInstance(Byte.class, arr.length);

        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

    public static Boolean[] toWrapperArray(boolean[] arr) {

        Boolean[] result = (Boolean[]) Array.newInstance(Boolean.class, arr.length);

        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

    public static Character[] toWrapperArray(char[] arr) {

        Character[] result = (Character[]) Array.newInstance(Character.class, arr.length);

        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

    public static Double[] toWrapperArray(double[] arr) {

        Double[] result = (Double[]) Array.newInstance(Double.class, arr.length);

        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

    public static Float[] toWrapperArray(float[] arr) {

        Float[] result = (Float[]) Array.newInstance(Float.class, arr.length);

        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

    public static Long[] toWrapperArray(long[] arr) {

        Long[] result = (Long[]) Array.newInstance(Long.class, arr.length);

        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

    public static Integer[] toWrapperArray(int[] arr) {

        Integer[] result = (Integer[]) Array.newInstance(Integer.class, arr.length);

        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

    public static Short[] toWrapperArray(short[] arr) {

        Short[] result = (Short[]) Array.newInstance(Short.class, arr.length);

        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

    @SuppressWarnings("all")
    private static class PrimitiveValue {
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
