package io.leaderli.litool.core.type;

import java.util.function.Supplier;

/**
 * 提供一个值的接口，表示它是一个构造函数。
 *
 * @author leaderli
 * @since 2022/9/25 12:51 PM
 */
@FunctionalInterface
public interface ObjectConstructor<T> extends Supplier<T> {
}
