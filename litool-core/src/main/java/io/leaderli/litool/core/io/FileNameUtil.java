package io.leaderli.litool.core.io;

import io.leaderli.litool.core.text.CharPool;
import io.leaderli.litool.core.text.CharUtils;
import io.leaderli.litool.core.text.StrPool;
import io.leaderli.litool.core.text.StringUtils;

import java.io.File;
import java.util.regex.Pattern;

/**
 * Some utilities about file name
 *
 * @since 2022/8/28
 */
public class FileNameUtil {

/**
 * the file extension of java source
 */
public static final String EXT_JAVA = ".java";
/**
 * the file extension of java class
 */
public static final String EXT_CLASS = ".class";
/**
 * the file extension of jar file
 */
public static final String EXT_JAR = ".jar";

/**
 * {@code  .jar!}
 * the url extension form of jar file in uri
 */
public static final String JAR_PATH_EXT = ".jar!";


/**
 * separator {@code '/'} on unix
 */
public static final char UNIX_SEPARATOR = CharPool.SLASH;
/**
 * separator {@code '\\'} on window
 */
public static final char WINDOWS_SEPARATOR = CharPool.BACKSLASH;

/**
 * invalid file name {@link  Pattern} on window
 */
public static final Pattern FILE_NAME_INVALID_PATTERN_WIN = Pattern.compile("[\\\\/:*?\"<>|]");

/**
 * special file suffix
 */
private static final CharSequence[] SPECIAL_SUFFIX = {"tar.bz2", "tar.Z", "tar.gz", "tar.xz"};


// -------------------------------------------------------------------------------------------- name start

/**
 * Return file name
 *
 * @param file file
 * @return file name
 */
public static String getName(File file) {
    return (null != file) ? file.getName() : null;
}

/**
 * Return file name
 * <pre>
 * "d:/test/aaa" return "aaa"
 * "/test/aaa.jpg" return "aaa.jpg"
 * </pre>
 *
 * @param filePath file path
 * @return file name
 * @since 1.2.0
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
 * Return the name of file extension without dot, directory always return null
 *
 * @param file file
 * @return the name of file extension without dot
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
 * Return the name of file extension without dot, return null if file end with {@link  File#separator}
 *
 * @param fileName fileName
 * @return the name of file extension without dot
 */
public static String extName(String fileName) {
    if (fileName == null || fileName.endsWith(File.separator)) {
        return null;
    }
    int index = fileName.lastIndexOf(StrPool.DOT);
    if (index == -1) {
        return StrPool.EMPTY;
    } else {
        // issue#I4W5FS@Gitee
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
 * Check file type against filename, ignoring case
 *
 * @param fileName file name
 * @param extNames Array of extensions to be checked, the same file type may have multiple extensions, the extension
 *                 name without dot
 * @return it is the specific file type
 */
public static boolean isType(String fileName, String... extNames) {
    return StringUtils.equalsAnyIgnoreCase(extName(fileName), extNames);
}

/**
 * Return Whether it is a Windows environment
 *
 * @return Whether it is a Windows environment
 */
public static boolean isWindows() {
    return WINDOWS_SEPARATOR == File.separatorChar;
}
// -------------------------------------------------------------------------------------------- name end
}
