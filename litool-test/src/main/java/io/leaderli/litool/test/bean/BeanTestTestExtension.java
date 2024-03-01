package io.leaderli.litool.test.bean;

import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.core.type.TypeUtil;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class BeanTestTestExtension implements TestTemplateInvocationContextProvider {

    List<Class<?>> bean = new ArrayList<>();

    /**
     * @param context junit context
     * @return whether is a valid support
     */
    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        if (!context.getTestMethod().isPresent()) {
            return false;
        }
//        Lino.optional(context.getTestMethod())
//                .map(testMethod -> testMethod.getAnnotation(BeanTest.class))
//                .map(BeanTest::value)
//                .filter(StringUtils::isNotEmpty)
//                .or(context.getRequiredTestClass().getName())
//                .map(_package -> new ClassScanner(this.getClass().getPackage().getName()).scan())
//                .toLira(Class.class)
//                .filter(ReflectUtil::newInstance)
//                .forEach(c -> bean.add(c));
        return !bean.isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        return Lira.of(bean)
                .map(c -> LiTuple.of(c, ReflectUtil.newInstance(c).get()))
                .map(tuple ->
                        Lino.of(tuple._2)
                                .map(Object::getClass)
                                .filter(c -> {
                                    if (c == Object.class || c.isArray()) {
                                        return false;
                                    }
                                    return c == TypeUtil.erase(tuple._1);
                                })
                                .mapIgnoreError(cls -> Introspector.getBeanInfo(cls, Object.class).getPropertyDescriptors())
                                .toLira(PropertyDescriptor.class)
                                .map(p -> new MyTestTemplateInvocationContext(tuple._2, p))
                                .get())
                .flatMap(l -> (Iterator<TestTemplateInvocationContext>) l)
                .stream();
    }

    private static class MyTestTemplateInvocationContext implements TestTemplateInvocationContext {
        private final PropertyDescriptor propertyDescriptor;
        private final Object instance;

        private MyTestTemplateInvocationContext(Object instance, PropertyDescriptor propertyDescriptor) {
            this.instance = instance;
            this.propertyDescriptor = propertyDescriptor;
        }
    }
}
