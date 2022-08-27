package io.leaderli.litool.runner;

/**
 * @author leaderli
 * @since 2022/8/16
 */
public class Interrupt {

public static final int NO = 0;
public static final int BREAK_LOOP = 1;
public static final int GOTO = 1 << 1;
public static final int ERROR = 1 << 2;

public static void main(String[] args) {
    System.out.println(GOTO);
}
}
