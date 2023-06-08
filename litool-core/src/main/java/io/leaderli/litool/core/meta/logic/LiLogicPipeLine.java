package io.leaderli.litool.core.meta.logic;

import java.util.function.Function;

/**
 * 用于提供逻辑操作的方法
 *
 * @param <T> 示输入数据的类型
 */
public class LiLogicPipeLine<T> implements CombineOperation<T>, UnionOperation<T> {

    // 初始逻辑操作为 BeginSome
    private LogicSome<T> logic = new BeginSome<>();

    /**
     * 开始一个新的逻辑管道
     *
     * @param <T> 示输入数据的类型
     * @return -
     */
    @SuppressWarnings("unchecked")
    public static <T> CombineOperation<T> begin() {
        LiLogicPipeLine<Object> objectLiLogicPipeLine = new LiLogicPipeLine<>();
        return (CombineOperation<T>) objectLiLogicPipeLine;
    }

    /**
     * 添加一个测试操作，
     *
     * @param predicate 测试条件
     * @return -
     */
    @Override
    public UnionOperation<T> test(Function<T, ?> predicate) {
        logic = new TestSome<>(logic, predicate);
        return this;
    }

    /**
     * 添加一个取反操作
     *
     * @return -
     */
    @Override
    public TestOperation<T> not() {
        logic = new NotSome<>(logic);
        return this;
    }

    /**
     * 添加一个与操作
     *
     * @return -
     */

    @Override
    public CombineOperation<T> and() {
        logic = new AndSome<>(logic);
        return this;
    }

    /**
     * 添加一个或操作
     *
     * @return -
     */
    @Override
    public CombineOperation<T> or() {
        logic = new OrSome<>(logic);
        return this;
    }

    /**
     * 应用逻辑管道，得到最终的布尔结果
     *
     * @return -
     */
    @Override
    public Boolean apply(T t) {
        return logic.apply(t);
    }
}
