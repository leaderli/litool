package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.collection.IterableItr;
import io.leaderli.litool.core.function.ThrowableConsumer;
import io.leaderli.litool.core.function.ThrowableFunction;
import io.leaderli.litool.core.lang.EqualComparator;
import io.leaderli.litool.core.meta.*;
import io.leaderli.litool.core.type.ClassUtil;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/6/28
 */
public abstract class Ra<T> implements Lira<T> {


    @Override
    public boolean present() {
        return first().present();
    }

    @Override
    public String name() {
        return "List";
    }

    @Override
    public <K, V> Lira<Map<K, V>> cast(Class<? extends K> keyType, Class<? extends V> valueType) {
        return map(m -> Lino.of(m).<K, V>cast(keyType, valueType).get());
    }

    @Override
    public boolean contains(T t) {

        LiBox<Integer> exits = LiBox.none();

        CancelConsumerSubscriber<T> actualSubscriber = new CancelConsumerSubscriber<>((v, s) -> {
            if (t == v) {
                exits.value(0);
                s.cancel();
            }
        });

        subscribe(actualSubscriber);
        return exits.present();
    }

    @Override
    public Lira<T> filter(Function<? super T, ?> filter) {
        return new FilterRa<>(this, filter);

    }

    @Override
    public Lira<T> filter_null() {
        return filter(v -> true);
    }

    @Override
    public Lino<T> first() {
        return get(0);
    }

    @Override
    public Lino<T> last() {
        return get(-1);
    }

    @Override
    public Lino<T> first(Function<? super T, ?> filter) {
        return filter(filter).first();
    }

    @Override
    public Lino<T> get(int index) {

        if (index > -1) {
            LiBox<T> box = LiBox.none();
            // remove null element, then limit n element and skip n-1 element
            filter_null().limit(index + 1).skip(index).subscribe(new ConsumerSubscriber<>(box::value));
            return box.lino();
        } else {
            // to avoid avoid generator duplicate request problem, convert to a limit iterator
            Lira<T> of = Lira.of(get());
            index = of.size() + index;
            if (index < 0) {
                return Lino.none();
            }
            return of.get(index);
        }
    }


    @Override
    public Iterator<T> iterator() {
        IterableSubscriber<T> iterator = new IterableSubscriber<>();
        // default iterator avoid null element
        filter(Objects::nonNull).subscribe(iterator);
        return iterator;

    }

    @Override
    public Iterator<T> nullableIterator() {
        IterableSubscriber<T> iterator = new IterableSubscriber<>();
        subscribe(iterator);
        return iterator;
    }

    @Override
    public Lira<T> limit(int max) {
        if (max > 0) {
            return new LimitRa<>(this, max);
        }
        return new NoneRa<>(this);

    }

    @Override
    public void forEach(Consumer<? super T> consumer) {
        subscribe(new ConsumerSubscriber<>(consumer));
    }

    @Override
    public <R> Lira<R> map(Function<? super T, ? extends R> mapper) {
        return new MapRa<>(this, mapper);

    }

    @Override
    public <R> Lira<LiTuple2<T, R>> tuple(Function<? super T, ? extends R> mapper) {
        return new TupleRa<>(this, mapper);
    }

    @Override
    public <R> Lira<R> flatMap() {
        return new FlatMap<>(this, IterableItr::of);
    }

    @Override
    public <R> Lira<R> flatMap(Function<? super T, Iterator<? extends R>> mapper) {

        return new FlatMap<>(this, mapper);
    }

    @Override
    public Lira<T> terminal(Function<List<T>, Iterable<T>> deliverAction) {
        return new TerminalRa<>(this, deliverAction);
    }

    @Override
    public Lira<T> takeWhile(Function<? super T, ?> filter) {
        return new TakeWhileRa<>(this, filter);
    }

    @Override
    public Lira<T> dropWhile(Function<? super T, ?> filter) {
        return new DropWhileRa<>(this, filter);
    }

    @Override
    public Lira<T> nullable(Supplier<? extends T> supplier) {
        return new NullableRa<>(this, supplier);

    }

    @Override
    public Lira<T> skip(int min) {
        if (min > 0) {
            return new SkipRa<>(this, min);
        }

        return this;
    }

