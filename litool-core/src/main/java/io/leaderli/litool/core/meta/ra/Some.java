package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.collection.LiIterator;
import io.leaderli.litool.core.function.ThrowableConsumer;
import io.leaderli.litool.core.function.ThrowableFunction;
import io.leaderli.litool.core.lang.EqualComparator;
import io.leaderli.litool.core.meta.*;
import io.leaderli.litool.core.type.ClassUtil;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * {@link Lira} 的有值实现类，采用响应式编程思路，仅在实际获取值的方法操作中才会对底层元素进行各种操作;
 *
 * @author leaderli
 * @since 2022/6/28
 */
public abstract class Some<T> implements Lira<T> {


    @Override
    public boolean present() {
        return first().present();
    }

    @Override
    public String name() {
        return "List";
    }

    @Override
    public <R> Lira<R> cast(Class<? extends R> type) {
        return map(m -> ClassUtil.cast(m, type));

    }

    @Override
    public <K, V> Lira<Map<K, V>> cast(Class<? extends K> keyType, Class<? extends V> valueType) {
        return map(m -> Lino.of(m).<K, V>cast(keyType, valueType).get());
    }

    @Override
    public boolean contains(T t) {

        LiBox<Integer> exits = LiBox.none();
        this.subscribe(new BiConsumerSubscriber<>((lino, s) ->

                Lino.of(lino)
                        .contain(t)
                        .ifPresent(v1 -> {
                            exits.value(1);
                            s.cancel();
                        }))
        );
        return exits.present();
    }

    @Override
    public Lira<T> filter(Function<? super T, ?> filter) {
        return new Filter<>(this, filter);

    }

    @Override
    public Lira<T> filter() {
        return filter(null);
    }

    @Override
    public Lino<T> first() {
        LiBox<T> value = LiBox.none();

        this.subscribe(new BiConsumerSubscriber<>((v, s) -> {
            value.value(v);
            s.cancel();
        }));
        return value.lino();
    }

    @Override
    public Lino<T> last() {

        LiBox<T> value = LiBox.none();

        this.subscribe(new ConsumerSubscriber<>(value::value));
        return value.lino();
    }

    @Override
    public Lino<T> first(Function<? super T, ?> filter) {
        return filter(filter).first();
    }

    @Override
    public List<T> getRaw() {
        List<T> result = new ArrayList<>();
        this.subscribe(new ConsumerSubscriber<>(result::add));

        return result;
    }

    @Override
    public Lino<T> get(int index) {

        if (index < 0) {
            return Lino.none();
        }
        LiBox<Integer> count = LiBox.of(0);
        LiBox<T> result = LiBox.none();
        this.subscribe(new Subscriber<T>() {
            private Subscription prevSubscription;

            @Override
            public void onSubscribe(Subscription prevSubscription) {
                this.prevSubscription = prevSubscription;
                prevSubscription.request();
            }

            @Override
            public void next(T t) {

                if (count.value() == index) {
                    result.value(t);
                    prevSubscription.cancel();
                }
                count.value(count.value() + 1);

            }

        });

        return result.lino();
    }

    @Override
    public Iterator<T> iterator() {
        return get().iterator();
    }

    @Override
    public void forEach(Consumer<? super T> consumer) {
        this.subscribe(new ConsumerSubscriber<>(consumer));
    }

    @Override
    public Lira<T> limit(int n) {
        if (n >= 0) {
            return new Limit<>(this, n);
        }
        return this;
    }

    @Override
    public <R> Lira<R> flatMap() {
        return new FlatMap<>(this, LiIterator::of);
    }

    @Override
    public <R> Lira<R> flatMap(Function<? super T, Iterator<? extends R>> mapper) {

        return new FlatMap<>(this, mapper);
    }

    @Override
    public <R> Lira<R> map(Function<? super T, ? extends R> mapper) {
        return new MapSome<>(this, mapper);

    }

    @Override
    public Lira<T> skip(int n) {
        if (n > 0) {
            return new Skip<>(this, n);
        }

        return this;
    }

    @Override
    public <R> Lira<R> throwable_map(ThrowableFunction<? super T, ? extends R> mapper) {
        return new ThrowableMap<>(this, mapper, LiConstant.WHEN_THROW);


    }

