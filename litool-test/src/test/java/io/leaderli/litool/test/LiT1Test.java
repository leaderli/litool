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

    @LiTest
    void test() {
        print("in", new Source().hello("world"));
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
