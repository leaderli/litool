package io.leaderli.litool.test.bean;

import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.type.ClassScanner;
import io.leaderli.litool.core.type.PrimitiveEnum;
import io.leaderli.litool.core.type.ReflectUtil;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class BeanTestTestExtension implements TestTemplateInvocationContextProvider {


    /**
     * @param context junit context
     * @return whether is a valid support
     */
    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        Method requiredTestMethod = context.getRequiredTestMethod();
        if (requiredTestMethod.getParameterTypes().length == 1 && requiredTestMethod.getParameterTypes()[0] == BeanMethod.class) {
            return true;
        }
        throw new IllegalArgumentException("only support test parameterType (BeanMethod)");
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {

        String scanPackage = context.getRequiredTestClass().getPackage().getName();

        // 忽略枚举类
        Set<Class<?>> scan = new ClassScanner(scanPackage, cls -> {
            if (cls.isEnum()) {
                return false;
            }
            // 无简单方法的直接跳过
            return BeanMethodUtil.scanSimpleMethod(cls).length > 0;

        }).scan();

        return
                scan.parallelStream()
                        .filter(c -> c != Object.class)
                        .map(BeanTestTestExtension::getMyTestTemplateInvocationContexts)
                        .filter(Objects::nonNull)
                        .flatMap(Arrays::stream);

    }

    private static MyTestTemplateInvocationContext[] getMyTestTemplateInvocationContexts(Class<?> clazz) {
        Object instance = ReflectUtil.newInstance(clazz).get();
        if (instance == null) {
            return null;
        }
        Function<Method, MyTestTemplateInvocationContext> methodMyTestTemplateInvocationContextFunction = method -> new MyTestTemplateInvocationContext(
                new BeanMethod(
                        instance,
                        method,
                        () -> {
                            Object[] args = ArrayUtils.map(method.getParameterTypes(), Object.class, c -> PrimitiveEnum.get(c).zero_value);
                            return PrimitiveEnum.get(method.getReturnType()).read(ReflectUtil.invokeMethod(method, instance, args).get());
                        }
                ));
        return ArrayUtils.map(BeanMethodUtil.scanSimpleMethod(clazz), MyTestTemplateInvocationContext.class, methodMyTestTemplateInvocationContextFunction);
    }


    private static class MyTestTemplateInvocationContext implements TestTemplateInvocationContext {

        private final BeanMethod beanMethod;

        private MyTestTemplateInvocationContext(BeanMethod beanMethod) {
            this.beanMethod = beanMethod;
        }


        @Override
        public String getDisplayName(int invocationIndex) {
            return beanMethod.toString();
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
                    return beanMethod;
                }
            });
        }
    }
}
