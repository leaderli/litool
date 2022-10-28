package io.leaderli.litool.test;

import io.leaderli.litool.core.type.ReflectUtil;
import net.bytebuddy.asm.Advice;

import java.lang.reflect.Field;

/**
 * @author leaderli
 * @since 2022/10/28 5:17 PM
 */
public class ConstructorAdvice {

    @Advice.OnMethodExit
    public static void exit(@Advice.This Object _this) {
        LiMock.mockedClasses.add(_this.getClass());
        for (Field field : ReflectUtil.getFields(_this.getClass())) {
            if (field.getType().isInterface() && ReflectUtil.getFieldValue(_this, field).absent()) {

                Object o = LiMock.mockInterface(field.getGenericType(), field.getType());
                ReflectUtil.setFieldValue(_this, field, o);
            }
        }
    }

}
