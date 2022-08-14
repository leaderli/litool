package io.leaderli.litool.runner.check;

import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.LongExpression;
import io.leaderli.litool.runner.xml.router.task.CoordinateElement;
import io.leaderli.litool.runner.xml.router.task.GotoDestination;
import io.leaderli.litool.runner.xml.router.task.IfElement;

/**
 * @author leaderli
 * @since 2022/8/13 4:28 PM
 */
public abstract class CheckVisitor extends VisitorAdapter {

    public final void visit(CheckVisitor visitor) {

        visitor.setMainElement(mainElement);
        visitor.setParseErrorMsgs(this.parseErrorMsgs);
        visitor.init();
        visit0(visitor);
    }

    protected void visit0(CheckVisitor visitor) {

    }



    public final void visit(Object obj, SaxBean saxBean) {

        if (obj instanceof Expression) {
            visit((Expression) obj, saxBean);
        } else if (obj instanceof CoordinateElement) {
            visit((CoordinateElement) obj, saxBean);
        } else if (obj instanceof GotoDestination) {
            visit((GotoDestination) obj, saxBean);
        } else if (obj instanceof LongExpression) {
            visit((LongExpression) obj, saxBean);
        } else if (obj instanceof IfElement) {
            visit((IfElement) obj, saxBean);
        }
    }

    public void visit(IfElement ifElement, SaxBean saxBean) {

    }

    public void visit(Expression expression, SaxBean saxBean) {

    }

    public void visit(LongExpression longExpression, SaxBean saxBean) {

    }

    public void visit(CoordinateElement coordinate, SaxBean saxBean) {

    }

    public void visit(GotoDestination gotoDestination, SaxBean saxBean) {


    }

    /**
     * 在设置了 {@link #mainElement} 和 {@link #parseErrorMsgs} 后的初始化动作
     *
     * @see ExpressionCheckVisitor
     */
    protected void init() {


    }

}
