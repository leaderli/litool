package io.leaderli.litool.core.type;

import java.lang.reflect.Method;

public interface MethodInvoker {
    Object invoke(Method method, Object[] args);
}
