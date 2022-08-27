package io.leaderli.litool.runner;

/**
 * @author leaderli
 * @since 2022/8/15
 */
public class ContextInfo {
public static final String INFO = "info";


private long elapse = -1;

public long getElapse() {
    return elapse;
}

public void setElapse(long elapse) {
    this.elapse = elapse;
}

@Override
public String toString() {
    return "InfoResponse{" +
            "elapse=" + elapse +
            '}';
}
}
