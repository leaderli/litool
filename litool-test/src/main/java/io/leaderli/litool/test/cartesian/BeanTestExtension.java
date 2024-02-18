package io.leaderli.litool.test.cartesian;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.core.type.ClassScanner;
import io.leaderli.litool.core.type.ReflectUtil;
import org.junit.jupiter.api.extension.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.platform.commons.util.AnnotationUtils.isAnnotated;

/**
 * @author leaderli
 * @since 2022/8/18
 */
public class BeanTestExtension implements TestTemplateInvocationContextProvider {


    /**
     * @param context junit context
     * @return whether is a valid support
     */
    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        if (!context.getTestMethod().isPresent()) {
            return false;
        }

        return context.getTestMethod()
                .map(testMethod -> isAnnotated(testMethod, BeanTest.class))
                .isPresent();
    }


    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {


        String packageName = Lino.optional(context.getTestMethod())
                .map(m -> m.getAnnotation(BeanTest.class).value())
                .filter(StringUtils::isNotBlank)
                .get(() -> Lino.optional(context.getTestClass()).map(t -> t.getPackage().getName()).get());

        Lira<TestTemplateInvocationContext> tests = Lira.of(new ClassScanner(packageName).scan())
                .unzip(ReflectUtil::newInstance)
                .map(BeanTestTemplateInvocationContext::new);

        return tests.stream();


    }

    private static class BeanTestTemplateInvocationContext implements TestTemplateInvocationContext {

        private final Object obj;

        private BeanTestTemplateInvocationContext(Object obj) {
            this.obj = obj;
        }

        @Override
        public String getDisplayName(int invocationIndex) {


            return "li:" + obj.getClass().getSimpleName();

        }

        @Override
        public List<Extension> getAdditionalExtensions() {
            return Collections.singletonList(new BeanParameterContext(obj));

        }
    }

    private static class BeanParameterContext implements ParameterResolver {
        private final Object obj;

        public BeanParameterContext(Object obj) {
            this.obj = obj;
        }

        @Override
        public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
            return true;
        }

        @Override
        public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
            return obj;
        }
    }

}

