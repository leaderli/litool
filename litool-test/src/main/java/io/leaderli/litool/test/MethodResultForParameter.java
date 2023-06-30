package io.leaderli.litool.test;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据方法参数不同，返回不同的结果,该类用于一个测试案例
 */
public class MethodResultForParameter {

    private final Map<MethodParameter, Object> valueOfParameter = new HashMap<>();
    private final Object defaultResult;

    /**
     * 默认值使用null
     *
     * @see #MethodResultForParameter(Object)
     */
    public MethodResultForParameter() {
        this(null);
    }

    /**
     * @param defaultResult 当指定参数的结果不存在时返回
     */
    public MethodResultForParameter(Object defaultResult) {
        this.defaultResult = defaultResult;
    }


    /**
     * @param key   方法请求参数
     * @param value 方法的返回值
     */
    public void put(Object[] key, Object value) {
        valueOfParameter.put(MethodParameter.of(key), value);
    }

    /**
     * @param key 方法请求参数
     * @return 根据方法请求参数所缓存的方法的返回值，如果找不到则返回默认值{@link  #defaultResult}
     */
    public Object get(Object[] key) {
        return valueOfParameter.getOrDefault(MethodParameter.of(key), defaultResult);
    }

    @Override
    public String toString() {
        return valueOfParameter + "|" + defaultResult;
    }
}
