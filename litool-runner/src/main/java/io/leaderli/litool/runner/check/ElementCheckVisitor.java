package io.leaderli.litool.runner.check;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.MethodUtil;
import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.dom.sax.SaxList;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.xml.MainElement;
import io.leaderli.litool.runner.xml.router.task.CoordinateElement;

import java.util.List;

/**
 * @author leaderli
 * @since 2022/8/13 2:41 PM
 */
public abstract class ElementCheckVisitor extends CheckVisitorAdapter{


    public void visit(CheckVisitor visitor) {
        visitor.setMainElement(mainElement);
        visitor.setParseErrorMsgs(this.parseErrorMsgs);
        visit0(mainElement, visitor);
    }
    private void visit0(SaxBean saxBean, CheckVisitor visitor) {

        Lira<?> lira = ReflectUtil.getMethods(saxBean.getClass())
                .filter(m -> m.getName().startsWith("get"))

                .filter(MethodUtil::notObjectMethod)
                .filter(m -> !ClassUtil.isPrimitiveOrWrapper(m.getReturnType()))
                .map(m -> ReflectUtil.getMethodValue(m, saxBean).get());

        for (Object obj : lira) {
            visitor.visit(obj, saxBean);
            if (obj instanceof SaxBean) {
                visit0((SaxBean) obj, visitor);
            } else if (obj instanceof SaxList) {
                ((SaxList<?>) obj).lira().forEach(sax -> this.visit0(sax, visitor));
            }
        }
    }
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
