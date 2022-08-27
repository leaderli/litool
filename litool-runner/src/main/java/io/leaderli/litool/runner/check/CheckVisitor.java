package io.leaderli.litool.runner.check;

import io.leaderli.litool.core.type.MethodScanner;
import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.dom.sax.SaxBean;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/8/13 4:28 PM
 */
public abstract class CheckVisitor extends VisitorAdapter {

private final Map<Class<?>, Method> specific_check;

protected CheckVisitor() {
    MethodScanner scanner = MethodScanner.of(getClass(), false, method ->
            "check".equals(method.getName())
                    && method.getReturnType() == void.class
                    && method.getParameterTypes().length == 2
                    && method.getParameterTypes()[1] == SaxBean.class
                    && method.getParameterTypes()[0] != Object.class);
    specific_check = scanner.scan().toMap(m -> m.getParameterTypes()[0],
            m -> m);

}

public final void check(CheckVisitor visitor) {

    visitor.setMainElement(mainElement);
    visitor.setParseErrorMsgs(this.parseErrorMsgs);
    visitor.init();
    visit(visitor);
}

/**
 * 在设置了 {@link #mainElement} 和 {@link #parseErrorMsgs} 后的初始化动作
 *
 * @see ExpressionCheckVisitor
 */
protected void init() {


}

protected void visit(CheckVisitor visitor) {

}

public final void check(Object obj, SaxBean saxBean) {

    // 调用于obj类型相同的校验器进行校验
    Method method = specific_check.get(obj.getClass());
    ReflectUtil.getMethodValue(method, this, obj, saxBean);
}

}
