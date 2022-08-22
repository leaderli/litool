package io.leaderli.litool.test;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.test.CartesianContext;
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


    /**
     * @param context junit 插件上下文
     * @return 是否支持运行 junit
     */
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

        Lira<Object[]> cartesian = new CartesianMethod(templateMethod, new CartesianContext()).cartesian();
        // 返回多个 junit 执行案例
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

    /**
     * @param invocationIndex 执行案例编号
     * @return 执行案例展示名
     */
    @Override
    public String getDisplayName(int invocationIndex) {
        return "li:" + Arrays.toString(parameters);
    }

    /**
     * @return 执行案例的插件，可用于执行 {@link org.junit.jupiter.api.BeforeAll} ，填充参数等行为
     */
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
