package io.leaderli.litool.core.net;

import io.leaderli.litool.core.text.CharsetUtil;
import io.leaderli.litool.core.text.StringUtils;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.jar.JarFile;

/**
 * @author leaderli
 * @since 2022/7/20
 */
public class URLUtil {

/**
 * 针对ClassPath路径的伪协议前缀（兼容Spring）: "classpath:"
 */
public static final String CLASSPATH_URL_PREFIX = "classpath:";
/**
 * URL 前缀表示文件: "file:"
 */
public static final String FILE_URL_PREFIX = "file:";
/**
 * URL 前缀表示jar: "jar:"
 */
public static final String JAR_URL_PREFIX = "jar:";
/**
 * URL 前缀表示war: "war:"
 */
public static final String WAR_URL_PREFIX = "war:";
/**
 * URL 协议表示文件: "file"
 */
public static final String URL_PROTOCOL_FILE = "file";
/**
 * URL 协议表示Jar文件: "jar"
 */
public static final String URL_PROTOCOL_JAR = "jar";
/**
 * URL 协议表示zip文件: "zip"
 */
public static final String URL_PROTOCOL_ZIP = "zip";
/**
 * Jar路径以及内部文件路径的分界符: "!/"
 */
public static final String JAR_URL_SEPARATOR = "!/";
/**
 * WAR路径及内部文件路径分界符
 */
public static final String WAR_URL_SEPARATOR = "*/";

/**
 * 解码application/x-www-form-urlencoded字符<br>
 * 将%开头的16进制表示的内容解码。
 *
 * @param content URL
 * @param charset 编码
 * @return 解码后的URL
 */
public static String decode(String content, String charset) {
    return decode(content, StringUtils.isEmpty(charset) ? null : CharsetUtil.charset(charset));
}

/**
 * 解码application/x-www-form-urlencoded字符<br>
 * 将%开头的16进制表示的内容解码。<br>
 * 规则见：<a href="https://url.spec.whatwg.org/#urlencoded-parsing">...</a>
 *
 * @param content 被解码内容
 * @param charset 编码，null表示不解码
 * @return 编码后的字符
 * @since 4.4.1
 */
public static String decode(String content, Charset charset) {
    return URLDecoder.decode(content, charset);
}

/**
 * 从URL中获取JarFile
 *
 * @param url URL
 * @return JarFile
 * @since 4.1.5
 */
public static JarFile getJarFile(URL url) {
    try {
        return ((JarURLConnection) url.openConnection()).getJarFile();
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
}
