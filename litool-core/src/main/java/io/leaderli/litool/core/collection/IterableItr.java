package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.meta.LiValue;

import java.util.*;
import java.util.stream.Stream;

/**
 * IterableItr 是一个综合了 {@link Iterable}、{@link Iterator} 以及 {@link Enumeration} 接口的综合型接口。
 *
 * @param <T> 迭代器中元素的类型
 */
public interface IterableItr<T> extends Iterable<T>, Iterator<T>, Enumeration<T>, LiValue, ToList<T, ArrayList<T>>, ToArray<T> {


    /**
     * make obj behave like , only support :
     * 将一个对象转换为 IterableItr。支持以下对象的转换，按照列出的顺序依次判断
     * <ul>
     *     <li>{@code null}</li>
     *     <li> </li>
     *     <li>{@link  Iterator}</li>
     *     <li>{@link  Iterable}</li>
     *     <li>{@link  Enumeration}</li>
     *     <li>{@link  Stream}</li>
     *     <li>{@link  Map}</li>
     *     <li>{@code Array}</li>
     * </ul>
     * <p>
     * 如果无法转换为 IterableItr，则返回 {@link NoneItr} 。
     * <p>
     * 大多数对象将会被转换为 {@link ArrayItr} 对象
     *
     * @param obj a obj
     * @param <T> IterableItr 迭续器中元素的类型
     * @return a
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    static <T> IterableItr<T> of(Object obj) {
        if (obj == null) {
            return NoneItr.of();
        }


        if (obj instanceof NoneItr) {
            return NoneItr.of();
        }
        if (obj instanceof ArrayItr) {
            return new ArrayItr<>((T[]) ((ArrayItr) obj).arr);
        }
        if (obj instanceof Iterator) {
            return of((Iterator<T>) obj);

        }
        if (obj instanceof Iterable) {

            return of((Iterable<T>) obj);

        }
        if (obj instanceof Enumeration) {
            return of((Enumeration) obj);
        }
        if (obj instanceof Stream) {
            return of((Stream) obj);
        }

        if (obj instanceof Map) {

            Iterable entries = ((Map<?, ?>) obj).entrySet();
            return ofs((T[]) ArrayUtils.toArray(Object.class, entries));
        }

        if (obj.getClass().isArray()) {

            return ofs(ArrayUtils.toArray(obj));
        }

        return NoneItr.of();

    }

    /**
     * @param noneItr -
     * @param <T>     泛型
     * @return noneItr
     */
    static <T> NoneItr<T> of(NoneItr<T> noneItr) {
        return noneItr;
    }

    /**
     * 将 {@link ArrayItr} 对象转换为  对象。
     *
     * @param iterator 待转换的 {@link ArrayItr} 对象
     * @param <T>      迭代器中元素的类型
     * @return 转换后的  对象
     */
    static <T> IterableItr<T> of(ArrayItr<T> iterator) {
        return new ArrayItr<>(iterator.arr);
    }


    static <T> IterableItr<T> of(IterableItr<T> itr) {
        return (IterableItr<T>) itr.iterator();
    }

    /**
     * 将 {@link Iterator} 对象转换为  对象。
     * * @param iterator 待转换的 {@link Iterator} 对象
     *
     * @param iterator -
     * @param <T>      迭代器中元素的类型
     * @return 转换后的  对象
     */
    @SuppressWarnings("unchecked")
    static <T> IterableItr<T> of(Iterator<T> iterator) {
        return ofs(ArrayUtils.toArray((Class<T>) Object.class, iterator));
    }

    /**
     * 将 {@link Iterable} 对象转换为  对象。
     *
     * @param iterable 待转换的 {@link Iterable} 对象
     * @param <T>      迭代器中元素的类型
     * @return 转换后的  对象
     */
    @SuppressWarnings("unchecked")
    static <T> IterableItr<T> of(Iterable<T> iterable) {
        return ofs(ArrayUtils.toArray((Class<T>) Object.class, iterable));
    }

    /**
     * 将 {@link Enumeration} 对象转换为  对象。
     *
     * @param enumeration 待转换的 {@link Enumeration} 对象
     * @param <T>         迭代器中元素的类型
     * @return 转换后的  对象
     */
    @SuppressWarnings("unchecked")
    static <T> IterableItr<T> of(Enumeration<T> enumeration) {
        return ofs(ArrayUtils.toArray((Class<T>) Object.class, enumeration));
    }

    /**
     * 将 {@link Stream} 对象转换为  对象。
     *
     * @param stream 待转换的 {@link Stream} 对象
     * @param <T>    迭代器中元素的类型
     * @return 转换后的  对象
     */
    @SuppressWarnings("unchecked")
    static <T> IterableItr<T> of(Stream<T> stream) {
        return ofs(ArrayUtils.toArray((Class<T>) Object.class, stream));
    }

    /**
     * 以指定元素构造一个 {@link ArrayItr} 对象。
     * 如果元素为 null 或长度为 0，则返回 {@link NoneItr#of()}。
     *
     * @param elements 待构造的元素
     * @param <T>      元素的类型
     * @return 一个包含指定元素的 {@link ArrayItr} 对象
     */
    @SafeVarargs
    static <T> IterableItr<T> ofs(T... elements) {
        if (elements == null || elements.length == 0) {
            return NoneItr.of();
        }
        return new ArrayItr<>(elements);
    }

    /**
     * 判断迭代器中是否还有下一个元素。
     *
     * @return 如果还有下一个元素，则返回 true；否则返回 false。
     */
    default boolean hasMoreElements() {
        return hasNext();
    }

    default boolean present() {
        return hasNext();
    }

    /**
     * Returns the next element of this enumeration if this enumeration
     *
     * @return the next element
     * @throws NoSuchElementException if not {@link  #hasNext()}
     * @see #next()
     */
    default T nextElement() {
        return next();
    }

    /**
     * @return 一个 {@link Iterable} 对象
     */
    default Iterable<T> iterable() {
        return IterableItr.of(iterator());
    }

    /**
     * @return 一个 {@link Enumeration} 对象
     */
    default Enumeration<T> enumeration() {
        return IterableItr.of(iterator());
    }


    /**
     * @return 返回一个包含所有元素的新的数组
     */
    Object[] toArray();


}
