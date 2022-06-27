package io.leaderli.litool.core.meta;

import io.leaderli.litool.core.exception.LiThrowableFunction;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/6/28
 */
public abstract class RaSome<T> implements Lira<T> {


    @Override
    public boolean isPresent() {
        return first().isPresent();
    }

    @Override
    public String name() {
        return "List";
    }


    @Override
    public <R> Lira<R> cast(Class<R> type) {
        return map(m -> Lino.of(m).cast(type).get());

    }

    @Override
    public <K, V> Lira<Map<K, V>> cast(Class<K> keyType, Class<V> valueType) {
        return map(m -> Lino.of(m).cast(keyType, valueType).get());
    }

    @Override
    public Lira<T> filter(Function<? super T, Object> filter) {
        return new RaFilter<>(this, filter);

    }

    @Override
    public Lira<T> filter() {
        return filter(null);
    }

    @Override
    public Lino<T> first() {
        LiBox<T> value = LiBox.none();

        this.subscribe(new BiConsumerRaSubscriber<>((v, s) -> {
            value.value(v.get());
            s.cancel();
        }));
        return value.lino();
    }

    @Override
    public Lino<T> first(Function<? super T, Object> filter) {
        return filter(filter).first();
    }

    @Override
    public void forEachLino(Consumer<Lino<T>> consumer) {
        this.subscribe(new ConsumerRaSubscriber<>(consumer));
    }


    @Override
    public void forEach(Consumer<T> consumer) {
        this.subscribe(new ConsumerRaSubscriber<>((v) -> consumer.accept(v.get())));
    }

    @Override
    public List<Lino<T>> get() {
        List<Lino<T>> result = new ArrayList<>();
        this.subscribe(new ConsumerRaSubscriber<>(result::add));

        return result;
    }

    @Override
    public Lira<T> limit(int n) {
        if (n >= 0) {
            return new RaLimit<>(this, n);
        }
        return this;
    }

    @Override
    public List<T> getRaw() {

        List<T> result = new ArrayList<>();
        this.subscribe(new ConsumerRaSubscriber<>(v -> result.add(v.get())));
        return result;
    }

    @Override
    public <R> Lira<R> map(Function<? super T, ? extends R> mapping) {
        return new RaMap<>(this, mapping);

    }

    @Override
    public Lira<T> skip(int n) {
        if (n > 0) {
            return new RaSkip<>(this, n);
        }

        return this;
    }

    @Override
    public <R> Lira<R> throwable_map(LiThrowableFunction<? super T, ? extends R> mapping) {
        return new RaThrowableMap<>(this, mapping, null);


    }

    @Override
    public <R> Lira<R> throwable_map(LiThrowableFunction<? super T, ? extends R> mapping, Consumer<Throwable> whenThrow) {
        return new RaThrowableMap<>(this, mapping, whenThrow);

    }


    @SafeVarargs
    @Override
    public final Lira<T> or(T... others) {

        return or(Arrays.asList(others));
    }

    @Override
    public Lira<T> or(Iterator<T> others) {
        List<T> raw = getRaw();
        if (raw.isEmpty()) {
            return Lira.of(others);
        }
        return Lira.of(raw);
    }

    @Override
    public Lira<T> or(Iterable<T> others) {
        return or(others.iterator());
    }


    @Override
    public int size() {
        return this.get().size();
    }


}
