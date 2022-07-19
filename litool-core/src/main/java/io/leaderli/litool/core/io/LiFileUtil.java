package io.leaderli.litool.core.io;

import io.leaderli.litool.core.util.LiURLUtil;

import java.io.File;
import java.util.regex.Pattern;

/**
 * @author leaderli
 * @since 2022/7/20
 */
public class LiFileUtil {


    /**
     * Class文件扩展名
     */
    public static final String CLASS_EXT = LiFileNameUtil.EXT_CLASS;
    /**
     * Jar文件扩展名
     */
    public static final String JAR_FILE_EXT = LiFileNameUtil.EXT_JAR;
    /**
     * 在Jar中的路径jar的扩展名形式
     */
    public static final String JAR_PATH_EXT = ".jar!";
    /**
     * 当Path为文件形式时, path会加入一个表示文件的前缀
     */
    public static final String PATH_FILE_PRE = LiURLUtil.FILE_URL_PREFIX;
    /**
     * 文件路径分隔符<br>
     * 在Unix和Linux下 是{@code '/'}; 在Windows下是 {@code '\'}
     */
    public static final String FILE_SEPARATOR = File.separator;
    /**
     * 多个PATH之间的分隔符<br>
     * 在Unix和Linux下 是{@code ':'}; 在Windows下是 {@code ';'}
     */
    public static final String PATH_SEPARATOR = File.pathSeparator;
    /**
     * 绝对路径判断正则
     */
    private static final Pattern PATTERN_PATH_ABSOLUTE = Pattern.compile("^[a-zA-Z]:([/\\\\].*)?");


    /**
     * 是否为Windows环境
     *
     * @return 是否为Windows环境
     * @since 3.0.9
     */
    public static boolean isWindows() {
        return LiFileNameUtil.WINDOWS_SEPARATOR == File.separatorChar;
    }
}
