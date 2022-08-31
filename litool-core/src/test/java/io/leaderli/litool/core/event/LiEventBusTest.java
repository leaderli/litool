package io.leaderli.litool.core.event;

import io.leaderli.litool.core.meta.LiBox;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/8/28
 */
class LiEventBusTest {


    LiBox<Integer> box = LiBox.none();

    @Test
    void getPublisher() {


        LiEventBus eventBus = new LiEventBus();

        eventBus.registerListener(new TestLiEventListener());
        eventBus.registerListener(new TestLiEventListener2());

        Assertions.assertDoesNotThrow(() -> {
            eventBus.push(new TestLiEventObject("456"));
            eventBus.push(new TestLiEventObject(null));
        });
        eventBus.registerListener(new TestLiEventListener3());
        Assertions.assertThrows(RuntimeException.class, () -> {
            eventBus.push(new TestLiEventObject("456"));
            Assertions.fail();
            eventBus.push(new TestLiEventObject(null));
        });
    }

    @Test
    void test1() {

        LiEventBus liEventBus = new LiEventBus();

        TempListener listener = new TempListener(true);
        Assertions.assertEquals(0, listener.count);
        liEventBus.registerListener(listener);
        liEventBus.push(new TestLiEventObject("123"));
        Assertions.assertEquals(1, listener.count);
        liEventBus.push(new TestLiEventObject("123"));
        Assertions.assertEquals(1, listener.count);
    }

    @Test
    void test2() {
        LiEventBus liEventBus = new LiEventBus();


        TempListener listener = new TempListener(false);
        Assertions.assertEquals(0, listener.count);
        liEventBus.registerListener(listener);
        liEventBus.push(new TestLiEventObject("123"));
        Assertions.assertEquals(1, listener.count);
        liEventBus.push(new TestLiEventObject("123"));
        Assertions.assertEquals(2, listener.count);
    }

    @SuppressWarnings("unchecked")
    @Test
    void unRegisterListener() {

        LiEventBus eventBus = new LiEventBus();
        ILiEventListener<String> listener = new StringEventObject();

        eventBus.registerListener(listener);
        eventBus.push("123");
        Assertions.assertEquals(1, box.value());

        box.reset();
        eventBus.push("123");
        Assertions.assertTrue(box.absent());

        ((LiEventBusBehavior<String>) eventBus).unRegisterListener(listener);
    }

    private static class TestLiEventObject extends LiEventObject<String> {

        public TestLiEventObject(String source) {
            super(source);
        }
    }

    private static class TestLiEventListener implements ILiEventListener<TestLiEventObject> {


        @Override
        public void listen(TestLiEventObject event) {
            assert event.getSource().get().equals("123");

        }

        @Override
        public void onError(Throwable throwable) {
        }

        @Override
        public Class<TestLiEventObject> componentType() {
            return TestLiEventObject.class;
        }
    }

    @SuppressWarnings("all")
    private static class TestLiEventListener2 implements ILiEventListener<TestLiEventObject> {


        @Override
        public void listen(TestLiEventObject event) {
            Assertions.fail();
        }

        @Override
        public void onError(Throwable throwable) {
        }

        @Override
        public Class<TestLiEventObject> componentType() {
            return TestLiEventObject.class;
        }
    }

    private static class TestLiEventListener3 implements ILiEventListener<TestLiEventObject> {


        @Override
        public void listen(TestLiEventObject event) {
            Assertions.fail();
        }

        @Override
        public void onError(Throwable throwable) {
            throw new RuntimeException(throwable);
        }

        @Override
        public Class<TestLiEventObject> componentType() {
            return TestLiEventObject.class;
        }
    }

    @SuppressWarnings("all")
    private static class TestListenerLi implements ILiEventListener<String> {

        @Override
        public void listen(String event) {

            assert event.equals("123");
        }

        @Override
        public Class<String> componentType() {
            return String.class;
        }
    }

    static class TempListener implements ILiEventListener<TestLiEventObject> {

        int count;

        boolean remove;

        TempListener(boolean remove) {
            this.remove = remove;
        }

        @Override
        public void listen(TestLiEventObject event) {

            Assertions.assertEquals("Some(123)",
                    event.getSource().toString());
            count++;

        }

        @Override
        public void after(LiEventBusBehavior<TestLiEventObject> eventBusBehavior) {
            if (remove) {
                eventBusBehavior.unRegisterListener(this);
            }
        }

        @Override
        public Class<TestLiEventObject> componentType() {
            return TestLiEventObject.class;
        }
    }

    private class StringEventObject implements ILiEventListener<String> {


        @Override
        public void listen(String event) {
            box.value(1);

        }

        @Override
        public boolean before(String event) {
            return ILiEventListener.super.before(event);
        }

        @Override
        public void after(LiEventBusBehavior<String> eventBusBehavior) {
            eventBusBehavior.unRegisterListener(this);
        }
    }
}
