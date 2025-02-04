package io.leaderli.litool.core.event;

import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.util.ThreadUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author leaderli
 * @since 2022/8/28
 */
class LiEventBusTest {


    LiBox<Integer> box = LiBox.none();

    @Test
    void getPublisher() {


        ExecutorService executorService = Executors.newCachedThreadPool();
//        executorService.execute();
        LiEventBus eventBus = new LiEventBus();

        eventBus.registerListener(new TestLiEventListener());
        eventBus.registerListener(new TestLiEventListener2());

        Assertions.assertDoesNotThrow(() -> {
            eventBus.push(new TestStringLiEventObject("456"));
            eventBus.push(new TestStringLiEventObject(null));
        });
        eventBus.registerListener(new TestLiEventListener3());

        Assertions.assertDoesNotThrow(() -> {
            eventBus.push(new TestStringLiEventObject("456"));
        });
        LiBox<Integer> count = LiBox.of(0);
        ILiEventListener<TestStringLiEventObject, String> listener = new ILiEventListener<TestStringLiEventObject, String>() {
            @Override
            public void onRegister() {
                count.value(1);
            }

            @Override
            public void onUnRegister() {
                count.value(2);
            }

            @Override
            public void listen(String source) {

            }
        };
        eventBus.registerListener(listener);
        Assertions.assertEquals(1, count.value());
        eventBus.unRegisterListener(listener);
        Assertions.assertEquals(2, count.value());

    }

    @Test
    void test1() {

        LiEventBus liEventBus = new LiEventBus();

        TempListener listener = new TempListener(true);
        Assertions.assertEquals(0, listener.count);
        liEventBus.registerListener(listener);
        liEventBus.push(new TestStringLiEventObject("123"));
        Assertions.assertEquals(1, listener.count);
        liEventBus.push(new TestStringLiEventObject("123"));
        Assertions.assertEquals(1, listener.count);


        TestLiEventListener4 listener4 = new TestLiEventListener4();
        liEventBus.registerListener(listener4);

        Assertions.assertEquals(0, listener4.value);

        liEventBus.push(new LiEventTuple("fuck", 100));
        Assertions.assertEquals(100, listener4.value);
    }

    @Test
    void test2() {
        LiEventBus liEventBus = new LiEventBus();


        TempListener listener = new TempListener(false);
        Assertions.assertEquals(0, listener.count);
        liEventBus.registerListener(listener);
        liEventBus.push(new TestStringLiEventObject("123"));
        Assertions.assertEquals(1, listener.count);
        liEventBus.push(new TestStringLiEventObject("123"));
        Assertions.assertEquals(2, listener.count);
    }

    @Test
    void test3() {
        LiEventBus liEventBus = new LiEventBus();
        ThreadListener listener = new ThreadListener();
        liEventBus.registerListener(listener);
        liEventBus.push(new TestStringLiEventObject("123"));
        Assertions.assertSame(Thread.currentThread(), listener.thread);
        Assertions.assertEquals("123", listener.source);
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        liEventBus.push(new TestStringLiEventObject("456"), (sourceProvider, l) -> {
            executorService.submit(() -> l.listen(sourceProvider.source));
        });
        ThreadUtil.sleep(100);
        Assertions.assertNotSame(Thread.currentThread(), listener.thread);
        Assertions.assertEquals("456", listener.source);
    }

    @Test
    void unRegisterListener() {

        LiEventBus eventBus = new LiEventBus();
        ILiEventListener<TestStringLiEventObject, String> listener = new StringEventObject();

        eventBus.registerListener(listener);
        ILiEventListener<TestStringLiEventObject, String> listener1 = new StringEventObject();
        ILiEventListener<TestStringLiEventObject, String> listener2 = new StringEventObject();
        ILiEventListener<TestStringLiEventObject, String> listener3 = new StringEventObject();

        eventBus.registerListener(listener1);
        eventBus.registerListener(listener2);
        eventBus.registerListener(listener3);

        eventBus.push(new TestStringLiEventObject("123"));
        Assertions.assertEquals(1, box.value());

        box.reset();
        eventBus.push(new TestStringLiEventObject("123"));
        Assertions.assertTrue(box.absent());

        ((LiEventBusBehavior) eventBus).unRegisterListener(listener);
    }

    static class TestStringLiEventObject extends LiEventObject<String> {

        public TestStringLiEventObject(String source) {
            super(source);
        }
    }

    private static class TestLiEventListener implements ILiEventListener<TestStringLiEventObject, String> {


        @Override
        public void listen(String source) {
            assert source.equals("123");

        }

        @Override
        public void onError(Throwable throwable) {
        }

        @Override
        public Class<TestStringLiEventObject> componentType() {
            return TestStringLiEventObject.class;
        }
    }

    @SuppressWarnings("all")
    private static class TestLiEventListener2 implements ILiEventListener<TestStringLiEventObject, String> {


        @Override
        public void listen(String source) {
            Assertions.fail();
        }

        @Override
        public void onError(Throwable throwable) {
        }

        @Override
        public Class<TestStringLiEventObject> componentType() {
            return TestStringLiEventObject.class;
        }
    }

    private static class TestLiEventListener3 implements ILiEventListener<TestStringLiEventObject, String> {


        @Override
        public void listen(String source) {
            Assertions.fail();
        }

        @Override
        public void onError(Throwable throwable) {
            throw new RuntimeException(throwable);
        }

        @Override
        public Class<TestStringLiEventObject> componentType() {
            return TestStringLiEventObject.class;
        }
    }

    static class ThreadListener implements ILiEventListener<TestStringLiEventObject, String> {

        Thread thread;
        String source;

        @Override
        public void listen(String source) {
            this.thread = Thread.currentThread();
            this.source = source;
        }
    }

    static class TempListener implements ILiEventListener<TestStringLiEventObject, String> {

        int count;

        boolean remove;

        TempListener(boolean remove) {
            this.remove = remove;
        }

        @Override
        public void listen(String source) {

            Assertions.assertEquals("123", source);
            count++;

        }

        @Override
        public void after(LiEventBusBehavior eventBusBehavior) {
            if (remove) {
                eventBusBehavior.unRegisterListener(this);
            }
        }

        @Override
        public Class<TestStringLiEventObject> componentType() {
            return TestStringLiEventObject.class;
        }
    }

    private class StringEventObject implements ILiEventListener<TestStringLiEventObject, String> {


        @Override
        public void listen(String event) {
            box.value(1);

        }

        @Override
        public boolean before(String event) {
            return ILiEventListener.super.before(event);
        }

        @Override
        public void after(LiEventBusBehavior eventBusBehavior) {
            eventBusBehavior.unRegisterListener(this);
        }
    }

    private static class LiEventTuple extends LiEventObject<LiTuple<String, Integer>> {
        public LiEventTuple(String s, int i) {
            super(LiTuple.of(s, i));
        }
    }

    private static class TestLiEventListener4 implements ILiEventListener<LiEventTuple, LiTuple<String, Integer>> {
        public int value;

        @Override
        public void listen(LiTuple<String, Integer> source) {

            this.value = source._2;
        }
    }
}
