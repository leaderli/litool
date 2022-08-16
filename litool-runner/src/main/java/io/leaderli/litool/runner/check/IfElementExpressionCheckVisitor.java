package io.leaderli.litool.runner.check;

import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.TypeAlias;
import io.leaderli.litool.runner.xml.router.task.IfElement;

public class IfElementExpressionCheckVisitor extends ElementCheckVisitor<IfElement> {


    public IfElementExpressionCheckVisitor(IfElement ifElement) {
        super(ifElement);
    }

    @Override
    public void visit(CheckVisitor visitor) {
        visit(element, visitor, false);
    }

//    @Override
    public void check(Expression expression, SaxBean saxBean) {
        Inner inner = new Inner();
        ExpressionCheckVisitor visitor = new ExpressionCheckVisitor(inner);
        this.check(visitor);
    }

    private static class Inner extends ModelCheckVisitor {


        @Override
        public void request(String name, String id) {
            mainElement.getRequest().entryList.lira()
                    .first(entry -> StringUtils.equals(name, entry.getKey()))
                    .ifPresent(entry -> addErrorMsgs(TypeAlias.getType(entry.getType()) == Boolean.class, String.format("cond expression [%s] only support boolean%s", name, id)));
        }

        @Override
        public void response(String name, String id) {
            throw new UnsupportedOperationException();

        }

        @Override
        public void func(String name, String id) {
            mainElement.getFuncs().funcList.lira()
                    .first(func -> StringUtils.equals(name, func.getName()))
                    .ifPresent(entry -> addErrorMsgs(TypeAlias.getType(entry.getType()) == Boolean.class, String.format("cond expression [%s] only support boolean%s", name, id)));
        }

        @Override
        public void temp(String name, String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void error(String name, String id) {
            throw new UnsupportedOperationException();
        }
    }
}
