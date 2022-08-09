package io.leaderli.litool.runner;

import io.leaderli.litool.core.collection.ImmutableMap;
import io.leaderli.litool.runner.xml.EntryElement;
import io.leaderli.litool.runner.xml.MainElement;
import io.leaderli.litool.runner.xml.ResponseElement;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/8/8
 */
public class Context {
    /**
     * 用于存储原始请求报文，待转换原始请求报文后，作为返回报文
     */
    public final Map<String, Object> origin_request_or_response = new HashMap<>();
    private ImmutableMap<String, Object> readonly_request;

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
}
