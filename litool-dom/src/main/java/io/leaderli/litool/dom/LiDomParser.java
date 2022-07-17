package io.leaderli.litool.dom;

import io.leaderli.litool.core.type.ComponentType;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/6 8:43 AM
 */
public interface LiDomParser<T> extends ComponentType<T>, Function<String, T> {
}
