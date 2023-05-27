package io.leaderli.litool.core.io;

import io.leaderli.litool.core.text.CharPool;
import io.leaderli.litool.core.text.CharUtils;
import io.leaderli.litool.core.text.StrPool;
import io.leaderli.litool.core.text.StringUtils;

import java.io.File;
import java.util.regex.Pattern;

/**
 * 文件名工具类，提供一些有关文件名的操作
 *
 * @since 2022/8/28
 */
public class FileNameUtil {

    /**
     * java源文件的扩展名
     */
    public static final String EXT_JAVA = ".java";
    /**
     * java类文件的扩展名
     */
    public static final String EXT_CLASS = ".class";
    /**
     * jar包文件的扩展名
     */
    public static final String EXT_JAR = ".jar";

    /**
     * jar包在URI中的URL扩展名
     */
    public static final String JAR_PATH_EXT = ".jar!";


    /**
     * Unix上的路径分隔符 '/'
     */
    public static final char UNIX_SEPARATOR = CharPool.SLASH;
    /**
     * Windows上的路径分隔符 '\'
     */
    public static final char WINDOWS_SEPARATOR = CharPool.BACKSLASH;

    /**
     * Windows系统下的非法文件名的正则表达式
     */
    public static final Pattern FILE_NAME_INVALID_PATTERN_WIN = Pattern.compile("[\\\\/:*?\"<>|]");

    /**
     * 特殊的文件后缀名
     */
    private static final CharSequence[] SPECIAL_SUFFIX = {"tar.bz2", "tar.Z", "tar.gz", "tar.xz"};


    /**
     * 获取文件名
     *
     * @param file 文件
     * @return 文件名
     */
    public static String getName(File file) {
        return (null != file) ? file.getName() : null;
    }

    /**
     * 获取文件名
     * <pre>
     * "d:/test/aaa" return "aaa"
     * "/test/aaa.jpg" return "aaa.jpg"
     * </pre>
     *
     * @param filePath 文件路径
     * @return 文件名
     */
    public static String getName(String filePath) {
        if (null == filePath) {
            return null;
        }
        int len = filePath.length();
        if (0 == len) {
            return filePath;
        }
        if (CharUtils.isFileSeparator(filePath.charAt(len - 1))) {
            // remove the trailing file separator
            len--;
        }

        int begin = 0;
        char c;
        for (int i = len - 1; i > -1; i--) {
            c = filePath.charAt(i);
            if (CharUtils.isFileSeparator(c)) {
                // 查找最后一个路径分隔符（/或者\）
                begin = i + 1;
                break;
            }
        }

        return filePath.substring(begin, len);
    }

    /**
     * 获取文件扩展名（不含点号），对于目录返回null
     *
     * @param file 文件
     * @return 文件扩展名（不含点号）
     */
    public static String extName(File file) {
        if (null == file) {
            return null;
        }
        if (file.isDirectory()) {
            return null;
        }
        return extName(file.getName());
    }

    /**
     * 获取文件扩展名（不含点号），对于以路径分隔符结尾的文件返回null
     *
     * @param fileName 文件名
     * @return 文件扩展名（不含点号）
     */
    public static String extName(String fileName) {
        if (fileName == null || fileName.endsWith(File.separator)) {
            return null;
        }
        int index = fileName.lastIndexOf(StrPool.DOT);
        if (index == -1) {
            return StrPool.EMPTY;
        } else {
            int secondToLastIndex = fileName.substring(0, index).lastIndexOf(StrPool.DOT);
            String substr = fileName.substring(secondToLastIndex == -1 ? index : secondToLastIndex + 1);
            if (StringUtils.containsAny(substr, SPECIAL_SUFFIX)) {
                return substr;
            }

            String ext = fileName.substring(index + 1);
            // 扩展名中不能包含路径相关的符号
            return StringUtils.containsAny(ext, UNIX_SEPARATOR, WINDOWS_SEPARATOR) ? StrPool.EMPTY : ext;
        }
    }


    /**
     * 检查文件是否是指定的类型，忽略大小写
     *
     * @param fileName 文件名
     * @param extNames 要检查的扩展名数组，同一种文件类型可能有多个扩展名，扩展名不含点号
     * @return 是否是指定的文件类型
     */
    public static boolean isType(String fileName, String... extNames) {
        return StringUtils.equalsAnyIgnoreCase(extName(fileName), extNames);
    }

}
