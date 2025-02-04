package io.leaderli.litool.test;

import io.leaderli.litool.core.type.MethodFilter;
import io.leaderli.litool.core.type.PrimitiveEnum;
import io.leaderli.litool.test.bean.BeanCreator;

public abstract class BaseMockerForBean<T, R> extends BaseMocker<T, R> {
    protected final boolean detach;
    protected boolean build;

    public BaseMockerForBean(Class<T> mockClass, boolean detach) {
        super(mockClass);
        this.detach = detach;
        // 仅在build过程中生效，用于记录方法的调用
        LiMock.mock(mockClass, MethodFilter.isMethod(), (method, args) -> {
            if (build) {
                return LiMock.SKIP_MARK;
            }
            currentMethodValue = methodValueMap.computeIfAbsent(method, MethodValue::new);
            currentMethodValue.args(args);
            return PrimitiveEnum.get(method.getReturnType()).zero_value;
        }, false);
        instance = BeanCreator.mockBean(mockClass);
    }


}
