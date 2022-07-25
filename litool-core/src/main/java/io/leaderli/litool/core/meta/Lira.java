package io.leaderli.litool.core.meta;

import io.leaderli.litool.core.collection.ArrayIter;
import io.leaderli.litool.core.collection.IterableIter;
import io.leaderli.litool.core.collection.LiIterator;
import io.leaderli.litool.core.function.ThrowableConsumer;
import io.leaderli.litool.core.function.ThrowableFunction;
import io.leaderli.litool.core.meta.ra.ArrayRa;
import io.leaderli.litool.core.meta.ra.PublisherRa;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.util.BooleanUtil;

import java.io.PrintStream;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author leaderli
 * @since 2022/6/19
 */
public interface Lira<T> extends LiValue, PublisherRa<T>, Iterable<T> {


    Lira<?> NONE = new ArrayRa<>(Collections.emptyIterator());

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

        return of((Iterator<? extends T>) ArrayIter.of(elements));

    }

    /**
     * @param <T> 泛型
     * @return 返回全局唯一的空 Lira
     */
    @SuppressWarnings("unchecked")
    static <T> Lira<T> none() {
        return (Lira<T>) NONE;

    }

    /**
     * @param iterator 迭代器
     * @param <T>      迭代器泛型
     * @return 返回一个新的实例
     */
    static <T> Lira<T> of(Iterator<? extends T> iterator) {

        Iterator<? extends T> iter = LiIterator.of(iterator);
        if (iter.hasNext()) {

            return new ArrayRa<>(iter);
        }
        return none();
    }

    /**
     * @param iterable 迭代器
     * @param <T>      迭代器泛型
     * @return 返回一个新的实例
     */
    static <T> Lira<T> of(Iterable<? extends T> iterable) {
        return of(LiIterator.of(iterable));
    }

    /**
     * @param iterableIter 迭代器
     * @param <T>          迭代器泛型
     * @return 返回一个新的实例
     */
    static <T> Lira<T> of(IterableIter<? extends T> iterableIter) {

        return of((Iterator<? extends T>) iterableIter);
    }

    /**
     * @param type 可转换的类型
     * @param <R>  可转换的类型的泛型
     * @return 若集合的元素 可以转换为 type 类型 ，则返回 转换后的类型，否则返回 {@link #none()}
     */
    <R> Lira<R> cast(Class<? extends R> type);

    /**
     * @param <K>       key 的泛型
     * @param <V>       value 的泛型
     * @param keyType   map 的 key 的类型
     * @param valueType map 的 value 的类型
     * @return 当 {@link Lino#get()} 的值为 map 时，且其 key 和 value 的类型可以转换为 keyType valueType 时，
     * 则返回泛型 {@code Lira<Map<K,V>>} 的 Lira，否则返回 {@link #none()}
     */
    <K, V> Lira<Map<K, V>> cast(Class<? extends K> keyType, Class<? extends V> valueType);

    /**
     * @param t 元素
     * @return 是否包含 t
     */
    boolean contains(T t);

    /**
     * @param filter 过滤函数
     * @return 一个新的过滤后的 Lira , 实际通过对对底层集合或数组，执行 filter
     * @see BooleanUtil#parse(Object)
     */
    Lira<T> filter(Function<? super T, ?> filter);

    /**
     * @return 一个新的过滤后的 Lira , 实际通过对对底层集合或数组，执行 {@link BooleanUtil#parse(Object)}
     * @see #filter(Function)
     */
    Lira<T> filter();

    /**
     * @return 返回第一个元素 , 当无元素时返回 {@link Lino#none()}
     */
    Lino<T> first();

    /**
     * @return 返回最后一个元素 , 当无元素时返回 {@link Lino#none()}
     */
    Lino<T> last();

    /**
     * @param filter 过滤函数
     * @return 过滤后返回第一个元素 , 当无元素时返回 {@link Lino#none()}
     * @see #filter(Function)
     */

    Lino<T> first(Function<? super T, ?> filter);


    /**
     * @return 返回 底层元素的一份拷贝
     */
    List<Lino<T>> get();

    /**
     * @param index 角标
     * @return 返回指定角标的位置的元素，当元素不存在时返回 {@link Lino#none()}
     */
    Lino<T> get(int index);

    /**
     * @return 返回迭代器
     */
    Iterator<T> iterator();

    void forEach(Consumer<? super T> consumer);

    /**
     * @param n 最多保留的元素
     * @return 仅保留几位元素
     */
    Lira<T> limit(int n);

    /**
     * 使用 LiIterator 作为迭代器
     *
     * @param <R> 迭代器的值类型
     * @return 展开后的 lira
     * @see LiIterator
     * @see #flatMap(Function)
     */
    <R> Lira<R> flatMap();

    /**
     * @param mapper 转换为一个迭代器
     * @param <R>    迭代器的值类型
     * @return 展开后的 lira
     */
    <R> Lira<R> flatMap(Function<? super T, Iterator<? extends R>> mapper);

    /**
     * @param mapper 转换函数
     * @param <R>    转换后的泛型
     * @return 转换后的 Lira
     */
    <R> Lira<R> map(Function<? super T, ? extends R> mapper);

    /**
     * @param n 跳过多少个元素
     * @return 截掉前 n 位元素
     */
    Lira<T> skip(int n);

    /**
     * 实际调用 {@link #throwable_map(ThrowableFunction, Consumer)},  第二个参数传  {@link LiConstant#WHEN_THROW}
     *
     * @param mapper 转换函数
     * @param <R>    转换后的泛型
     * @return 转换后的 Lira
     * @see LiConstant#WHEN_THROW
     */
    <R> Lira<R> throwable_map(ThrowableFunction<? super T, ? extends R> mapper);

    /**
     * @param mapper    转换函数
     * @param whenThrow 当转换函数抛出异常时执行的函数
     * @param <R>       转换后的泛型
     * @return 转换后的 Lira
     */
    <R> Lira<R> throwable_map(ThrowableFunction<? super T, ? extends R> mapper, Consumer<Throwable> whenThrow);

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
    Lira<T> or(Iterator<? extends T> others);

    /**
     * @param others 数组
     * @return 当 值 存在时返回 this，不存在时则返回新的 {@code  of(other)}
     */
    Lira<T> or(Iterable<? extends T> others);

    /**
     * @return 返回底层元素的数量
     */
    int size();

    Lira<T> print();
    Lira<T> print(Consumer<T> out);
    Lira<T> distinct();

    Lira<T> sort();

    Lira<T> sort(Comparator<? super T> comparator);

    void forEachLino(Consumer<Lino<? super T>> consumer);

    void forThrowableEach(ThrowableConsumer<? super T> consumer);

    void forThrowableEach(ThrowableConsumer<? super T> consumer, Consumer<Throwable> whenThrow);

    default Stream<T> stream() {
        return getRaw().stream();
    }

    /**
     * @return 返回被 lino 包裹的实际元素的一个新的 list
     */
    List<T> getRaw();

    default T[] toArray(Class<? extends T> type) {
        return getRaw().toArray((T[]) ClassUtil.newArray(type, 0));
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


}
