package io.leaderli.litool.core.type;

/**
 * ClassLoaderUtil 提供了一些获取类加载器的方法
 * 注意：如果当前线程上下文类加载器为null，则使用系统类加载器
 *
 * @since 2022/7/17
 */
public class ClassLoaderUtil {

    /**
     * 获取类加载器，按照以下顺序查找：
     * <pre>
     * 1、获取上下文类加载器 {@link #getContextClassLoader()}
     * 2、获取系统类加载器 {@link ClassLoader#getSystemClassLoader()}）
     * </pre>
     *
     * @return 返回找到的类加载器
     */
    public static ClassLoader getClassLoader() {
        ClassLoader classLoader = getContextClassLoader();
        if (classLoader == null) {
            classLoader = getSystemClassLoader();
        }
        return classLoader;
    }

    /**
     * 获取当前线程上下文类加载器
     *
     * @return 返回当前线程上下文类加载器
     * @see Thread#getContextClassLoader()
     */
    public static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }


    /**
     * 获取系统类加载器
     *
     * @return 返回系统类加载器
     * @see ClassLoader#getSystemClassLoader()
     * @since 5.7.0
     */
    public static ClassLoader getSystemClassLoader() {
        return ClassLoader.getSystemClassLoader();
    }
}
