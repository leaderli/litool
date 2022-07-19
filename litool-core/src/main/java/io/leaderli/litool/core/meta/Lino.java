package io.leaderli.litool.core.meta;

import io.leaderli.litool.core.collection.EnumerationIter;
import io.leaderli.litool.core.exception.RuntimeExceptionTransfer;
import io.leaderli.litool.core.function.ThrowableConsumer;
import io.leaderli.litool.core.function.ThrowableFunction;
import io.leaderli.litool.core.meta.condition.LiIf;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.LiPrimitive;
import io.leaderli.litool.core.util.BooleanUtil;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/6/16
 * <p>
 * 基于函数式编程思维，所有对其包含的value的值的操作，都是类型安全的。
 * <p>
 * 当且仅当value的值 {@link #present()} 时，才会实际对其进行方法调用
 */
public interface Lino<T> extends LiValue {


    /**
     * @param supplier 获取值的提供者函数
     * @param <T>      泛型
     * @return 返回一个实例，
     * 当 {@code supplier == null} 时返回 {@link #none()}
     * 否则返回 {@link #of(Object)}
     * @see #of(Object)
     */
    static <T> Lino<T> of(Supplier<T> supplier) {
        if (supplier == null) {
            return none();
        }
        return of(supplier.get());
    }

    /**
     * @param <T> 泛型
     * @return 返回全局唯一的空 Lino
     */
    @SuppressWarnings("unchecked")
    static <T> Lino<T> none() {
        return (Lino<T>) None.INSTANCE;

    }

    /**
     * @param value 值
     * @param <T>   泛型
     * @return 返回一个实例，
     * 当 {@code value == null} 时返回 {@link #none()}
     * 否则返回 {@link Some}
     */
    static <T> Lino<T> of(T value) {
        if (value == null) {
            return none();
        }
        return new Some<>(value);
    }

    /**
     * 需要注意的是，所有窄化 extend 的操作，是只需要读取操作，不允许写操作的，因为 Lino 不涉及更新值，所以此处是安全的，也因此我们是无法窄化 super
     *
     * @param value 值
     * @param <T>   泛型
     * @return 窄化一个宽泛的泛型， {@code <? extends T> } 转换为  {@code  <T> }
     */
    @SuppressWarnings("unchecked")
    static <T> Lino<T> narrow(Lino<? extends T> value) {

        return (Lino<T>) value;

    }

    /**
     * 当为 none 时 直接抛出异常
     *
     * @param msg 断言信息
     * @return this
     */
    Lino<T> assertNotNone(String msg);


    /**
     * @param <R>  可转换的类型的泛型
     * @param type 可转换的类型
     * @return 若 value 可以转换为 type 类型 ，则返回 转换后的类型，否则返回 {@link #none()}
     */
    <R> Lino<R> cast(Class<? extends R> type);

    /**
     * @param <K>       key 的泛型
     * @param <V>       value 的泛型
     * @param keyType   map 的 key 的类型
     * @param valueType map 的 value 的类型
     * @return 当 {@link Lino#get()} 的值为 map 时，且其 key 和 value 的类型可以转换为 keyType valueType 时，
     * 则返回泛型 {@code Lino<Map<K,V>>} 的 Lino，否则返回 {@link #none()}
     */
    <K, V> Lino<Map<K, V>> cast(Class<? extends K> keyType, Class<? extends V> valueType);


    /**
     * @return 通过 {@link BooleanUtil#parse(Object)} 对 实际值 进行过滤
     * @see BooleanUtil
     * @see #filter(boolean)
     */
    Lino<T> filter();

    /**
     * @param remain 是否保留
     * @return 当 {@code remain == true} 则返回 this ， 否则 返回 {@link #none()}
     */
    Lino<T> filter(boolean remain);

    /**
     * @param function 转换函数
     * @return lino 实际值经过转换函数转换后，通过 {@link BooleanUtil#parse(Object)} 后，调用 {@link #filter(boolean)}
     * @see #filter(boolean)
     * @see BooleanUtil#parse(Object)
     */
    Lino<T> filter(Function<? super T, ?> function);

    T get();

    T get(T other);


    /**
     * @param consumer 当 {@link #present()}  时消费
     * @return this
     */
    Lino<T> ifPresent(Consumer<? super T> consumer);


    /**
     * @param consumer 当 {@link #present()}  时消费，可能会抛出 {@link RuntimeException}
     * @return this
     * @see RuntimeExceptionTransfer
     */
    Lino<T> ifThrowablePresent(ThrowableConsumer<? super T> consumer);


    /**
     * @param runnable 当 {@link #absent()}   时执行
     * @return this
     */
    Lino<T> ifAbsent(Runnable runnable);

    /**
     * @return 当实际值为数组或者集合时保留，否则返回 {@link #none()}
     * @see #filter(boolean)
     */
    Lino<T> isArray();

    /**
     * @param mapping 转换函数
     * @param <R>     转换后的泛型
     * @return 转换后的 Lino
     */
    <R> Lino<R> map(Function<? super T, ? extends R> mapping);

    /**
     * @param consumer 以 Lino 作为参数的消费者
     * @return this
     */
    Lino<T> nest(Consumer<? super Lino<T>> consumer);


    /**
     * @param other 值
     * @return 当 值 存在时返回 this，不存在时则返回新的 {@code  of(other)}
     */
    Lino<T> or(T other);

    /**
     * @param supplier 提供值的函数
     * @return 当 值 存在时返回 this，不存在时则返回新的 {@code  of(supplier.get())}
     */
    Lino<T> or(Supplier<? extends T> supplier);

    Lino<T> or(Lino<? extends T> lino);


    /**
     * @param other 实例
     * @return 当 other 与 lino 实际值 equals 时，返回 this，否则返回 {@link #none()}
     */
    Lino<T> same(T other);

    /**
     * 实际调用 {@link #throwable_map(ThrowableFunction, Consumer)}, 第二个参数传  {@link LiConstant#WHEN_THROW}
     *
     * @param mapping 转换函数
     * @param <R>     转换后的泛型
     * @return 转换后的 Lino
     * @see LiConstant#WHEN_THROW
     */
    <R> Lino<R> throwable_map(ThrowableFunction<? super T, ? extends R> mapping);

    /**
     * @param mapping   转换函数
     * @param whenThrow 当转换函数抛出异常时执行的函数
     * @param <R>       转换后的泛型
     * @return 转换后的 Lino
     */
    <R> Lino<R> throwable_map(ThrowableFunction<? super T, ? extends R> mapping, Consumer<Throwable> whenThrow);

    /**
     * @param <R> 泛型
     * @return 返回 LiCase 实例
     */
    <R> LiIf<? super T, R> toIf();

    /**
     * 支持的转换的类型有
     * {@link Iterable}
     * {@link Iterator}
     * {@link Enumeration}
     * 数组
     *
     * @return 根据实际值的类型，将数组或集合转换为 {@link Lira}
     */
    Lira<?> toLira();

    /**
     * @param <R>  泛型
     * @param type 类型
     * @return 根据实际值的类型，将数组或集合转换为 {@link Lira}
     * @see #toLira()
     */
    <R> Lira<R> toLira(Class<? extends R> type);


    final class Some<T> implements Lino<T> {

        private final T value;

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
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Some<?> some = (Some<?>) o;
            return Objects.equals(value, some.value);
        }

        @Override
        public String toString() {
            return name() + "(" + value + ")";
        }

        @Override
        public Lino<T> assertNotNone(String msg) {
            return this;
        }

        @Override
        public <R> Lino<R> cast(Class<? extends R> type) {
            return of(ClassUtil.cast(this.value, type));
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
        public Lino<T> filter(Function<? super T, ?> function) {
            if (function == null) {
                return this;
            }
            return filter(BooleanUtil.parse(function.apply(this.value)));
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public T get(T other) {
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
        public Lino<T> isArray() {
            if (this.value.getClass().isArray()) {
                return this;
            }

            if (this.value instanceof Iterable) {
                return this;
            }
            if (this.value instanceof Iterator) {
                return this;
            }
            return Lino.none();
        }

        @Override
        public <R> Lino<R> map(Function<? super T, ? extends R> mapping) {
            return of(mapping.apply(this.value));
        }

        @Override
        public Lino<T> nest(Consumer<? super Lino<T>> consumer) {
            consumer.accept(this);
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
        public Lino<T> or(Lino<? extends T> lino) {
            return this;
        }

        @Override
        public Lino<T> same(T other) {
            if (this.value.equals(other)) {
                return this;
            }
            return none();
        }

        @Override
        public <R> Lino<R> throwable_map(ThrowableFunction<? super T, ? extends R> mapping) {
            return throwable_map(mapping, LiConstant.WHEN_THROW);
        }

        @Override
        public <R> Lino<R> throwable_map(ThrowableFunction<? super T, ? extends R> mapping, Consumer<Throwable> whenThrow) {
            try {

                return of(mapping.apply(this.value));
            } catch (Throwable throwable) {
                if (whenThrow != null) {
                    whenThrow.accept(throwable);
                }
            }
            return none();
        }

        @Override
        public <R> LiIf<? super T, R> toIf() {
            return LiIf.of(this);
        }

        @Override
        public Lira<?> toLira() {
            Class<?> valueClass = this.value.getClass();
            if (valueClass.isArray()) {
                Class<?> componentType = valueClass.getComponentType();

                if (!componentType.isPrimitive()) {
                    return Lira.of(((Object[]) this.value));
                }

                if (componentType == boolean.class) {
                    return Lira.of(LiPrimitive.toWrapperArray((boolean[]) this.value));
                }
                if (componentType == byte.class) {
                    return Lira.of(LiPrimitive.toWrapperArray((byte[]) this.value));
                }
                if (componentType == char.class) {
                    return Lira.of(LiPrimitive.toWrapperArray((char[]) this.value));
                }
                if (componentType == double.class) {
                    return Lira.of(LiPrimitive.toWrapperArray((double[]) this.value));
                }
                if (componentType == float.class) {
                    return Lira.of(LiPrimitive.toWrapperArray((float[]) this.value));
                }
                if (componentType == int.class) {

                    return Lira.of(LiPrimitive.toWrapperArray((int[]) this.value));
                }
                if (componentType == long.class) {
                    return Lira.of(LiPrimitive.toWrapperArray((long[]) this.value));
                }
                if (componentType == short.class) {
                    return Lira.of(LiPrimitive.toWrapperArray((short[]) this.value));
                }
            }

            if (this.value instanceof Iterable) {
                return Lira.of((Iterable<?>) this.value);
            }
            if (this.value instanceof Iterator) {
                return Lira.of((Iterator<?>) this.value);
            }
            if (this.value instanceof Enumeration) {
                return Lira.of((Iterator<?>) EnumerationIter.of((Enumeration<?>) this.value));
            }

            return Lira.of(this.value);
        }


        @Override
        public <R> Lira<R> toLira(Class<? extends R> type) {
            return toLira().cast(type);
        }

    }


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
        public Lino<T> assertNotNone(String msg) {
            throw new IllegalStateException(msg);
        }

        @Override
        public <R> Lino<R> cast(Class<? extends R> type) {
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
        public Lino<T> filter(Function<? super T, ?> function) {
            return this;
        }

        @Override
        public T get() {
            return null;
        }

        @Override
        public T get(T other) {
            return other;
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
        public Lino<T> isArray() {
            return this;
        }

        @Override
        public <R> Lino<R> map(Function<? super T, ? extends R> mapping) {
            return none();
        }

        @Override
        public Lino<T> nest(Consumer<? super Lino<T>> consumer) {
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
        public Lino<T> or(Lino<? extends T> lino) {
            return Lino.narrow(lino);
        }

        @Override
        public Lino<T> same(T other) {
            return this;
        }

        @Override
        public <R> Lino<R> throwable_map(ThrowableFunction<? super T, ? extends R> mapping) {
            return none();
        }

        @Override
        public <R> Lino<R> throwable_map(ThrowableFunction<? super T, ? extends R> mapping, Consumer<Throwable> whenThrow) {
            return none();
        }

        @Override
        public <R> LiIf<? super T, R> toIf() {
            return LiIf.of();
        }

        @Override
        public Lira<?> toLira() {
            return Lira.none();
        }

        @Override
        public <R> Lira<R> toLira(Class<? extends R> type) {
            return Lira.none();
        }
    }
}
