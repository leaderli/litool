package io.leaderli.litool.core.event;

import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.type.ReflectUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class LiEventObjectMapTest {

    @SuppressWarnings("unchecked")
    @Test
    void put() {

        LiEventMap liEventMap = new LiEventMap();

        Temp listener1 = new Temp();
        Temp listener2 = new Temp();

        liEventMap.put(TempEventObject.class, listener1);
        liEventMap.put(TempEventObject.class, listener1);
        Map<Class<?>, List<ILiEventListener<?>>> eventListenerMap =
                (Map<Class<?>, List<ILiEventListener<?>>>) ReflectUtil.getFieldValue(liEventMap, "eventListenerMap").get();
        Assertions.assertEquals(1, eventListenerMap.get(TempEventObject.class).size());

        liEventMap.put(TempEventObject.class, listener2);
        Assertions.assertEquals(2, eventListenerMap.get(TempEventObject.class).size());

        liEventMap.remove(listener2);
        Assertions.assertEquals(1, eventListenerMap.get(TempEventObject.class).size());


        Assertions.assertNotNull(eventListenerMap.get(TempEventObject.class));

    }

    @Test
    void compute() {
        LiEventMap liEventMap = new LiEventMap();

        LiBox<Integer> num = LiBox.of(0);
        liEventMap.put(String.class, new StringEventListener());

        liEventMap.compute(String.class, li -> num.value(num.value() + 1));
        Assertions.assertSame(1, num.value());
        liEventMap.put(String.class, new StringEventListener());
        liEventMap.compute(String.class, li -> num.value(num.value() + 1));
        Assertions.assertSame(3, num.value());

        liEventMap.compute(String.class, null);
        Assertions.assertSame(3, num.value());

    }

    @SuppressWarnings("unchecked")
    @Test
    void remove() {
        LiEventMap liEventMap = new LiEventMap();
        Map<Class<?>, List<ILiEventListener<?>>> eventListenerMap =
                (Map<Class<?>, List<ILiEventListener<?>>>) ReflectUtil.getFieldValue(liEventMap, "eventListenerMap").get();
        eventListenerMap.clear();
        Temp listener1 = new Temp();
        Temp listener2 = new Temp();

        liEventMap.put(TempEventObject.class, listener1);
        liEventMap.put(TempEventObject.class, listener2);
        Assertions.assertEquals(2, eventListenerMap.get(TempEventObject.class).size());

        liEventMap.remove(listener2);
        Assertions.assertEquals(1, eventListenerMap.get(TempEventObject.class).size());
        liEventMap.remove(listener1);
        Assertions.assertNull(eventListenerMap.get(TempEventObject.class));


    }

    private static class StringEventListener implements ILiEventListener<String> {

        @Override
        public void listen(String event) {
        }
    }

    private static class TempEventObject extends LiEventObject<String> {

        public TempEventObject(String source) {
            super(source);
        }
    }

    private static class Temp implements ILiEventListener<TempEventObject> {

        @Override
        public void listen(TempEventObject event) {

        }

        @Override
        public Class<TempEventObject> componentType() {
            return TempEventObject.class;
        }
    }


}
