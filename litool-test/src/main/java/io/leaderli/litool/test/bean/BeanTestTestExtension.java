package io.leaderli.litool.test.bean;

import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.exception.RuntimeExceptionTransfer;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.core.type.ClassScanner;
import io.leaderli.litool.core.type.ModifierUtil;
import io.leaderli.litool.core.type.PrimitiveEnum;
import io.leaderli.litool.core.type.ReflectUtil;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
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
        throw new IllegalArgumentException("only support test with parameterType (BeanMethod)");
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {

        BeanTest beanTest = context.getRequiredTestMethod().getAnnotation(BeanTest.class);
        String skipRegex = beanTest.value();
        boolean allowInit = beanTest.allowInit();
        String scanPackage = StringUtils.isEmpty(beanTest.scan()) ? context.getRequiredTestClass().getPackage().getName() : beanTest.scan();

        // 忽略枚举类
        Predicate<Class<?>> classPredicate = cls -> {
            if (cls.isEnum()) {
                return false;
            }
            // 类名的一部分满足正则，则跳过
            return cls.getName().equals(cls.getName().replaceAll(skipRegex, ""));

        };

        return new ClassScanner(scanPackage, classPredicate)
                .scan()
                .parallelStream()
                .flatMap(cls -> flatTestTemplateStream(cls, scanPackage, allowInit, ReflectUtil.newInstance(cls).get()));


    }

    private static Stream<MyTestTemplateInvocationContext> flatTestTemplateStream(Class<?> cls, String scanPackage, boolean allowInit, Object instance) {

        Method[] methods;
        try {
            methods = BeanMethodUtil.scanSimpleMethod(cls, scanPackage, allowInit);
        } catch (Exception ignore) {
            return Stream.empty();
        }
        return Stream
                .of(methods)
                .map(method -> {
                    if (instance != null || ModifierUtil.isStatic(method)) {
                        Object[] args = ArrayUtils.map(method.getParameterTypes(), Object.class, c -> PrimitiveEnum.get(c).zero_value);
                        Supplier<Object> testSupplier = () -> PrimitiveEnum.get(method.getReturnType()).read(RuntimeExceptionTransfer.get(() -> {
                            method.setAccessible(true);
                            return method.invoke(instance, args);
                        }));
                        BeanMethod beanMethod = new BeanMethod(instance, method, testSupplier);
                        return new MyTestTemplateInvocationContext(beanMethod);
                    }
                    return null;
                })
                .filter(Objects::nonNull);
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
