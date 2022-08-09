package io.leaderli.litool.runner.func;

import java.util.function.Function;

public interface IFunc extends Function<Object[], String> {

    Class<?>[] support();
}
