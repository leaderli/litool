package io.leaderli.litool.runner.check;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.text.StrSubstitution;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.LongExpression;
import io.leaderli.litool.runner.constant.VariablesModel;
import io.leaderli.litool.runner.util.ExpressionUtil;
import io.leaderli.litool.runner.xml.router.task.CoordinateElement;
import io.leaderli.litool.runner.xml.router.task.IfElement;

/**
 * @author leaderli
 * @since 2022/8/13 3:04 PM
 */
public class ExpressionCheckVisitor extends CheckVisitor {


    private final ModelCheckVisitor modelCheckVisitor;

    public ExpressionCheckVisitor(ModelCheckVisitor modelCheckVisitor) {
        this.modelCheckVisitor = modelCheckVisitor;
    }

//    @Override
//    public void visit(CheckVisitor visitor) {
//        super.visit(visitor);
////        modelCheckVisitor.setMainElement(this.mainElement);
////        modelCheckVisitor.setParseErrorMsgs(this.parseErrorMsgs);
//    }



    @Override
    public void init() {
        modelCheckVisitor.setMainElement(this.mainElement);
        modelCheckVisitor.setParseErrorMsgs(this.parseErrorMsgs);
    }

    @Override
    public void visit(Expression expression, SaxBean saxBean) {


        VariablesModel model = expression.getModel();
        String name = expression.getName();
        String id = saxBean.id();
        id = Lino.of(id).filter(StringUtils::isNotBlank).map(i -> " id:" + i).get("");
        switch (model) {
            case FUNC:
                modelCheckVisitor.func(name, id);
                break;
            case REQUEST:
                modelCheckVisitor.request(name, id);
                break;
            case RESPONSE:
                modelCheckVisitor.response(name, id);
                break;
            case TEMP:
                modelCheckVisitor.temp(name, id);
                break;
            case ERROR:
                modelCheckVisitor.error(name, id);
                break;
            default:
                break;
        }
    }

    @Override
    public void visit(IfElement ifElement, SaxBean saxBean) {
        Expression cond = ifElement.getCond();
        visit(cond, saxBean);
    }
    @Override
    public void visit(LongExpression longExpression, SaxBean saxBean) {

        // 依次对占位符进行校验
        StrSubstitution.replace(longExpression.getExpr(), expr -> {
            visit(ExpressionUtil.getExpression(expr), saxBean);
            return null;
        });
    }


    @Override
    public void visit(CoordinateElement coordinate, SaxBean saxBean) {
        CoordinateElementCheckVisitor coordinateElementCheckVisitor = new CoordinateElementCheckVisitor(coordinate);
        coordinateElementCheckVisitor.setMainElement(mainElement);
        coordinateElementCheckVisitor.setParseErrorMsgs(parseErrorMsgs);
        coordinateElementCheckVisitor.visit();
    }
}
