package io.leaderli.litool.core.event;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LiEventObjectStoreTest {


    private static class TestLiEventObject extends LiEventObject<String> {

        public TestLiEventObject(String source) {
            super(source);
        }
    }


    private static class TestLiEventListener implements ILiEventListener<TestLiEventObject> {


        @Override
        public void listen(TestLiEventObject source) {
            assert source.getSource().get().equals("123");

        }

        @Override
        public Class<TestLiEventObject> componentType() {
            return TestLiEventObject.class;
        }
    }


    @SuppressWarnings("all")
    private static class TestLiEventListener2 implements ILiEventListener<TestLiEventObject> {


        @Override
        public void listen(TestLiEventObject source) {

            Assertions.assertEquals(source.getSource().get(), "123");

        }

        @Override
        public Class<TestLiEventObject> componentType() {
            return TestLiEventObject.class;
        }
    }

    @SuppressWarnings("all")
    private static class TestListenerLi implements ILiEventListener<String> {

        @Override
        public void listen(String source) {

            assert source.equals("123");
        }

        @Override
        public Class<String> componentType() {
            return String.class;
        }
    }


    @Test
    void getPublisher() {


        LiEventBus eventStore = new LiEventBus();

        eventStore.registerListener(new TestLiEventListener());
        eventStore.registerListener(new TestLiEventListener2());

        Assertions.assertThrows(AssertionError.class, () -> {

            eventStore.push(new TestLiEventObject("456"));
            eventStore.push(null);
        });

    }

    static class TempListener implements ILiEventListener<TestLiEventObject> {

        int count;

        boolean remove;

        TempListener(boolean remove) {
            this.remove = remove;
        }

        @Override
        public Class<TestLiEventObject> componentType() {
            return TestLiEventObject.class;
        }

        @Override
        public void listen(TestLiEventObject source) {

            Assertions.assertEquals("Some(123)",
                    source.getSource().toString());
            count++;

        }

        @Override
        public boolean removeIf() {
            return remove;
        }
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


}
