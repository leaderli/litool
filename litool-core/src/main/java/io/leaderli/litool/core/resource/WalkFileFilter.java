package io.leaderli.litool.core.resource;

import java.io.File;

/**
 * a filter use to walk file, it's used for {@link ResourceUtil#getResourceFiles(WalkFileFilter)}  }
 *
 * @author leaderli
 * @since 2022/9/5 1:13 PM
 */
@FunctionalInterface
public interface WalkFileFilter {

    /**
     * walk sub-files of directory if {@code  true}
     *
     * @param dir the directory
     * @return should walk sub-files of directory
     */
    default boolean dir(File dir) {
        return true;
    }

    /**
     * accept file if {@code  true}
     *
     * @param file the normal file
     * @return should accept file
     */
    boolean file(File file);
}
