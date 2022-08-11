package io.leaderli.litool.runner.xml;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.MethodUtil;
import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.dom.sax.EndEvent;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.executor.ElementExecutor;
import io.leaderli.litool.runner.executor.MainElementExecutor;
import io.leaderli.litool.runner.xml.funcs.FuncsElement;

import java.lang.reflect.Method;

/**
 * @author leaderli
 * @since 2022/7/24
 */

public class MainElement implements SaxBean, ElementExecutor<MainElementExecutor> {

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
    public void end(EndEvent endEvent) {
        SaxBean.super.end(endEvent);

        checkExpression(this);
    }

    public void checkExpression(SaxBean saxBean) {

        Lira<Method> lira = ReflectUtil.getMethods(saxBean.getClass())
                .filter(m -> m.getName().startsWith("get"))

                .filter(MethodUtil::notObjectMethod)
                .filter(m -> !ClassUtil.isPrimitiveOrWrapper(m.getReturnType()))
                .map(m->ReflectUtil.getMethodValue(m,saxBean))
                ;

        for (Method method : lira) {
            System.out.println(method.getName());

        }
    }
}
