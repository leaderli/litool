package io.leaderli.litool.test.bean;

import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.function.GetSet;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ClassScanner;
import io.leaderli.litool.core.type.ReflectUtil;
import org.junit.jupiter.api.extension.*;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class BeanTestTestExtension implements TestTemplateInvocationContextProvider {


    /**
     * @param context junit context
     * @return whether is a valid support
     */
    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        Method requiredTestMethod = context.getRequiredTestMethod();
        if (requiredTestMethod.getParameterTypes().length == 1 && requiredTestMethod.getParameterTypes()[0] == GetSet.class) {
            return true;
        }
        throw new IllegalArgumentException("only support test parameterType (GetSet)");
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {

        String scanPackage = context.getRequiredTestClass().getPackage().getName();

        Set<Class<?>> scan = new ClassScanner(scanPackage).scan();

        return Lira.of(scan)
                .filter(c -> c != Object.class)
                .mapIgnoreError(BeanTestTestExtension::getMyTestTemplateInvocationContexts)
                .<TestTemplateInvocationContext>flatMap()
                .stream();

    }

    @SuppressWarnings("unchecked")
    private static MyTestTemplateInvocationContext[] getMyTestTemplateInvocationContexts(Class<?> clazz) throws IntrospectionException {
        Object instance = ReflectUtil.newInstance(clazz).get();
        if (instance == null) {
            return null;
        }
        return ArrayUtils.map(
                Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors(),
                MyTestTemplateInvocationContext.class,
                p -> new MyTestTemplateInvocationContext(GetSet.propertyDescriptor(instance, p))
        );
    }


    private static class MyTestTemplateInvocationContext implements TestTemplateInvocationContext {

        private final GetSet<Object> getSet;

        private MyTestTemplateInvocationContext(GetSet<Object> getSet) {
            this.getSet = getSet;
        }


        @Override
        public String getDisplayName(int invocationIndex) {
            return getSet.toString();
        }

        @Override
        public List<Extension> getAdditionalExtensions() {

            return Collections.singletonList(new ParameterResolver() {
                @Override
                public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
                    return true;
                }

                @Override
                public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
                    return getSet;
                }
            });
        }
    }
}
