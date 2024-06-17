package io.leaderli.litool.core.cache;

import io.leaderli.litool.core.collection.ArrayEqual;
import io.leaderli.litool.core.exception.ExceptionUtil;
import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.function.ThrowableFunction;
import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.type.LiTypeToken;
import io.leaderli.litool.core.type.ReflectUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class CacheProxy<T> {

    public static Consumer<Exception> LOGGER = Throwable::printStackTrace;

    private final Class<T> interfaceType;
    private final T proxy;
    private final Map<Method, Map<ArrayEqual<?>, LiTuple<Long, Object>>> cache = new HashMap<>();
    private final Map<Method, ThrowableFunction<ArrayEqual<?>, Object>> invokes = new HashMap<>();

    @SuppressWarnings("unchecked")
    public CacheProxy(LiTypeToken<T> liTypeToken, T proxy) {
        this((Class<T>) liTypeToken.getRawType(), proxy);
    }

    @SuppressWarnings("unchecked")
    public CacheProxy(LiTypeToken<T> liTypeToken, T proxy, Function<Method, LiCache> cacheStrategy) {
        this((Class<T>) liTypeToken.getRawType(), proxy, cacheStrategy);
    }

    public CacheProxy(Class<T> interfaceType, T proxy) {
        this(interfaceType, proxy, m -> m.getAnnotation(LiCache.class));
    }

    public CacheProxy(Class<T> interfaceType, T proxy, Function<Method, LiCache> cacheStrategy) {

        LiAssertUtil.assertTrue(interfaceType.isInterface(), IllegalArgumentException::new, "only support interface");
        this.interfaceType = interfaceType;
        this.proxy = proxy;
        for (Method method : interfaceType.getMethods()) {
            cache.put(method, new HashMap<>());
            LiCache liCache = cacheStrategy.apply(method);
            if (liCache != null) {
                long check_millis = liCache.millis();
                invokes.put(method, ae -> {
                    Map<ArrayEqual<?>, LiTuple<Long, Object>> invokeCache = cache.get(method);
                    LiTuple<Long, Object> lastResult = invokeCache.computeIfAbsent(ae, a -> LiTuple.of(0L, null));
                    long now = System.currentTimeMillis();
                    if (now - lastResult._1 < check_millis) {
                        return lastResult._2;
                    }
                    synchronized (proxy) {
                        if (now - lastResult._1 < check_millis) {
                            return lastResult._2;
                        }
                        Object value = null;
                        try {
                            value = method.invoke(proxy, (Object[]) ae.arr);
                        } catch (Exception e) {
                            if (liCache.ignoreError()) {
                                LOGGER.accept(e);
                            } else {
                                throw ExceptionUtil.unwrapThrowable(e);
                            }
                        }
                        if (value != null || liCache.allowNullValue()) {
                            invokeCache.put(ae, LiTuple.of(now, value));
                        }
                        proxy.notifyAll();

                    }
                    return invokeCache.get(ae)._2;
                });
            }
        }
    }

    public T instance() {
        return ReflectUtil.newProxyInstance(interfaceType.getClassLoader(), interfaceType, (proxy, method, args) -> invoke(method, args));
    }

    private Object invoke(Method method, Object[] args) throws Throwable {
        ThrowableFunction<ArrayEqual<?>, Object> function = invokes.get(method);
        if (function != null) {
            return function.apply(ArrayEqual.of(args));
        }
        return method.invoke(proxy, args);
    }

}
