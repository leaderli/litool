package io.leaderli.litool.test.cartesian;

import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ModifierUtil;
import io.leaderli.litool.core.type.ReflectUtil;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.platform.commons.util.AnnotationUtils.isAnnotated;

/**
 * @author leaderli
 * @since 2022/8/18
 */
public class CartesianTestExtension implements TestTemplateInvocationContextProvider {


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
                .map(testMethod -> isAnnotated(testMethod, CartesianTest.class))
                .isPresent();
    }

    private static void addTestTemplateInvocationContext(List<TestTemplateInvocationContext> tests, Class<?>[] mockingClasses, Lira<Method> mockingMethods, Object[] parameters, Object[][] mockingMethodReturnCartesian) {
        if (mockingMethodReturnCartesian.length == 0) {
            tests.add(new CartesianTestTemplateInvocationContext(parameters, mockingClasses, new HashMap<>()));
        } else {

            for (Object[] scenario : mockingMethodReturnCartesian) {

                Map<Method, Object> methodReturn = CollectionUtils.tuple(mockingMethods.toArray(Method.class), scenario).toMap(l -> l);
                tests.add(new CartesianTestTemplateInvocationContext(parameters, mockingClasses, methodReturn));
            }
        }
    }

    private static void setUpMock(Method templateMethod) {
        ReflectUtil.getAnnotation(templateMethod, MockInit.class)
                .map(MockInit::value)
                .unzip(name -> ReflectUtil.getMethod(templateMethod.getDeclaringClass(), name))
                .ifPresent(staticInitMethod -> {
                    LiAssertUtil.assertTrue(ModifierUtil.isStatic(staticInitMethod) && staticInitMethod.getParameterTypes().length == 0, "must be static method without parameter");
                    ReflectUtil.invokeMethod(staticInitMethod, null);
                });
        for (Class<?> redefine : CartesianMock.redefineClassesInMockInit) {
            CartesianMock.byteBuddy.redefine(redefine).make()
                    .load(redefine.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

        }
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext extensionContext) {


        CartesianMock.reset();

        Method templateMethod = extensionContext.getRequiredTestMethod();
        List<TestTemplateInvocationContext> tests = new ArrayList<>();
        CartesianContext cartesianContext = new CartesianContext();

        setUpCartesianContext(templateMethod, cartesianContext);
        setUpMock(templateMethod);

        Lira<Object[]> parameterCartesian = new CartesianMethodParameters(templateMethod, cartesianContext).cartesian();

        Class<?>[] mockingClasses = Lira.of(CartesianMock.mockedClasses).toArray(Class.class);
        Lira<Method> mockingMethods = Lira.of(CartesianMock.methodValuesDependsOnParameters.keySet());

        for (Object[] parameters : parameterCartesian) {

            Object[][] mockingReturns = mockingMethods
                    .map(m -> CartesianMock.methodValuesDependsOnParameters.get(m).catesian())
                    .assertNoError()
                    .toNullableArray(Object[].class);

            Object[][] mockingMethodReturnCartesian = CollectionUtils.cartesian(mockingReturns);

            addTestTemplateInvocationContext(tests, mockingClasses, mockingMethods, parameters, mockingMethodReturnCartesian);
        }

        CartesianMock.reset();

        return tests.stream();

    }

    private static void setUpCartesianContext(Method templateMethod, CartesianContext cartesianContext) {
        ReflectUtil.getAnnotation(templateMethod, MockContext.class)
                .or(ReflectUtil.getAnnotation(templateMethod.getDeclaringClass(), MockContext.class))
                .map(MockContext::value)
                .unzip(name -> ReflectUtil.getMethod(templateMethod.getDeclaringClass(), name))
                .ifPresent(staticInitMethod -> {
                    LiAssertUtil.assertTrue(ModifierUtil.isStatic(staticInitMethod) && staticInitMethod.getParameterTypes().length == 1, "must be static method with CartesianContext parameter");
                    ReflectUtil.invokeMethod(staticInitMethod, null, cartesianContext);
                });
    }


}

