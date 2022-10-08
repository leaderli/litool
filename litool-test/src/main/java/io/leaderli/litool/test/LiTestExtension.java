package io.leaderli.litool.test;

import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.test.CartesianContext;
import io.leaderli.litool.core.test.CartesianMethodParameters;
import io.leaderli.litool.core.type.ModifierUtil;
import io.leaderli.litool.core.type.ReflectUtil;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
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


        Method templateMethod = extensionContext.getRequiredTestMethod();

        List<TestTemplateInvocationContext> list = new ArrayList<>();

        Lira<Object[]> cartesian = new CartesianMethodParameters(templateMethod, new CartesianContext()).cartesian();


        Lino<Method> limock = ReflectUtil.getAnnotation(templateMethod, LiMock.class)
                .map(LiMock::value)
                .unzip(name -> ReflectUtil.getMethod(templateMethod.getDeclaringClass(), name));
        if (limock.present()) {

            limock.assertTrue(ModifierUtil::isStatic, "must be static method")
                    .ifPresent(m -> ReflectUtil.getMethodValue(m, null));
        }


        // 返回多个 junit 执行案例
        for (Object[] parameters : cartesian) {
            Lira<Method> methods = Lira.of(LiMockCartesian.cache.keySet());

            Object[][] objects = methods.map(LiMockCartesian.cache::get).toArray(Object[].class);
            System.out.println(Arrays.deepToString(objects));
            System.out.println(Arrays.deepToString(CollectionUtils.cartesian(objects)));

            if (objects.length == 0) {
                list.add(new LiTestTemplateInvocationContext(parameters, LiMockCartesian.mockClass, new HashMap<>()));
            } else {

                for (Object[] mapValues : CollectionUtils.cartesian(objects)) {


                    AtomicInteger i = new AtomicInteger();
                    Map<Method, Object> methodObjectMap = methods.toMap(m -> m, m -> mapValues[i.getAndIncrement()]);
                    System.out.println(methodObjectMap);

                    list.add(new LiTestTemplateInvocationContext(parameters, LiMockCartesian.mockClass, methodObjectMap));
                }
            }
        }
//
//        if (list.isEmpty()) {
//            list.add(new MyTestTemplateInvocationContext());
//        }

        return list.stream();

    }


}

