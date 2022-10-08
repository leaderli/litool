package io.leaderli.litool.test;

import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import org.junit.jupiter.api.BeforeAll;

import java.lang.reflect.Method;

import static io.leaderli.litool.core.util.ConsoleUtil.print;


//@ExtendWith(ContainerExtension.class)
public class LiT1Test {

    @BeforeAll
    static void beforeAll() {
        ByteBuddyAgent.install();
    }
//
//    @BeforeEach
//    void before() {
//
//        print("before", new Source().hello("world"));
//        new ByteBuddy()
//                .rebase(Source.class)
//                .visit(Advice.to(MockMethodAdvice.class).on(target -> target.isMethod() && !target.isConstructor()))
//                .make()
//                .load(Source.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
//
//    }

//    @AfterEach
//    void after() {
//        new ByteBuddy()
//                .rebase(Source.class)
//                .make()
//                .load(Source.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
//
//        print("after", new Source().hello("world"));
//    }

    @LiTest
    void test() {
//        System.out.println(source);

//        Systemm.out.println(source.hello("w1"));
//        System.out.println(source.hello2("w2"))
        print("in", new Source().hello("world"));
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
            return "fuck";
        }

        public String hello2(String name) {
            return null;
        }

        public String hello3(String name) {
            return null;
        }
    }


}
