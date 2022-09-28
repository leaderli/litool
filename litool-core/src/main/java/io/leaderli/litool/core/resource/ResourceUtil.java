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
     * @see #getResourceURLs(String)
     */
    public static Lira<File> getResourceFiles(String resource_name, WalkFileFilter fileFilter) {


        List<File> result = new ArrayList<>();
        getResourceURLs(resource_name).throwable_map(URL::toURI).map(Paths::get).forThrowableEach(path -> {

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
    public static Lira<URL> getResourceURLs(String resource_name) {

        return Lino.of(resource_name).throwable_map(ClassLoaderUtil.getClassLoader()::getResources).toLira(URL.class);

    }

    /**
     * Return  the map of line of content
     *
     * @param path file path under classpath
     * @return a map of line of content
     */
    public static Map<Integer, String> lineStrOfResourcesFile(String path) {

        return Lino.of(path)
                .map(ResourceUtil::getResource).throwable_map(URL::openStream)
                .map(InputStreamReader::new).map(BufferedReader::new)
                .throwable_map(reader -> {
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
     * get file URL
     *
     * <pre>
     * config/a/db.config
     * spring/xml/test.xml
     * </pre>
     *
     * @param path file path under classpath
     * @return file url
     */
    public static URL getResource(String path) {

        return getResource(path, null);
    }

    /**
     * Return the relative URL under benchmark class
     *
     * @param resource_name the relative path of resource, {@code null} and "" both represent classpath path
     * @param baseClass     the benchmark Class，
     * @return {@link URL}
     */
    public static URL getResource(String resource_name, Class<?> baseClass) {
        resource_name = StringUtils.stripToEmpty(resource_name);
        if (baseClass == null) {
            resource_name = StringUtils.removeStart(resource_name, "/");
            return ClassLoaderUtil.getClassLoader().getResource(resource_name);
        }
        return baseClass.getResource(resource_name);
    }

    /**
     * An inputStream of resource under classpath
     *
     * @param resource_path the relative path under classpath
     * @return an  inputStream
     * @see #getResourceAsStream(String, Class)
     */
    public static InputStream getResourceAsStream(String resource_path) {

        return getResourceAsStream(resource_path, null);

    }

    /**
     * An inputStream of resource under benchmark class
     *
     * @param resource_relative_path the relative path under benchmark class
     * @param baseClass              benchmark class
     * @return an inputStream
     * @see #getResource(String, Class)
     * @see URL#openStream()
     */
    public static InputStream getResourceAsStream(String resource_relative_path, Class<?> baseClass) {

        try {
            return getResource(resource_relative_path, baseClass).openStream();
        } catch (IOException e) {
            return null;
        }

    }


}
