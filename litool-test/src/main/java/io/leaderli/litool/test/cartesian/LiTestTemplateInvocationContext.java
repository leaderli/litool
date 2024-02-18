package io.leaderli.litool.test.cartesian;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StringUtils;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

class LiTestTemplateInvocationContext implements TestTemplateInvocationContext {
    private final Object[] parameters;
    private final Class<?>[] mockingClasses;
    private final Map<Method, Object> methodValue;
    private final ByteBuddy byteBuddy = new ByteBuddy();

    LiTestTemplateInvocationContext(Object[] parameters, Class<?>[] mockingClasses, Map<Method, Object> methodValue) {
        this.parameters = parameters;
        this.mockingClasses = mockingClasses;
        this.methodValue = methodValue;
    }

    @Override
    public String getDisplayName(int invocationIndex) {


        String paramsString = Lira.of(parameters).map(StringUtils::obj2String).toString();
        Map<String, Object> nameValue = Lira.of(methodValue.entrySet()).toMap(e -> e.getKey().getName(), e -> StringUtils.obj2String(e.getValue()));
        return "li:" + invocationIndex + " " + paramsString + " " + nameValue;

    }

    /**
     * @return the extension that used in test execution
     * @see ExtendWith
     */
    @SuppressWarnings("java:S2696")
    @Override
    public List<Extension> getAdditionalExtensions() {

        //redefine mockClass mock method returnValue
        BeforeTestExecutionCallback beforeTestExecutionCallback = context -> {
            for (Class<?> mockClass : mockingClasses) {

                byteBuddy.redefine(mockClass)
                        .visit(Advice.to(ConstructorAdvice.class).on(MethodDescription::isConstructor))
                        .visit(Advice.to(TemplateInvocationMockMethodAdvice.class).on(target ->
                                Lira.of(methodValue.keySet()).filter(target::represents).present()
                        ))

                        .make()
                        .load(mockClass.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
            }
            LiTestAssert.reset();
            TemplateInvocationMockMethodAdvice.METHOD_VALUE = methodValue;
        };

        // reset mockClass to origin after test executed
        AfterTestExecutionCallback afterTestExecutionCallback = context -> {
            for (Class<?> mockClass : mockingClasses) {
                byteBuddy.redefine(mockClass)
                        .make()
                        .load(mockClass.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

            }

        };
        LiCartesianParameterResolver liCartesianParameterResolver = new LiCartesianParameterResolver(parameters);

        return Arrays.asList(beforeTestExecutionCallback, afterTestExecutionCallback, liCartesianParameterResolver);
    }

}
