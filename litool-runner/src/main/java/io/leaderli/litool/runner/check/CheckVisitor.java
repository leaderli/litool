package io.leaderli.litool.runner.check;

import io.leaderli.litool.core.type.MethodScanner;
import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.LongExpression;
import io.leaderli.litool.runner.xml.router.task.CoordinateElement;
import io.leaderli.litool.runner.xml.router.task.GotoDestination;
import io.leaderli.litool.runner.xml.router.task.IfElement;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/8/13 4:28 PM
 */
public abstract class CheckVisitor extends VisitorAdapter {
    private final Map<? extends Class<?>, Method> overloadCheckMethod;

    CheckVisitor() {

        MethodScanner methodScanner = new MethodScanner(getClass(), false, new Function<Method, Object>() {
            @Override
            public Object apply(Method method) {
                if (!"check".equals(method.getName())) {
                    return false;
                }
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 2) {

                    if (parameterTypes[1] == SaxBean.class) {
                        return parameterTypes[0] != Object.class;
                    }
                }
                return false;
            }
        });

        overloadCheckMethod = methodScanner.scan().toMap(m -> m.getParameterTypes()[0], m -> m);
    }

    public final void check(CheckVisitor visitor) {

        visitor.setMainElement(mainElement);
        visitor.setParseErrorMsgs(this.parseErrorMsgs);
        visitor.init();
        visit(visitor);
    }

    protected void visit(CheckVisitor visitor) {

    }


    public final void check(Object obj, SaxBean saxBean) {

        ReflectUtil.getMethodValue(overloadCheckMethod.get(obj.getClass()), obj, saxBean);
    }



    /**
     * 在设置了 {@link #mainElement} 和 {@link #parseErrorMsgs} 后的初始化动作
     *
     * @see ExpressionCheckVisitor
     */
    protected void init() {

    }

}
