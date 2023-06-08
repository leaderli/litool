package io.leaderli.litool.core.meta;

import io.leaderli.litool.core.function.ThrowableConsumer;
import io.leaderli.litool.core.function.ThrowableFunction;
import io.leaderli.litool.core.function.ThrowableRunner;
import io.leaderli.litool.core.function.ThrowableSupplier;
import io.leaderli.litool.core.meta.link.PublisherLink;
import io.leaderli.litool.core.meta.link.ValueLink;
import io.leaderli.litool.core.util.BooleanUtil;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 * 链式执行，每个节点执行成功后才会执行下一个节点，若某一个节点执行结果为失败或者执行结果为false，
 * 则调用最靠近的一组onInterrupt方法
 * 执行动作是响应式的，需要显式的调用present()才会触发
 *
 * @param <T> 泛型
 * @see BooleanUtil#parse(Object)
 */
public interface LiLink<T> extends LiValue, PublisherLink<T>, Runnable {

    /**
     * 返回一个值为1的LiLink，这个值仅用于驱动链式执行。
     *
     * @return a liLink with value 1, the value only to drive chain execute
     */
    static LiLink<Integer> of() {

        return new ValueLink<>(1);
    }

    /**
     * @param supplier -
     * @param <T>      泛型
     * @return a liLink
     * @see #of(Object)
     */
    static <T> LiLink<T> supplier(Supplier<T> supplier) {

        if (supplier == null) {
            return of(null);
        }
        return of(supplier.get());
    }

    /**
     * 返回一个带有指定值的LiLink。如果value的值为null，则直接执行最近的一组onInterrupt方法
     *
     * @param value LiLink的值
     * @param <T>   泛型
     * @return a liLink
     */
    static <T> LiLink<T> of(T value) {

        return new ValueLink<>(value);
    }

    /**
     * 当函数结果为null时，会中断执行链。
     *
     * @param mapper 映射函数
     * @param <R>    映射结果泛型
     * @return 新的LiLink
     * @see Function#apply(Object)
     */
    <R> LiLink<R> map(Function<? super T, ? extends R> mapper);

    /**
     * 当函数值为false时，会中断执行链。
     *
     * @param filter 过滤函数
     * @return 新的LiLink
     * @see BooleanUtil#parse(Object)
     */

    LiLink<T> then(Function<? super T, ?> filter);

    /**
     * 当函数值为false时，会中断执行链。
     *
     * @param supplier -
     * @return new link
     * @see BooleanUtil#parse(Object)
     */
    LiLink<T> then(Supplier<?> supplier);

    /**
     * 在执行下一个节点之前执行一个消费者。
     *
     * @param consumer 消费者
     * @return 新的LiLink
     */
    LiLink<T> then(Consumer<? super T> consumer);

    /**
     * 在执行下一个节点之前执行一个Runnable。
     *
     * @param runnable -
     * @return 新的LiLink
     */
    LiLink<T> then(Runnable runnable);


    /**
     * 当函数值为false时或者异常时，会中断执行链。
     *
     * @param filter 过滤函数
     * @return new link
     * @see BooleanUtil#parse(Object)
     */
    LiLink<T> throwable_then(ThrowableFunction<? super T, ?> filter);


    /**
     * 当函数值为false时或者异常时，会中断执行链。
     *
     * @param supplier -
     * @return new link
     * @see BooleanUtil#parse(Object)
     */
    LiLink<T> throwable_then(ThrowableSupplier<?> supplier);

    /**
     * 在执行下一个节点之前执行一个Runnable。异常时，会中断执行链。
     *
     * @param consumer -
     * @return new link
     * @see BooleanUtil#parse(Object)
     */
    LiLink<T> throwable_then(ThrowableConsumer<? super T> consumer);

    /**
     * 在执行下一个节点之前执行一个消费者。异常时，会中断执行链。
     *
     * @param runner -
     * @return new link
     */
    LiLink<T> throwable_then(ThrowableRunner runner);


    /**
     * 当执行链被中断时执行
     *
     * @param runnable the runnable
     * @return new link
     * @see WhenThrowBehavior#WHEN_THROW
     */
    LiLink<T> onInterrupt(Runnable runnable);

    /**
     * 当执行链被中断且发出信息的值不为null时执行
     *
     * @param consumer the consumer
     * @return new link
     * @see WhenThrowBehavior#WHEN_THROW
     */
    LiLink<T> onInterrupt(Consumer<? super T> consumer);

    /**
     * 当执行链被中断时执行
     *
     * @param runnable the runnable
     * @return new link
     * @see WhenThrowBehavior#WHEN_THROW
     */
    LiLink<T> onThrowableInterrupt(ThrowableRunner runnable);

    /**
     * 当执行链被中断且发出信息的值不为null时执行
     *
     * @param consumer the consumer
     * @return new link
     * @see WhenThrowBehavior#WHEN_THROW
     */
    LiLink<T> onThrowableInterrupt(ThrowableConsumer<? super T> consumer);


    /**
     * 终结操作
     *
     * @return {@code  true} 如果执行链没有被中断
     */
    @Override
    boolean present();

    @Override
    String name();

    /**
     * 终止操作，调用最后的消费者函数，并传递执行链是否被中断的参数
     *
     * @param onFinally 最后的消费者函数，无论如何都会被调用
     * @see #present()
     */
    void onFinally(Consumer<Boolean> onFinally);

    /**
     * 终结操作
     *
     * @see #present()
     */
    @Override
    void run();


}


