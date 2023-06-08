package io.leaderli.litool.core.meta.logic;

import java.util.function.Function;

/**
 * 提供 and、or、apply 操作，之后只允许 not、test 操作。
 * 因此它将返回 {@link CombineOperation}。
 *
 * @param <T> 测试值的类型，参见 {@link UnionOperation#apply(Object)}
 */
public interface UnionOperation<T> extends Function<T, Boolean> {

    /**
     * 类似于逻辑运算符 and。
     *
     * @return 返回 {@link AndSome}
     */
    CombineOperation<T> and();

    /**
     * 类似于逻辑运算符 or。
     *
     * @return 返回 {@link OrSome}
     */
    CombineOperation<T> or();
}
