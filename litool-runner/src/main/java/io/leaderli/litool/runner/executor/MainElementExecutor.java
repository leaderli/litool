package io.leaderli.litool.runner.executor;

import io.leaderli.litool.core.collection.ImmutableList;
import io.leaderli.litool.runner.ContextVisitor;
import io.leaderli.litool.runner.executor.funcs.FuncsElementExecutor;
import io.leaderli.litool.runner.xml.MainElement;

import java.util.List;

/**
 * @author leaderli
 * @since 2022/8/9 4:48 PM
 */
public class MainElementExecutor extends BaseElementExecutor<MainElement> {
//    final ContextVisitor request;
//    final ContextVisitor response;
//    final ContextVisitor funcs;
//    final ContextVisitor router;

    final ImmutableList<ContextVisitor> contextVisitors;

    public MainElementExecutor(MainElement mainElement) {
        super(mainElement);
        ContextVisitor request = mainElement.getRequest().executor();
        ContextVisitor response = mainElement.getResponse().executor();
        FuncsElementExecutor funcs = mainElement.getFuncs().executor();
        ContextVisitor router = mainElement.getRouter().executor();
        contextVisitors = ImmutableList.of(request, response, funcs, router);

    }


    @Override
    public List<ContextVisitor> visit() {
        return contextVisitors.copy();
    }
}
