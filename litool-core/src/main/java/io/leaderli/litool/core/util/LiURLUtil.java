package io.leaderli.litool.core.util;

import com.sun.xml.internal.ws.util.UtilException;
import io.leaderli.litool.core.exception.RuntimeExceptionTransfer;
import io.leaderli.litool.core.lang3.LiStringUtils;
import io.leaderli.litool.core.net.URLDecoder;

import java.net.JarURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.jar.JarFile;

/**
 * @author leaderli
 * @since 2022/7/20
 */
public class LiURLUtil {

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
     * URL 协议表示WebSphere文件: "wsjar"
     */
    public static final String URL_PROTOCOL_WSJAR = "wsjar";
    /**
     * URL 协议表示JBoss zip文件: "vfszip"
     */
    public static final String URL_PROTOCOL_VFSZIP = "vfszip";
    /**
     * URL 协议表示JBoss文件: "vfsfile"
     */
    public static final String URL_PROTOCOL_VFSFILE = "vfsfile";
    /**
     * URL 协议表示JBoss VFS资源: "vfs"
     */
    public static final String URL_PROTOCOL_VFS = "vfs";
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
     * @throws UtilException UnsupportedEncodingException
     */
    public static String decode(String content, String charset) throws UtilException {
        return decode(content, LiStringUtils.isEmpty(charset) ? null : LiCharsetUtil.charset(charset));
    }

    /**
     * 解码application/x-www-form-urlencoded字符<br>
     * 将%开头的16进制表示的内容解码。<br>
     * 规则见：https://url.spec.whatwg.org/#urlencoded-parsing
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
        return RuntimeExceptionTransfer.get(() ->
                ((JarURLConnection) url.openConnection()).getJarFile()
        );
    }
}
