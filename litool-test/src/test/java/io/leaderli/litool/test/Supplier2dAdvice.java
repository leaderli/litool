package io.leaderli.litool.test;

import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;

public class Supplier2dAdvice {
    @Advice.OnMethodEnter
    public static Object enter(
//                @Advice.This Object mock,   // 静态方法无this
            @Advice.Origin Method origin,
            @Advice.AllArguments Object[] arguments) {

        return 100;
    }
}
