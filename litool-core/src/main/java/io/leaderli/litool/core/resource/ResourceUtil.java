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
 * a  utilities  of  resource such as {@link  File}, {@link  InputStream}
 */
public class ResourceUtil {

    /**
     * {@link  StandardCharsets#UTF_8}
     */
    public static final Charset DEFAULT_CHARACTER_ENCODING = StandardCharsets.UTF_8;

    /**
     * Return  an inputStream create by string
     *
     * @param string a  string of charset {@link  #DEFAULT_CHARACTER_ENCODING}
     * @return an inputStream create by string
     */
    public static InputStream createContentStream(String string) {
        if (string == null) {
            string = "";
        }
        return (new ByteArrayInputStream(string.getBytes(DEFAULT_CHARACTER_ENCODING)));
    }

    /**
     * Return a lira of resource files under classpath, include subdirectories
     *
     * @param walkFileFilter the filter of {@link  File}
     * @return a lira of files
     * @see #getResourceFiles(String, WalkFileFilter)
     */
    public static Lira<File> getResourceFiles(WalkFileFilter walkFileFilter) {

        return getResourceFiles("", walkFileFilter);
    }

    /**
     * Return a lira of resource files under classpath, include subdirectories
     *
     * @param resource_name the name of resource under classpath
     * @param fileFilter    the filter of {@link  File}
     * @return a lira of files
     * @see #getResourcesLira(String)
     */
    public static Lira<File> getResourceFiles(String resource_name, WalkFileFilter fileFilter) {


        List<File> result = new ArrayList<>();
        getResourcesLira(resource_name).throwable_map(URL::toURI).map(Paths::get).forThrowableEach(path -> {

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
     * Return a lira consist of {@link  URL} according to  {@link  ClassLoader#getResources(String)}
     *
     * @param resource_name the name of resource_name
     * @return a lira consist of {@link  URL}
     */
    public static Lira<URL> getResourcesLira(String resource_name) {

        return Lino.of(resource_name).throwable_map(ClassLoaderUtil.getClassLoader()::getResources).toLira(URL.class);

    }

    /**
     * Return  the map of line of content
     *
     * @param path file path under classpath
     * @return a map of line of content
     */
    public static Map<Integer, String> lineStrOfResourcesFile(String path) {

        return Lino.of(path).map(ResourceUtil::getResource).throwable_map(URL::openStream).map(InputStreamReader::new).map(BufferedReader::new).throwable_map(reader -> {
            Map<Integer, String> lines = new HashMap<>();
            int i = 0;
            while (reader.ready()) {
                lines.put(++i, reader.readLine());
            }

            return lines;
        }).or(HashMap::new).get();
    }

    /**
     * 获得资源的URL<br>
     * 路径用/分隔，例如:
     *
     * <pre>
     * config/a/db.config
     * spring/xml/test.xml
     * </pre>
     *
     * @param path file path under classpath
     * @return 资源URL
     */
    public static URL getResource(String path) {

        return getResource(path, null);
    }

    /**
     * 获得资源相对路径对应的URL
     *
     * @param resource  资源相对路径，{@code null}和""都表示classpath根路径
     * @param baseClass 基准Class，获得的相对路径相对于此Class所在路径，如果为{@code null}则相对ClassPath
     * @return {@link URL}
     */
    public static URL getResource(String resource, Class<?> baseClass) {
        resource = StringUtils.stripToEmpty(resource);
        if (baseClass == null) {
            resource = StringUtils.removeStart(resource, "/");
            return ClassLoaderUtil.getClassLoader().getResource(resource);
        }
        return baseClass.getResource(resource);
    }

    /**
     * @param resource 资源路径
     * @return 流
     * @see #getResourceAsStream(String, Class)
     */
    public static InputStream getResourceAsStream(String resource) {

        return getResourceAsStream(resource, null);

    }

    /**
     * @param resource  资源路径
     * @param baseClass 类路径下找
     * @return 流
     * @see #getResource(String, Class)
     * @see URL#openStream()
     */
    public static InputStream getResourceAsStream(String resource, Class<?> baseClass) {

        try {
            return getResource(resource, baseClass).openStream();
        } catch (IOException e) {
            return null;
        }

    }


}
