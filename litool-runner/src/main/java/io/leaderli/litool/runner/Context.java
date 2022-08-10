package io.leaderli.litool.runner;

import io.leaderli.litool.core.collection.ImmutableMap;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/8/8
 */
public class Context {
    /**
     * 用于存储原始请求报文，待转换原始请求报文后，作为返回报文
     */
    public final Map<String, Object> origin_request_or_response = new HashMap<>();
    /**
     * 用于存储所有func的计算过程
     * TODO 最终校验哪些func的结果可以被缓存
     */
    public final Map<String, Function<Context, Object>> func_result = new HashMap<>();
    private ImmutableMap<String, Object> readonly_request;
    /**
     * 存储临时变量使用，每个临时变量都有一个唯一的名称，其类型是固定的，临时变量在使用前必须先初始化，即临时变量一定有默认值。
     * TODO
     */
    private Map<String, Object> temp = new HashMap<>();

    public Context(Map<String, String> origin_request) {
        this.origin_request_or_response.putAll(origin_request);
    }

    public void visit(ContextVisitor contextVisitor) {
        contextVisitor.visit(this);
    }


    @SuppressWarnings("unchecked")
    public <T> T getRequest(String key) {
        return (T) this.readonly_request.get(key);
    }

    public void setResponse(String key, Object value) {
        this.origin_request_or_response.put(key, value);
    }


    @SuppressWarnings("unchecked")
    public <T> T getResponse(String key) {
        return (T) this.origin_request_or_response.get(key);
    }

    public void setReadonly_request(ImmutableMap<String, Object> readonly_request) {
        this.readonly_request = readonly_request;
    }

    public void setFuncResultMap(Map<String, Function<Context, Object>> func_result_map) {
        this.func_result.putAll(func_result_map);
    }

    public void setTemp(String key, Object value) {
        this.temp.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getTemp(String key) {
        return (T) temp.get(key);
    }

    public Object getExpressionValue(Expression expression) {
        return expression.apply(this);
    }
}
