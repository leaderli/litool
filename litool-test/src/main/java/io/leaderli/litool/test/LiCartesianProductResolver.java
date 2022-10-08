package io.leaderli.litool.test;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

class LiCartesianProductResolver implements ParameterResolver {

    private final Object[] parameters;

    LiCartesianProductResolver(Object[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {

        return true;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameters[parameterContext.getIndex()];
    }
}
