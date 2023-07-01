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
 * 基于函数式编程思想，所有对值的操作都是类型安全和空安全的，大多数操作只有在值存在时才会被调用。
 * 其实现类有两个，一个是有值的 {@link Some},一个是无值的全局共享的{@link None}
 *
 * @param <T> 实际值的类型
 * @author leaderli
 * @since 2022/6/16
 */
public interface Lino<T> extends LiValue, Supplier<T> {


    /**
     * 对 Optional 类型进行转换，若 optional 为 null 或者其值为空，则返回 {@link #none()}
     * 否则，返回一个包含 optional 值的 Some 对象。
     *
     * @param optional 值提供者
     * @param <T>      值的类型
     * @return 一个 Lino 对象
     */
    static <T> Lino<T> optional(Optional<T> optional) {
        if (optional == null || !optional.isPresent()) {
            return none();
        }
        return new Some<>(optional.get());
    }

    /**
     * 该方法返回一个泛型类型为T的Lino实例，其值为null，该实例是None类的唯一实例。
     * None类的所有操作都是无状态的。
     *
     * @param <T> lino的类型
     * @return None类的唯一实例
     */
    @SuppressWarnings("unchecked")
    static <T> Lino<T> none() {
        return (Lino<T>) None.INSTANCE;
    }

