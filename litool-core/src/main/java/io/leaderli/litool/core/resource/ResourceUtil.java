package io.leaderli.litool.core.resource;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.core.type.ClassLoaderUtil;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ResourceUtil 是一个工具类，用于处理资源类工作，其中包括文件和输入流等资源。
 */
public class ResourceUtil {

    /**
     * {@link  StandardCharsets#UTF_8}
     */
    public static final Charset DEFAULT_CHARACTER_ENCODING = StandardCharsets.UTF_8;


    /**
     * 根据字符串创建一个流
     *
     * @param string 字符串，编码格式为 {@link #DEFAULT_CHARACTER_ENCODING}
     * @return 返回一个对应的输入流
     */
    public static InputStream createContentStream(String string) {
        if (string == null) {
            string = "";
        }
        return (new ByteArrayInputStream(string.getBytes(DEFAULT_CHARACTER_ENCODING)));
    }


    /**
     * 获取资源文件列表，包括子目录中的文件
     *
     * @param walkFileFilter 过滤器
     * @return 返回一个 {@link Lira} 列表，其中包含符合条件的文件
     * @see #getResourceFiles(String, WalkFileFilter)
     */
    public static Lira<File> getResourceFiles(WalkFileFilter walkFileFilter) {

        return getResourceFiles("", walkFileFilter);
    }


    /**
     * 获取资源文件列表，包括子目录中的文件
     *
     * @param resourceName 资源名称
     * @param fileFilter   过滤器
     * @return 返回一个 {@link Lira} 列表，其中包含符合条件的文件。
     * @see #getResourceURLs(String)
     */
    public static Lira<File> getResourceFiles(String resourceName, WalkFileFilter fileFilter) {


        List<File> result = new ArrayList<>();
        getResourceURLs(resourceName).mapIgnoreError(URL::toURI).map(Paths::get).forEachIgnoreError(path -> {

            SimpleFileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    if (fileFilter == null || fileFilter.dir(dir.toFile())) {
                        return FileVisitResult.CONTINUE;
                    }
                    return FileVisitResult.SKIP_SUBTREE;
                }

                @Override
                public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) {

                    File file = filePath.toFile();
                    if (fileFilter == null || fileFilter.file(file)) {
                        result.add(file);
                    }
                    return FileVisitResult.CONTINUE;
                }

            };
            Files.walkFileTree(path, visitor);
        });

        return Lira.of(result);
    }

    /**
     * 获取资源列表中的 URL
     *
     * @param resourceName 相对于 classpath 的资源名
     * @return 返回一个 {@link Lira} 列表，其中包含对应的 URL
     */
    public static Lira<URL> getResourceURLs(ClassLoader classLoader, String resourceName) {

        return Lino.of(resourceName).mapIgnoreError(classLoader::getResources).toLira(URL.class);

    }

    /**
     * 获取资源列表中的 URL
     *
     * @param resourceName 相对于 classpath 的资源名
     * @return 返回一个 {@link Lira} 列表，其中包含对应的 URL
     */
    public static Lira<URL> getResourceURLs(String resourceName) {
        return getResourceURLs(ClassLoaderUtil.getClassLoader(), resourceName);
    }

    /**
     * 返回一个资源文件的每一行内容组成的 Map。
     *
     * @param resourceName 相对于 classpath 的资源名
     * @return 返回一个 Map，其中 key 为行号，value 为对应的行内容。
     */
    public static Map<Integer, String> lineStrOfResourcesFile(String resourceName) {

        return Lino.of(resourceName)
                .map(ResourceUtil::getResource).mapIgnoreError(URL::openStream)
                .map(InputStreamReader::new).map(BufferedReader::new)
                .mapIgnoreError(reader -> {
                    Map<Integer, String> lines = new HashMap<>();
                    int i = 0;
                    while (reader.ready()) {
                        lines.put(++i, reader.readLine());
                    }

                    return lines;
                })
                .get(HashMap::new);
    }


    /**
     * 获取文件的 URL
     * <pre>
     * config/a/db.config
     * spring/xml/test.xml
     * </pre>*
     *
     * @param path 相对于 classpath 的文件路径
     * @return 返回对应文件的 URL
     */
    public static URL getResource(String path) {

        return getResource(path, null);
    }


    /**
     * 获取相对于指定类的路径的资源文件 URL
     *
     * @param path  相对于 classpath 的文件路径
     * @param clazz 指定的类
     * @return 返回对应文件的 URL
     */
    public static URL getResource(String path, Class<?> clazz) {
        path = StringUtils.stripToEmpty(path);
        if (clazz == null) {
            path = StringUtils.removeStart(path, "/");
            return ClassLoaderUtil.getClassLoader().getResource(path);
        }
        return clazz.getResource(path);
    }

    /**
     * 从classpath下获取资源的输入流
     *
     * @param resourcePath 资源的相对路径
     * @return 资源的输入流
     * @see #getResourceAsStream(String, Class)
     */
    public static InputStream getResourceAsStream(String resourcePath) {

        return getResourceAsStream(resourcePath, null);

    }


    /**
     * 从基准类所在的路径下获取资源的输入流
     *
     * @param resourceRelativePath 资源的相对路径
     * @param baseClass            基准类
     * @return 资源的输入流
     * @see #getResource(String, Class)
     * @see URL#openStream()
     */
    public static InputStream getResourceAsStream(String resourceRelativePath, Class<?> baseClass) {

        try {
            return getResource(resourceRelativePath, baseClass).openStream();
        } catch (IOException e) {
            return null;
        }

    }

}
