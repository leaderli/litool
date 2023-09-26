package io.leaderli.litool.core.io;

import java.io.File;

public interface TailerListener {
    default void init(Tailer tailer) {

    }

    default void fileNotFound(File file) {

    }

    default void fileRotated() {

    }

    default void handle(Exception exception) {

    }

    default void handle(String line) {
        System.out.println(line);
    }

    default void endOfFileReached() {

    }

    default void delay(Tailer tailer) {

    }
}
