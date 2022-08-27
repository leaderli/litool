package io.leaderli.litool.core.meta;

import io.leaderli.litool.core.collection.IterableItr;
import io.leaderli.litool.core.collection.IteratorProxy;
import io.leaderli.litool.core.function.ThrowableConsumer;
import io.leaderli.litool.core.function.ThrowableFunction;
import io.leaderli.litool.core.lang.EqualComparator;
import io.leaderli.litool.core.meta.ra.ArraySome;
import io.leaderli.litool.core.meta.ra.Publisher;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.util.BooleanUtil;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author leaderli
 * @since 2022/6/19
 */
public interface Lira<T> extends LiValue, Publisher<T>, Iterable<T> {


    Lira<?> NONE_INSTANCE = new ArraySome<>(Collections.emptyIterator());

    /**
     * Returns the narrow type lira, convert {@code <? extends T>} to {@code  <T>}
     *
     * @param lira the lira with wild type
     * @param <T>  the type of the lira
     * @return the narrow type lira
     */
    @SuppressWarnings("unchecked")
    static <T> Lira<T> narrow(Lira<? extends T> lira) {

        return (Lira<T>) lira;

    }

    /**
     * Returns the new lira consisting the elements
     *
     * @param elements an array
     * @param <T>      the type of array
     * @return the new lira
     */
    @SafeVarargs
    static <T> Lira<T> of(T... elements) {

        return of(IterableItr.ofs(elements));

    }

    /**
     * Returns the uniq none lira that not consisting any element
     *
     * @param <T> the type of lira
     * @return the uniq  none lira
     * @see #NONE_INSTANCE
     */
    @SuppressWarnings("unchecked")
    static <T> Lira<T> none() {
        return (Lira<T>) NONE_INSTANCE;

    }

    /**
     * Returns an lira that consisting elements provided by given iterator
     *
     * @param iterator the iterator that provide  elements
     * @param <T>      the type of elements returned by this iterator
     * @return the new lira
     */
    static <T> Lira<T> of(Iterator<? extends T> iterator) {

        return of(IterableItr.of(iterator));

    }

    /**
     * Returns an lira that consisting elements provided by given iterable
     *
     * @param iterable the iterator that provide  elements
     * @param <T>      the type of elements returned by this iterable
     * @return the new lira
     */
    static <T> Lira<T> of(Iterable<? extends T> iterable) {
        return of(IterableItr.of(iterable));
    }

    /**
     * Returns an lira that consisting elements provided by given iterableIter
     *
     * @param iterableItr the iterableIter that provide  elements
     * @param <T>         the type of elements returned by this iterableIter
     * @return the new lira
     */
    static <T> Lira<T> of(IterableItr<? extends T> iterableItr) {

        if (iterableItr.hasNext()) {

            return new ArraySome<>(iterableItr);
        }
        return none();
    }


    /**
     * filter element that can be cast
     *
     * @param type the class or interface represented by elements
     * @param <R>  the generic parameter of {@code type}
     * @return this
     */
    <R> Lira<R> cast(Class<? extends R> type);


    /**
     * filter element that can be cast to map , and  the map key, value can be cast
     *
     * @param <K>       the generic parameter of casted map key type
     * @param <V>       the generic parameter of casted map value type
     * @param keyType   the type of casted map key
     * @param valueType the type of casted map value
     * @return this
     */
    <K, V> Lira<Map<K, V>> cast(Class<? extends K> keyType, Class<? extends V> valueType);

    /**
     * Returns if this lira contains the specified element
     *
     * @param t element whose presence in this lira is to be tested
     * @return if this lira contains the specified element
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
     * return an {@code List} consisting of the elements
     *
     * @return the new list
     */
    List<T> get();

    /**
     * @param index 角标
     * @return 返回指定角标的位置的元素，当元素不存在时返回 {@link Lino#none()}
     */
    Lino<T> get(int index);

    /**
     * @return 返回迭代器
     */
    @Override
    Iterator<T> iterator();

    @Override
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
     * @see IteratorProxy
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
     * @param mapper 返回 Lino 的转换函数
     * @param <R>    转换后的泛型
     * @return 返回转换后的 Lira ,会自动将 mapping 执行后的  Lino 展开
     */
    default <R> Lira<R> unzip(Function<? super T, Supplier<? extends R>> mapper) {
        return map(mapper).map(Supplier::get);
    }

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
     * @param mapper 返回 Lino 的转换函数
     * @param <R>    转换后的泛型
     * @return 返回转换后的 Lira ,会自动将 mapping 执行后的  Lino 展开
     * @see #throwable_map(ThrowableFunction)
     */
    default <R> Lira<R> throwable_unzip(ThrowableFunction<? super T, Supplier<? extends R>> mapper) {
        return throwable_map(mapper).map(Supplier::get);
    }

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
     * Performs a reduction on the element of this lira, using the associative accumulation functions,
     * and returns the reduced value. this is equivalent to:
     * <pre> {@code
     *     T result =  null
     *     for (T element : this stream){
     *          if(result == null){
     *              result = element;
     *          }else{
     *             result = accumulator.apply(result, element)
     *             if(result == null){
     *                 return null;
     *             }
     *          }
     *     }
     *     return result;
     * }</pre>
     *
     * <p>This is a terminal operation
     *
     * @param accumulator an associative non-interfering stateless function for combining two values
     * @return the result of reduction
     */
    Lino<T> reduce(BinaryOperator<T> accumulator);

