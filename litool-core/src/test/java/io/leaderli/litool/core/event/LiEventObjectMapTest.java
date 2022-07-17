package io.leaderli.litool.core.event;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LiEventObjectMapTest {

    @Test
    void test() {

        LiEventMap liEventMap = new LiEventMap();

        Temp listener1 = new Temp();
        Temp listener2 = new Temp();

        liEventMap.put(TempEventObject.class, listener1);
        liEventMap.put(TempEventObject.class, listener1);
        Assertions.assertEquals(1, liEventMap.get(TempEventObject.class).size());

        liEventMap.put(TempEventObject.class, listener2);
        Assertions.assertEquals(2, liEventMap.get(TempEventObject.class).size());

        liEventMap.remove(listener2);
        Assertions.assertEquals(1, liEventMap.get(TempEventObject.class).size());
    }

    @Test
    void get() {
        LiEventMap liEventMap = new LiEventMap();

        Assertions.assertNotNull(liEventMap.get(TempEventObject.class));

        Temp listener1 = new Temp();
        Temp listener2 = new Temp();

        liEventMap.put(TempEventObject.class, listener1);
        liEventMap.put(TempEventObject.class, listener2);
        Assertions.assertEquals(2, liEventMap.get(TempEventObject.class).size());
        liEventMap.get(TempEventObject.class).remove(listener1);
        Assertions.assertEquals(2, liEventMap.get(TempEventObject.class).size());

        liEventMap.remove(listener2);
        Assertions.assertEquals(1, liEventMap.get(TempEventObject.class).size());


    }

    private static class TempEventObject extends LiEventObject<String> {

        public TempEventObject(String source) {
            super(source);
        }
    }

    private static class Temp implements ILiEventListener<TempEventObject> {

        @Override
        public void listen(TempEventObject source) {

        }

        @Override
        public Class<TempEventObject> componentType() {
            return TempEventObject.class;
        }
    }


}