    @Override
    public <R> Lira<R> throwable_map(ThrowableFunction<? super T, ? extends R> mapper) {
        return new ThrowableMapRa<>(this, mapper);


    }

    @Override
    public <R> Lira<R> throwable_map(ThrowableFunction<? super T, ? extends R> mapper, Consumer<Throwable> whenThrow) {
        return new ThrowableMapRa<>(this, mapper);

    }

    @Override
    public Lira<T> onError(Exceptionable onError) {
        return new OnErrorRa<>(this, onError);
    }

    @Override
    public Lira<T> debug(DebugConsumer<T> action) {


        return new DebugRa<>(this, action);
    }

    @SafeVarargs
    @Override
    public final Lira<T> or(T... alternate) {

        return or(Arrays.asList(alternate));
    }

    @Override
    public Lira<T> or(Iterator<? extends T> alternate) {
        List<T> raw = get();
        if (raw.isEmpty()) {
            return Lira.of(alternate);
        }
        return Lira.of(raw);
    }

    @Override
    public Lira<T> or(Iterable<? extends T> alternate) {
        return or(alternate.iterator());
    }

    @Override
    public Lino<T> reduce(BinaryOperator<T> accumulator) {

        if (accumulator == null) {
            return Lino.none();
        }
        LiBox<T> box = LiBox.none();
        subscribe(new CancelConsumerSubscriber<>((t, sub) -> {
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
        subscribe((new ConsumerSubscriber<>(t -> {
            if (box.absent()) {
                box.value(t);
            } else {
                box.apply(accumulator, t);
            }
        })));
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
    public Lira<T> sleep(int countdown, long milliseconds) {
        return new SleepRa<>(this, countdown, milliseconds);
    }


    @Override
    public int hashCode() {
        return get().hashCode();

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Ra) {

            return get().equals(((Ra<?>) obj).get());
        }
        return false;
    }

    @Override
    public Lira<T> distinct(EqualComparator<T> comparator) {

        return terminal(list -> {

            List<T> distinct = new ArrayList<>();
            out:
            for (T t : list) {

                if (t == null) {
                    if (!distinct.contains(null)) {
                        distinct.add(null);
                    }
                } else {

                    for (T di : distinct) {

                        if (di != null) {
                            if (comparator.apply(t, di)) {
                                continue out;
                            }

                        }
                    }
                    distinct.add(t);
                }
            }
            return distinct;
        });
    }


    @Override
    public Lira<T> sorted(Comparator<? super T> comparator) {
        return terminal(l -> {

            l.sort(comparator);
            return l;
        });
    }


    @Override
    public void forThrowableEach(ThrowableConsumer<? super T> action) {
        forThrowableEach(action, LiConstant.WHEN_THROW);
    }

    @Override
    public void forThrowableEach(ThrowableConsumer<? super T> action, Consumer<Throwable> whenThrow) {

        ThrowableConsumer<? super T> finalConsumer = action == null ? t -> {
        } : action;
        subscribe((new SubscriberRa<T>() {

            @Override
            public void onSubscribe(SubscriptionRa prevSubscription) {
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


        }));
    }

    @Override
    public final List<T> get() {

        List<T> result = new ArrayList<>();
        ConsumerSubscriber<T> delegate = new ConsumerSubscriber<>(result::add);
        subscribe(delegate);
        return result;
    }

    @Override
    public <R> Lira<R> cast(Class<? extends R> type) {
        return map(m -> ClassUtil.cast(m, type));

    }

    @Override
    public <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {

        Map<K, V> result = new HashMap<>();

        subscribe((new ConsumerSubscriber<>(e -> Lino.of(e).map(keyMapper).ifPresent(key -> {
            V value = Lino.of(e).map(valueMapper).get();
            result.put(key, value);
        }))));

        return result;
    }


    @Override
    public <K, V> Map<K, V> toMap(Function<? super T, LiTuple2<? extends K, ? extends V>> mapper) {
        Map<K, V> result = new HashMap<>();
        subscribe(new ConsumerSubscriber<>(e -> Lino.of(e).map(mapper).filter(tuple2 -> tuple2._1 != null).ifPresent(tuple2 -> result.put(tuple2._1, tuple2._2))));
        return result;
    }

    @Override
    public String toString() {
        return get().toString();
    }

}
