package io.leaderli.litool.runner.util;

import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.MethodUtil;
import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.dom.sax.SaxEventHandler;
import io.leaderli.litool.dom.sax.SaxList;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.TempNameEnum;
import io.leaderli.litool.runner.constant.VariablesModel;
import io.leaderli.litool.runner.xml.EntryElement;
import io.leaderli.litool.runner.xml.MainElement;
import io.leaderli.litool.runner.xml.funcs.FuncElement;

import java.util.List;

public class ExpressionUtil {

    public static final LiTuple2<String, VariablesModel> ERROR = new LiTuple2<>("", VariablesModel.ERROR);
    public static final String REQUEST_PREFIX = "$";
    public static final String RESPONSE_PREFIX = "@";
    public static final String TEMP_PREFIX = "#";
    public static final String FUNC_SUFFIX = "()";

    public static LiTuple2<String, VariablesModel> getExpression(String expr) {
        if (StringUtils.isBlank(expr)) {
            return ERROR;
        }
        if (expr.startsWith(REQUEST_PREFIX)) {
            return new LiTuple2<>(StringUtils.removeStart(expr, REQUEST_PREFIX), VariablesModel.REQUEST);
        }
        if (expr.startsWith(RESPONSE_PREFIX)) {
            return new LiTuple2<>(StringUtils.removeStart(expr, RESPONSE_PREFIX), VariablesModel.RESPONSE);
        }
        if (expr.startsWith(TEMP_PREFIX)) {
            return new LiTuple2<>(StringUtils.removeStart(expr, TEMP_PREFIX), VariablesModel.TEMP);
        }
        if (expr.endsWith(FUNC_SUFFIX)) {
            return new LiTuple2<>(StringUtils.removeEnd(expr, FUNC_SUFFIX), VariablesModel.FUNC);
        }
        return new LiTuple2<>(expr, VariablesModel.LITERAL);
    }

    /**
     * 递归校验所有表达式是否合法
     *
     * @param mainElement    入口
     * @param parseErrorMsgs 错误消息集合
     * @param saxBean        标签类
     */
    public static void checkExpression(MainElement mainElement, List<String> parseErrorMsgs, SaxBean saxBean) {

        Lira<?> lira = ReflectUtil.getMethods(saxBean.getClass())
                .filter(m -> m.getName().startsWith("get"))

                .filter(MethodUtil::notObjectMethod)
                .filter(m -> !ClassUtil.isPrimitiveOrWrapper(m.getReturnType()))
                .map(m -> ReflectUtil.getMethodValue(m, saxBean).get());

        for (Object obj : lira) {

            if (obj instanceof SaxBean) {
                checkExpression(mainElement, parseErrorMsgs, (SaxBean) obj);
            } else if (obj instanceof SaxList) {

                ((SaxList<?>) obj).lira().forEach(sax -> checkExpression(mainElement, parseErrorMsgs, sax));
            } else if (obj instanceof Expression) {

                Expression expression = (Expression) obj;

                VariablesModel model = expression.getModel();
                String name = expression.getName();
                String id = saxBean.id();
                id = Lino.of(id).filter(StringUtils::isNotBlank).map(i -> " id:" + i).get("");
                switch (model) {
                    case FUNC:
                        Lino<FuncElement> find_func = mainElement.getFuncs().getFuncList().lira().first(func -> StringUtils.equals(name, func.getName()));
                        SaxEventHandler.addErrorMsgs(parseErrorMsgs, find_func.present(), String.format("func [%s] not exists%s", name, id));
                        break;
                    case REQUEST:
                        Lino<EntryElement> find_request = mainElement.getRequest().entryList.lira().first(entry -> StringUtils.equals(name, entry.getKey()));
                        SaxEventHandler.addErrorMsgs(parseErrorMsgs, find_request.present(), String.format("request variable [%s] not exists%s", name, id));
                        break;
                    case RESPONSE:
                        Lino<EntryElement> find_response = mainElement.getResponse().entryList.lira().first(entry -> StringUtils.equals(name, entry.getKey()));
                        SaxEventHandler.addErrorMsgs(parseErrorMsgs, find_response.present(), String.format("response variable [%s] not exists%s", name, id));
                        break;
                    case TEMP:
                        SaxEventHandler.addErrorMsgs(parseErrorMsgs, Lira.of(TempNameEnum.values()).map(TempNameEnum::name).contains(name), String.format("temp variable [%s] not exists%s", name, id));
                        break;
                    case ERROR:
                        SaxEventHandler.addErrorMsgs(parseErrorMsgs, false, "expression is error " + id);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
