package io.leaderli.litool.runner;

import io.leaderli.litool.core.bit.BitState;

/**
 * @author leaderli
 * @since 2022/8/16
 */
public class Interrupt extends BitState {

    public static final int NO = 0;
    public static final int BREAK_LOOP = 1;
    public static final int GOTO = 1 << 1;
    public static final int ERROR = 1 << 2;


}
