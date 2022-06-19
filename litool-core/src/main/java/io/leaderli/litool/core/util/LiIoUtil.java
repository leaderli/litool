package io.leaderli.litool.core.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class LiIoUtil {

    public static final Charset DEFAULT_CHARACTER_ENCODING = StandardCharsets.UTF_8;

    /**
     * @param string 字符,需要为 UTF-8 格式
     * @return 字符转换为流
     */
    public static InputStream createContentStream(String string) {
        if (string == null) {
            string = "";
        }
        return (new ByteArrayInputStream(string.getBytes(DEFAULT_CHARACTER_ENCODING)));
    }
}
