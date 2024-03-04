package io.leaderli.litool.test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Consumer;

public abstract class MethodAssertInvocation implements MethodAssert, Runnable {


    protected Method method;
    protected Object[] args;
    protected Object _return;

    protected MethodAssertInvocation() {
    }


    public static MethodAssertInvocation of(Consumer<MethodAssertInvocation> consumer) {
        return new MethodAssertInvocation() {
            @Override
            public void run() {
                consumer.accept(this);
            }
        };
    }

    @Override
    public void apply(Method method, Object[] args, Object _return) {
        this.method = method;
        this.args = args;
        this._return = _return;
        this.run();
    }

    @Override
    public String toString() {
        return "MethodAssertInvocation{" +
                "method=" + method +
                ", args=" + Arrays.toString(args) +
                ", return=" + _return +
                '}';
    }
}
