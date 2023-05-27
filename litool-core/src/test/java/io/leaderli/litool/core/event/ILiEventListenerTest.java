package io.leaderli.litool.core.event;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/8/28
 */
class ILiEventListenerTest {

    @Test
    void componentType() {
        ILiEventListener<LiEventObject<String>, String> listener = new StringLiEventListener();
        Assertions.assertSame(LiEventObject.class, listener.componentType());
    }

    private static class StringLiEventListener implements ILiEventListener<LiEventObject<String>, String> {
        @Override
        public void listen(String event) {

        }
    }
}
