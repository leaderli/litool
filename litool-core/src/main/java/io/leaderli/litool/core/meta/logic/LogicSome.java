package io.leaderli.litool.core.meta.logic;

import io.leaderli.litool.core.meta.LiBox;

import java.util.function.Function;

/**
 * 逻辑操作基类
 *
 * @param <T> 泛型类型
 */
abstract class LogicSome<T> implements CombineOperation<T>, UnionOperation<T>,
        Publisher<T> {
    /**
     * 上一个发布者
     */
    protected final Publisher<T> prevPublisher;

    /**
     * 构造函数
     *
     * @param prevPublisher 上一个发布者
     */
    protected LogicSome(Publisher<T> prevPublisher) {
        this.prevPublisher = prevPublisher;
    }

    /**
     * 进行测试操作
     *
     * @param predicate 谓词函数
     * @return 返回 UnionOperation 对象
     */
    @Override
    public UnionOperation<T> test(Function<T, ?> predicate) {
        return new TestSome<>(this, predicate);
    }

    /**
     * 进行取反操作
     *
     * @return 返回 TestOperation 对象
     */
    @Override
    public TestOperation<T> not() {
        return new NotSome<>(this);
    }

    /**
     * 进行与操作
     *
     * @return 返回 CombineOperation 对象
     */
    @Override
    public CombineOperation<T> and() {
        return new AndSome<>(this);
    }

    /**
     * 进行或操作
     *
     * @return 返回 CombineOperation 对象
     */
    @Override
    public CombineOperation<T> or() {
        return new OrSome<>(this);
    }

    /**
     * 进行逻辑操作
     *
     * @param t 参数
     * @return 返回 Boolean 对象
     */
    @Override
    public Boolean apply(T t) {
        LiBox<Boolean> box = LiBox.of(false);
        subscribe(new Subscriber<T>() {
            @Override
            public void onSubscribe(Subscription<T> subscription) {
                // 请求参数
                subscription.request(t);
            }

            @Override
            public void next(T t, boolean lastState) {
                // 记录上一个状态
                box.value(lastState);
            }

            @Override
            public void onNot() {
                // 不支持操作
                throw new UnsupportedOperationException();
            }

            @Override
            public void onComplete(boolean result) {
                // 记录最终结果
                box.value(result);
            }
        });
        return box.value();
    }
}
