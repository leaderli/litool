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
    public void visit0(CheckVisitor visitor) {
        visit0(element, visitor);
    }

    public final void visit() {
        super.visit(this);
    }

    protected void visit0(SaxBean saxBean, CheckVisitor visitor) {

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
