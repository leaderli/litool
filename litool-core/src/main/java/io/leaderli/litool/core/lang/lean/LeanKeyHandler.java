package io.leaderli.litool.core.lang.lean;

import io.leaderli.litool.core.lang.lean.adapters.ReflectTypeAdapterFactory;

import java.lang.reflect.Field;
import java.util.function.Function;

/**
 * 用于填充类的成员变量使用，提供自定义行为来定义类的成员变量如何取值，默认根据类的成员变量名称去目标类取值
 *
 * @author leaderli
 * @see ReflectTypeAdapterFactory.ReflectAdapter
 * @since 2022/9/27 11:53 AM
 */
public interface LeanKeyHandler extends Function<Field, String> {
}
