package io.leaderli.litool.core.event;

import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.type.ReflectUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

class LiEventObjectMapTest {

    @SuppressWarnings("unchecked")
    @Test
    void put() {

        LiEventMap liEventMap = new LiEventMap();

        Temp listener1 = new Temp();
        Temp listener2 = new Temp();

        liEventMap.put(TempStringEventObject.class, listener1);
        liEventMap.put(TempStringEventObject.class, listener1);
        Map<Class<?>, Set<ILiEventListener<?, ?>>> eventListenerMap =
                (Map<Class<?>, Set<ILiEventListener<?, ?>>>) ReflectUtil.getFieldValue(liEventMap, "eventListenerMap").get();
        Assertions.assertEquals(1, eventListenerMap.get(TempStringEventObject.class).size());

        liEventMap.put(TempStringEventObject.class, listener2);
        Assertions.assertEquals(2, eventListenerMap.get(TempStringEventObject.class).size());

        liEventMap.remove(listener2);
        Assertions.assertEquals(1, eventListenerMap.get(TempStringEventObject.class).size());


        Assertions.assertNotNull(eventListenerMap.get(TempStringEventObject.class));

    }

    @Test
    void compute() {
        LiEventMap liEventMap = new LiEventMap();

        LiBox<Integer> num = LiBox.of(0);
        liEventMap.put(TempStringEventObject.class, new StringEventListener());

        liEventMap.compute(TempStringEventObject.class, li -> num.value(num.value() + 1));
        Assertions.assertSame(1, num.value());
        liEventMap.put(TempStringEventObject.class, new StringEventListener());
        liEventMap.compute(TempStringEventObject.class, li -> num.value(num.value() + 1));
        Assertions.assertSame(3, num.value());

        liEventMap.compute(TempStringEventObject.class, null);
        Assertions.assertSame(3, num.value());

    }

    @SuppressWarnings("unchecked")
    @Test
    void remove() {
        LiEventMap liEventMap = new LiEventMap();
        Map<Class<?>, Set<ILiEventListener<?, ?>>> eventListenerMap =
                (Map<Class<?>, Set<ILiEventListener<?, ?>>>) ReflectUtil.getFieldValue(liEventMap, "eventListenerMap").get();
        eventListenerMap.clear();
        Temp listener1 = new Temp();
        Temp listener2 = new Temp();

        liEventMap.put(TempStringEventObject.class, listener1);
        liEventMap.put(TempStringEventObject.class, listener2);
        Assertions.assertEquals(2, eventListenerMap.get(TempStringEventObject.class).size());

        liEventMap.remove(listener2);
        Assertions.assertEquals(1, eventListenerMap.get(TempStringEventObject.class).size());
        liEventMap.remove(listener1);
        Assertions.assertTrue(eventListenerMap.get(TempStringEventObject.class).isEmpty());


    }

    private static class StringEventListener implements ILiEventListener<TempStringEventObject, String> {

        @Override
        public void listen(String event) {
        }
    }

    private static class TempStringEventObject extends LiEventObject<String> {

        public TempStringEventObject(String source) {
            super(source);
        }
    }

    private static class Temp implements ILiEventListener<TempStringEventObject, String> {

        @Override
        public void listen(String event) {

        }

        @Override
        public Class<TempStringEventObject> componentType() {
            return TempStringEventObject.class;
        }
    }


}
