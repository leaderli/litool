package io.leaderli.litool.core.type;

/**
 * @author leaderli
 * @since 2022/8/17 6:53 PM
 */
public enum PrimitiveEnum {

    BYTE() {
        @Override
        public Object[] _toWrapperArray(Object obj) {
            return LiPrimitive.toWrapperArray((byte[]) obj);
        }
    },
    BOOLEAN {
        @Override
        public Object[] _toWrapperArray(Object obj) {
            return LiPrimitive.toWrapperArray((boolean[]) obj);
        }
    },
    CHAR {
        @Override
        public Object[] _toWrapperArray(Object obj) {
            return LiPrimitive.toWrapperArray((char[]) obj);
        }
    },
    DOUBLE {
        @Override
        public Object[] _toWrapperArray(Object obj) {
            return LiPrimitive.toWrapperArray((double[]) obj);
        }
    },
    FLOAT {
        @Override
        public Object[] _toWrapperArray(Object obj) {
            return LiPrimitive.toWrapperArray((float[]) obj);
        }
    },
    LONG {
        @Override
        public Object[] _toWrapperArray(Object obj) {
            return LiPrimitive.toWrapperArray((long[]) obj);
        }
    },
    INT {
        @Override
        public Object[] _toWrapperArray(Object obj) {
            return LiPrimitive.toWrapperArray((int[]) obj);
        }
    },
    SHORT {
        @Override
        public Object[] _toWrapperArray(Object obj) {
            return LiPrimitive.toWrapperArray((short[]) obj);
        }
    },
    VOID {
        @Override
        public Object[] _toWrapperArray(Object obj) {
            return new Object[0];
        }
    },
    OBJECT {
        @Override
        public Object[] _toWrapperArray(Object obj) {
            return (Object[]) obj;
        }
    };

    public static PrimitiveEnum get(Object cls) {
        return get(ClassUtil.getClass(cls));
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

    protected abstract Object[] _toWrapperArray(Object obj);

    public static Object[] toWrapperArray(Object obj) {

        if (obj == null) {
            return new Object[0];
        }
        if (obj.getClass().isArray()) {

            return PrimitiveEnum.get(obj.getClass().getComponentType())._toWrapperArray(obj);
        }
        return new Object[]{obj};
    }


}
