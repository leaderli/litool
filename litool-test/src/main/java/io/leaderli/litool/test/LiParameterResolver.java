package io.leaderli.litool.test;

import io.leaderli.litool.core.type.ReflectUtil;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class LiParameterResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        System.out.println(parameterContext.getIndex() + " " + parameterContext.getParameter() + " " + parameterContext.getTarget().get());
        return true;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return ReflectUtil.newInstance(parameterContext.getParameter().getType()).get();
    }
}
