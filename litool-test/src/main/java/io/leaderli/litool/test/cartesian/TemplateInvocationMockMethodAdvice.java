package io.leaderli.litool.test.cartesian;

import io.leaderli.litool.core.type.PrimitiveEnum;
import io.leaderli.litool.core.type.TypeUtil;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * 单条测试用例的切面
 */
public class TemplateInvocationMockMethodAdvice {


    /**
     * 方法的执行结果缓存，用于拦截实际请求，直接返回结果,支持根据参数进行返回
     */
    public static Map<Method, Object> METHOD_VALUE;

    /**
     * for skip real method call, the return value must not be null. use {@link CartesianMock#SKIP} to
     * mark the return null. and put it back at {@link #exit(Object, Object, Method, Object[])}
     *
     * @param origin origin method
     * @param args   origin args
     * @param _this  origin  this
     * @return the method return value
     */
    @SuppressWarnings("all")
    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
    public static Object enter(@Advice.Origin Method origin,
                               @Advice.AllArguments Object[] args,
                               @Advice.This(optional = true) Object _this) {

        Object value = METHOD_VALUE.get(origin);
        if (value instanceof MethodResultForParameter) {
            value = ((MethodResultForParameter) value).get(args);
        }
        Class<?> returnType = origin.getReturnType();
        if (value == CartesianMock.SKIP) {

            if (returnType == void.class) {
                value = CartesianMock.SKIP;
            } else {

                Type type = origin.getGenericReturnType();
                if (_this != null) {
                    type = TypeUtil.resolve(_this.getClass(), origin.getGenericReturnType());
                }
                value = MockBean.mockBean(type);
            }
        } else {

            PrimitiveEnum primitiveEnum = PrimitiveEnum.get(returnType);
            if (value == null && primitiveEnum != PrimitiveEnum.OBJECT) {
                value = primitiveEnum.zero_value;
            }
        }
        if (value == null) {
            return CartesianMock.SKIP;
        }
        return value;
    }

    @SuppressWarnings("all")
    @Advice.OnMethodExit
    public static void exit(
            @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object _return,
            @Advice.Enter(readOnly = false) Object mock,
            @Advice.Origin Method origin,
            @Advice.AllArguments Object[] args) {

        if (mock == CartesianMock.SKIP) {
            mock = null;
        }
        _return = mock;
    }
}
