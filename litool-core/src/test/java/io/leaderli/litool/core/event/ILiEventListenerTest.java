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
    ILiEventListener<String> listener = new StringILiEventListener();
    Assertions.assertSame(String.class, listener.componentType());
}

private static class StringILiEventListener implements ILiEventListener<String> {
    @Override
    public void listen(String event) {

    }
}
}
