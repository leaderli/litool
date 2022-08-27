package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.meta.Lino;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/7/14 10:37 AM
 */
class TupleMapTest {

@Test
void test() {

    TupleMap<String, String> tupleMap = new TupleMap<>();

    Assertions.assertEquals(Lino.none(), tupleMap.getValueByKey("k1"));
    Assertions.assertEquals(Lino.none(), tupleMap.getKeyByValue("v1"));

    tupleMap.putKeyValue("k1", "v1");
    Assertions.assertEquals("v1", tupleMap.getValueByKey("k1").get());
    Assertions.assertEquals("k1", tupleMap.getKeyByValue("v1").get());
    tupleMap.putKeyValue("k1", "v2");
    Assertions.assertEquals(Lino.none(), tupleMap.getKeyByValue("k1"));

    Assertions.assertEquals("v2", tupleMap.getValueByKey("k1").get());
    Assertions.assertEquals("k1", tupleMap.getKeyByValue("v2").get());


    tupleMap.removeByKey("k1");
    Assertions.assertEquals(Lino.none(), tupleMap.getValueByKey("k1"));
    Assertions.assertEquals(Lino.none(), tupleMap.getKeyByValue("v1"));

    tupleMap.putKeyValue("k1", "v2");

    tupleMap.clear();
    Assertions.assertEquals(Lino.none(), tupleMap.getValueByKey("k1"));
    Assertions.assertEquals(Lino.none(), tupleMap.getKeyByValue("v1"));


    TupleMap<String, Integer> of = TupleMap.of("1", 2);
    Assertions.assertEquals(2, of.getValueByKey("1").get());
    Assertions.assertEquals("1", of.getKeyByValue(2).get());

    tupleMap.putKeyValue("k1", "v1");
    tupleMap.putKeyValue("k2", "v2");

    tupleMap.removeIf(t -> t._1.contains("1"));


    Assertions.assertEquals(1, tupleMap.keySet().size());
}

}
