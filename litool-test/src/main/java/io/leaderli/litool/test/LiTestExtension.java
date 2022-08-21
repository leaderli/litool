package io.leaderli.litool.test;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.test.CartesianMethod;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.platform.commons.util.AnnotationUtils.isAnnotated;

/**
 * @author leaderli
 * @since 2022/8/18
 */
public class LiTestExtension implements TestTemplateInvocationContextProvider {


    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        if (!context.getTestMethod().isPresent()) {
            return false;
        }

        Method testMethod = context.getTestMethod().get();
        return isAnnotated(testMethod, LiTest.class);
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext extensionContext) {


        Method templateMethod = extensionContext.getRequiredTestMethod();
        String displayName = extensionContext.getDisplayName();

        List<TestTemplateInvocationContext> list = new ArrayList<>();

        Lira<Object[]> cartesian = new CartesianMethod(templateMethod, null).cartesian();
        for (Object[] parameters : cartesian) {
            list.add(new MyTestTemplateInvocationContext(parameters));
        }

        return list.stream();
    }

}

class MyTestTemplateInvocationContext implements TestTemplateInvocationContext {
    private final Object[] parameters;

    MyTestTemplateInvocationContext(Object... parameters) {
        this.parameters = parameters;
    }

    @Override
    public String getDisplayName(int invocationIndex) {
        return "li:" + Arrays.toString(parameters);
    }

    @Override
    public List<Extension> getAdditionalExtensions() {
        return Collections.singletonList(new MyCartesianProductResolver(parameters));
    }
}

class MyCartesianProductResolver implements ParameterResolver {

    private final Object[] parameters;

    MyCartesianProductResolver(Object[] parameters) {
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
