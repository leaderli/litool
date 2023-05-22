package io.leaderli.litool.core.type;

import io.leaderli.litool.core.collection.IterableItr;
import io.leaderli.litool.core.exception.RuntimeExceptionTransfer;
import io.leaderli.litool.core.io.FileNameUtil;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.resource.ResourceUtil;
import io.leaderli.litool.core.resource.net.URLUtil;
import io.leaderli.litool.core.text.CharPool;
import io.leaderli.litool.core.text.CharsetUtil;
import io.leaderli.litool.core.text.StrPool;
import io.leaderli.litool.core.text.StringUtils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author leaderli
 * @since 2022/7/19
 * <p>
 * 参考 hutool
 */
public class ClassScanner {

    /**
     * 包名
     */
    private final String packageName;
    /**
     * 包名，最后跟一个点，表示包名，避免在检查前缀时的歧义<br>
     * 如果包名指定为空，不跟点
     */
    private final String packageNameWithDot;
    /**
     * 包路径，用于文件中对路径操作
     */
    private final String packageDirName;
    /**
     * 包路径，用于jar中对路径操作，在Linux下与packageDirName一致
     */
    private final String packagePath;
    /**
     * 过滤器
     */
    private final Predicate<Class<?>> classFilter;
    /**
     * 编码
     */
    private final Charset charset;
    /**
     * 扫描结果集
     */
    private final Set<Class<?>> classes = new HashSet<>();
    /**
     * 类加载器
     */
    private ClassLoader classLoader;


    private boolean forceScanJavaClassPaths;


    /**
     * 构造，默认UTF-8编码
     */
    public ClassScanner() {
        this(null);
    }

    /**
     * 构造，默认UTF-8编码
     *
     * @param packageName 包名，所有包传入""或者null
     */
    public ClassScanner(String packageName) {
        this(packageName, null);
    }

    /**
     * 构造，默认UTF-8编码
     *
     * @param packageName 包名，所有包传入""或者null
     * @param classFilter 过滤器，无需传入null
     */
    public ClassScanner(String packageName, Predicate<Class<?>> classFilter) {
        this(packageName, classFilter, CharsetUtil.CHARSET_UTF_8);
    }


