package io.leaderli.litool.core.meta.logic;

/**
 * 将{@link NotOperation}和{@link TestOperation}进行组合的接口。
 * <p>
 * 组合操作提供了“非”操作与“测试”操作的结合。
 * </p>
 *
 * @param <T> 测试值的类型 {@link UnionOperation#apply(Object)}
 */
public interface CombineOperation<T> extends TestOperation<T>, NotOperation<T> {

}
