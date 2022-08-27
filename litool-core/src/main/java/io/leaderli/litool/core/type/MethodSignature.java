package io.leaderli.litool.core.type;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author leaderli
 * @since 2022/7/26 10:36 AM
 * 方法的描述符
 */
public class MethodSignature {

/**
 * 方法名
 */
public final String name;
/**
 * 方法返回类型
 */
public final Class<?> returnType;
/**
 * 方法参数类型
 */
public final Class<?>[] parameterTypes;


public MethodSignature(String name, Class<?> returnType, Class<?>[] parameterTypes) {
    if (returnType == null) {
        returnType = void.class;
    }
    if (parameterTypes == null) {
        parameterTypes = new Class[]{};
    }
    this.name = name;
    this.returnType = returnType;
    this.parameterTypes = parameterTypes;
}

public static MethodSignature of(String name, Class<?> returnType, Class<?>[] parameterTypes) {
    return new MethodSignature(name, returnType, parameterTypes);
}


/**
 * @param method 方法
 * @return 方法描述符匹配
 * @see #name
 * @see #returnType
 * @see #parameterTypes
 */
public boolean same(Method method) {
    return method.getName().equals(name)
            && method.getReturnType() == returnType
            && Arrays.equals(parameterTypes, method.getParameterTypes());
}

}
