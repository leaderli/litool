package io.leaderli.litool.test;

import net.bytebuddy.agent.ByteBuddyAgent;
import org.junit.jupiter.api.BeforeEach;

class LiTestTest {

    @BeforeEach
    public void before() {
        System.out.println("before----->");
        ByteBuddyAgent.install();
    }

    @LiTest
    public void test(int a) {
        System.out.println("test--------->");
    }


}
