package io.leaderli.litool.runner.xml;

import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.SaxBeanVisitor;

/**
 * @author leaderli
 * @since 2022/7/24
 */

public class MainElement implements SaxBeanVisitor {

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
    public void visit(Context context) {

        context.visit(request);
        context.visit(response);

    }
}
