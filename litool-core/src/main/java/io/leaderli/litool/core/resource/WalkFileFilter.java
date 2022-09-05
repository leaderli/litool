package io.leaderli.litool.core.resource;

import java.io.File;

/**
 * @author leaderli
 * @since 2022/9/5 1:13 PM
 */
@FunctionalInterface
public interface WalkFileFilter {

    default boolean dir(File dir) {
        return true;
    }

    boolean file(File file);
}
