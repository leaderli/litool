package io.leaderli.litool.runner.instruct;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BetweenTimeInstructTest {

    @Test
    void test1() {
        BetweenTimeInstruct betweenTimeInstruct = new BetweenTimeInstruct();
        assertTrue(betweenTimeInstruct.invoke("0700", "2000", "1200"));
        assertFalse(betweenTimeInstruct.invoke("0700", "2000", "2200"));
        assertTrue(betweenTimeInstruct.invoke("2000", "0700", "0500"));
        assertFalse(betweenTimeInstruct.invoke("2000", "0700", "1200"));

    }

}