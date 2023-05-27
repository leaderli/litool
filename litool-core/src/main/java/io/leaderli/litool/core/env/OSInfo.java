package io.leaderli.litool.core.env;

import io.leaderli.litool.core.io.FileNameUtil;

import java.io.File;

/**
 * @author leaderli
 * @since 2022/9/24 9:08 AM
 */
public class OSInfo {
    /**
     * 判断当前操作系统是否为Windows。
     *
     * @return 如果当前操作系统为Windows，则返回 true；否则返回 false。
     */
    public static boolean isWindows() {
        return FileNameUtil.WINDOWS_SEPARATOR == File.separatorChar;
    }

}
