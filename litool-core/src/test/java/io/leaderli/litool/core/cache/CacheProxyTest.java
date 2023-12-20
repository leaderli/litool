package io.leaderli.litool.core.cache;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.type.LiTypeToken;
import io.leaderli.litool.core.util.ThreadUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Supplier;

class CacheProxyTest {


    @Test
    void test1() {
        CacheProxy.LOGGER = e -> {
        };
        AtomicInteger num = new AtomicInteger(0);
        AtomicLong time = new AtomicLong(System.currentTimeMillis());
        Supplier<Integer> cache = new CacheProxy<>(new LiTypeToken<Supplier<Integer>>() {
        }, () -> {
            int result = num.incrementAndGet();
            if (result % 3 == 0) {
                throw new IllegalStateException();
            }
            if (result % 5 == 0) {
                return null;
            }
            long now = System.currentTimeMillis();
            return result;
        }, m -> LiCacheImpl.of().millis(50).ignoreError(true)).instance();

        Supplier<Integer> finalCache = cache;
        new Thread(() -> {
            for (; ; ) {
                finalCache.get();
            }
        }).start();

        for (int i = 0; i < 50; i++) {

            int now = cache.get();
            Assertions.assertTrue(now % 3 != 0 && now % 5 != 0);
            ThreadUtil.sleep(5);
        }
        Assertions.assertTrue(cache.get() < 16);
        cache = new CacheProxy<>(new LiTypeToken<Supplier<Integer>>() {
        }, () -> {
            throw new IllegalStateException();
        }, m -> LiCacheImpl.of()).instance();

        Assertions.assertThrows(RuntimeException.class, cache::get);
        cache = new CacheProxy<>(new LiTypeToken<Supplier<Integer>>() {
        }, () -> {
            throw new IllegalStateException();
        }, m -> LiCacheImpl.of().ignoreError(true)).instance();
        Assertions.assertDoesNotThrow(cache::get);

    }

    @Test
    void test() {

        CacheProxy<Supplier<Integer>> cacheProxy = new CacheProxy<>(new LiTypeToken<Supplier<Integer>>() {
        }, Lino.of(1));
        Supplier<Integer> instance = cacheProxy.instance();
        Assertions.assertEquals(1, instance.get());
        CacheProxy<Function<String, String>> cache = new CacheProxy<>(new LiTypeToken<Function<String, String>>() {

        }, s -> s, m -> LiCacheImpl.of().allowNullValue(true));
        Function<String, String> cache2 = cache.instance();
        Assertions.assertNull(cache2.apply(null));
        Assertions.assertNull(cache2.apply(null));


        Map<Integer, AtomicInteger> map = new HashMap<>();
        map.put(1, new AtomicInteger(10));
        map.put(2, new AtomicInteger(20));
        Function<Integer, Integer> cacheFunction = new CacheProxy<>(new LiTypeToken<Function<Integer, Integer>>() {
        }, i -> map.get(i).incrementAndGet(), m -> LiCacheImpl.of().millis(50)).instance();


        Assertions.assertEquals(11, cacheFunction.apply(1));
        Assertions.assertEquals(11, cacheFunction.apply(1));
        Assertions.assertEquals(11, cacheFunction.apply(1));
        ThreadUtil.sleep(50);
        Assertions.assertEquals(12, cacheFunction.apply(1));
        ThreadUtil.sleep(50);
        Assertions.assertEquals(13, cacheFunction.apply(1));
        Assertions.assertEquals(21, cacheFunction.apply(2));

    }
}