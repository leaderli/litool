package io.leaderli.litool.core.type;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @author leaderli
 * @since 2022/7/17
 */
public class ClassLoaderUtil {
    /**
     * return the found {@link ClassLoader}<br>
     * <p>
     * find  in order:
     * <pre>
     * 1、get context classLoader {@link  #getContextClassLoader()}
     * 2、get system classloader {@link ClassLoader#getSystemClassLoader()}）
     * </pre>
     *
     * @return the classLoader
     */
    public static ClassLoader getClassLoader() {
        ClassLoader classLoader = getContextClassLoader();
        if (classLoader == null) {
            classLoader = getSystemClassLoader();
        }
        return classLoader;
    }

    /**
     * Return:
     * <pre>
     *     Thread.currentThread().getContextClassLoader()
     * </pre>
     *
     * @return the CurrentThread  ClassLoader
     * @see Thread#getContextClassLoader()
     */
    public static ClassLoader getContextClassLoader() {
        if (System.getSecurityManager() == null) {
            return Thread.currentThread().getContextClassLoader();
        } else {
            return AccessController.doPrivileged(
                    (PrivilegedAction<ClassLoader>) () -> Thread.currentThread().getContextClassLoader());
        }
    }


    /**
     * Return:
     * <pre>
     * ClassLoader.getSystemClassLoader();
     * </pre>
     *
     * @return systemClassLoader
     * @see ClassLoader#getSystemClassLoader()
     * @since 5.7.0
     */
    public static ClassLoader getSystemClassLoader() {
        if (System.getSecurityManager() == null) {
            return ClassLoader.getSystemClassLoader();
        } else {
            return AccessController.doPrivileged(
                    (PrivilegedAction<ClassLoader>) ClassLoader::getSystemClassLoader);
        }
    }
}
