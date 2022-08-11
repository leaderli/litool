package io.leaderli.litool.runner.executor;

import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.xml.MainElement;

/**
 * @author leaderli
 * @since 2022/8/9 4:48 PM
 */
public class MainElementExecutor extends BaseElementExecutor<MainElement> {
    final RequestElementExecutor request;
    final ResponseElementExecutor response;

    public MainElementExecutor(MainElement mainElement) {
        super(mainElement);
        this.request = mainElement.getRequest().executor();
        this.response = mainElement.getResponse().executor();

    }

    @Override
    public void visit(Context context) {

        request.visit(context);
        response.visit(context);
    }
}
