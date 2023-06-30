package io.leaderli.litool.core.meta;

import io.leaderli.litool.core.collection.IterableItr;
import io.leaderli.litool.core.exception.RuntimeExceptionTransfer;
import io.leaderli.litool.core.function.ThrowableConsumer;
import io.leaderli.litool.core.function.ThrowableFunction;
import io.leaderli.litool.core.function.ThrowableSupplier;
import io.leaderli.litool.core.meta.ra.DebugConsumer;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.LiTypeToken;
import io.leaderli.litool.core.util.BooleanUtil;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Base on the functional programming thinking, all operation
 * on the value is type-safe and null-safe, the most action will
 * invoked only when {@link  #present()}.
 *
 * @param <T> 实际值的类型
 * @author leaderli
 * @since 2022/6/16
 */
public interface Lino<T> extends LiValue, Supplier<T> {


    /**
     * all lino with value of null is the same instance of {@link  None#INSTANCE},
     * all operation on {@link  None} is stateless
     *
     * @param <T> the type of lino
     * @return the unique instance of  {@link  None#INSTANCE}
     */
    @SuppressWarnings("unchecked")
    static <T> Lino<T> none() {
        return (Lino<T>) None.INSTANCE;
    }

    /**
     * if {@code value == null} return {@link #none()}
     * or return a new {@link Some}
     *
     * @param value the value beyond lino
     * @param <T>   the type of lino
     * @return a lino
     */
    static <T> Lino<T> of(T value) {
        if (value == null) {
            return none();
        }
        return new Some<>(value);
    }

    /**
     * if {@code optional == null || optional.get() == null } return {@link #none()}
     * or return a new {@link Some}
     *
     * @param optional the value provider
     * @param <T>      the type of lino
     * @return a lino
     */
    static <T> Lino<T> optional(Optional<T> optional) {
        if (optional == null || !optional.isPresent()) {
            return none();
        }
        return new Some<>(optional.get());
    }

    /**
     * if {@code supplier == null || supplier.get() == null } return {@link #none()}
     * or return a new {@link Some}
     *
     * @param supplier the value provider
     * @param <T>      the type of lino
     * @return a lino
     */
    static <T> Lino<T> supplier(Supplier<T> supplier) {
        if (supplier == null) {
            return none();
        }
        T value = supplier.get();
        if (value == null) {
            return none();
        }
        return new Some<>(value);
    }

    /**
     * when {@code supplier == null} or {@link  ThrowableSupplier#get()} throw a error
     * return {@link #none()}, or return {@link #of(Object)} by the {@link  ThrowableSupplier#get()}
     *
     * @param supplier the supplier provide value
     * @param <T>      the type of lino
     * @return a lino
     * @see #of(Object)
     */
    static <T> Lino<T> throwable_of(ThrowableSupplier<? extends T> supplier) {
        if (supplier == null) {
            return none();
        }
        try {
            T value = supplier.get();
            if (value == null) {
                return Lino.none();
            }
            return new Some<>(value);
        } catch (Throwable e) {
            WhenThrowBehavior.whenThrow(e);
            return none();
        }
    }

    /**
     * when {@code supplier == null} or {@link  ThrowableSupplier#get()} throw a error
     * return {@link #none()}, or return {@link #of(Object)} by the {@link  ThrowableSupplier#get()}
     *
     * @param supplier the supplier provide value
     * @param <T>      the type of lino
     * @param function the function when error occur
     * @return a lino
     * @see #of(Object)
     */
    static <T> Lino<T> throwable_of(ThrowableSupplier<? extends T> supplier, Function<Throwable, T> function) {
        if (supplier == null) {
            return none();
        }
        try {
            T value = supplier.get();
            if (value == null) {
                return Lino.none();
            }
            return new Some<>(value);
        } catch (Throwable e) {
            return of(function.apply(e));
        }
    }

    /**
     * narrow the generic type of lino, {@code <? extends T> } convert {@code  <T> }.
     * it should be noted that all operation on lino is readable but writeable, the value
     * under lino cannot be updated, so the narrow operation of extend is allowed. that also
     * the reason of narrow cannot action on super
     *
     * @param value the lino
     * @param <T>   the type of lino
     * @return a narrowed  lino
     */
    @SuppressWarnings("unchecked")
    static <T> Lino<T> narrow(Lino<? extends T> value) {

        return (Lino<T>) value;

    }

    /**
     * @param filter the filter function
     * @return return  this if match the assert
     * @throws IllegalStateException if assert false
     * @see #assertTrue(Function, String)
     */
    default Lino<T> assertTrue(Function<? super T, ?> filter) {
        return assertTrue(filter, "");
    }

    /**
     * assert the result of function and parse to boolean by {@link BooleanUtil#parse(Object)} is true. otherwise
     * throw a
     * {@link  IllegalStateException}
     *
     * @param filter the filter function
     * @param msg    the error msg of assert false
     * @return return  this if match the assert
     * @throws IllegalStateException if assert false
     * @see BooleanUtil#parse(Object)
     */
    Lino<T> assertTrue(Function<? super T, ?> filter, String msg);

    /**
     * assert the result of function and parse to boolean by {@link BooleanUtil#parse(Object)} is true. otherwise
     * throw a custom runtimeException
     *
     * @param filter           the filter function
     * @param runtimeException throw it when assert false
     * @return return  this if match the assert
     * @throws IllegalStateException if assert false
     * @see BooleanUtil#parse(Object)
     */
    Lino<T> assertTrue(Function<? super T, ?> filter, RuntimeException runtimeException);

    /**
     * assert {@link  #present()}
     *
     * @return this
     */
    Lino<T> assertNotNone();

    /**
     * assert {@link  #present()}
     *
     * @param msg the error msg
     * @return this
     */
    Lino<T> assertNotNone(String msg);


    /**
     * assert {@link  #present()}
     *
     * @param supplier the error msg provider
     * @return this
     */
    Lino<T> assertNotNone(Supplier<String> supplier);


    /**
     * {@link #none()} 一定不会触发
     *
     * @param throwableConsumer 当有异常时发生时则会调用
     * @return this
     */
    Lino<T> onError(Consumer<Throwable> throwableConsumer);

    /**
     * @param <R>  the type parameter of casted
     * @param type the type of casted
     * @return return this if value instanceof type  otherwise return {@link  #none()}
     */
    <R> Lino<R> cast(Class<? extends R> type);

    /**
     * the typeToken is for generic use, the real cast class is {@link LiTypeToken#getRawType()}
     *
     * @param <R>       the type parameter of casted
     * @param typeToken the typeToken of casted
     * @return return this if value instanceof type  otherwise return {@link  #none()}
     */
    <R> Lino<R> cast(LiTypeToken<R> typeToken);


    /**
     * filter element that can be cast to map , and  the map key, value can be cast
     *
     * @param <K>       the generic parameter of casted map key type
     * @param <V>       the generic parameter of casted map value type
     * @param keyType   the type of casted map key
     * @param valueType the type of casted map value
     * @return if the value is map, will convert map by {@link  ClassUtil#filterCanCast(Map, Class, Class)}
     * if the value is not map or the converted map is empty, will also return {@link  #none()}
     */
    <K, V> Lino<Map<K, V>> cast(Class<? extends K> keyType, Class<? extends V> valueType);


    /**
     * @return return this if {@link BooleanUtil#parse(Object)} is true otherwise return {@link  #none()}
     * @see BooleanUtil
     * @see #filter(boolean)
     */
    Lino<T> filter();

    /**
     * @param remain the filter function
     * @return return this if remain is true otherwise return {@link  #none()}
     */
    Lino<T> filter(boolean remain);

    /**
     * @param filter the filter function
     * @return return this if   the result of function and parse to boolean by {@link BooleanUtil#parse(Object)} is true
     * otherwise return {@link  #none()}
     * @see #filter(boolean)
     * @see BooleanUtil#parse(Object)
     */
    Lino<T> filter(Function<? super T, ?> filter);

    /**
     * @return the underlying value
     */
    @Override
    T get();

    /**
     * @param alternate the alternate value
     * @return the underlying value if {@link  #present()} otherwise return alternate
     */

    T get(T alternate);

    /**
     * @param alternate the alternate value provider
     * @return the underlying value if {@link  #present()} otherwise return alternate
     */
    T get(Supplier<T> alternate);


    /**
     * perform action only when {@link #present()}}
     *
     * @param consumer the consumer
     * @return this
     */
    Lino<T> ifPresent(Consumer<? super T> consumer);

    /**
     * perform action only when {@link #present()}, the action may throw a exception
     *
     * @param consumer the consumer
     * @return this
     * @see RuntimeExceptionTransfer
     */
    Lino<T> ifPresentIgnoreError(ThrowableConsumer<? super T> consumer);


    /**
     * perform action only when {@link #absent()}
     *
     * @param runnable the runnable
     * @return this
     */
    Lino<T> ifAbsent(Runnable runnable);


    /**
     * <pre>
     *     of(map(mapper))
     * </pre>
     *
     * @param mapper the mapper provide lino
     * @param <R>    the type of mapper provide value
     * @return a new lino
     */
    <R> Lino<R> map(Function<? super T, ? extends R> mapper);

    /**
     * <pre>
     *     of(map(mapper).map(Supplier::get))
     * </pre>
     *
     * @param mapper the mapper provide lino
     * @param <R>    the type of mapper provide lino's value
     * @return a new lino
     */
    <R> Lino<R> unzip(Function<? super T, Supplier<? extends R>> mapper);

    /**
     * 当原值为 none时，恒返回 {@code LiTuple.of(null, null)}
     *
     * @param mapper 根据源值计算得到第二个值
     * @param <R>    第二个值的类型
     * @return 转换为{@link  LiTuple}
     */
    <R> LiTuple<T, R> zip(Function<? super T, ? extends R> mapper);

    /**
     * 当原值为 none时，恒返回 {@code LiTuple.of(null, null)}
     *
     * @param t2  第二个值
     * @param <R> 第二个值的类型
     * @return 一个由当前值和第二个值组成的元祖
     */
    <R> LiTuple<T, R> tuple(R t2);

    /**
     * @param consumer the consumer of lino
     * @return this
     */
    Lino<T> nest(Consumer<? super Lino<T>> consumer);


    /**
     * if {@link  #present()} return {@link  Either#right(Object)}
     * otherwise return {@link  Either#left(Object)}
     *
     * @param l   left value
     * @param <L> the type of left value
     * @return a  either value
     */
    <L> Lino<Either<L, T>> either(L l);

    /**
     * if {@link  #present()} return {@link  Either#right(Object)}
     * otherwise return {@link  Either#left(Object)}
     *
     * @param l   left value provider
     * @param <L> the type of left value
     * @return a  either value
     */
    <L> Lino<Either<L, T>> eitherSupplier(Supplier<? extends L> l);

    /**
     * use {@code system.out.println} as consumer
     *
     * @return this
     * @see #debug(DebugConsumer)
     */
    default Lino<T> debug() {
        return debug(System.out::println);
    }

    /**
     * @param debug the consumer perform on value
     * @return this
     */
    Lino<T> debug(DebugConsumer<? super T> debug);


    /**
     * @param other the other
     * @return return {@link  #of(Object)} if this is {@link  #none()} otherwise return this
     */
    Lino<T> or(T other);


    /**
     * @param supplier the other
     * @return return {@link  #of(Object)} )} if this is {@link  #none()} otherwise return this
     */
    Lino<T> or(Supplier<? extends T> supplier);


    /**
     * @param comparing the comparing value
     * @return if {@code  comparing.equals(value)} return this, otherwise return {@link  #none()}
     */
    Lino<T> contain(T comparing);


    /**
     * when the error occurs will return {@link  Lino#none()}
     * <p>
     * use {@link  WhenThrowBehavior#WHEN_THROW} as error consumer
     *
     * @param mapper the  mapper
     * @param <R>    the type of after mapper
     * @return a new lino of type R
     * @see #mapIgnoreError(ThrowableFunction, Consumer)
     * @see WhenThrowBehavior#WHEN_THROW
     */
    <R> Lino<R> mapIgnoreError(ThrowableFunction<? super T, ? extends R> mapper);


    /**
     * when the error occurs will return {@link  Lino#none()}
     *
     * @param mapper    the  mapper
     * @param <R>       the type of after mapper
     * @param whenThrow the consumer when {@link  ThrowableFunction#apply(Object)} throw
     * @return a new lino of type R
     */
    <R> Lino<R> mapIgnoreError(ThrowableFunction<? super T, ? extends R> mapper, Consumer<Throwable> whenThrow);

    /**
     * @param <R> the type of lira result
     * @return a liIf
     */
    <R> LiIf<T, R> toIf();

    /**
     * if the value type of supported by {@link  IterableItr#of(Object)}, will return a new lira by
     * call {@link  Lira#of(IterableItr)} otherwise return {@link  Lira#of(Object[])} with one single
     * element
     *
     * @return convert to lira according to the type of value
     */
    Lira<Object> toLira();

    /**
     * support convert to lira type is: {@link Iterable}, {@link Iterator}, {@link Enumeration} and the array.
     * otherwise will return {@link  Lira#none()}
     *
     * @param <R>  the type parameter of lira
     * @param type the type of lira
     * @return convert to lira according to the type of value, and will perform an action of {@link Lira#cast(Class)}
     */
    <R> Lira<R> toLira(Class<? extends R> type);

    /**
     * support convert to lira type is: {@link Iterable}, {@link Iterator}, {@link Enumeration} and the array.
     * otherwise will return {@link  Lira#none()}
     *
     * @param <R>  the type parameter of lira
     * @param type the type of lira
     * @return convert to lira according to the type of value, and will perform an action of {@link Lira#cast(LiTypeToken)}
     */
    <R> Lira<R> toLira(LiTypeToken<? extends R> type);


    /**
     * @param <T> 值的类型
     */
    final class Some<T> implements Lino<T> {

        private final T value;

        /**
         * @param value -
         */
        public Some(T value) {
            this.value = value;
        }

        @Override
        public boolean present() {
            return true;
        }

        @Override
        public String name() {
            return "Some";
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Some<?> some = (Some<?>) o;
            return Objects.equals(value, some.value);
        }

        @Override
        public String toString() {
            return name() + "(" + value + ")";
        }

        @Override
        public Lino<T> assertTrue(Function<? super T, ?> filter, String msg) {
            if (filter(filter).absent()) {
                throw new IllegalStateException(msg);
            }
            return this;
        }

        @Override
        public Lino<T> assertTrue(Function<? super T, ?> filter, RuntimeException runtimeException) {
            if (filter(filter).absent()) {
                throw runtimeException;
            }
            return this;
        }

        @Override
        public Lino<T> assertNotNone() {
            return this;
        }

        @Override
        public Lino<T> assertNotNone(String msg) {
            return this;
        }

        @Override
        public Lino<T> assertNotNone(Supplier<String> supplier) {
            return this;
        }

        @Override
        public Lino<T> onError(Consumer<Throwable> throwableConsumer) {
            return null;
        }

        @Override
        public <R> Lino<R> cast(Class<? extends R> type) {
            return of(ClassUtil.cast(this.value, type));
        }

        @SuppressWarnings("unchecked")
        @Override
        public <R> Lino<R> cast(LiTypeToken<R> typeToken) {
            return (Lino<R>) cast(typeToken.getRawType());
        }

        @Override
        public <K, V> Lino<Map<K, V>> cast(Class<? extends K> keyType, Class<? extends V> valueType) {

            return cast(Map.class).map(m -> ClassUtil.<K, V>filterCanCast(m, keyType, valueType)).filter();
        }

        @Override
        public Lino<T> filter() {
            return filter(BooleanUtil.parse(this.value));
        }

        @Override
        public Lino<T> filter(boolean remain) {
            return remain ? this : none();
        }

        @Override
        public Lino<T> filter(Function<? super T, ?> filter) {
            Objects.requireNonNull(filter);
            return filter(BooleanUtil.parse(filter.apply(this.value)));
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public T get(T alternate) {
            return value;
        }

        @Override
        public T get(Supplier<T> alternate) {
            return value;
        }

        @Override
        public Lino<T> ifPresent(Consumer<? super T> consumer) {
            consumer.accept(this.value);
            return this;
        }

        @Override
        public Lino<T> ifPresentIgnoreError(ThrowableConsumer<? super T> consumer) {
            RuntimeExceptionTransfer.accept(consumer, this.value);
            return this;
        }

        @Override
        public Lino<T> ifAbsent(Runnable runnable) {
            return this;
        }


        @Override
        public <R> Lino<R> map(Function<? super T, ? extends R> mapper) {
            return of(mapper.apply(this.value));
        }

        @Override
        public <R> Lino<R> unzip(Function<? super T, Supplier<? extends R>> mapper) {

            return map(mapper).map(Supplier::get);
        }

        @Override
        public <R> LiTuple<T, R> zip(Function<? super T, ? extends R> mapper) {
            return LiTuple.of(value, mapper.apply(value));
        }

        @Override
        public <R> LiTuple<T, R> tuple(R t2) {

            return LiTuple.of(value, t2);
        }

        @Override
        public Lino<T> nest(Consumer<? super Lino<T>> consumer) {
            consumer.accept(this);
            return this;
        }


        @Override
        public <L> Lino<Either<L, T>> either(L l) {
            return Lino.of(Either.right(value));
        }

        @Override
        public <L> Lino<Either<L, T>> eitherSupplier(Supplier<? extends L> l) {
            return Lino.of(Either.right(value));
        }

        @Override
        public Lino<T> debug(DebugConsumer<? super T> debug) {
            debug.accept(value);
            return this;
        }

        @Override
        public Lino<T> or(T other) {
            return this;
        }

        @Override
        public Lino<T> or(Supplier<? extends T> supplier) {
            return this;
        }


        @Override
        public Lino<T> contain(T comparing) {
            if (this.value.equals(comparing)) {
                return this;
            }
            return none();
        }


        @Override
        public <R> Lino<R> mapIgnoreError(ThrowableFunction<? super T, ? extends R> mapper) {
            return mapIgnoreError(mapper, WhenThrowBehavior.WHEN_THROW);
        }


        @Override
        public <R> Lino<R> mapIgnoreError(ThrowableFunction<? super T, ? extends R> mapper,
                                          Consumer<Throwable> whenThrow) {
            try {

                return of(mapper.apply(this.value));
            } catch (Throwable throwable) {
                if (whenThrow != null) {
                    whenThrow.accept(throwable);
                }
            }
            return none();
        }

        @Override
        public <R> LiIf<T, R> toIf() {
            return LiIf.of(this);
        }

        @Override
        public Lira<Object> toLira() {

            IterableItr<Object> iterable = IterableItr.of(this.value);
            return Lira.of(iterable);

        }


        @Override
        public <R> Lira<R> toLira(Class<? extends R> type) {
            return toLira().cast(type);
        }

        @Override
        public <R> Lira<R> toLira(LiTypeToken<? extends R> type) {
            return toLira().cast(type);
        }
    }


    /**
     * @param <T> 值的类型
     */
    final class None<T> implements Lino<T> {
        private static final None<?> INSTANCE = new None<>();

        private None() {
        }


        @Override
        public boolean present() {
            return false;
        }

        @Override
        public String name() {
            return "None";
        }

        @Override
        public String toString() {
            return name() + "()";
        }

        @Override
        public Lino<T> assertTrue(Function<? super T, ?> filter, String msg) {
            throw new IllegalStateException(msg);
        }

        @Override
        public Lino<T> assertTrue(Function<? super T, ?> filter, RuntimeException runtimeException) {
            throw runtimeException;
        }

        @Override
        public Lino<T> assertNotNone() {
            throw new IllegalStateException();
        }

        @Override
        public Lino<T> assertNotNone(String msg) {
            throw new IllegalStateException(msg);
        }

        @Override
        public Lino<T> assertNotNone(Supplier<String> supplier) {
            throw new IllegalStateException(supplier.get());
        }

        @Override
        public Lino<T> onError(Consumer<Throwable> throwableConsumer) {
            return this;
        }

        @Override
        public <R> Lino<R> cast(Class<? extends R> type) {
            return none();
        }

        @Override
        public <R> Lino<R> cast(LiTypeToken<R> typeToken) {
            return none();
        }

        @Override
        public <K, V> Lino<Map<K, V>> cast(Class<? extends K> keyType, Class<? extends V> valueType) {
            return none();
        }


        @Override
        public Lino<T> filter() {
            return this;
        }

        @Override
        public Lino<T> filter(boolean remain) {
            return this;
        }

        @Override
        public Lino<T> filter(Function<? super T, ?> filter) {
            return this;
        }

        @Override
        public T get() {
            return null;
        }

        @Override
        public T get(T alternate) {
            return alternate;
        }

        @Override
        public T get(Supplier<T> alternate) {
            return alternate.get();
        }

        @Override
        public Lino<T> ifPresent(Consumer<? super T> consumer) {
            return this;
        }

        @Override
        public Lino<T> ifPresentIgnoreError(ThrowableConsumer<? super T> consumer) {
            return this;
        }

        @Override
        public Lino<T> ifAbsent(Runnable runnable) {
            runnable.run();
            return this;
        }


        @Override
        public <R> Lino<R> map(Function<? super T, ? extends R> mapper) {
            return none();
        }

        @Override
        public <R> Lino<R> unzip(Function<? super T, Supplier<? extends R>> mapper) {
            return Lino.none();
        }

        @Override
        public <R> LiTuple<T, R> zip(Function<? super T, ? extends R> mapper) {
            return LiTuple.of(null, null);
        }

        @Override
        public <R> LiTuple<T, R> tuple(R t2) {
            return LiTuple.of(null, null);
        }

        @Override
        public Lino<T> nest(Consumer<? super Lino<T>> consumer) {
            return this;
        }


        @Override
        public <L> Lino<Either<L, T>> either(L l) {
            return Lino.of(Either.left(l));
        }

        @Override
        public <L> Lino<Either<L, T>> eitherSupplier(Supplier<? extends L> l) {
            if (l == null) {
                return Lino.of(Either.none());
            }
            return Lino.of(Either.left(l.get()));
        }

        @Override
        public Lino<T> debug(DebugConsumer<? super T> debug) {

            debug.onNull();
            return this;
        }

        @Override
        public Lino<T> or(T other) {
            return of(other);
        }

        @Override
        public Lino<T> or(Supplier<? extends T> supplier) {
            return of(supplier.get());
        }

        @Override
        public Lino<T> contain(T comparing) {
            return this;
        }


        @Override
        public <R> Lino<R> mapIgnoreError(ThrowableFunction<? super T, ? extends R> mapper) {
            return none();
        }


        @Override
        public <R> Lino<R> mapIgnoreError(ThrowableFunction<? super T, ? extends R> mapper,
                                          Consumer<Throwable> whenThrow) {
            return none();
        }

        @Override
        public <R> LiIf<T, R> toIf() {
            return LiIf.of(null);
        }

        @Override
        public Lira<Object> toLira() {
            return Lira.none();
        }

        @Override
        public <R> Lira<R> toLira(Class<? extends R> type) {
            return Lira.none();
        }

        @Override
        public <R> Lira<R> toLira(LiTypeToken<? extends R> type) {
            return Lira.none();
        }
    }
}
