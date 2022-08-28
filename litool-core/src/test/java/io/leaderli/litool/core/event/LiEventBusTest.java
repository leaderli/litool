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

private class StringEventObject implements ILiEventListener<String> {


    @Override
    public void listen(String event) {
        box.value(1);
        System.out.println(box);

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
