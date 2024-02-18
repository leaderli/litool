package io.leaderli.litool.test.cartesian;

import io.leaderli.litool.core.collection.ArrayUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MethodResultCartesianForParameterTest {


    @Test
    void test() {

        MethodResultCartesianForParameter methodResultCartesianForParameter = new MethodResultCartesianForParameter();
        methodResultCartesianForParameter.add(new Object[]{"1"}, "1", "2");
        methodResultCartesianForParameter.add(new Object[]{"2"}, "a", "b");


        Assertions.assertEquals("[{[1]=1, [2]=a}|null, {[1]=1, [2]=b}|null, {[1]=2, [2]=a}|null, {[1]=2, [2]=b}|null]",
                ArrayUtils.toString(methodResultCartesianForParameter.catesian()));

        methodResultCartesianForParameter = new MethodResultCartesianForParameter(1, 2, 3);


        Assertions.assertEquals("[{}|1, {}|2, {}|3]",
                ArrayUtils.toString(methodResultCartesianForParameter.catesian()));

        methodResultCartesianForParameter = new MethodResultCartesianForParameter(1, 2, 3);
        methodResultCartesianForParameter.add(new Object[]{"1"}, "1", "2");


        Assertions.assertEquals("[{[1]=1}|1, {[1]=1}|2, {[1]=1}|3, {[1]=2}|1, {[1]=2}|2, {[1]=2}|3]", ArrayUtils.toString(methodResultCartesianForParameter.catesian()));
    }
}
