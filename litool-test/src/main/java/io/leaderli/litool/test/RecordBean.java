package io.leaderli.litool.test;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.type.BeanCreator;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

public class RecordBean<T> extends AbstractRecorder<RecordBean<T>> {

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

    public RecordBean<T> when(Function<T, Object> function) {
        function.apply(instance);
        return this;
    }

    public T build() {
        build = true;
        LiMock.record(mockClass, m -> !methodAsserts.getOrDefault(m, new ArrayList<>()).isEmpty(),
                (m, _this, args, _return) -> methodAsserts.get(m).forEach(methodAssert -> methodAssert.apply(m, _this, args, _return)));
        return instance;
    }


}
