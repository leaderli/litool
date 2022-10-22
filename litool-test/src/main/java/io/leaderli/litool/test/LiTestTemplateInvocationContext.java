package io.leaderli.litool.test;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.core.type.PrimitiveEnum;
import io.leaderli.litool.core.type.TypeUtil;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
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

        /**
         * for skip real method call, the return value must not be null. use {@link LiMock#SKIP} to
         * mark the return null. and put it back at {@link #exit(Object, Object, Method, Object[])}
         *
         * @param origin origin method
         * @param args   origin args
         * @param _this  origin  this
         * @return the method return value
         */
        @SuppressWarnings("all")
        @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
        public static Object enter(@Advice.Origin Method origin,
                                   @Advice.AllArguments Object[] args,
                                   @Advice.This(optional = true) Object _this) {

            Object value = methodValue.get(origin);

            Class<?> returnType = origin.getReturnType();
            if (value == LiMock.SKIP) {

                if (returnType == void.class) {
                    value = LiMock.SKIP;
                } else {

                    Type type = origin.getGenericReturnType();
                    if (_this != null) {
                        type = TypeUtil.resolve(_this.getClass(), origin.getGenericReturnType());
                    }
                    value = LiMock.mockBean(type);
                }
            } else {

                PrimitiveEnum primitiveEnum = PrimitiveEnum.get(returnType);
                if (value == null && primitiveEnum != PrimitiveEnum.OBJECT) {
                    value = primitiveEnum.zero_value;
                }
            }
            if (value == null) {
                return LiMock.SKIP;
            }
            return value;
        }

        @SuppressWarnings("all")
        @Advice.OnMethodExit
        public static void exit(
                @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object _return,
                @Advice.Enter(readOnly = false) Object mock,
                @Advice.Origin Method origin,
                @Advice.AllArguments Object[] args) {

            if (mock == LiMock.SKIP) {
                mock = null;
            }
            _return = mock;
        }
    }

}
