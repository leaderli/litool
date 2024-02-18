package io.leaderli.litool.test.cartesian;

/**
 * @author leaderli
 * @since 2022/8/21
 */
public class StringCartesian implements CartesianFunction<StringValues, String> {
    @Override
    public String[] apply(StringValues annotatedByValuable, CartesianContext context) {
        return annotatedByValuable.value();
    }
}
