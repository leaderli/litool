package io.leaderli.litool.runner.xml;

import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.executor.ElementExecutor;
import io.leaderli.litool.runner.executor.MainElementExecutor;

/**
 * @author leaderli
 * @since 2022/7/24
 */

public class MainElement implements SaxBean, ElementExecutor<MainElementExecutor> {

    private String id;
    private RequestElement request;
    private ResponseElement response;

    public RequestElement getRequest() {
        return request;
    }

    public void setRequest(RequestElement request) {
        this.request = request;
    }

    public ResponseElement getResponse() {
        return response;
    }

    public void setResponse(ResponseElement response) {
        this.response = response;
    }

    @Override
    public MainElementExecutor executor() {
        return new MainElementExecutor(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
