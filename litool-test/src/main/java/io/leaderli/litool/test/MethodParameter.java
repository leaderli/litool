package io.leaderli.litool.test;

import java.util.Arrays;

/**
 * 方法请求参数的包装类
 */
public class MethodParameter {

    private final Object[] parameters;

    /**
     * @param parameters 方法请求参数
     */
    public MethodParameter(Object[] parameters) {
        this.parameters = parameters;
    }

    /**
     * @param parameters 方法请求参数
     * @return 返回一个新的实例
     */
    public static MethodParameter of(Object[] parameters) {
        return new MethodParameter(parameters);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(parameters);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodParameter that = (MethodParameter) o;
        return Arrays.equals(parameters, that.parameters);
    }

    @Override
    public String toString() {
        return Arrays.toString(parameters);
    }
}
