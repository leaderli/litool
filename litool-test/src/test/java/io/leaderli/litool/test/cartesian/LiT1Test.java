package io.leaderli.litool.test.cartesian;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import java.lang.reflect.Method;


//@ExtendWith(ContainerExtension.class)
public class LiT1Test {


    @BeforeAll
    static void beforeAll() {
    }


    @CartesianTest
    void test() {
        Assertions.assertEquals("fuck", new Source().hello("world"));
    }

    @SuppressWarnings("all")
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
