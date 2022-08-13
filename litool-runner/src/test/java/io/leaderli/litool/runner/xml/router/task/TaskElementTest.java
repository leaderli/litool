package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.runner.xml.RequestElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author leaderli
 * @since 2022/8/13 6:16 PM
 */
class TaskElementTest {

    @Test
    void executor() {

        Assertions.assertNotNull(new AssignElement().executor());
        Assertions.assertNotNull(new RequestElement().executor());
    }
}
