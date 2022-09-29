package io.leaderli.litool.core.type;

import java.util.function.Supplier;

/**
 * a interface that provide a value, indicate it's a constructor
 *
 * @author leaderli
 * @since 2022/9/25 12:51 PM
 */
@FunctionalInterface
public interface ObjectConstructor<T> extends Supplier<T> {
}
