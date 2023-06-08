package io.leaderli.litool.core.meta.logic;

import java.util.function.Function;


/**
 * 提供测试操作，测试操作完成后只能使用 and、or、apply 操作，
 * 返回一个 {@link UnionOperation} 对象。
 *
 * @param <T> 测试值的类型，参见 {@link UnionOperation#apply(Object)}
 */
@FunctionalInterface
public interface TestOperation<T> {

    /**
     * 对输入值进行测试，如果测试结果为 true，则返回一个 {@link TestSome} 对象，
     *
     * @param predicate 测试函数，函数的输入类型为 T，输出类型没有限制。
     * @return {@link UnionOperation} 对象，可以进行 and、or、apply 操作。
     */
    UnionOperation<T> test(Function<T, ?> predicate);

}
