package io.leaderli.litool.runner.xml;

import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.executor.ElementExecutor;
import io.leaderli.litool.runner.executor.MainElementExecutor;
import io.leaderli.litool.runner.util.ExpressionUtil;
import io.leaderli.litool.runner.xml.funcs.FuncsElement;

import java.util.List;

/**
 * @author leaderli
 * @since 2022/7/24
 */

public class MainElement implements SaxBean, ElementExecutor<MainElement,MainElementExecutor> {

    private RequestElement request;
    private ResponseElement response;
    private FuncsElement funcs;

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

    public FuncsElement getFuncs() {
        return funcs;
    }

    public void setFuncs(FuncsElement funcs) {
        this.funcs = funcs;
    }

    @Override
    public MainElementExecutor executor() {
        return new MainElementExecutor(this);
    }

    @Override
    public void end_check(List<String> parseErrorMsgs) {
        ExpressionUtil.checkExpression(this, parseErrorMsgs, this);
    }
}
