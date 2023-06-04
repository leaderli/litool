package io.leaderli.litool.runner.executor;

import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.ContextVisitor;
import io.leaderli.litool.runner.event.VisitorEvent;

import java.util.List;

/**
 * @author leaderli
 * @since 2022/8/9 5:02 PM
 */
public abstract class BaseElementExecutor<S extends SaxBean> extends ContextVisitor {
    public final S element;

    protected BaseElementExecutor(S element) {
        this.element = element;
    }

    @Override
    public void visit(Context context) {
        List<ContextVisitor> contextVisitors = visit();

        context.publishEvent(new VisitorEvent(element));
        this.execute(context);

        for (ContextVisitor contextVisitor : contextVisitors) {

            if (context.interrupt.any()) {

                if (!notify(context)) {
                    return;
                }
            }

            if (context.interrupt.none()) {
                contextVisitor.visit(context);
            }

        }

        // 一些 executor 没有子 executor，或者是子 executor 最后一个抛出事件
        notify(context);
    }

    public boolean notify(Context context) {

        return false;
    }

    public List<ContextVisitor> visit() {
        return CollectionUtils.emptyList();
    }


}
