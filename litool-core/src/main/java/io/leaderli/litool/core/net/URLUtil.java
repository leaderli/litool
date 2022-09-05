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
     * get jar file from url
     *
     * @param url URL
     * @return JarFile
     */
    public static JarFile getJarFile(URL url) {
        try {
            return ((JarURLConnection) url.openConnection()).getJarFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
