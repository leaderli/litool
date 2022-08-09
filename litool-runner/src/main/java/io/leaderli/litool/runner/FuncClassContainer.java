package io.leaderli.litool.runner;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.core.type.ClassScanner;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.runner.func.FuncClass;
import io.leaderli.litool.runner.func.IFunc;
import io.leaderli.litool.runner.xml.funcs.FuncElement;
import io.leaderli.litool.runner.xml.funcs.ParamElement;
import io.leaderli.litool.runner.xml.funcs.ParamList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FuncClassContainer {

    private static final Map<String, Method> FUNC_MAP = new HashMap<>();

    static {
        ClassScanner.getSubTypesOf(IFunc.class, IFunc.class).forEach(iFuncClass -> {
            FuncClass annotation = iFuncClass.getAnnotation(FuncClass.class);
            if (annotation == null) {
                throw new IllegalStateException(String.format("%s must have annotation %s", iFuncClass, FuncClass.class));
            }
            String funcClass = annotation.value();

            Method[] methods = iFuncClass.getMethods();
            List<Method> invokes = Arrays.stream(methods)
                    .filter(method -> StringUtils.equals(method.getName(), "invoke"))
                    .collect(Collectors.toList());
            if (invokes.size() != 1) {
                throw new IllegalStateException(String.format("%s must have a invoke method", iFuncClass));
            }
            Method invokeMethod = invokes.get(0);
            if (invokeMethod.getReturnType() != String.class) {
                throw new IllegalStateException(String.format("%s invoke method must return String", iFuncClass));
            }
            Class<?>[] parameterTypes = invokeMethod.getParameterTypes();
            for (Class<?> parameterType : parameterTypes) {
                if (parameterType.isPrimitive()) {
                    throw new IllegalStateException(String.format("%s invoke method can not use basic type", iFuncClass));
                }
            }
            FUNC_MAP.put(funcClass, invokeMethod);
        });
    }

    public static void funcElementCheck(FuncElement funcElement) {
        String name = funcElement.getName();
        String clazz = funcElement.getClazz();
        LiAssertUtil.assertTrue(FUNC_MAP.containsKey(clazz), String.format("the clazz [%s] of func [%s] is unsupported", name, clazz));

        ParamList paramList = funcElement.getParamList();
        LiAssertUtil.assertFalse(paramList.lira().size() == 0, String.format("the paramList of func [%s] is empty", name));

        Method method = FUNC_MAP.get(clazz);
        LiAssertUtil.assertTrue(match(method, paramList), String.format("the paramList of func [%s] is not match clazz [%s]", name, clazz));
    }

    // 检测paramList的各参数类型与method匹配
    private static boolean match(Method method, ParamList paramList) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        // method的参数列表数量与paramList的数量一致
        int length = parameterTypes.length;
        int size = paramList.lira().size();

        int index_parameterTypes = 0, index_paramList = 0;
        Class<?> temp = null;
        while (index_parameterTypes < length && index_paramList < size) {
            Class<?> parameterType = parameterTypes[index_parameterTypes];
            Class<?> paramElementType = TypeAlias.getType(paramList.lira().get(index_paramList).get().getType());
            if (temp != null && temp == paramElementType) {
                index_paramList++;
                continue;
            }
            if (parameterType.isArray()) {
                temp = ClassUtil.getArrayClass(paramElementType);
                continue;
            }
            if (parameterType != paramElementType) {
                return false;
            }
            index_parameterTypes++;
            index_paramList++;
        }

        return true;
    }

    private static Function<Context, String> getFunc(FuncElement funcElement) {

        return context -> {
            // TODO 获取function
            Object[] objects = funcElement.getParamList().lira().map(paramElement -> null).toArray(Object.class);
            return "";
        };
    }

}
