package io.leaderli.litool.test;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.type.MethodFilter;
import io.leaderli.litool.test.bean.BeanCreator;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

public class RecordBean<T> extends AbstractRecorder<RecordBean<T>> implements IRecorder<RecordBean<T>, T> {

    private final T instance;

    public RecordBean(Class<T> mockClass) {
        super(mockClass);
        this.instance = BeanCreator.mockBean(mockClass);
        LiAssertUtil.assertNotNull(instance, "can not create instance of " + mockClass);
    }

    public RecordBean<T> consume(Consumer<T> consumer) {
        consumer.accept(instance);
        return this;
    }

    public RecordBean<T> function(Function<T, Object> function) {
        function.apply(instance);
        return this;
    }

    public T build() {
        build = true;
        LiMock.record(mockClass, MethodFilter.of(methodAsserts::containsKey),
                (m, args, originReturn) ->
                        methodAsserts
                                .getOrDefault(m, new ArrayList<>())
                                .forEach(methodAssert -> methodAssert.apply(m, args, originReturn)));
        return instance;
    }


}
