package io.leaderli.litool.runner.check;

import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.Expression;

/**
 * @author leaderli
 * @since 2022/8/13 2:41 PM
 */
public abstract class ElementCheckVisitor implements CheckVisitor {


    public final void visit(Object obj, SaxBean saxBean) {
        if (obj instanceof Expression) {
            visit((Expression) obj, saxBean);
        }
    }

    public void visit(Expression expression, SaxBean saxBean) {

    }
}
