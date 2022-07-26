package io.leaderli.litool.core.type;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author leaderli
 * @since 2022/7/26 10:36 AM
 * 方法的描述符
 */
public class MethodSignature {

    public final String name;
    public final Class<?> returnType;
    public final Class<?>[] parameterTypes;


    public MethodSignature(String name, Class<?> returnType, Class<?>[] parameterTypes) {
        this.name = name;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
    }


    public boolean same(Method method) {
        return method.getName().equals(name)
                && method.getReturnType() == returnType
                && Arrays.equals(parameterTypes, method.getParameterTypes());
    }
}
