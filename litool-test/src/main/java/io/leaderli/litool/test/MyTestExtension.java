package io.leaderli.litool.test;

import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author leaderli
 * @since 2022/8/18
 */
public class MyTestExtension implements TestTemplateInvocationContextProvider {


    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext extensionContext) {


        Method templateMethod = extensionContext.getRequiredTestMethod();
        String displayName = extensionContext.getDisplayName();
        // @formatter:on        return

        List<TestTemplateInvocationContext> list = new ArrayList<>();
        list.add(new MyTestTemplateInvocationContext(1, 1,1));
        list.add(new MyTestTemplateInvocationContext(2, 2,2));
        list.add(new MyTestTemplateInvocationContext(3, 3,3));

        return list.stream();
    }

    private static class MyTestTemplateInvocationContext implements TestTemplateInvocationContext {
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

    private static class MyCartesianProductResolver implements ParameterResolver {

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
            System.out.println("resolve:"+parameters[parameterContext.getIndex()]);
            return parameters[parameterContext.getIndex()];
        }
    }
}
