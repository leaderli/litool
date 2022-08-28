package io.leaderli.litool.core.event;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LiEventObjectTest {


@Test
void getSource() {

    Assertions.assertTrue(new LiEventObject<>(null).getSource().absent());

}


}