    /**
     * 该方法根据提供的值提供者{@code supplier}返回一个Lino对象
     * 如果{@code supplier == null || supplier.get() == null }，则返回{@link #none()}，否则返回一个新的{@link Some}对象
     *
     * @param supplier 值提供者
     * @param <T>      值的类型
     * @return 一个Lino对象
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
     * 根据提供的 {@link ThrowableSupplier} 获取值，如果 {@code supplier == null} 或者 {@link ThrowableSupplier#get()} 抛出异常，
     * 返回 {@link #none()}，否则返回 {@link #of(Object)} 获取的值。
     *
     * @param supplier 提供值的 supplier
     * @param <T>      lino 的类型
     * @return 一个 lino
     * @see #of(Object)
     */
    static <T> Lino<T> ofIgnoreError(ThrowableSupplier<? extends T> supplier) {
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
     * 该方法用于获取一个Lino对象，当{@code supplier == null}或{@link ThrowableSupplier#get()}方法抛出异常时，返回空的Lino对象，
     * 否则返回从{@link ThrowableSupplier#get()}方法获取到的对象。
     *
     * @param supplier 提供值的接口对象
     * @param <T>      Lino对象中值的类型
     * @param function 当发生异常时的处理函数
     * @return 一个Lino对象
     * @see #of(Object)
     */
    static <T> Lino<T> ofIgnoreError(ThrowableSupplier<? extends T> supplier, Function<Throwable, T> function) {
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
     * 如果参数value为null，则返回None类的唯一实例；
     * 否则返回一个新的Some实例
     *
     * @param value lino的值
     * @param <T>   lino的类型
     * @return 一个lino
     */
    static <T> Lino<T> of(T value) {
        if (value == null) {
            return none();
        }
        return new Some<>(value);
    }

    /**
     * 该方法是用于将 Lino 类型的泛型类型窄化，即将 ? extends T 转换为 T。
     * 需要注意的是，所有的对于 Lino 对象的操作都是只读的，即不能更新 Lino 中的值，因此，extend 类型的窄化操作是被允许的，但是 super 类型的窄化操作是不被允许的。
     *
     * @param value 需要进行窄化操作的 Lino 对象
     * @param <T>   Lino 对象的泛型类型
     * @return 窄化后的 Lino 对象
     */
    @SuppressWarnings("unchecked")
    static <T> Lino<T> narrow(Lino<? extends T> value) {

        return (Lino<T>) value;

    }

    /**
     * 这是一个泛型接口Lino，该方法为其默认方法，作用是将传入的参数filter应用于泛型T，如果满足断言条件，则返回当前对象，否则抛出IllegalStateException异常。
     *
     * @param filter 过滤器函数，接收泛型T的参数，返回值类型为任意类型
     * @return 如果断言成功，则返回this
     * @throws IllegalStateException 如果断言条件不成立，抛出该异常
     * @see #assertTrue(Function, String)
     */
    default Lino<T> assertTrue(Function<? super T, ?> filter) {
        return assertTrue(filter, "");
    }


    /**
     * 该方法用于断言给定的函数过滤后的值经过{@link BooleanUtil#parse(Object)}转换后的结果为true，否则会抛出{@link IllegalStateException}异常。
     *
     * @param filter 过滤函数
     * @param msg    断言失败时的错误消息
     * @return 如果断言成功，则返回this
     * @throws IllegalStateException 如果断言失败
     * @see BooleanUtil#parse(Object)
     */
    Lino<T> assertTrue(Function<? super T, ?> filter, String msg);

    /**
     * 该方法用于断言函数返回的结果经过 {@link BooleanUtil#parse(Object)} 转换为 true。
     * 否则将抛出自定义的运行时异常。
     *
     * @param filter           过滤函数
     * @param runtimeException 断言失败时需要抛出的运行时异常
     * @return 如果断言匹配，则返回当前对象
     * @throws RuntimeException 如果断言失败，则抛出自定义的运行时异常
     * @see BooleanUtil#parse(Object)
     */
    Lino<T> assertTrue(Function<? super T, ?> filter, RuntimeException runtimeException);

    /**
     * 该方法用于断言当前Lino实例中的元素不为null，如果为null则抛出异常
     *
     * @return this
     * @throws IllegalStateException 为null时抛出
     */
    Lino<T> assertNotNone();

    /**
     * 该方法用于断言当前Lino实例中的元素不为null，如果为null则抛出异常
     *
     * @param msg 异常信息
     * @return this
     * @throws IllegalStateException 为null时抛出
     */
    Lino<T> assertNotNone(String msg);


    /**
     * 该方法用于断言当前Lino实例中的元素不为null，如果为null则抛出异常
     *
     * @param msgSupplier 异常信息
     * @return this
     * @throws IllegalStateException 为null时抛出
     */
    Lino<T> assertNotNone(Supplier<String> msgSupplier);


    /**
     * 该方法用于设置异常处理函数，当流中发生异常时，会调用该函数进行异常处理。
     *
     * @param throwableConsumer 异常处理函数，接收一个 Throwable 类型的参数，无返回值
     * @return 返回当前 Lino 流对象
     * @see #none()
     */
    Lino<T> onError(Consumer<Throwable> throwableConsumer);


    /**
     * 该方法用于将当前Lino对象强制类型转换为指定的类型，如果当前对象类型不是指定类型的实例，则返回一个空的Lino对象。
     *
     * @param <R>  泛型参数，用于指定返回值类型
     * @param type 指定的类型
     * @return 如果当前对象类型是指定类型的实例，返回当前对象，否则返回一个空的Lino对象
     */
    <R> Lino<R> cast(Class<? extends R> type);

    /**
     * typeToken是用于转换带泛型的类型，实际类型为 {@link LiTypeToken#getRawType()}，注意该方法并不会保证泛型的准确性，仅会保证 rawType的准确性
     *
     * @param typeToken 带泛型的类型
     * @param <R>       typeToken所代表的类型
     * @return 如果当前对象类型是指定类型的实例，返回当前对象，否则返回一个空的Lino对象
     */
    <R> Lino<R> cast(LiTypeToken<R> typeToken);


    /**
     * 过滤可以强制转换为Map类型的元素，并且Map的键和值可以强制转换
     *
     * @param <K>       强制转换后的Map键的类型
     * @param <V>       强制转换后的Map值的类型
     * @param keyType   强制转换后的Map键的类型
     * @param valueType 强制转换后的Map值的类型
     * @return 如果元素是Map类型，将通过 {@link ClassUtil#filterCanCast(Map, Class, Class)} 进行转换。
     * 如果元素不是Map类型或转换后的Map为空，则也将返回 {@link #none()}
     */
    <K, V> Lino<Map<K, V>> cast(Class<? extends K> keyType, Class<? extends V> valueType);

    /**
     * 该方法用于过滤数据，如果BooleanUtil.parse(Object)返回true，则返回当前对象，否则返回空对象
     *
     * @return 如果BooleanUtil.parse(Object)返回true，则返回当前对象，否则返回空对象
     * @see BooleanUtil
     */

    Lino<T> filter();


    /**
     * 优化后的代码，Lino类的过滤方法，用于过滤集合中的元素
     *
     * @param filter 过滤函数，需要传入Function类型的参数，用于指定过滤条件
     * @return 若过滤函数的返回值经过BooleanUtil.parse(Object)方法转换后为true，则返回当前Lino对象
     * 否则返回 {@link #none()}
     * @see BooleanUtil#parse(Object)
     */
    Lino<T> filter(Function<? super T, ?> filter);

    /**
     * @return 返回值
     */
    @Override
    T get();

    /**
     * @param alternate 备选值
     * @return 如果存在该值，则返回该值，否则返回备选值
     */
    T get(T alternate);

    /**
     * 该方法用于获取当前对象的值，如果值为null，则返回备选值
     *
     * @param alternate 备选值提供者，为一个Supplier类型的函数接口，用于提供备选值
     * @return 如果当前对象的值不为null，则返回当前对象的值，否则返回备选值
     */
    T get(Supplier<T> alternate);

    /**
     * 该方法只有在对象非空时才执行指定操作
     *
     * @param consumer 消费者
     * @return 返回当前对象
     */
    Lino<T> ifPresent(Consumer<? super T> consumer);

    /**
     * 当对象存在时，执行操作（可能会抛出异常）
     *
     * @param consumer 操作的消费者
     * @return 返回当前对象
     * @see RuntimeExceptionTransfer
     */
    Lino<T> ifThrowablePresent(ThrowableConsumer<? super T> consumer);


    /**
     * 当对象不存在时，执行操作
     *
     * @param runnable 要执行的操作
     * @return 返回当前对象
     */
    Lino<T> ifAbsent(Runnable runnable);


    /**
     * <pre>
     *     of(mapper.apply(value))
     * </pre>
     * 该方法用于将一个lino中的元素通过给定的映射函数进行转换，并返回一个新的lino
     *
     * @param mapper 用于对元素进行转换的映射函数
     * @param <R>    转换后元素的泛型类型
     * @return 返回一个新的lino，其中包含转换后的元素
     */
    <R> Lino<R> map(Function<? super T, ? extends R> mapper);

    /**
     * <pre>
     *     of(map(mapper).map(Supplier::get))
     * </pre>
     *
     * @param mapper 一个将 T 映射为 Supplier 的函数
     * @param <R>    mapper 返回值的类型
     * @return 一个新的 Lino 对象
     */
    <R> Lino<R> unzip(Function<? super T, Supplier<? extends R>> mapper);

    /**
     * 当原值为 none时，恒返回 {@code LiTuple.of(null, null)}
     *
     * @param mapper 用于计算第二个值的函数
     * @param <R>    第二个值的类型
     * @return 返回一个包含原值和计算得到的第二个值的 LiTuple 对象
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
     * 将当前 Lino 对象传递给指定的 Consumer 进行处理
     *
     * @param consumer 用于处理 Lino 对象的 Consumer
     * @return this
     */
    Lino<T> nest(Consumer<? super Lino<T>> consumer);


    /**
     * 给定一个左值l，如果present()返回Either的right value，则返回Either的right value，否则返回Either的left value
     *
     * @param l   左值
     * @param <L> 左值的类型
     * @return 一个Either值，包含左值或右值
     */
    <L> Lino<Either<L, T>> either(L l);


    /**
     * 该方法用于生成 Either<L, T> 类型的对象，当 present() 方法返回 Either.right(Object) 时，
     * 返回包含 right(Object) 的 Either<L, T> 对象，否则返回包含 left(Object) 的 Either<L, T> 对象。
     *
     * @param l   左值提供器，用于生成 Either.left(Object) 中的值
     * @param <L> 左值的数据类型
     * @return 包含 left(Object) 或 right(Object) 的 Either<L, T> 对象
     */
    <L> Lino<Either<L, T>> eitherSupplier(Supplier<? extends L> l);

    /**
     * 该方法使用 System.out.println 作为消费者，用于调试。
     *
     * @return 返回当前对象
     * @see #debug(DebugConsumer)
     */
    default Lino<T> debug() {
        return debug(System.out::println);
    }

    /**
     * 使用传入的消费者对当前对象进行调试。
     *
     * @param debug 对当前对象进行调试的消费者
     * @return 返回当前对象
     */
    Lino<T> debug(DebugConsumer<? super T> debug);

    /**
     * 如果对象不为null，返回包含该对象的Lino实例，否则返回包含other对象的Lino实例
     *
     * @param other 包含的对象
     * @return 如果对象不为null，返回包含该对象的Lino实例，否则返回包含other对象的Lino实例
     */
    Lino<T> or(T other);


    /**
     * 如果对象不为null，返回包含该对象的Lino实例，否则返回包含supplier提供的对象的Lino实例
     *
     * @param supplier 提供对象的函数式接口
     * @return 如果对象不为null，返回包含该对象的Lino实例，否则返回包含supplier提供的对象的Lino实例
     */
    Lino<T> or(Supplier<? extends T> supplier);


    /**
     * @param comparing 比较对象
     * @return 如果 {@code  comparing.equals(value)} 返回 this, 否则返回{@link  #none()}
     */
    Lino<T> contain(T comparing);


    /**
     * 该方法将执行一个函数式接口，如果执行过程中出现异常，则返回一个{@link Lino#none()}的Lino对象。
     * <p>
     * 在执行过程中，如果出现异常，默认使用 {@link WhenThrowBehavior#WHEN_THROW} 作为异常处理。
     *
     * @param mapper 函数式接口，用于执行映射操作
     * @param <R>    映射操作后的类型参数
     * @return 返回一个新的Lino对象，类型为R
     * @see #mapIgnoreError(ThrowableFunction, Consumer)
     * @see WhenThrowBehavior#WHEN_THROW
     */
    <R> Lino<R> mapIgnoreError(ThrowableFunction<? super T, ? extends R> mapper);


    /**
     * 该方法将会对lino对象进行映射，并在发生错误时返回一个空的Lino对象
     *
     * @param mapper    映射函数
     * @param <R>       映射后的类型
     * @param whenThrow 当映射函数抛出异常时，对异常进行处理的回调函数
     * @return 一个新的lino对象，类型为R
     */
    <R> Lino<R> mapIgnoreError(ThrowableFunction<? super T, ? extends R> mapper, Consumer<Throwable> whenThrow);

    /**
     * 将当前对象转换为 LiIf 对象
     *
     * @param <R> LiIf 的结果类型
     * @return 返回一个 LiIf 对象
     */
    <R> LiIf<T, R> toIf();


    /**
     * 将当前对象转换为 Lira 对象，支持的类型包括 {@link Iterable}、{@link Iterator}、{@link Enumeration} 以及数组。
     * 如果不支持当前对象的类型，则返回一个空的 Lira 对象。
     *
     * @param <R>  Lira 的类型参数
     * @param type Lira 的类型
     * @return 返回一个转换后的 Lira 对象，并执行 {@link Lira#cast(Class)} 方法
     * @see IterableItr#of(Object)
     */
    <R> Lira<R> toLira(Class<? extends R> type);


    /**
     * 将当前对象转换为 Lira 对象，支持的类型包括 {@link Iterable}、{@link Iterator}、{@link Enumeration} 以及数组。
     * 如果不支持当前对象的类型，则返回一个空的 Lira 对象。
     *
     * @param <R>  Lira 的类型参数
     * @param type Lira 的类型
     * @return 返回一个转换后的 Lira 对象，并执行 {@link Lira#cast(LiTypeToken)} 方法
     * @see IterableItr#of(Object)
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
            return BooleanUtil.parse(this.value) ? this : Lino.none();
        }


        @Override
        public Lino<T> filter(Function<? super T, ?> filter) {
            Objects.requireNonNull(filter);
            return BooleanUtil.parse(filter.apply(this.value)) ? this : Lino.none();
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
        public Lino<T> ifThrowablePresent(ThrowableConsumer<? super T> consumer) {
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
        public <R> Lira<R> toLira(Class<? extends R> type) {
            IterableItr<Object> iterable = IterableItr.of(this.value);
            return Lira.of(iterable).cast(type);
        }

        @Override
        public <R> Lira<R> toLira(LiTypeToken<? extends R> type) {
            IterableItr<Object> iterable = IterableItr.of(this.value);
            return Lira.of(iterable).cast(type);
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
        public Lino<T> ifThrowablePresent(ThrowableConsumer<? super T> consumer) {
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
        public <R> Lira<R> toLira(Class<? extends R> type) {
            return Lira.none();
        }

        @Override
        public <R> Lira<R> toLira(LiTypeToken<? extends R> type) {
            return Lira.none();
        }
    }
}
