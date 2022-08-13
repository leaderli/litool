package io.leaderli.litool.runner.check;

import io.leaderli.litool.runner.xml.MainElement;

/**
 * @author leaderli
 * @since 2022/8/13 3:16 PM
 */
public interface CheckVisitor {
    default void addErrorMsgs(boolean success, String error) {

    }

    default MainElement mainElement() {
        throw new UnsupportedOperationException();
    }
}