    /**
     * Performs a reduction on the element of this lira, using the associative accumulation functions,
     * and returns the reduced value. this is equivalent to:
     * <pre> {@code
     *     T result =  identify
     *     for (T element : this stream){
     *             result = accumulator.apply(result, element)
     *             if(result == null){
     *                 return null;
     *             }
     *     }
     *     return result;
     * }</pre>
     *
     * <p>This is a terminal operation
     *
     * @param identity    the identity value for the accumulating function
     * @param accumulator an associative non-interfering stateless function for combining two values
     * @return the result of reduction
     */
    Lino<T> reduce(T identity, BinaryOperator<T> accumulator);


    /**
     * @return the number of elements in this list
     */
    int size();

    /**
     * Performs an {@code System.out.println(element)} for each element of this lira
     *
     * @return this
     * @see #debug(Consumer)
     */
    Lira<T> debug();

    /**
     * Performs an action  for each element of this lira
     *
     * @param action a  action to perform on the elements
     * @return this
     */
    Lira<T> debug(Consumer<T> action);

    /**
     * return this lira that consisting of the distinct elements (according to {@link Object#equals(Object)})
     *
     * @return this
     */
    Lira<T> distinct();

    /**
     * Return this that consisting of the distinct elements according to {@code EqualComparator}
     *
     * @param comparator a {@code EqualComparator}  to be used to compare lira elements is equals
     * @return this
     * TODO 通过 onComplete 实现中间状态
     */
    Lira<T> distinct(EqualComparator<T> comparator);

    /**
     * Returns a lira consisting of the elements of this lira ,sorted  according to natural order
     *
     * @return the new lira
     * <p>
     * TODO 通过 onComplete 实现中间状态
     */
    Lira<T> sorted();

    /**
     * Returns a lira consisting of the elements of this lira ,sorted  according to  the provided {@code Comparator}
     *
     * @param comparator the provided {@link Comparator}
     * @return the new lira
     */
    Lira<T> sorted(Comparator<? super T> comparator);


    /**
     * <p>This is a terminal operation
     * <p>
     * when exception occur , it will perform Throwable on {@link LiConstant#WHEN_THROW} action
     *
     * @param action a  catchable action to perform on the elements
     */
    void forThrowableEach(ThrowableConsumer<? super T> action);

    /**
     * <p>This is a terminal operation
     *
     * @param action    a  catchable action to perform on the elements
     * @param whenThrow use to perform on {@code action} throw an Throwable
     */
    void forThrowableEach(ThrowableConsumer<? super T> action, Consumer<Throwable> whenThrow);

    /**
     * @return the new stream
     * @see Stream
     */
    default Stream<T> stream() {
        return get().stream();
    }

    /**
     * return an {@code List} consisting of the elements
     *
     * @return the new list
     * @deprecated use {@link #get()}
     */
    @Deprecated
    default List<T> getRaw(){
        return get();
    }


    /**
     * Returns an array containing the elements of this stream.
     *
     * <p>This is a terminal operation
     *
     * @return an array containing the elements of this  lira
     */
    default Object[] toArray() {
        return get().toArray();
    }

    /**
     * Returns an typed array containing the typed elements  in this lira
     *
     * <p>This is a terminal operation
     *
     * @param type the array type
     * @return an array containing the elements in this lira
     */
    default T[] toArray(Class<? extends T> type) {
        return cast(type).get().toArray((T[]) ClassUtil.newWrapperArray(type, 0));
    }

    /**
     * Returns a map  consisting of the results of applying the given function to the elements of this stream
     * using {@code keyMapper.apply(element), valueMapper.apply(element)} as key, value
     *
     * <p> the {@code key == null } entry will be  removed
     *
     * <p>This is a terminal operation
     *
     * @param keyMapper   a stateless function apply to each element
     * @param valueMapper a stateless function apply to each element
     * @param <K>         the type of  {@code Map} key
     * @param <V>         the type of  {@code Map} value
     * @return the new map
     */

    <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper);

    /**
     * Returns a map  consisting of the results of applying the given function to the elements of this stream
     * using {@code LiTuple2.-1, LiTuple2.-2} as key, value
     *
     * <p> the {@code key == null } entry will be  removed
     *
     * <p>This is a terminal operation
     *
     * @param mapper a stateless function apply to each element
     * @param <K>    the type of  {@code Map} key
     * @param <V>    the type of  {@code Map} value
     * @return the new map
     */
    <K, V> Map<K, V> toMap(Function<? super T, LiTuple2<? extends K, ? extends V>> mapper);


}
