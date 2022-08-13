package io.leaderli.litool.runner.check;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.sax.SaxEventHandler;
import io.leaderli.litool.runner.TempNameEnum;
import io.leaderli.litool.runner.xml.EntryElement;
import io.leaderli.litool.runner.xml.funcs.FuncElement;

/**
 * @author leaderli
 * @since 2022/8/13 3:12 PM
 */
public class ModelCheckVisitor extends CheckVisitorAdapter {


    public ModelCheckVisitor() {
    }





    public void func(String name, String id) {
        Lino<FuncElement> find_func = mainElement.getFuncs().getFuncList().lira().first(func -> StringUtils.equals(name, func.getName()));
        addErrorMsgs(find_func.present(), String.format("func [%s] not exists%s", name, id));
    }

    public void request(String name, String id) {
        Lino<EntryElement> find_request = mainElement.getRequest().entryList.lira().first(entry -> StringUtils.equals(name, entry.getKey()));
        addErrorMsgs(find_request.present(), String.format("request variable [%s] not exists%s", name, id));
    }

    public void response(String name, String id) {
        Lino<EntryElement> find_response = mainElement.getResponse().entryList.lira().first(entry -> StringUtils.equals(name, entry.getKey()));
        addErrorMsgs(find_response.present(), String.format("response variable [%s] not exists%s", name, id));
    }

    public void temp(String name, String id) {
        addErrorMsgs(Lira.of(TempNameEnum.values()).map(TempNameEnum::name).contains(name), String.format("temp variable [%s] not exists%s", name, id));
    }

    public void error(String name, String id) {
        addErrorMsgs(false, "expression is error " + id);
    }
}
