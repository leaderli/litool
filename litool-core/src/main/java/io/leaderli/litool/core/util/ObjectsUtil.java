package io.leaderli.litool.core.util;

import java.util.Objects;

/**
 * @author leaderli
 * @since 2022/6/16 1:02 PM
 */
public class ObjectsUtil {

    public static boolean anyNull(Object... objects) {

        for (Object object : objects) {
            if (object == null) {
                return true;
            }
        }
        return false;

    }

    /**
     * 检查对象是否为null
     *
     * @param objects 需要检查的对象
     * @throws NullPointerException 如果其中任意一个对象为null，则抛出空指针异常
     */
    public static void requireNotNull(Object... objects) {
        for (Object object : objects) {
            if (object == null) {
                throw new NullPointerException();
            }
        }
    }

    /**
     * 检查对象是否为null
     *
     * @param msg     {@link  NullPointerException(String)}
     * @param objects 需要检查的对象
     * @throws NullPointerException 如果其中任意一个对象为null，则抛出空指针异常
     */
    public static void requireNotNull(String msg, Object... objects) {
        for (Object object : objects) {
            if (object == null) {
                throw new NullPointerException(msg);
            }
        }
    }

    /**
     * @param comparing 需要比较的对象
     * @param compares  用于比较的对象数组
     * @return 如果compares数组中有任何一个对象与comparing相同，则返回true，否则返回false
     */
    public static boolean sameAny(Object comparing, Object... compares) {


        for (Object compare : compares) {
            if (compare == comparing) {
                return true;
            }
        }
        return false;

    }


    /**
     * 计算对象的哈希值
     *
     * @param obj 需要计算哈希值的对象
     * @return 对象的哈希值，如果对象为null，则返回-1
     */
    public static int hashCodeOrZero(Object obj) {
        return obj != null ? obj.hashCode() : -1;
    }

    /**
     * @param a 需要比较的对象
     * @param b 用于比较的对象
     * @return 如果a与b不相等，则返回true，否则返回false
     */
    public static boolean notEquals(Object a, Object b) {
        return !Objects.equals(a, b);
    }


}
