package io.leaderli.litool.test;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.core.type.PrimitiveEnum;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

class LiTestTemplateInvocationContext implements TestTemplateInvocationContext {
    private final Object[] parameters;
    private final Class<?>[] mockClasses;
    private final Map<Method, Object> methodValue;
    private final ByteBuddy byteBuddy = new ByteBuddy();

    LiTestTemplateInvocationContext(Object[] parameters, Class<?>[] mockClasses, Map<Method, Object> methodValue) {
        this.parameters = parameters;
        this.mockClasses = mockClasses;
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

            for (Class<?> mockClass : mockClasses) {
                byteBuddy.redefine(mockClass)
                        .visit(Advice.to(TemplateInvocationMockMethodAdvice.class).on(target ->
                                Lira.of(methodValue.keySet()).filter(target::represents).present()
                        ))

                        .make()
                        .load(mockClass.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
            }
            LiTestAssert.reset();
            TemplateInvocationMockMethodAdvice.methodValue = methodValue;
        };

        // reset mockClass to origin after test executed
        AfterTestExecutionCallback afterTestExecutionCallback = context -> {
            for (Class<?> mockClass : mockClasses) {
                byteBuddy.redefine(mockClass)
                        .make()
                        .load(mockClass.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

            }

        };
        LiCartesianParameterResolver liCartesianParameterResolver = new LiCartesianParameterResolver(parameters);

        return Arrays.asList(beforeTestExecutionCallback, afterTestExecutionCallback, liCartesianParameterResolver);
    }

    public static class TemplateInvocationMockMethodAdvice {


        public static Map<Method, Object> methodValue;

        @SuppressWarnings("all")
        @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
        public static Object enter(@Advice.Origin Method origin, @Advice.AllArguments Object[] args) {

            Object value = methodValue.get(origin);
            if (origin.getReturnType() == void.class) {
                return LiMock.NONE;
            }
            if (value == null && origin.getReturnType().isPrimitive()) {
                return PrimitiveEnum.get(origin.getReturnType()).zero_value;
            }
            return value;
        }

        @SuppressWarnings("all")
        @Advice.OnMethodExit
        public static void exit(
                @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object _return,
                @Advice.Enter Object mock,
                @Advice.Origin Method origin,
                @Advice.AllArguments Object[] args) {

            _return = mock;
        }
    }

}
