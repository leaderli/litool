package io.leaderli.litool.core.type;

import java.lang.reflect.Array;

/**
 * @author leaderli
 * @since 2022/8/17 6:53 PM
 */
public enum PrimitiveEnum {

    BYTE,
    BOOLEAN,
    CHAR,
    DOUBLE,
    FLOAT,
    LONG,
    INT,
    SHORT,
    VOID,
    OBJECT;

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


}
