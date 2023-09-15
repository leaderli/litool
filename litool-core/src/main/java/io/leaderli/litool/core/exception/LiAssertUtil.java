package io.leaderli.litool.core.exception;


import java.util.function.Function;
import java.util.function.Supplier;

/**
 * LiAssertUtil是一个断言工具类，提供了多种判断条件并抛出异常的方法
 */
public class LiAssertUtil {

    /**
     * 抛出一个IllegalStateException异常，提示不应该运行到这里
     */
    public static void assertNotRun() {
        throw new IllegalStateException(" assert not to run");
    }

    /**
     * 抛出一个IllegalStateException异常，并带有自定义的错误信息
     *
     * @param msg 错误信息
     * @param <T> 返回类型
     * @return 返回类型的默认值
     */
    public static <T> T assertNotRunWithMsg(String msg) {
        throw new IllegalStateException(msg);
    }

    /**
     * 判断assertTrue是否为true，如果为false就抛出一个AssertException异常
     *
     * @param assertTrue 判断条件
     */
    public static void assertTrue(boolean assertTrue) {
        assertTrue(assertTrue, "");
    }

    /**
     * 判断assertTrue是否为true，如果为false就抛出一个自定义的异常，并带有错误信息
     *
     * @param assertTrue 判断条件
     * @param msg        错误信息
     */
    public static void assertTrue(boolean assertTrue, String msg) {
        if (!assertTrue) {
            throw new AssertException(msg);
        }
    }

    /**
     * 判断assertTrue是否为true，如果为false就抛出一个RuntimeException异常
     *
     * @param assertTrue 判断条件
     * @param throwable  RuntimeException异常
     */
    public static void assertTrue(boolean assertTrue, RuntimeException throwable) {
        if (!assertTrue) {
            throw throwable;
        }
    }

    /**
     * 判断assertTrue是否为true，如果为false就抛出一个自定义的异常，并带有错误信息
     *
     * @param assertTrue 判断条件
     * @param throwable  自定义异常
     * @param msg        错误信息
     */
    public static void assertTrue(boolean assertTrue, Function<String, RuntimeException> throwable, String msg) {
        if (!assertTrue) {
            throw throwable.apply(msg);
        }
    }

    /**
     * 判断对象obj是否为null，如果为null就抛出一个自定义的异常，并带有错误信息
     *
     * @param obj       判断对象
     * @param throwable 自定义异常
     * @param msg       错误信息
     */
    public static void assertNotNull(Object obj, Function<String, RuntimeException> throwable, String msg) {
        if (obj == null) {
            throw throwable.apply(msg);
        }
    }

    /**
     * 判断对象obj是否为null，如果为null就抛出异常，并带有错误信息
     *
     * @param obj 判断对象
     * @param msg 错误信息
     */
    public static void assertNotNull(Object obj, String msg) {
        if (obj == null) {
            throw new NullPointerException(msg);
        }
    }

    /**
     * 判断assertTrue是否为true，如果为false就抛出一个自定义的异常，并带有错误信息
     *
     * @param assertTrue 判断条件
     * @param supplier   错误信息的Supplier
     */
    public static void assertTrue(boolean assertTrue, Supplier<String> supplier) {
        if (!assertTrue) {
            throw new AssertException(supplier.get());
        }
    }

    /**
     * 断言assertFalse是否为false，如果为true就抛出一个AssertException异常
     *
     * @param assertFalse 判断条件
     */
    public static void assertFalse(boolean assertFalse) {
        assertFalse(assertFalse, "");
    }

    /**
     * 断言assertFalse是否为false，如果为true就抛出一个AssertException异常，并带有错误信息
     *
     * @param assertFalse 判断条件
     * @param msg         错误信息
     */
    public static void assertFalse(boolean assertFalse, String msg) {
        if (assertFalse) {
            throw new AssertException(msg);
        }
    }

    /**
     * 断言assertFalse是否为false，如果为true就抛出一个AssertException异常，并带有错误信息
     *
     * @param assertFalse 判断条件
     * @param supplier    错误信息的Supplier
     */
    public static void assertFalse(boolean assertFalse, Supplier<String> supplier) {
        if (assertFalse) {
            throw new AssertException(supplier.get());
        }
    }
}
