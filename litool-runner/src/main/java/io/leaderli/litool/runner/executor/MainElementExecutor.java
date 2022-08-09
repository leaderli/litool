package io.leaderli.litool.runner.executor;

import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.xml.MainElement;

/**
 * @author leaderli
 * @since 2022/8/9 4:48 PM
 */
public class MainElementExecutor extends BaseElementExecutor<MainElement> {

    public MainElementExecutor(MainElement mainElement) {
        super(mainElement);
    }


    @Override
    public void visit(Context context) {

        element.getRequest().executor().visit(context);
        element.getResponse().executor().visit(context);
    }
}