    /**
     * 构造
     *
     * @param packageName 包名，所有包传入""或者null
     * @param classFilter 过滤器，无需传入null
     * @param charset     编码
     */
    public ClassScanner(String packageName, Predicate<Class<?>> classFilter, Charset charset) {
        packageName = StringUtils.stripToEmpty(packageName);
        this.packageName = packageName;
        this.packageNameWithDot = StringUtils.appendIfMissing(packageName, StrPool.DOT);
        this.packageDirName = packageName.replace(CharPool.DOT, File.separatorChar);
        this.packagePath = packageName.replace(CharPool.DOT, CharPool.SLASH);
        this.classFilter = classFilter;
        this.charset = charset;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Lira<Class<T>> getSubTypesOf(String packageName, Class<T> cls) {


        ClassScanner classScanner = new ClassScanner(packageName, find -> ClassUtil.isAssignableFromOrIsWrapper(cls,
                find) && cls != find);
        classScanner.scan();

        return (Lira) Lira.of(classScanner.classes);

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Lira<Class<T>> getSubTypesOf(Class<?> packageClass, Class<T> cls) {


        ClassScanner classScanner = new ClassScanner(packageClass.getPackage().getName(),
                find -> ClassUtil.isAssignableFromOrIsWrapper(cls, find) && cls != find);
        classScanner.scan();

        return (Lira) Lira.of(classScanner.classes);

    }

    public static Lira<Class<?>> getClassOfAnnotated(Class<?> packageClass,
                                                     Class<? extends Annotation> annotationType) {
        ClassScanner classScanner = new ClassScanner(packageClass.getPackage().getName(),
                find -> find.isAnnotationPresent(annotationType));
        classScanner.scan();
        return Lira.of(classScanner.classes);

    }


    /**
     * 扫描包路径下满足class过滤器条件的所有class文件
     *
     * @return 类集合
     * @since 5.7.5
     */
    public Set<Class<?>> scan() {
        for (URL url : ResourceUtil.getResourceURLs(this.packagePath).get()) {
            if ("file".equals(url.getProtocol())) {
                scanFile(new File(URLUtil.decode(url.getFile(), this.charset.name())), null);
            } else if ("jar".equals(url.getProtocol())) {
                scanJar(URLUtil.getJarFile(url));
            }
        }

        // classpath下未找到，则扫描其他jar包下的类
        if (forceScanJavaClassPaths) {
            scanJavaClassPaths();
        }

        return Collections.unmodifiableSet(this.classes);
    }

    /**
     * 扫描Java指定的ClassPath路径
     */
    private void scanJavaClassPaths() {
        final Lira<String> javaClassPaths = ClassUtil.getJavaClassPaths();
        for (String classPath : javaClassPaths.get()) {
            // bug修复，由于路径中空格和中文导致的Jar找不到
            classPath = URLUtil.decode(classPath, CharsetUtil.systemCharsetName());

            scanFile(new File(classPath), null);
        }
    }

    /**
     * 扫描文件或目录中的类
     *
     * @param file    文件或目录
     * @param rootDir 包名对应classpath绝对路径
     */
    private void scanFile(File file, String rootDir) {
        if (file.isFile()) {
            final String fileName = file.getAbsolutePath();
            if (fileName.endsWith(FileNameUtil.EXT_CLASS)) {

                final String className = fileName//
                        // 8为classes长度，fileName.length() - 6为".class"的长度
                        .substring(rootDir == null ? 0 : rootDir.length(), fileName.length() - 6)//
                        .replace(File.separatorChar, CharPool.DOT);//
                //加入满足条件的类
                addIfAccept(className);
            } else if (fileName.endsWith(FileNameUtil.EXT_JAR)) {

                RuntimeExceptionTransfer.run(() -> scanJar(new JarFile(file)));
            }
        } else if (file.isDirectory()) {
            final File[] files = file.listFiles();
            if (null != files) {
                for (File subFile : files) {
                    scanFile(subFile, (null == rootDir) ? subPathBeforePackage(file) : rootDir);
                }
            }
        }
    }

    /**
     * 截取文件绝对路径中包名之前的部分
     *
     * @param file 文件
     * @return 包名之前的部分
     */
    private String subPathBeforePackage(File file) {
        String filePath = file.getAbsolutePath();
        if (StringUtils.isNotEmpty(this.packageDirName)) {
            filePath = StringUtils.substringBeforeLast(filePath, this.packageDirName);
        }
        return StringUtils.appendIfMissing(filePath, File.separator);
    }

    /**
     * 加载类
     *
     * @param className 类名
     * @return 加载的类
     */
    private Class<?> loadClass(String className) {
        ClassLoader loader = this.classLoader;
        if (null == loader) {
            loader = ClassLoaderUtil.getClassLoader();
            this.classLoader = loader;
        }

        Class<?> clazz = null;
        try {
            clazz = Class.forName(className, false, loader);
        } catch (NoClassDefFoundError | ClassNotFoundException |
                 UnsupportedClassVersionError e) {

            // 版本导致的不兼容的类，跳过
            // 由于依赖库导致的类无法加载，直接跳过此类
        }
        return clazz;
    }

    /**
     * 通过过滤器，是否满足接受此类的条件
     *
     * @param className 类名
     */
    private void addIfAccept(String className) {
        if (StringUtils.isBlank(className)) {
            return;
        }
        int classLen = className.length();
        int packageLen = this.packageName.length();
        //检查类名是否以指定包名为前缀，包名后加.（避免类似于cn.hutool.A和cn.hutool.ATest这类类名引起的歧义）
        if (classLen == packageLen) {
            //类名和包名长度一致，用户可能传入的包名是类名
            if (className.equals(this.packageName)) {
                addIfAccept(loadClass(className));
            }
        } else if (classLen > packageLen && (".".equals(this.packageNameWithDot) || className.startsWith(this.packageNameWithDot))) {
            addIfAccept(loadClass(className));
        }
    }

    /**
     * 通过过滤器，是否满足接受此类的条件
     *
     * @param clazz 类
     */
    private void addIfAccept(Class<?> clazz) {
        if (null != clazz && (classFilter == null || classFilter.test(clazz))) {
            this.classes.add(clazz);
        }
    }

    /**
     * 扫描jar包
     *
     * @param jar jar包
     */
    private void scanJar(JarFile jar) {
        String name;
        Iterable<JarEntry> jarEntryEnumerationItr = IterableItr.of(jar.entries());
        for (JarEntry entry : jarEntryEnumerationItr) {
            name = StringUtils.removeStart(entry.getName(), StrPool.SLASH);
            if ((StringUtils.isEmpty(packagePath) || name.startsWith(this.packagePath)) && name.endsWith(FileNameUtil.EXT_CLASS) && !entry.isDirectory()) {
                final String className = name.substring(0, name.length() - 6).replace(CharPool.SLASH, CharPool.DOT);
                addIfAccept(loadClass(className));
            }
        }
    }
}
