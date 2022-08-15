package io.leaderli.litool.runner.xml;

import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.check.ExpressionCheckVisitor;
import io.leaderli.litool.runner.check.GotoElementCheckVisitor;
import io.leaderli.litool.runner.check.MainCheckVisitor;
import io.leaderli.litool.runner.check.ModelCheckVisitor;
import io.leaderli.litool.runner.executor.ElementExecutor;
import io.leaderli.litool.runner.executor.MainElementExecutor;
import io.leaderli.litool.runner.xml.funcs.FuncsElement;
import io.leaderli.litool.runner.xml.router.RouterElement;

import java.util.List;

/**
 * @author leaderli
 * @since 2022/7/24
 */

public class MainElement implements SaxBean, ElementExecutor<MainElement, MainElementExecutor> {

    private RequestElement request;
    private ResponseElement response;
    private FuncsElement funcs;
    private RouterElement router;

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

    public RouterElement getRouter() {
        return router;
    }

    public void setRouter(RouterElement router) {
        this.router = router;
    }


    @Override
    public void end_check(List<String> parseErrorMsgs) {
        // 递归校验所有表达式是否合法
        MainCheckVisitor mainCheckVisitor = new MainCheckVisitor(this, parseErrorMsgs);

        // 校验表达式
        ExpressionCheckVisitor expressionCheckVisitor = new ExpressionCheckVisitor(new ModelCheckVisitor());
//        expressionCheckVisitor.setMainElement(this);
//        expressionCheckVisitor.setParseErrorMsgs(parseErrorMsgs);
        mainCheckVisitor.visit(expressionCheckVisitor);

        mainCheckVisitor.visit(new GotoElementCheckVisitor());
//        mainCheckVisitor.visit();

    }
}
