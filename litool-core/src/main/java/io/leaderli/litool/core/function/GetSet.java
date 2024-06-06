package io.leaderli.litool.core.function;

import io.leaderli.litool.core.type.PrimitiveEnum;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public interface GetSet<T> {

    T get() throws Throwable;

    void set(T t) throws Throwable;

    Class<T> type();

    Object instance();

    @SuppressWarnings("rawtypes")
    static GetSet propertyDescriptor(Object instance, PropertyDescriptor descriptor) {
        return new GetSet() {
            @Override
            public Object get() throws Throwable {
                Method readMethod = descriptor.getReadMethod();
                if (readMethod == null) {
                    return PrimitiveEnum.get(type()).zero_value;
                }
                readMethod.setAccessible(true);
                return readMethod.invoke(instance);
            }

            @Override
            public void set(Object arg) throws Throwable {
                Method writeMethod = descriptor.getWriteMethod();
                if (writeMethod == null) {
                    return;
                }
                writeMethod.setAccessible(true);
                writeMethod.invoke(instance, arg);
            }

            @Override
            public Class type() {
                return descriptor.getPropertyType();
            }

            @Override
            public Object instance() {
                return instance;
            }

            @Override
            public String toString() {
                return instance + "." + type();
            }
        };

    }
}
