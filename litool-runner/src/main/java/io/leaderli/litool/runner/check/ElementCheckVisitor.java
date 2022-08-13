package io.leaderli.litool.runner.check;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.TempNameEnum;
import io.leaderli.litool.runner.constant.VariablesModel;
import io.leaderli.litool.runner.xml.EntryElement;
import io.leaderli.litool.runner.xml.MainElement;
import io.leaderli.litool.runner.xml.funcs.FuncElement;

/**
 * @author leaderli
 * @since 2022/8/13 2:41 PM
 */
public abstract class ElementCheckVisitor implements CheckVisitor {


    public final void visit(Object obj, SaxBean saxBean) {
        if (obj instanceof Expression) {
            visit((Expression) obj, saxBean);
        }
    }

    public void visit(Expression expression, SaxBean saxBean) {

    }
}
