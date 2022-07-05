package io.leaderli.litool.core.meta;

import io.leaderli.litool.core.collection.LiListUtil;
import io.leaderli.litool.core.exception.LiThrowableFunction;
import io.leaderli.litool.core.type.LiClassUtil;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author leaderli
 * @since 2022/6/19
 */
public interface Lira<T> extends LiValue, RaPublisher<T> {


    /**
     * @param value 值
     * @param <T>   泛型
     * @return 窄化一个宽泛的泛型， {@code <? extends T> } 转换为  {@code  <T> }
     */
    @SuppressWarnings("unchecked")
    static <T> Lira<T> narrow(Lira<? extends T> value) {

        return (Lira<T>) value;

    }

    /**
     * @param elements 数组
     * @param <T>      数组泛型
     * @return 返回一个新的实例
     */
    @SafeVarargs
    static <T> Lira<T> of(T... elements) {

        if (elements == null || elements.length == 0 || (elements.length == 1) && elements[0] == null) {
            return none();
        }

        return of(Arrays.asList(elements));

    }

    /**
     * @param iterator 迭代器
     * @param <T>      迭代器泛型
     * @return 返回一个新的实例
     */
    static <T> Lira<T> of(Iterator<? extends T> iterator) {
        if (iterator == null || !iterator.hasNext()) {
            return none();
        }
        return new RaArray<>(iterator);
    }

    /**
     * @param iterable 迭代器
     * @param <T>      迭代器泛型
     * @return 返回一个新的实例
     */
    static <T> Lira<T> of(Iterable<T> iterable) {
        if (iterable == null) {
            return none();
        }
        return of(iterable.iterator());
    }

    /**
     * @param <T> 泛型
     * @return 返回全局唯一的空 Lira
     */
    static <T> Lira<T> none() {
        @SuppressWarnings("unchecked") final None<T> none = (None<T>) None.INSTANCE;
        return none;
    }

    /**
     * @param type 可转换的类型
     * @param <R>  可转换的类型的泛型
     * @return 若集合的元素 可以转换为 type 类型 ，则返回 转换后的类型，否则返回 {@link #none()}
     */
    <R> Lira<R> cast(Class<R> type);

    /**
     * @param keyType   map 的 key 的类型
     * @param valueType map 的 value 的类型
     * @param <K>       key 的泛型
     * @param <V>       value 的泛型
     * @return 当 {@link Lino#get()} 的值为 map 时，且其 key 和 value 的类型可以转换为 keyType valueType 时，
     * 则返回泛型 {@code Lira<Map<K,V>>} 的 Lira，否则返回 {@link #none()}
     */
    <K, V> Lira<Map<K, V>> cast(Class<K> keyType, Class<V> valueType);

    /**
     * @param filter 过滤函数
     * @return 一个新的过滤后的 Lira , 实际通过对对底层集合或数组，执行 filter
     * @see io.leaderli.litool.core.util.LiBoolUtil#parse(Object)
     */
    Lira<T> filter(Function<? super T, Object> filter);

    /**
     * @return 一个新的过滤后的 Lira , 实际通过对对底层集合或数组，执行 {@link io.leaderli.litool.core.util.LiBoolUtil#parse(Object)}
     * @see #filter(Function)
     */
    Lira<T> filter();

    /**
     * @return 返回第一个元素 , 当无元素时返回 {@link Lino#none()}
     */
    Lino<T> first();

    /**
     * @param filter 过滤函数
     * @return 过滤后返回第一个元素 , 当无元素时返回 {@link Lino#none()}
     * @see #filter(Function)
     */

    Lino<T> first(Function<? super T, Object> filter);


    /**
     * @return 返回 底层元素的一份拷贝
     */
    List<Lino<T>> get();

    /**
     * @param n 最多保留的元素
     * @return 仅保留几位元素
     */
    Lira<T> limit(int n);

    /**
     * @return 返回被 lino 包裹的实际元素的一个新的 list
     */
    List<T> getRaw();

    /**
     * @param mapping 转换函数
     * @param <R>     转换后的泛型
     * @return 转换后的 Lira
     */
    <R> Lira<R> map(Function<? super T, ? extends R> mapping);


    /**
     * @param n 跳过多少个元素
     * @return 截掉前 n 位元素
     */
    Lira<T> skip(int n);

    /**
     * 实际调用 {@link #throwable_map(LiThrowableFunction, Consumer)}, 第二个参数传 null
     *
     * @param mapping 转换函数
     * @param <R>     转换后的泛型
     * @return 转换后的 Lira
     */
    <R> Lira<R> throwable_map(LiThrowableFunction<? super T, ? extends R> mapping);

    /**
     * @param mapping   转换函数
     * @param whenThrow 当转换函数抛出异常时执行的函数
     * @param <R>       转换后的泛型
     * @return 转换后的 Lira
     */
    <R> Lira<R> throwable_map(LiThrowableFunction<? super T, ? extends R> mapping, Consumer<Throwable> whenThrow);

    /**
     * @return 提前触发短路操作, 返回一个新的 Lira ， 当为 {@link #none()} 则返回 {@link #none()}
     */
    Lira<T> eager();

