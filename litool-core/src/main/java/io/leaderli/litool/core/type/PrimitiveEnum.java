package io.leaderli.litool.core.type;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.text.StrSubstitution;
import io.leaderli.litool.core.text.StringConvert;
import io.leaderli.litool.core.util.BooleanUtil;
import io.leaderli.litool.core.util.ObjectsUtil;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 枚举类，封装了Java的基本数据类型及其包装类型，提供了一些常用的操作方法。
 *
 * @author leaderli
 * @since 2022 /8/17 6:53 PM
 */
public enum PrimitiveEnum {
    /**
     * The Byte.
     */
    BYTE(PrimitiveZeroValue.BYTE, Byte.TYPE, Byte.class, obj -> {
        if (obj instanceof Number) {
            return ((Number) obj).byteValue();
        }
        return null;
    }),
    /**
     * Boolean primitive enum.
     */
    BOOLEAN(PrimitiveZeroValue.BOOLEAN, Boolean.TYPE, Boolean.class, BooleanUtil::parse),
    /**
     * The Char.
     */
    CHAR(PrimitiveZeroValue.CHAR, Character.TYPE, Character.class, obj -> {
        if (obj instanceof Number) {
            return (char) ((Number) obj).intValue();
        }
        return null;
    }),
    /**
     * The Double.
     */
    DOUBLE(PrimitiveZeroValue.DOUBLE, Double.TYPE, Double.class, obj -> {
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        return null;
    }),
    /**
     * The Float.
     */
    FLOAT(PrimitiveZeroValue.FLOAT, Float.TYPE, Float.class, obj -> {
        if (obj instanceof Number) {
            return ((Number) obj).floatValue();
        }
        return null;
    }),
    /**
     * The Long.
     */
    LONG(PrimitiveZeroValue.LONG, Long.TYPE, Long.class, obj -> {
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }
        return null;
    }),
    /**
     * The Int.
     */
    INT(PrimitiveZeroValue.INT, Integer.TYPE, Integer.class, obj -> {
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        return null;
    }),
    /**
     * The Short.
     */
    SHORT(PrimitiveZeroValue.SHORT, Short.TYPE, Short.class, obj -> {
        if (obj instanceof Number) {
            return ((Number) obj).shortValue();
        }
        return null;
    }),
    /**
     * Void primitive enum.
     */
    VOID(null, Void.TYPE, Void.class),
    /**
     * Object primitive enum.
     */
    OBJECT(null, null, null);

    /**
     * The constant PRIMITIVES.
     */
    public static final PrimitiveEnum[] PRIMITIVES;
    /**
     * The Primitive wrapper map.
     */
    static final Map<Type, Class<?>> PRIMITIVE_WRAPPER_MAP = new HashMap<>();
    /**
     * The Wrapper primitive map.
     */
    static final Map<Type, Class<?>> WRAPPER_PRIMITIVE_MAP = new HashMap<>();

    static {

        PRIMITIVES = Arrays.stream(values()).filter(e -> e != OBJECT).toArray(PrimitiveEnum[]::new);
        for (PrimitiveEnum tu : PRIMITIVES) {
            PRIMITIVE_WRAPPER_MAP.put(tu.primitive, tu.wrapper);
            WRAPPER_PRIMITIVE_MAP.put(tu.wrapper, tu.primitive);
        }

    }

    /**
     * zero value
     */
    public final Object zero_value;
    /**
     * The Primitive.
     */
    public final Class<?> primitive;
    /**
     * The Wrapper.
     */
    public final Class<?> wrapper;
    /**
     * The Convert.
     */
    public final Function<Object, ?> convert;

    @SuppressWarnings("unchecked")
    <T> PrimitiveEnum(T zero_value, Class<?> primitive, Class<T> wrapper) {
        this(zero_value, primitive, wrapper, o -> (T) o);
    }

    <T> PrimitiveEnum(T zero_value, Class<?> primitive, Class<T> wrapper, Function<Object, T> convert) {
        this.zero_value = zero_value;
        this.primitive = primitive;
        this.wrapper = wrapper;
        this.convert = convert;
    }

    /**
     * @param obj the obj
     * @return 如果是基本数字类型，返回true，否则返回false
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
     * @return 如果是基本数字类型，返回true，否则返回false
     */
    public static boolean isNumber(PrimitiveEnum primitiveEnum) {
        return ObjectsUtil.sameAny(primitiveEnum, BYTE, FLOAT, DOUBLE, LONG, INT, SHORT);
    }

    /**
     * 获取指定类型的PrimitiveEnum实例
     *
     * @param obj 类
     * @return PrimitiveEnum实例，如果不是基本数据类型或其包装类型，返回 {@link #OBJECT}
     */
    public static PrimitiveEnum get(Object obj) {
        return get(ClassUtil.getDeclaringClass(obj));
    }

    /**
     * 获取指定类型的PrimitiveEnum实例
     *
     * @param clazz 类型
     * @return PrimitiveEnum实例，如果不是基本数据类型或其包装类型，返回 {@link #OBJECT}
     */
    public static PrimitiveEnum get(Class<?> clazz) {

        clazz = ClassUtil.primitiveToWrapper(clazz);

        if (clazz == Byte.class) {
            return BYTE;
        }
        if (clazz == Boolean.class) {
            return BOOLEAN;
        }
        if (clazz == Character.class) {
            return CHAR;
        }
        if (clazz == Double.class) {
            return DOUBLE;
        }
        if (clazz == Float.class) {
            return FLOAT;
        }
        if (clazz == Long.class) {
            return LONG;
        }
        if (clazz == Integer.class) {
            return INT;
        }

        if (clazz == Short.class) {
            return SHORT;
        }
        if (clazz == Void.class) {
            return VOID;
        }

        return OBJECT;
    }

    /**
     * @param name 类型名称
     * @return 根据名称返回类型
     */
    public static PrimitiveEnum get(String name) {
        for (PrimitiveEnum value : values()) {
            if (value.name().equalsIgnoreCase(name)) {
                return value;
            }
        }
        return OBJECT;
    }

    /**
     * 判断指定的类型是否为基本数据类型或其包装类型
     *
     * @param type 要判断的类型
     * @return 如果是基本数据类型或其包装类型，返回true，否则返回false
     */
    public static boolean isPrimitive(Type type) {
        return PRIMITIVE_WRAPPER_MAP.get(type) != null;
    }

    /**
     * 断言类型不是基本类型
     *
     * @param type 类型
     */
    public static void checkNotPrimitive(Type type) {
        LiAssertUtil.assertFalse(isPrimitive(type));
    }

    /**
     * Is number boolean.
     *
     * @param cls the class
     * @return the class is one of sub class of  {@link  Number}
     */
    public static boolean isNumber(Class<?> cls) {

        return isNumber(get(cls));
    }

    /**
     * 获取指定类型的零值
     *
     * @param type 类型
     * @return 零值，如果不是基本数据类型或其包装类型，返回null
     */
    @SuppressWarnings({"unchecked", "java:S1845"})
    public static <T> T zero_value(Class<T> type) {
        return (T) get(type).zero_value;
    }


    /**
     * 如果value为null，则返回{@link #zero_value}
     *
     * @param <T>   枚举所指代的类型
     * @param value 值
     * @return 将值转换为枚举所指代的类型
     * @see StringConvert#parser(String, Object)
     */
    @SuppressWarnings("unchecked")
    public <T> T read(Object value) {

        if (get(value) == this) {
            return (T) value;
        }
        if (value == null) {
            return (T) zero_value;
        }
        return (T) Lino.of(value)
                .cast(String.class)
                .map(v -> (Object) StringConvert.parser(wrapper, v).get())
                .or(() -> convert.apply(value))
                .assertNotNone(() -> StrSubstitution.format("{value} not support convert to {type}", value, this))
                .get();
    }

    /**
     * The type Primitive zero value.
     */
    @SuppressWarnings("all")
    static class PrimitiveZeroValue {
        /**
         * The constant BYTE.
         */
        public static byte BYTE;
        /**
         * The constant BOOLEAN.
         */
        public static boolean BOOLEAN;
        /**
         * The constant CHAR.
         */
        public static char CHAR;
        /**
         * The constant DOUBLE.
         */
        public static double DOUBLE;
        /**
         * The constant FLOAT.
         */
        public static float FLOAT;
        /**
         * The constant LONG.
         */
        public static long LONG;
        /**
         * The constant INT.
         */
        public static int INT;
        /**
         * The constant SHORT.
         */
        public static short SHORT;
    }
}
