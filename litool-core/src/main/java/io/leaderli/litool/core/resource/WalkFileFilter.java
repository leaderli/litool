package io.leaderli.litool.core.resource;

import java.io.File;

/**
 * 用于遍历文件的过滤器，用于 {@link ResourceUtil#getResourceFiles(WalkFileFilter)} 方法。
 *
 * @author leaderli
 * @since 2022/9/5 1:13 PM
 */
@FunctionalInterface
public interface WalkFileFilter {

    /**
     * 判断是否遍历目录下的文件，默认为 true。
     *
     * @param dir 目录
     * @return 是否遍历目录下的文件
     */
    default boolean dir(File dir) {
        return true;
    }

    /**
     * 判断是否接受文件，默认为 false。
     *
     * @param file 文件
     * @return 是否接受文件
     */
    boolean file(File file);
}
