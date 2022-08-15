package io.leaderli.litool.runner.executor;

import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.ContextVisitor;

import java.util.List;

/**
 * @author leaderli
 * @since 2022/8/9 5:02 PM
 */
public abstract class BaseElementExecutor<S extends SaxBean> extends ContextVisitor {
    public final S element;
    private final VisitorStack checkVisitors = new VisitorStack();

    public BaseElementExecutor(S element) {
        this.element = element;
    }

    @Override
    protected void visit(Context context) {

    }

    @Override
    public void visit0(Context context) {
        List<ContextVisitor> contextVisitors = visit();

        this.visit(context);
        for (ContextVisitor contextVisitor : contextVisitors) {

//            System.out.println(contextVisitor);
            if (context.interrupt.allow()) {

                if (!notify(context)) {
//                    contextVisitor.visit0(context);
                    return;
                }
            }

            if (context.interrupt.none()) {
                contextVisitor.visit0(context);
            }
        }
    }

    public boolean notify(Context context) {

        return false;
    }

    public List<ContextVisitor> visit() {
        return CollectionUtils.emptyList();
    }


}
