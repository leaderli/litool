package io.leaderli.litool.runner;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.function.Filter;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ClassScanner;
import io.leaderli.litool.core.type.ModifierUtil;
import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.runner.instruct.Instruct;
import io.leaderli.litool.runner.xml.funcs.FuncElement;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public class InstructContainer {

    private static final Map<String, Method> ALIAS_INSTRUCT;

    static {
        ALIAS_INSTRUCT = scanner();
    }

    /**
     * 基础方法类需要满足仅有一个方法，且方法的参数和返回值都要是解析器支持的类型，且方法为静态公共方法
     *
     * @return 所有符合条件的基础方法类
     * @see TypeAlias#support(Class)
     */
    public static Map<String, Method> scanner() {
        Filter<Class<?>> classFilter = cls -> {
            boolean annotationPresent = cls.isAnnotationPresent(Instruct.class);
            if (annotationPresent) {

                Lira<Method> methods = ReflectUtil.getMethods(cls).filter(f -> f.getDeclaringClass() == cls);
                Method method = methods.first().get();

                String funcName = cls.getSimpleName();
                LiAssertUtil.assertTrue(methods.size() == 1, String.format("instruct [%s] can only have single method ", funcName));
                LiAssertUtil.assertTrue(ModifierUtil.isPublic(method) && ModifierUtil.isStatic(method), String.format("instruct [%s] should be static and public  ", funcName));
                LiAssertUtil.assertTrue(TypeAlias.support(method.getReturnType()), String.format("instruct %s returnType is unsupported", funcName));

                for (int i = 0; i < method.getParameterTypes().length; i++) {


                    Class<?> parameterType = method.getParameterTypes()[i];
                    Class<?> temp = parameterType;
                    if (temp.isArray()) {
                        // 仅允许最后一位为数组参数
                        LiAssertUtil.assertTrue(i == method.getParameterTypes().length - 1, String.format("instruct [%s] arr parameterType is only support on the last: %s", funcName, Arrays.toString(method.getParameterTypes())));
                        temp = temp.getComponentType();

                    }
                    LiAssertUtil.assertTrue(TypeAlias.ALIAS.containsValue(temp), String.format("instruct [%s] parameterType [%s] is unsupported", funcName, parameterType));

                }
                return true;
            }
            return false;

        };

        ClassScanner classScanner = new ClassScanner(Instruct.class.getPackage().getName(), classFilter);

        return Lira.of(classScanner.scan())
                .toMap(
                        cls -> cls.getAnnotation(Instruct.class).value(),
                        cls -> cls.getMethods()[0]
                );
    }

    public static Method getInnerMethodByAlias(String alias) {
        return ALIAS_INSTRUCT.get(alias);
    }

}
