package io.leaderli.litool.core.event;

import io.leaderli.litool.core.meta.LiBox;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/8/28
 */
class ILiEventObjectListenerTest {


    LiEventBus eventBus;
    LiBox<Integer> box;

    @BeforeEach
    void before() {
        eventBus = new LiEventBus();
        StringILiEventListener listener = new StringILiEventListener();
        eventBus.registerListener(listener);
        box = listener.box;

    }

    @Test
    void componentType() {
        ILiEventListener<StringLiEventObject> listener = new StringILiEventListener();
        Assertions.assertSame(StringLiEventObject.class, listener.componentType());
    }

    @Test
    void receive() {

        Assertions.assertTrue(box.absent());

        eventBus.push(new StringLiEventObject("123"));
        Assertions.assertSame(3, box.value());

        box.reset();
        eventBus.push(new StringLiEventObject(null));
        Assertions.assertTrue(box.absent());
    }


    private static class StringLiEventObject extends LiEventObject<String> {

        public StringLiEventObject(String source) {
            super(source);
        }
    }

    private static class StringILiEventListener extends ILiEventObjectListener<StringLiEventObject, String> {
        private final LiBox<Integer> box = LiBox.none();

        @Override
        void receive(String source) {

            box.value(source.length());
        }
    }
}