    /**
     * @param others 数组
     * @return 当 值 存在时返回 this，不存在时则返回新的 {@code  of(other)}
     */
    @SuppressWarnings("unchecked")
    Lira<T> or(T... others);


    /**
     * @param others 数组
     * @return 当 值 存在时返回 this，不存在时则返回新的 {@code  of(other)}
     */
    Lira<T> or(Iterator<T> others);

    /**
     * @param others 数组
     * @return 当 值 存在时返回 this，不存在时则返回新的 {@code  of(other)}
     */
    Lira<T> or(Iterable<T> others);


    /**
     * @return 返回底层元素的数量
     */
    int size();

    void forEachLino(Consumer<Lino<T>> consumer);

    void forEach(Consumer<T> consumer);

    default Stream<T> stream() {
        return getRaw().stream();
    }

    default T[] toArray(Class<T> type) {
        return getRaw().toArray(LiClassUtil.newArray(type, 0));
    }

    /**
     * key 为 null 的值 会被忽略掉，value 为 null 不会
     *
     * @param keyMapping   转换 key 的函数
     * @param valueMapping 转换 value 的函数
     * @param <K>          key 泛型
     * @param <V>          value 泛型
     * @return 返回 map
     */
    <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keyMapping, Function<? super T, ? extends V> valueMapping);


    final class RaArray<T> extends RaSome<T> {

        private final T[] arr;

        @SuppressWarnings("unchecked")
        private RaArray(Iterator<? extends T> values) {

            List<T> list = new ArrayList<>();
            values.forEachRemaining(list::add);
            this.arr = (T[]) list.toArray();
        }


        @Override
        public void subscribe(RaSubscriber<? super T> actualSubscriber) {
            actualSubscriber.onSubscribe(new ArrayRaSubscription<>(actualSubscriber, arr));
        }


    }

    final class ArrayRaSubscription<T> implements RaSubscription {

        private final T[] arr;
        private final RaSubscriber<? super T> actualSubscriber;


        private boolean canceled;

        public ArrayRaSubscription(RaSubscriber<? super T> actualSubscriber, T[] arr) {
            this.actualSubscriber = actualSubscriber;
            this.arr = arr;
        }

        @Override
        public void request(int n) {
            if (canceled) {
                return;
            }

            if (n < 0 || n > arr.length) {
                n = arr.length;
            }
            for (int i = 0; i < n; i++) {


                Lino.of(arr[i]).nest(l -> actualSubscriber.next(Lino.narrow(l)));
                // 通过 onSubscribe 将 Subscription 传递给订阅者，由订阅者来调用 cancel方法从而实现提前结束循环
                if (canceled) {
                    return;
                }

            }

            actualSubscriber.onComplete();
        }

        @Override
        public void cancel() {
            canceled = true;
        }
    }

    final class None<T> implements Lira<T> {

        public static final Object INSTANCE = new None<>();

        private None() {

        }

        @Override
        public boolean present() {
            return false;
        }

        @Override
        public String name() {
            return "Empty";
        }

        @Override
        public String toString() {
            return name() + "[]";
        }

        @Override
        public <R> Lira<R> cast(Class<R> type) {
            return none();
        }

        @Override
        public <K, V> Lira<Map<K, V>> cast(Class<K> keyType, Class<V> valueType) {
            return none();
        }

        @Override
        public Lira<T> filter(Function<? super T, Object> filter) {
            return this;
        }

        @Override
        public Lira<T> filter() {
            return this;
        }

        @Override
        public Lino<T> first() {
            return Lino.none();
        }

        @Override
        public Lino<T> first(Function<? super T, Object> filter) {
            return Lino.none();
        }

        @Override
        public List<Lino<T>> get() {
            return LiListUtil.emptyList();

        }


        @Override
        public List<T> getRaw() {
            return LiListUtil.emptyList();
        }

        @Override
        public <R> Lira<R> map(Function<? super T, ? extends R> mapping) {
            return none();
        }

        @Override
        public Lira<T> skip(int n) {
            return this;
        }

        @Override
        public Lira<T> limit(int n) {
            return this;
        }

        @Override
        public <R> Lira<R> throwable_map(LiThrowableFunction<? super T, ? extends R> mapping) {
            return none();
        }

        @Override
        public <R> Lira<R> throwable_map(LiThrowableFunction<? super T, ? extends R> mapping, Consumer<Throwable> whenThrow) {
            return none();
        }

        @Override
        public Lira<T> eager() {
            return this;
        }

        @SafeVarargs
        @Override
        public final Lira<T> or(T... others) {
            return of(others);
        }

        @Override
        public Lira<T> or(Iterator<T> others) {
            return of(others);
        }

        @Override
        public Lira<T> or(Iterable<T> others) {
            return of(others);
        }


        @Override
        public int size() {
            return 0;
        }

        @Override
        public void forEachLino(Consumer<Lino<T>> consumer) {

        }

        @Override
        public void forEach(Consumer<T> consumer) {

        }

        @Override
        public <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keyMapping, Function<? super T, ? extends V> valueMapping) {
            return new HashMap<>();
        }


        @Override
        public void subscribe(RaSubscriber<? super T> subscriber) {


        }
    }
}
