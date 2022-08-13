package io.leaderli.litool.runner.check;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.MethodUtil;
import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.dom.sax.SaxEventHandler;
import io.leaderli.litool.dom.sax.SaxList;
import io.leaderli.litool.runner.xml.MainElement;

import java.util.List;

/**
 * @author leaderli
 * @since 2022/8/13 2:36 PM
 */
public class MainCheckVisitor extends ElementCheckVisitor {


    public MainCheckVisitor(MainElement mainElement, List<String> parseErrorMsgs) {
        this.mainElement = mainElement;
        this.parseErrorMsgs = parseErrorMsgs;
    }

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



}
