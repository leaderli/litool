package io.leaderli.litool.test;

import java.lang.reflect.Method;
import java.util.function.Consumer;

public abstract class MethodAssertInvocation implements MethodAssert, Runnable {


    protected Method method;
    protected Object _this;
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
    public void apply(Method method, Object _this, Object[] args, Object _return) {
        this.method = method;
        this._this = _this;
        this.args = args;
        this._return = _return;
        this.run();
    }

}
