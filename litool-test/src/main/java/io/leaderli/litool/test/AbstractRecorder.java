package io.leaderli.litool.test;

import io.leaderli.litool.core.meta.Either;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.MethodFilter;
import io.leaderli.litool.core.type.PrimitiveEnum;
import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public class AbstractRecorder<T> {

    public static final Set<Method> recordMethodCall = new HashSet<>();
    public static final Set<Method> actualMethodCall = new HashSet<>();
    public static final List<Throwable> assertThrow = new ArrayList<>();
    protected final Class<?> mockClass;
    protected final Map<Method, List<MethodAssert>> methodAsserts = new HashMap<>();
    private Method currentMethod;
    protected boolean build;

    public AbstractRecorder(Class<?> mockClass) {
        this.mockClass = mockClass;
        // 仅在build过程中生效，用于记录方法的调用
        LiMock.mock(mockClass, MethodFilter.isMethod(), (method, args) -> {
            if (build) {
                return Either.none();
            }
            currentMethod = method;
            methodAsserts.put(method, new ArrayList<>());
            return PrimitiveEnum.get(method.getReturnType()).zero_value;
        }, false);
    }


    /**
     * 该方法会清除之前的调用记录
     *
     * @see #actualMethodCall
     */
    public T called() {
        actualMethodCall.remove(currentMethod);
        recordMethodCall.add(currentMethod);
        add((method, _this, args, _return) -> actualMethodCall.add(currentMethod));
        return (T) this;
    }


    public T arg(int index, Object arg) {
        add((method, _this, args, _return) -> {
            Assertions.assertTrue(args.length > index);
            Assertions.assertEquals(arg, args[index]);
        });
        return (T) this;
    }

    public <R> T argAssert(int index, Consumer<R> argAssert, Class<R> paraType) {
        add((method, _this, args, _return) -> {
            Assertions.assertTrue(args.length > index);
            Assertions.assertEquals(ClassUtil.primitiveToWrapper(method.getParameterTypes()[index]), ClassUtil.primitiveToWrapper(paraType));
            argAssert.accept((R) args[index]);
        });
        return (T) this;
    }


    public T args(Object... compareArgs) {
        add((method, _this, args, _return) -> Assertions.assertArrayEquals(compareArgs, args));
        return (T) this;
    }

    public T assertReturn(Object compareReturn) {
        add((method, _this, args, _return) -> Assertions.assertEquals(compareReturn, _return));
        return (T) this;
    }

    protected void add(MethodAssert methodAssert) {
        if (currentMethod == null) {
            throw new IllegalStateException("method may not called , or it's abstract");
        }
        methodAsserts.get(currentMethod).add(methodAssert);
    }

}

