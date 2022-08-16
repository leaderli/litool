package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.runner.TypeAlias;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author leaderli
 * @since 2022/8/10
 */
public interface Instruct {

    // 实际执行调用的方法名
    String INVOKE_METHOD_NAME = "invoke";

    /**
     * invoke 方法的参数和返回值都要是解析器支持的类型
     *
     * @return 返回 invoke 方法
     * @see TypeAlias#support(Class)
     */
    default Lira<Method> getInstructMethod() {
        Class<? extends Instruct> cls = getClass();
        Lira<Method> methods = ReflectUtil.getMethods(cls)
                .filter(f -> f.getDeclaringClass() == cls)
                .filter(f -> INVOKE_METHOD_NAME.equals(f.getName()));

        LiAssertUtil.assertTrue(methods.present(), "don't have invoke method");
        Class<?> returnType = methods.first().get().getReturnType();
        LiAssertUtil.assertTrue(TypeAlias.support(returnType), "invoke  returnType is unsupported");

        for (Method method : methods) {

            LiAssertUtil.assertTrue(returnType == method.getReturnType(), "all invoke method must have same returnType");

            for (int i = 0; i < method.getParameterTypes().length; i++) {


                Class<?> parameterType = method.getParameterTypes()[i];
                Class<?> temp = parameterType;
                if (temp.isArray()) {
                    // 仅允许最后一位为数组参数
                    LiAssertUtil.assertTrue(i == method.getParameterTypes().length - 1, String.format("invoke arr parameterType is only support on the last: %s", Arrays.toString(method.getParameterTypes())));
                    temp = temp.getComponentType();

                }
                LiAssertUtil.assertTrue(TypeAlias.ALIAS.containsValue(temp), String.format("invoke parameterType [%s] is unsupported", parameterType));

            }
        }
        return methods;

    }

    Object apply(Object[] objects);

    String name();

    default FuncScope getScope() {
        return FuncScope.CONSTANT;
    }

}
