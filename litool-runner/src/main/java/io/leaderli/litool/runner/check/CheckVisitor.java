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

    public final void check(CheckVisitor visitor) {

        visitor.setMainElement(mainElement);
        visitor.setParseErrorMsgs(this.parseErrorMsgs);
        visitor.init();
        visit(visitor);
    }

    protected void visit(CheckVisitor visitor) {

    }



    public final void check(Object obj, SaxBean saxBean) {

        if (obj instanceof Expression) {
            check((Expression) obj, saxBean);
        } else if (obj instanceof CoordinateElement) {
            check((CoordinateElement) obj, saxBean);
        } else if (obj instanceof GotoDestination) {
            check((GotoDestination) obj, saxBean);
        } else if (obj instanceof LongExpression) {
            check((LongExpression) obj, saxBean);
        } else if (obj instanceof IfElement) {
            check((IfElement) obj, saxBean);
        }
    }

    public void check(IfElement ifElement, SaxBean saxBean) {

    }

    public void check(Expression expression, SaxBean saxBean) {

    }

    public void check(LongExpression longExpression, SaxBean saxBean) {

    }

    public void check(CoordinateElement coordinate, SaxBean saxBean) {

    }

    public void check(GotoDestination gotoDestination, SaxBean saxBean) {


    }

    /**
     * 在设置了 {@link #mainElement} 和 {@link #parseErrorMsgs} 后的初始化动作
     *
     * @see ExpressionCheckVisitor
     */
    protected void init() {


    }

}
