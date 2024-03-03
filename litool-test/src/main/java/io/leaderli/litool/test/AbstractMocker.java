package io.leaderli.litool.test;

import io.leaderli.litool.core.collection.ArrayEqual;
import io.leaderli.litool.core.meta.Either;
import io.leaderli.litool.core.type.PrimitiveEnum;

@SuppressWarnings({"rawtypes"})
public abstract class AbstractMocker<T> extends MethodValueRecorder<T> {
    protected final boolean detach;
    protected boolean build;

    public AbstractMocker(Class<?> mockClass, boolean detach) {
        super(mockClass);
        this.detach = detach;
        // 仅在build过程中生效，用于记录方法的调用
        LiMock.mock(mockClass, m -> true, (method, args) -> {
            if (build) {
                return Either.none();
            }
            currentMethod = method;
            currentArgs = ArrayEqual.of(args);
            System.out.println(currentMethod + "  currentMethod  " + currentArgs);
            methodValueMap.put(currentMethod, new MethodValue(currentMethod));
            System.out.println(methodValueMap);
            return PrimitiveEnum.get(method.getReturnType()).zero_value;
        }, false);
    }


}
