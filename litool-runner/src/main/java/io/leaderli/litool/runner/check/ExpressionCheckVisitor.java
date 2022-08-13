package io.leaderli.litool.runner.check;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.TempNameEnum;
import io.leaderli.litool.runner.constant.VariablesModel;
import io.leaderli.litool.runner.xml.EntryElement;
import io.leaderli.litool.runner.xml.funcs.FuncElement;

/**
 * @author leaderli
 * @since 2022/8/13 3:04 PM
 */
public class ExpressionCheckVisitor extends ElementCheckVisitor {


    public void visit(Expression expression, SaxBean saxBean) {

        VariablesModel model = expression.getModel();
        String name = expression.getName();
        String id = saxBean.id();
        id = Lino.of(id).filter(StringUtils::isNotBlank).map(i -> " id:" + i).get("");
        switch (model) {
            case FUNC:
                Lino<FuncElement> find_func = mainElement().getFuncs().getFuncList().lira().first(func -> StringUtils.equals(name, func.getName()));
                addErrorMsgs(find_func.present(), String.format("func [%s] not exists%s", name, id));
                break;
            case REQUEST:
                Lino<EntryElement> find_request = mainElement().getRequest().entryList.lira().first(entry -> StringUtils.equals(name, entry.getKey()));
                addErrorMsgs(find_request.present(), String.format("request variable [%s] not exists%s", name, id));
                break;
            case RESPONSE:
                Lino<EntryElement> find_response = mainElement().getResponse().entryList.lira().first(entry -> StringUtils.equals(name, entry.getKey()));
                addErrorMsgs(find_response.present(), String.format("response variable [%s] not exists%s", name, id));
                break;
            case TEMP:
                addErrorMsgs(Lira.of(TempNameEnum.values()).map(TempNameEnum::name).contains(name), String.format("temp variable [%s] not exists%s", name, id));
                break;
            case ERROR:
                addErrorMsgs(false, "expression is error " + id);
                break;
            default:
                break;
        }
    }
}
