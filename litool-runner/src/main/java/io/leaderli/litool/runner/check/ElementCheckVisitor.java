package io.leaderli.litool.runner.check;

import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.xml.MainElement;
import io.leaderli.litool.runner.xml.router.task.CoordinateElement;

import java.util.List;

/**
 * @author leaderli
 * @since 2022/8/13 2:41 PM
 */
public abstract class ElementCheckVisitor extends CheckVisitorAdapter{


    public final void visit(Object obj, SaxBean saxBean) {
        if (obj instanceof Expression) {
            visit((Expression) obj, saxBean);
        } else if (obj instanceof CoordinateElement) {
            visit((CoordinateElement) obj, saxBean);
        }

    }

    public void visit(Expression expression, SaxBean saxBean) {

    }

    public void visit(CoordinateElement coordinate, SaxBean saxBean) {

    }
}
