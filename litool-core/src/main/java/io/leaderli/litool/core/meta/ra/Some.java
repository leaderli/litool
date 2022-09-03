package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.collection.IterableItr;
import io.leaderli.litool.core.function.ThrowableConsumer;
import io.leaderli.litool.core.function.ThrowableFunction;
import io.leaderli.litool.core.lang.CompareBean;
import io.leaderli.litool.core.lang.EqualComparator;
import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ClassUtil;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
        return new FilterSome<>(this, filter);

    }

    @Override
    public Lira<T> filter() {
        return filter(null);
    }

    @Override
    public Lino<T> first() {
        return get(0);
    }

    @Override
    public Lino<T> last() {

        LiBox<T> value = LiBox.none();

        subscribe(new ConsumerSubscriber<>(value::value));
        return value.lino();
    }

    @Override
    public Lino<T> first(Function<? super T, ?> filter) {
        return filter(filter).first();
    }

    @Override
    public Lino<T> get(int index) {


        // limit n element and skip n-1 element
        return BoxSubscriber.subscribe(limit(index + 1).skip(index));
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
    public Lira<T> sleep(int countdown, long milliseconds) {
        return new SleepSome<>(this, countdown, milliseconds);
    }

    @Override
    public void forEach(Consumer<? super T> consumer) {
        subscribe(new ConsumerSubscriber<>(consumer));
    }

    @Override
    public Lira<T> limit(int n) {
        if (n > 0) {
            return new LimitSome<>(this, n);
        }
        return new NoneSome<>(this);

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
    public <R> Lira<R> map(Function<? super T, ? extends R> mapper) {
        return new MapSome<>(this, mapper);

    }

    @Override
    public Lira<T> takeWhile(Function<? super T, ?> filter) {
        return new TakeWhileSome<>(this, filter);
    }

    @Override
    public Lira<T> dropWhile(Function<? super T, ?> filter) {
        return new DropWhileSome<>(this, filter);
    }


    @Override
    public Lira<T> nullable(Supplier<? extends T> supplier) {
        return new NullableSome<>(this, supplier);

    }

    @Override
    public Lira<T> skip(int n) {
        if (n > 0) {
            return new SkipSome<>(this, n);
        }

        return this;
    }

    @Override
    public <R> Lira<R> throwable_map(ThrowableFunction<? super T, ? extends R> mapper) {
        return new ThrowableMap<>(this, mapper);


    }

    @Override
    public <R> Lira<R> throwable_map(ThrowableFunction<? super T, ? extends R> mapper, Consumer<Throwable> whenThrow) {
        return new ThrowableMap<>(this, mapper);

    }

    @Override
    public Lira<T> onError(Exceptionable onError) {
        return new OnErrorSome<>(this, onError);
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
    public Lira<T> debug(DebugConsumer<T> action) {


        return new DebugSome<>(this, action);
    }


    @Override
    public Lira<T> distinct(EqualComparator<T> comparator) {


        List<CompareBean<T>> compareBeans = map(e -> new CompareBean<>(e, comparator)).get();
        return Lira.of(new LinkedHashSet<>(compareBeans)).map(w -> w.value);


    }

    @Override
    public Lira<T> sorted() {
        return sorted(null);
    }

    @Override
    public Lira<T> sorted(Comparator<? super T> comparator) {
        List<T> sorted = get();
        sorted.sort(comparator);
        return Lira.of(sorted);
    }


    @Override
    public void forThrowableEach(ThrowableConsumer<? super T> action) {
        forThrowableEach(action, Throwable::printStackTrace);
    }

    @Override
    public void forThrowableEach(ThrowableConsumer<? super T> action, Consumer<Throwable> whenThrow) {

        ThrowableConsumer<? super T> finalConsumer = action == null ? t -> {
        } : action;
        subscribe((new Subscriber<T>() {

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
    public <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keyMapper,
                                  Function<? super T, ? extends V> valueMapper) {

        Map<K, V> result = new HashMap<>();

        subscribe((new ConsumerSubscriber<>(e ->
                Lino.of(e)
                        .map(keyMapper)
                        .ifPresent(key ->
                                {
                                    V value = Lino.of(e).map(valueMapper).get();
                                    result.put(key, value);
                                }
                        ))
        ));

        return result;
    }


    @Override
    public <K, V> Map<K, V> toMap(Function<? super T, LiTuple2<? extends K, ? extends V>> mapper) {
        Map<K, V> result = new HashMap<>();
        subscribe(new ConsumerSubscriber<>(e ->
                Lino.of(e)
                        .map(mapper)
                        .filter(tuple2 -> tuple2._1 != null)
                        .ifPresent(tuple2 -> result.put(tuple2._1, tuple2._2))
        ));
        return result;
    }

    @Override
    public String toString() {
        return get().toString();
    }

}
