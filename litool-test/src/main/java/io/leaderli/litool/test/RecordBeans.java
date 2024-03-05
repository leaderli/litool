package io.leaderli.litool.test;

import io.leaderli.litool.core.exception.LiAssertUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class RecordBeans<T> implements RecordBeanInterface<RecordBeans<T>, T> {
    private final List<RecordBean<T>> recordBeans = new ArrayList<>();
    private final List<Consumer<RecordBean<T>>> recordBeanAction = new ArrayList<>();


    @SuppressWarnings("unchecked")
    public RecordBeans(Class<? extends T>... mockClasses) {

        for (Class<? extends T> mockClass : mockClasses) {
            recordBeans.add(new RecordBean<>((Class<T>) mockClass));
        }
    }

    public RecordBeans<T> consume(Consumer<T> consumer) {
//        recordBeanAction.add(recordbean -> recordbean.consume(consumer));
        for (RecordBean<T> recordBean : recordBeans) {
            recordBean.consume(consumer);
        }

        return this;
    }

    public RecordBeans<T> when(Function<T, Object> function) {
//        recordBeanAction.add(recordbean -> recordbean.when(function));
        for (RecordBean<T> recordBean : recordBeans) {
            recordBean.when(function);
        }

        return this;
    }

    @Override
    public RecordBeans<T> called() {
//        recordBeanAction.add(AbstractRecorder::called);
        for (RecordBean<T> recordBean : recordBeans) {
            recordBean.called();
        }
        return this;
    }

    @Override
    public RecordBeans<T> arg(int index, Object arg) {
//        recordBeanAction.add(recordbean -> recordbean.arg(index, arg));
        for (RecordBean<T> recordBean : recordBeans) {
            recordBean.arg(index, arg);
        }
        return this;
    }

    @Override
    public RecordBeans<T> args(Object... args) {
//        recordBeanAction.add(recordbean -> recordbean.args(args));
        for (RecordBean<T> recordBean : recordBeans) {
            recordBean.args(args);
        }
        return this;
    }

    @Override
    public RecordBeans<T> assertReturn(Object compareReturn) {
        for (RecordBean<T> recordBean : recordBeans) {
            recordBean.assertReturn(compareReturn);
        }
//        recordBeanAction.add(recordbean -> recordbean.assertReturn(compareReturn));
        return this;
    }

    public T build() {
        T instance = null;
        for (RecordBean<T> mockBean : recordBeans) {
//            recordBeanAction.forEach(action -> action.accept(mockBean));
            instance = mockBean.build();
        }
        LiAssertUtil.assertFalse(instance == null, new IllegalArgumentException());
        return instance;
    }


}
