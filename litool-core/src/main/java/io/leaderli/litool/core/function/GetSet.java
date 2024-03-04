package io.leaderli.litool.core.function;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public interface GetSet<T> {

    T get() throws Throwable;

    void set(T t) throws Throwable;

    Object instance();

    static GetSet<Object> propertyDescriptor(Object instance, PropertyDescriptor descriptor) {
        return new GetSet<Object>() {
            @Override
            public Object get() throws Throwable {
                Method readMethod = descriptor.getReadMethod();
                readMethod.setAccessible(true);
                return readMethod.invoke(instance);
            }

            @Override
            public void set(Object arg) throws Throwable {
                Method writeMethod = descriptor.getWriteMethod();
                writeMethod.setAccessible(true);
                writeMethod.invoke(instance, arg);
            }

            @Override
            public Object instance() {
                return instance;
            }
        };
    }
}
