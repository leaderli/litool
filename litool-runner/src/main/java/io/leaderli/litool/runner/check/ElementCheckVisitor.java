package io.leaderli.litool.runner.check;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.MethodUtil;
import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.dom.sax.SaxList;

/**
 * @author leaderli
 * @since 2022/8/13 2:41 PM
 */
public abstract class ElementCheckVisitor<T extends SaxBean> extends CheckVisitor {

protected final T element;

protected ElementCheckVisitor(T element) {
    this.element = element;
}

@Override
public void visit(CheckVisitor visitor) {
    visit(element, visitor, true);
}

public final void visit() {
    super.check(this);
}

protected void visit(SaxBean saxBean, CheckVisitor visitor, boolean deep) {

    Lira<?> lira = ReflectUtil.getMethods(saxBean.getClass())
            .filter(m -> m.getName().startsWith("get"))

            .filter(MethodUtil::notObjectMethod)
            .filter(m -> !ClassUtil.isPrimitiveOrWrapper(m.getReturnType()))
            .map(m -> ReflectUtil.getMethodValue(m, saxBean).get());

    for (Object obj : lira) {
        visitor.check(obj, saxBean);
        if (deep) {
            if (obj instanceof SaxBean) {
                visit((SaxBean) obj, visitor, true);
            } else if (obj instanceof SaxList) {

                for (SaxBean sax : ((SaxList<?>) obj).lira()) {
                    visitor.check(sax, saxBean);
                    this.visit(sax, visitor, true);
                }
            }
        }
    }
}


}
