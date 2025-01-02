package io.leaderli.litool.test;

import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.test.bean.BeanCreator;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Field;

public class LiTestInstanceFactory implements TestInstanceFactory, ParameterResolver, BeforeEachCallback {
    private BeanCreator.MockBeanBuilder<Void> beanBuilder = BeanCreator.create(Void.class);

    private void resetBeanBuilder(Object testInstance) {
        if (testInstance instanceof MockBeanBuilderConfig) {
            ((MockBeanBuilderConfig) testInstance).init(beanBuilder);
            this.beanBuilder = BeanCreator.create(Void.class);
        }
    }

    @Override
    public Object createTestInstance(TestInstanceFactoryContext factoryContext, ExtensionContext extensionContext) throws TestInstantiationException {
        Class<?> testClass = factoryContext.getTestClass();
        Object testInstance = ReflectUtil.newInstance(testClass).get();
        resetBeanBuilder(testInstance);
        for (Field field : ReflectUtil.getFields(testClass)) {
            setMockField(testInstance, field);
        }

        return testInstance;
    }

    public void setMockField(Object instance, Field field) {

        if (field.isAnnotationPresent(Mock.class)) {
            BeanCreator<?> build = beanBuilder.type(field.getType()).build();
            ReflectUtil.setFieldValue(instance, field, build.create());
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return true;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return beanBuilder.type(parameterContext.getParameter().getType()).build().create();
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        context.getTestInstance().ifPresent(this::resetBeanBuilder);
    }
}
