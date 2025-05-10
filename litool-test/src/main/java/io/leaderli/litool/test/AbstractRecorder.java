package io.leaderli.litool.test;

import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.MethodFilter;
import io.leaderli.litool.core.type.PrimitiveEnum;
import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public class AbstractRecorder<T> {

    protected static final Set<Method> recordMethodCall = new HashSet<>();
    protected static final Set<Method> recordMethodNotCall = new HashSet<>();
    protected static final Set<Method> actualMethodCall = new HashSet<>();
    protected static final List<Throwable> assertThrow = new ArrayList<>();
    protected final Class<?> mockClass;
    protected final Map<Method, List<MethodAssert>> methodAsserts = new HashMap<>();
    private Method currentMethod;
    protected boolean build;

    public AbstractRecorder(Class<?> mockClass) {
        this.mockClass = mockClass;
        // 仅在build过程中生效，用于记录方法的调用
        LiMock.mock(mockClass, MethodFilter.isMethod(), (method, args) -> {
            if (build) {
                return LiMock.SKIP_MARK;
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
        add((method, args, originReturn) -> actualMethodCall.add(currentMethod));
        return (T) this;
    }

    /**
     * 该方法会清除之前的调用记录
     *
     * @see #actualMethodCall
     */
    public T notCalled() {
        actualMethodCall.remove(currentMethod);
        recordMethodNotCall.add(currentMethod);
        add((method, args, originReturn) -> actualMethodCall.add(currentMethod));
        return (T) this;
    }

    public T arg(int index, Object arg) {
        add((method, args, originReturn) -> {
            Assertions.assertTrue(args.length > index);
            Assertions.assertEquals(arg, args[index]);
        });
        return (T) this;
    }

    public <R> T argAssert(int index, Consumer<R> argAssert, Class<R> paraType) {
        add((method, args, originReturn) -> {
            Assertions.assertTrue(args.length > index);
            Assertions.assertEquals(ClassUtil.primitiveToWrapper(method.getParameterTypes()[index]), ClassUtil.primitiveToWrapper(paraType));
            argAssert.accept((R) args[index]);
        });
        return (T) this;
    }


    public T args(Object... compareArgs) {
        add((method, args, originReturn) -> Assertions.assertArrayEquals(compareArgs, args));
        return (T) this;
    }

    public T assertThrowException(Class<? extends Throwable> exceptionClass) {
        add((method, args, originReturn) -> Assertions.assertSame(originReturn.getClass(), exceptionClass));
        return (T) this;
    }

    public T assertReturn(Object compareReturn) {
        add((method, args, originReturn) -> Assertions.assertEquals(compareReturn, originReturn));
        return (T) this;
    }

    public T record(MethodFilter methodFilter, MethodAssert methodAssert) {
        for (Method declaredMethod : LiMock.findDeclaredMethods(mockClass, methodFilter)) {
            methodAsserts.computeIfAbsent(declaredMethod, k -> new ArrayList<>()).add(methodAssert);
        }
        return (T) this;
    }

    protected void add(MethodAssert methodAssert) {
        if (currentMethod == null) {
            throw new IllegalStateException("method may not called , or it's abstract");
        }
        methodAsserts.get(currentMethod).add(methodAssert);
    }

}

