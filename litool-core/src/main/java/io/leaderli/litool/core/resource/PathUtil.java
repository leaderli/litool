package io.leaderli.litool.core.resource;

import io.leaderli.litool.core.meta.Lino;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author leaderli
 * @since 2022/6/26
 */
public class PathUtil {


    private static final String FILE_SIMPLE_NAME_REGEX = "^.*?([^/]+)$";
    static final Pattern pattern = Pattern.compile(FILE_SIMPLE_NAME_REGEX);

    /**
     * @param path 文件路径
     * @return 获取文件名
     */
    public static Lino<String> get_file_name(String path) {

        return Lino.of(path).map(pattern::matcher).filter(Matcher::find).map(m -> m.group(1));

    }
}
