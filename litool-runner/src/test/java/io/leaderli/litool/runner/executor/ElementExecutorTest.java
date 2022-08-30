package io.leaderli.litool.runner.executor;

import io.leaderli.litool.runner.xml.RequestElement;
import io.leaderli.litool.runner.xml.router.task.AssignElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/8/14
 */
class ElementExecutorTest {

    @Test
    void executor() {

        Assertions.assertNotNull(new AssignElement().executor());
        Assertions.assertNotNull(new RequestElement().executor());
    }
}
