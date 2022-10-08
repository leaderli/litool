package io.leaderli.litool.test;

import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.core.meta.Lira;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

class LiTestTemplateInvocationContext implements TestTemplateInvocationContext {
    private final Object[] parameters;
    private final Iterable<Class<?>> mocks;
    private final Map<Method, Object> cache;
    private final ByteBuddy byteBuddy = new ByteBuddy();

    LiTestTemplateInvocationContext(Object[] parameters, Iterable<Class<?>> mocks, Map<Method, Object> cache) {
        this.parameters = parameters;
        this.mocks = mocks;
        this.cache = cache;
    }

    /**
     * @param invocationIndex 执行案例编号
     * @return 执行案例展示名
     */
    @Override
    public String getDisplayName(int invocationIndex) {


        return "li:" + Arrays.toString(parameters) + " " + Lira.of(cache.entrySet())
                .map(LiTuple2::of)
                .map(tu2 -> tu2.map1(Method::getName))
                .toMap(t -> t);

    }

    /**
     * @return 执行案例的插件，可用于执行 {@link org.junit.jupiter.api.BeforeAll} ，填充参数等行为
     */
    @Override
    public List<Extension> getAdditionalExtensions() {
        return Arrays.asList((BeforeTestExecutionCallback) context -> {


            for (Class<?> mock : mocks) {
                byteBuddy.rebase(mock)
                        .visit(Advice.to(MockMethodAdvice.class).on(target ->
                                Lira.of(cache.keySet()).filter(target::represents).present()
                        ))

                        .make()
                        .load(mock.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
            }
            MockMethodAdvice.cache = cache;
        }, (AfterTestExecutionCallback) context -> {
            for (Class<?> mock : mocks) {
                byteBuddy.rebase(mock)
                        .make()
                        .load(mock.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

            }

        }, new LiCartesianProductResolver(parameters));
    }

    public static class MockMethodAdvice {
        public static Map<Method, Object> cache;

        @SuppressWarnings("UnusedAssignment")
        @Advice.OnMethodExit
        public static void enter(
                @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object ret,
                @Advice.Origin Method origin,
                @Advice.AllArguments Object[] arguments) {

            ret = cache.get(origin);
        }
    }

}
