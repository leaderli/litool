package io.leaderli.litool.core.cache;

import java.lang.annotation.Annotation;

@SuppressWarnings("ClassExplicitlyAnnotation")
public class LiCacheImpl implements LiCache {
    private long millis = 5000;

    private boolean allowNullValue = false;

    private boolean ignoreError = false;

    public static LiCacheImpl of() {
        return new LiCacheImpl();
    }

    public LiCacheImpl millis(long millis) {
        this.millis = millis;
        return this;
    }

    public LiCacheImpl allowNullValue(boolean allowNullValue) {
        this.allowNullValue = allowNullValue;
        return this;

    }

    public LiCacheImpl ignoreError(boolean ignoreError) {
        this.ignoreError = ignoreError;
        return this;
    }

    @Override
    public long millis() {
        return this.millis;
    }

    @Override
    public boolean allowNullValue() {
        return this.allowNullValue;
    }


    public boolean ignoreError() {
        return this.ignoreError;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return LiCache.class;
    }
}
