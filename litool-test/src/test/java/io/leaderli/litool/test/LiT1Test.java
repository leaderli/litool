package io.leaderli.litool.test;

import io.leaderli.litool.core.test.ObjectValues;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Method;


//@ExtendWith(ContainerExtension.class)
public class LiT1Test {

    @BeforeAll
    static void beforeAll() {
        ByteBuddyAgent.install();
    }

    @BeforeEach
    void before() {

        new ByteBuddy()
                .rebase(Source.class)
                .visit(Advice.to(MockMethodAdvice.class).on(target -> target.isMethod() && !target.isConstructor()))
                .make()
                .load(Source.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

    }

    @AfterEach
    void after() {
        new ByteBuddy()
                .rebase(Source.class)
                .make()
                .load(Source.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
    }

    @LiTest
    void test(@ObjectValues Source source) {
//        System.out.println(source);

//        Systemm.out.println(source.hello("w1"));
//        System.out.println(source.hello2("w2"))
        System.out.println("---------------------------->");
        System.out.println(new Source().hello("world"));
//        System.out.println(new Source().hello2("world"));

    }

    @SuppressWarnings("UnusedAssignment")
    public static class MockMethodAdvice {
        @Advice.OnMethodExit
        public static void enter(
                @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object ret,
                @Advice.Origin Method origin,
                @Advice.AllArguments Object[] arguments) {

            ret = origin.getName();
        }
    }

    public static class Source {
        public String hello(String name) {
            return null;
        }

        public String hello2(String name) {
            return null;
        }

        public String hello3(String name) {
            return null;
        }
    }


}
