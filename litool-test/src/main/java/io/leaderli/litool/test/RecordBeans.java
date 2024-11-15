package io.leaderli.litool.test;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.type.MethodFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class RecordBeans<T> implements IRecorder<RecordBeans<T>, T> {
    private final List<RecordBean<T>> recordBeans = new ArrayList<>();
    private final List<Consumer<RecordBean<T>>> recordBeanAction = new ArrayList<>();


    @SuppressWarnings("unchecked")
    public RecordBeans(Class<? extends T>... mockClasses) {

        for (Class<? extends T> mockClass : mockClasses) {
            recordBeans.add(new RecordBean<>((Class<T>) mockClass));
        }
    }

    public RecordBeans<T> consume(Consumer<T> consumer) {
        recordBeans.forEach(r -> r.consume(consumer));
        return this;
    }

    public RecordBeans<T> function(Function<T, Object> function) {

        recordBeans.forEach(r -> r.function(function));
        return this;
    }

    @Override
    public RecordBeans<T> called() {
        recordBeans.forEach(AbstractRecorder::called);
        return this;
    }

    @Override
    public RecordBeans<T> notCalled() {
        recordBeans.forEach(AbstractRecorder::notCalled);
        return this;
    }

    @Override
    public RecordBeans<T> arg(int index, Object arg) {
        recordBeans.forEach(r -> r.arg(index, arg));
        return this;
    }

    @Override
    public RecordBeans<T> args(Object... args) {
        recordBeans.forEach(r -> r.args(args));
        return this;
    }

    @Override
    public RecordBeans<T> assertThrowException(Class<? extends Throwable> exceptionClass) {
        recordBeans.forEach(r -> r.assertThrowException(exceptionClass));
        return this;
    }

    @Override
    public RecordBeans<T> assertReturn(Object compareReturn) {
        recordBeans.forEach(r -> r.assertReturn(compareReturn));
        return this;
    }

    @Override
    public RecordBeans<T> record(MethodFilter methodFilter, MethodAssert methodAssert) {
        recordBeans.forEach(r -> r.record(methodFilter, methodAssert));
        return this;
    }

    public T build() {
        T instance = null;
        for (RecordBean<T> mockBean : recordBeans) {
            instance = mockBean.build();
        }
        LiAssertUtil.assertFalse(instance == null, new IllegalArgumentException());
        return instance;
    }


}
