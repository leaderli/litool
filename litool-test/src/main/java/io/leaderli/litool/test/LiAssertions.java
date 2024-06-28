package io.leaderli.litool.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;


public class LiAssertions {

    public static <T extends Throwable> void assertThrows(Class<T> expectedType, String errorMsg, Executable executable) {

        T throwable = Assertions.assertThrows(expectedType, executable);
        Assertions.assertEquals(errorMsg, throwable.getMessage());
    }
}
