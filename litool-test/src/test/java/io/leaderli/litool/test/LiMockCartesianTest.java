package io.leaderli.litool.test;

import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.meta.Lira;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

class LiMockCartesianTest {

    @Test
    void test() {

        System.out.println(this.m1());
        LiMockCartesian.mock(this.getClass());
        System.out.println(this.m1());
        LiMockCartesian.when(this::m1, 100, 200);
        LiMockCartesian.when(this::m2, -100, -200);
        System.out.println(this.m1());

        Lira<Method> methods = Lira.of(LiMockCartesian.cache.keySet());


        Object[][] objects = methods.map(LiMockCartesian.cache::get).toArray(Object[].class);
        System.out.println(Arrays.deepToString(objects));
        System.out.println(Arrays.deepToString(CollectionUtils.cartesian(objects)));


        for (Object[] mapValues : CollectionUtils.cartesian(objects)) {


            AtomicInteger i = new AtomicInteger();
            Map<Method, Object> methodObjectMap = methods.toMap(m -> m, m -> mapValues[i.getAndIncrement()]);
            System.out.println(methodObjectMap);

        }
        for (Method method : methods) {

            LiMockCartesian.cache.get(method);
        }
    }

    private int m1() {

        return 1;
    }

    private int m2() {

        return 1;
    }
}
