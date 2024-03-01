package io.leaderli.litool.test;

import io.leaderli.litool.core.function.Filter;

import java.lang.reflect.Method;

public class MethodFilter {

    final Filter<Method> filter;

    public MethodFilter(Filter<Method> filter) {
        this.filter = filter;
    }
}
