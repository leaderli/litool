package io.leaderli.litool.test.bean;

import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.function.GetSet;
import io.leaderli.litool.core.type.ClassScanner;
import io.leaderli.litool.core.type.ReflectUtil;
import org.junit.jupiter.api.extension.*;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.Method;
import java.util.Arrays;
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

        // 忽略枚举类
        Set<Class<?>> scan = new ClassScanner(scanPackage, cls -> {
            if (cls.isEnum()) {
                return false;
            }
            // 无 get set 方法的直接跳过
            try {
                return Introspector.getBeanInfo(cls, Object.class).getPropertyDescriptors().length > 0;
            } catch (IntrospectionException e) {
                return false;
            }


        }).scan();

        return
                scan.parallelStream()
                        .filter(c -> c != Object.class)
                        .map(BeanTestTestExtension::getMyTestTemplateInvocationContexts)
                        .flatMap(array -> array != null ? Arrays.stream(array) : null);

    }

    @SuppressWarnings("unchecked")
    private static MyTestTemplateInvocationContext[] getMyTestTemplateInvocationContexts(Class<?> clazz) {
        Object instance = ReflectUtil.newInstance(clazz).get();
        if (instance == null) {
            return null;
        }
        try {
            return ArrayUtils.map(
                    Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors(),
                    MyTestTemplateInvocationContext.class,
                    p -> new MyTestTemplateInvocationContext(GetSet.propertyDescriptor(instance, p))
            );
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
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
