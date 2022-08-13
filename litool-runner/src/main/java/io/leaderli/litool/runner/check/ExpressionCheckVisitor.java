package io.leaderli.litool.runner.check;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.constant.VariablesModel;

/**
 * @author leaderli
 * @since 2022/8/13 3:04 PM
 */
public class ExpressionCheckVisitor extends ElementCheckVisitor{


    private final ModelCheckVisitor modelCheckVisitor;

    public ExpressionCheckVisitor(ModelCheckVisitor modelCheckVisitor) {
        this.modelCheckVisitor = modelCheckVisitor;
    }

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
}
