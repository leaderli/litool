package io.leaderli.litool.test;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ModifierUtil;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassDefinition;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.leaderli.litool.test.LiMock.*;

public class ClassMock {
    public final Class<?> mockClass;
    /**
     * 表示类是否已经修改过
     */
    public boolean redefine;

    /**
     * 方法签名和方法的映射
     */
    private final Map<String, Method> methodSignature;
    /**
     * 表示类所有方法的挡板
     */
    private final Map<String, List<MethodProxy<?>>> methodProxyList;

    public ClassMock(Class<?> mockClass) {
        this.mockClass = mockClass;
        Lira<Method> methods = Lira.of(mockClass.getDeclaredMethods()).filter(method -> !(method.isSynthetic() || ModifierUtil.isAbstract(method)));
        methodSignature = Collections.unmodifiableMap(methods.toMap(m -> m + "", m -> m));
        methodProxyList = Collections.unmodifiableMap(methods.toMap(m -> m + "", m -> new ArrayList<>()));
        redefine();
    }

    private void redefine() {
        try {
            backup(mockClass);
            CtClass ct = getCtClass(mockClass);

            for (Method method : methodSignature.values()) {

                CtMethod ctMethod = getCtMethod(method, ct);

                String bean = ModifierUtil.isStatic(method) ? "null" : "$0";
                String src = "Either either = MethodValueFactory.invoke($class,\"" + method + "\"," + bean + ",$args);\r\n"
                        + "if(either.isRight()) return ($r)either.getRight();";
                if (ctMethod.isEmpty()) { // 接口或者抽象方法
                    continue;
                }
                ctMethod.insertBefore(src);
            }
            instrumentation.redefineClasses(new ClassDefinition(mockClass, toBytecode(ct)));
        } catch (Throwable e) {
            throw new MockException(e);
        }
    }
}
