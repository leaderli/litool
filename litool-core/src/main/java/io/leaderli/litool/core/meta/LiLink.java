package io.leaderli.litool.core.meta;

import io.leaderli.litool.core.exception.LiThrowableConsumer;
import io.leaderli.litool.core.exception.LiThrowableFunction;
import io.leaderli.litool.core.exception.LiThrowableRunner;
import io.leaderli.litool.core.exception.LiThrowableSupplier;
import io.leaderli.litool.core.meta.link.CancelConsumerLink;
import io.leaderli.litool.core.meta.link.PublisherLink;
import io.leaderli.litool.core.meta.link.ValueLink;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @param <T> 泛型
 *            <p>
 *            链式执行，每个节点执行成功后才会执行下一个节点，若某一个节点执行结果为失败，
 *            则调用最靠近的一组  error 节点
 *            执行动作是响应式的， 需要显式的调用 {@link #present()} 才会触发
 * @author leaderli
 * @since 2022/7/16
 */
public interface LiLink<T> extends LiValue, PublisherLink<T>, Runnable {

    /**
     * @return 返回一个值为 1 的实例
     */
    static LiLink<Integer> of() {

        return new ValueLink<>(1);
    }

    /**
     * @param value 实例
     * @param <T>   泛型
     * @return 返回一个新的实例
     */
    static <T> LiLink<T> of(T value) {

        return new ValueLink<>(value);
    }


    /**
     * 当返回 false 时 ，跳过执行后续的  filter,then, 执行最近的 连续的 error 节点，并且终止执行
     *
     * @param filter 过滤器
     * @return this
     * @see io.leaderli.litool.core.util.LiBoolUtil#parse(Object)
     */
    LiLink<T> then(Function<? super T, ?> filter);

    /**
     * @param supplier 过滤器
     * @return this
     * @see #then(Function)
     */
    LiLink<T> then(Supplier<?> supplier);

    /**
     * 未中断链条时执行，且继续执行下一个节点，该节点不会捕获异常，
     *
     * @param consumer 消费者
     * @return this
     */
    LiLink<T> then(Consumer<? super T> consumer);

    /**
     * 未中断链条时执行，且继续执行下一个节点，该节点不会捕获异常，
     *
     * @param runnable 运行函数
     * @return this
     */
    LiLink<T> then(Runnable runnable);


    /**
     * 当抛出异常时，中断链条执行
     *
     * @param filter 过滤器
     * @return this
     * @see #then(Function)
     */
    LiLink<T> throwable_then(LiThrowableFunction<? super T, ?> filter);

    /**
     * 当抛出异常时，中断链条执行
     *
     * @param filter 过滤器
     * @return this
     * @see #then(Supplier)
     */
    LiLink<T> throwable_then(LiThrowableSupplier<?> filter);

    /**
     * 当抛出异常时，中断链条执行
     *
     * @param consumer 消费者
     * @return this
     * @see #then(Consumer)
     */
    LiLink<T> throwable_then(LiThrowableConsumer<? super T> consumer);

    /**
     * 当抛出异常时，中断链条执行
     *
     * @param runner 运行函数
     * @return this
     * @see #then(Runnable)
     */
    LiLink<T> throwable_then(LiThrowableRunner runner);


    /**
     * 当链条失败时执行
     *
     * @param runnable 执行函数
     * @return this
     */
    LiLink<T> error(Runnable runnable);

    /**
     * 当链条失败且 value 不为 null 时执行
     *
     * @param consumer 消费者
     * @return this
     */
    CancelConsumerLink<T> error(Consumer<? super T> consumer);

    /**
     * 当链条失败时执行，无视异常
     *
     * @param runnable 执行函数
     * @return this
     * @see LiConstant#WHEN_THROW
     */
    LiLink<T> throwable_error(LiThrowableRunner runnable);

    /**
     * 当链条失败且 value 不为 null 时执行，无视异常
     *
     * @param consumer 消费者
     * @return this
     * @see LiConstant#WHEN_THROW
     */
    CancelConsumerLink<T> throwable_error(LiThrowableConsumer<? super T> consumer);

    /**
     * @return 链条是否正确执行完成，没有任何 error 节点执行
     */
    @Override
    boolean present();


    Runnable request(T t);

    /**
     * @param onFinally 最后一个消费者
     * @see #present()
     */
    void onFinally(Consumer<Boolean> onFinally);

    /**
     * 触发执行动作
     *
     * @see #present()
     */
    @Override
    void run();

    @Override
    String name();


}


