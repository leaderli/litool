package io.leaderli.litool.test;

import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.test.CartesianContext;
import io.leaderli.litool.core.test.CartesianMethodParameters;
import io.leaderli.litool.core.type.ModifierUtil;
import io.leaderli.litool.core.type.ReflectUtil;
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

        return context.getTestMethod()
                .map(testMethod -> isAnnotated(testMethod, LiTest.class))
                .isPresent();
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext extensionContext) {


        LiMockCartesian.clear();
        Method templateMethod = extensionContext.getRequiredTestMethod();

        List<TestTemplateInvocationContext> list = new ArrayList<>();

        Lira<Object[]> parameterCartesian = new CartesianMethodParameters(templateMethod, new CartesianContext()).cartesian();


        ReflectUtil.getAnnotation(templateMethod, LiMock.class)
                .map(LiMock::value)
                .unzip(name -> ReflectUtil.getMethod(templateMethod.getDeclaringClass(), name))
                .ifPresent(staticInitMethod -> {
                    LiAssertUtil.assertTrue(ModifierUtil.isStatic(staticInitMethod), "must be static method");
                    ReflectUtil.invokeMethod(staticInitMethod, null);
                });


        Object[] classes = ArrayUtils.toArray(LiMockCartesian.mockClass);
        Class<?>[] mockClasses = classes.length == 0 ? new Class[0] : (Class<?>[]) classes;
        Lira<Method> methods = Lira.of(LiMockCartesian.methodValues.keySet());
        Object[][] testScenario = CollectionUtils.cartesian(methods.map(LiMockCartesian.methodValues::get).toArray(Object[].class));

        for (Object[] parameters : parameterCartesian) {
            if (testScenario.length == 0) {

                list.add(new LiTestTemplateInvocationContext(parameters, mockClasses, new HashMap<>()));
            } else {


                for (Object[] scenario : testScenario) {

                    Map<Method, Object> methodValue = CollectionUtils.tuple(methods.toArray(Method.class), scenario).toMap(l -> l);
                    list.add(new LiTestTemplateInvocationContext(parameters, mockClasses, methodValue));
                }
            }
        }

        LiMockCartesian.clear();

        return list.stream();

    }


}

