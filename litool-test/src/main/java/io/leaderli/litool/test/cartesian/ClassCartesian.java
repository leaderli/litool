package io.leaderli.litool.test.cartesian;

/**
 * @author leaderli
 * @since 2022/8/21
 */
public class ClassCartesian implements CartesianFunction<ClassValues, Class<?>> {

    @Override
    public Class<?>[] apply(ClassValues annotatedByValuable, CartesianContext context) {
        return annotatedByValuable.value();
    }
}
