package io.leaderli.litool.core.util;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class LiResourceUtil {

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

    public static Lira<File> getResourcesFile(FileFilter fileFilter) {

        return Lino.of(LiResourceUtil.class.getResource("/"))
                .map(URL::getFile)
                .map(File::new)
                .map(f -> f.listFiles(fileFilter))
                .toLira(File.class);

    }

    /**
     * @param path 路径
     * @return 返回每一行字符串的 map
     */
    public static Map<Integer, String> lineStrOfResourcesFile(String path) {

        return Lino.of(path)
                .map(LiResourceUtil::getResourceAsStream)
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

    public static InputStream getResourceAsStream(String path) {
        return LiResourceUtil.class.getResourceAsStream(path);
    }


}
