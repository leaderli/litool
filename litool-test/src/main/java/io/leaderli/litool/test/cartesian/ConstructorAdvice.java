package io.leaderli.litool.test.cartesian;

import io.leaderli.litool.core.type.*;
import io.leaderli.litool.test.MockBean;
import net.bytebuddy.asm.Advice;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;

/**
 * delegate class constructor, the field that is interface and the value is null will be set a proxy by {@link  Proxy#newProxyInstance(ClassLoader, Class[], InvocationHandler)}
 *
 * @author leaderli
 * @since 2022/10/28 5:17 PM
 */
public class ConstructorAdvice {
    public static <T> MockBean<T> instance(Type type) {

        LinkedHashMap<Type, InstanceCreator<?>> head = new LinkedHashMap<>();
        head.put(MockMap.class, t -> new MockMap<>());
        head.put(MockList.class, t -> new MockList<>());
        return MockBean.<T>create(type).head(head).build();
    }

    @Advice.OnMethodExit
    public static void exit(@Advice.This Object _this) {

        CartesianMock.mockedClasses.add(_this.getClass());

        for (Field field : ReflectUtil.getFields(_this.getClass())) {

            if (field.getType().isInterface() && ReflectUtil.getFieldValue(_this, field).absent()) {

                Object o = mockInterface(field.getGenericType(), field.getType());
                ReflectUtil.setFieldValue(_this, field, o);
            }
        }
    }

    /**
     * @param context the context
     * @param type    the type of interface
     * @param <T>     generic type of interface
     * @return use {@link Proxy#newProxyInstance(ClassLoader, Class[], InvocationHandler)} to generate a new instance
     * of the interface
     * @see TypeUtil#resolve(Type, Type)
     */
    @SuppressWarnings("unchecked")
    public static <T> T mockInterface(Type context, Class<T> type) {

        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, (proxy, origin, args) -> {
            if (MethodUtil.notObjectMethod(origin)) {


                if (CartesianMock.onMockProgress) {
                    // use for record mocking method call
                    CartesianMock.mockMethod = origin;
                    return null;
                }
                Object value = TemplateInvocationMockMethodAdvice.METHOD_VALUE.get(origin);
                Class<?> returnType = origin.getReturnType();
                if (value == CartesianMock.SKIP) {

                    if (returnType == void.class) {
                        return null;
                    } else {
                        Type originReturnType = TypeUtil.resolve(context, origin.getGenericReturnType());
                        return instance(originReturnType).create();
                    }
                } else {

                    PrimitiveEnum primitiveEnum = PrimitiveEnum.get(returnType);
                    if (value == null && primitiveEnum != PrimitiveEnum.OBJECT) {
                        value = primitiveEnum.zero_value;
                    }
                    return value;
                }

            }
            return origin.invoke(CartesianMock.NONE, args);
        });
    }
}
