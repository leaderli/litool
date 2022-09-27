package io.leaderli.litool.core.lang.lean;

import java.lang.reflect.Field;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/9/27 11:53 AM
 */
public interface LeanFieldKey extends Function<Field, String> {
}
