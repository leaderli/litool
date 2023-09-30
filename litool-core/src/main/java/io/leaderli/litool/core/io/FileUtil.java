package io.leaderli.litool.core.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

/**
 * @author leaderli
 * @since 2022/11/8 12:50 PM
 */
public class FileUtil {
    public static final String FILE_PROTOCOL = "file";
    public static final String JAR_PROTOCOL = "jar";

    public static long lastModified(File file) throws IOException {
        return Files.getLastModifiedTime(Objects.requireNonNull(file.toPath(), "file")).toMillis();
    }

    public static boolean isFileNewer(final File file, final long timeMillis) throws IOException {
        Objects.requireNonNull(file, "file");
        return file.exists() && lastModified(file) > timeMillis;
    }

    public static boolean createNewFile(File file) {
        try {
            return file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getWorkDir() {
        return System.getProperty("user.dir");
    }


    /**
     * @return 返回用户跟目录
     */
    public static String getHome() {
        return System.getProperty("user.home");
    }

    /**
     * @return 返回用户跟目录
     */
    public static File getHomeFile() {
        return new File(System.getProperty("user.home"));
    }


    /**
     * @param dir  目录
     * @param name 目录下的文件
     * @return 返回目录下的文件名
     * @see File#pathSeparator
     */
    public static File getFile(File dir, String name) {
        return new File(dir.getAbsolutePath() + File.pathSeparator + name);
    }
}