    @Override
    public <R> Lira<R> throwable_map(ThrowableFunction<? super T, ? extends R> mapper, Consumer<Throwable> whenThrow) {
        return new ThrowableMap<>(this, mapper, whenThrow);

    }

    @SafeVarargs
    @Override
    public final Lira<T> or(T... others) {

        return or(Arrays.asList(others));
    }

    @Override
    public Lira<T> or(Iterator<? extends T> others) {
        List<T> raw = get();
        if (raw.isEmpty()) {
            return Lira.of(others);
        }
        return Lira.of(raw);
    }

    @Override
    public Lira<T> or(Iterable<? extends T> others) {
        return or(others.iterator());
    }

    @Override
    public Lino<T> reduce(BinaryOperator<T> accumulator) {

        if (accumulator == null) {
            return Lino.none();
        }
        LiBox<T> box = LiBox.none();
        this.subscribe(new BiConsumerSubscriber<>((t, sub) -> {
            if (box.absent()) {
                box.value(t);
            } else {
                box.apply(accumulator, t);
                if (box.absent()) {
                    sub.cancel();
                }
            }
        }));
        return box.lino();
    }

    @Override
    public Lino<T> reduce(T identity, BinaryOperator<T> accumulator) {


        if (accumulator == null) {
            return Lino.none();
        }
        LiBox<T> box = LiBox.of(identity);
        this.subscribe(new ConsumerSubscriber<>(t -> {
            if (box.absent()) {
                box.value(t);
            } else {
                box.apply(accumulator, t);
            }
        }));
        return box.lino();
    }

    @Override
    public int size() {
        return this.get().size();
    }

    @Override
    public Lira<T> debug() {
        return debug(System.out::println);

    }

    @Override
    public Lira<T> debug(Consumer<T> action) {
        List<T> raw = get();

        raw.forEach(action);
        return Lira.of(raw);
    }

    @Override
    public Lira<T> distinct() {


        return distinct(Object::equals);

    }

    @Override
    public Lira<T> distinct(EqualComparator<T> comparator) {


        return new Distinct<>(this, comparator);

    }

    @Override
    public Lira<T> sorted() {
        return sorted(null);
    }

    @Override
    public Lira<T> sorted(Comparator<? super T> comparator) {

        List<T> raw = get();
        raw.sort(comparator);
        return Lira.of(raw);
    }


    @Override
    public void forThrowableEach(ThrowableConsumer<? super T> action) {
        forThrowableEach(action, Throwable::printStackTrace);
    }

    @Override
    public void forThrowableEach(ThrowableConsumer<? super T> action, Consumer<Throwable> whenThrow) {

        ThrowableConsumer<? super T> finalConsumer = action == null ? t -> {
        } : action;
        this.subscribe(new Subscriber<T>() {

            @Override
            public void onSubscribe(Subscription prevSubscription) {
                prevSubscription.request();

            }

            @Override
            public void next(T t) {
                try {
                    finalConsumer.accept(t);
                } catch (Throwable e) {
                    whenThrow.accept(e);
                }

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public List<T> get() {

        List<T> result = new ArrayList<>();
        this.subscribe(new ConsumerSubscriber<>(result::add));
        return result;
    }

    @Override
    public <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {

        Map<K, V> result = new HashMap<>();

        this.subscribe(new ConsumerSubscriber<>(e ->
                Lino.of(e)
                        .map(keyMapper)
                        .ifPresent(key ->
                                {
                                    V value = Lino.of(e).map(valueMapper).get();
                                    result.put(key, value);
                                }
                        ))
        );

        return result;
    }

    @Override
    public <K, V> Map<K, V> toMap(Function<? super T, LiTuple2<? extends K, ? extends V>> mapper) {
        Map<K, V> result = new HashMap<>();
        this.subscribe(new ConsumerSubscriber<>(e ->
                        Lino.of(e)
                                .map(mapper)
                                .filter(tuple2 -> tuple2._1 != null)
                                .ifPresent(tuple2 -> result.put(tuple2._1, tuple2._2))
                )
        );
        return result;
    }

    @Override
    public String toString() {
        return get().toString();
    }


}
