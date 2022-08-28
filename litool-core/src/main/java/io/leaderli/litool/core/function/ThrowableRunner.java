package io.leaderli.litool.core.function;

/**
 * Represents an action which may throw a  error
 *
 * @author leaderli
 * @since 2022/6/16
 */

@FunctionalInterface
public interface ThrowableRunner {
/**
 * Performs an action
 *
 * @throws Throwable error
 */
void run() throws Throwable;
}
