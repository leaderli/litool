package io.leaderli.litool.core.resource;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.core.type.ClassLoaderUtil;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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
 * Return a lira of resource files , include subdirectories
 *
 * @param fileFilter the filter of {@link  File}
 * @return a lira of files
 */
public static Lira<File> getResourceFile(FileFilter fileFilter) {

    return getResourcesLira("")
            .map(URL::getFile)
            .map(File::new)
            .map(f -> f.listFiles(fileFilter))
            .flatMap();

}

/**
 * Return a lira consist of {@link  URL} according to  {@link  ClassLoader#getResources(String)}
 *
 * @param resource the name of resource
 * @return a lira consist of {@link  URL}
 */
public static Lira<URL> getResourcesLira(String resource) {

    return Lino.of(resource).throwable_map(ClassLoaderUtil.getClassLoader()::getResources).toLira(URL.class);

}

/**
 * @param path 路径
 * @return 返回每一行字符串的 map
 */
public static Map<Integer, String> lineStrOfResourcesFile(String path) {

    return Lino.of(path)
            .map(ResourceUtil::getResource)
            .throwable_map(URL::openStream)
            .map(InputStreamReader::new)
            .map(BufferedReader::new)
            .throwable_map(reader -> {
                Map<Integer, String> lines = new HashMap<>();
                int i = 0;
                while (reader.ready()) {
                    lines.put(++i, reader.readLine());
                }

                return lines;
            }).or(HashMap::new)
            .get();
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
 * @param resource 资源（相对Classpath的路径）
 * @return 资源URL
 */
public static URL getResource(String resource) {

    return getResource(resource, null);
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
