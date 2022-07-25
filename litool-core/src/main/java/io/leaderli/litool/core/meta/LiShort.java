package io.leaderli.litool.core.meta;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/26
 */


public interface LiShort<T, R> {

    static <T, R> LiShort<T, R> of(T src) {

        if (src == null) {
            return new None<>(null);
        }
        return new Some<>(src);
    }

    LiShort<T, R> or(Function<T, R> mapper);

    LiShort<T, R> def(R target);

    R get();
}


class Some<T, R> implements LiShort<T, R> {


    private final T src;

    Some(T src) {
        this.src = src;
    }


    @Override
    public LiShort<T, R> or(Function<T, R> mapper) {


        R target = mapper.apply(src);
        if (target == null) {
            return new Some<>(src);

        }
        return new None<>(target);
    }

    @Override
    public LiShort<T, R> def(R target) {
        return new None<>(target);
    }

    @Override
    public R get() {
        return null;
    }
}

class None<T, R> implements LiShort<T, R> {
    private final R target;

    None(R target) {
        this.target = target;
    }

    @Override
    public LiShort<T, R> or(Function<T, R> mapper) {
        return this;
    }

    @Override
    public LiShort<T, R> def(R def) {
        if (this.target == null) {
            return new None<>(def);
        }
        return this;
    }

    @Override
    public R get() {
        return target;
    }
}
