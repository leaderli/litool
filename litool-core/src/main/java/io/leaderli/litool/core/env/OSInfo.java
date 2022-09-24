package io.leaderli.litool.core.env;

import io.leaderli.litool.core.io.FileNameUtil;

import java.io.File;

/**
 * @author leaderli
 * @since 2022/9/24 9:08 AM
 */
public class OSInfo {
    /**
     * Return Whether it is a Windows environment
     *
     * @return Whether it is a Windows environment
     */
    public static boolean isWindows() {
        return FileNameUtil.WINDOWS_SEPARATOR == File.separatorChar;
    }

}
