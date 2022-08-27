package io.leaderli.litool.core.test;

/**
 * @author leaderli
 * @since 2022/8/21
 */
public class StringCartesian implements CartesianFunction<StringValues, String> {
@Override
public String[] apply(StringValues stringValues, CartesianContext context) {
    return stringValues.value();
}
}
