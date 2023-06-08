package io.leaderli.litool.core.meta.logic;

/**
 * 提供逻辑非操作，经过逻辑非操作后只允许进行测试操作，返回{@link TestOperation}类型的对象。
 *
 * @param <T> 测试值的类型 {@link UnionOperation#apply(Object)}
 */
@FunctionalInterface
public interface NotOperation<T> {
    /**
     * 对嵌套的测试操作执行逻辑非操作，返回 {@link NotSome} 类型的对象进行测试操作。
     *
     * @return 返回一个 {@link NotSome} 类型的对象进行测试操作。
     */
    TestOperation<T> not();
}
