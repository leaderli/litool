package io.leaderli.litool.core.proxy;

import java.lang.reflect.Method;

public class DynamicDelegation {

    protected Method origin;

    public void setOrigin(Method origin) {
        this.origin = origin;
    }
}
