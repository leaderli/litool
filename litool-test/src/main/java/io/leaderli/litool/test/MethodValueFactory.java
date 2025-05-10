package io.leaderli.litool.test;

import io.leaderli.litool.core.meta.Either;
import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.*;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassDefinition;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

import static io.leaderli.litool.test.LiMock.*;

public class MethodValueFactory {

    /**
     * 每个class对应一套挡板map，每个挡板根据每个方法设定一个挡板list，按照顺序依次拦截
     */
    private static final Map<Class<?>, ClassMock> mockClasses = new HashMap<>();
    private static final Map<String, LiTuple<MethodProxy<?>, Method>> invokers = new HashMap<>();
    public static final Map<String, LiTuple<MethodAssert, Method>> recorders = new HashMap<>();


    public static ClassMock computeIfAbsent(Class<?> clazz) {
        return mockClasses.computeIfAbsent(clazz, k -> new ClassMock(clazz));
    }

//    public static List<LiTuple<Method, MethodProxy<?>>> getMethodMockers(Class<?> clazz, String method) {
//        return getClassMockers(clazz).computeIfAbsent(method, k -> new ArrayList<>());
//    }

//    public static void putMethodProxy(Class<?> clazz, Method method, MethodProxy<?> methodProxy, MethodFilter methodFilter) {
//        if (Boolean.TRUE.equals(methodFilter.apply(method))) {
//            getClassMockers(clazz);
//            getMethodMockers(clazz, method + "").add(0, LiTuple.of(method, methodProxy));
//        }
//    }

    public static Either<?, ?> invoke(Class<?> mockClass, String methodSig, Object bean, Object[] args) throws Throwable {
        ClassMock classMock = mockClasses.get(mockClass);
        if (classMock != null) {

            return classMock.invoke(methodSig, bean, args);
        }
        return Either.none();
    }

    /**
     * 用于方法调用的断言
     */
    public static void record(Class<?> mockClass, String methodSig, Object bean, Object[] args, Object originReturn) {
        ClassMock classMock = mockClasses.get(mockClass);
        if (classMock != null) {
            classMock.record(methodSig, bean, args, originReturn);
        }
    }

    /**
     * @param returnValue       返回值
     * @param clazz             方法的声明类
     * @param genericReturnType 方法
     * @return 根据返回类型修正返回值，如果为null，则返回零值，如果类型不匹配，则返回 {@link  Either#none()}。
     * @see PrimitiveEnum#zero_value
     */
    public static Either<Void, Object> adjustReturnValue(Object returnValue, Type clazz, Type genericReturnType) {

        // 不mock
        if (returnValue == LiMock.SKIP_MARK) {
            return Either.none();
        }
        // 尝试将泛型返回类型解析为实际类型
        Class<?> returnType = TypeUtil.erase(TypeUtil.resolve(clazz, genericReturnType));
        if (returnValue == null) {
            return Either.right(PrimitiveEnum.get(returnType).zero_value);
        }
        if (ClassUtil.isInstanceof(returnValue, returnType)) {
            return Either.right(returnValue);
        }
        return Either.none();
    }


    public static void detach(Class<?> clazz) {
        ClassMock classMock = mockClasses.get(clazz);
        if (classMock != null) {
            classMock.detach();
        }
    }

    static class ClassMock {
        public final Class<?> mockClass;

        /**
         * 方法签名和方法的映射
         */
        private final Map<String, Method> methodSignatures;
        /**
         * 表示类所有方法的挡板
         */
        private final Map<String, List<MethodProxy<?>>> methodProxyList;
        private final Map<String, List<MethodAssert>> methodAssertList;

        private ClassMock(Class<?> mockClass) {
            this.mockClass = mockClass;
            Lira<Method> methods = Lira.of(mockClass.getDeclaredMethods()).filter(method -> !(method.isSynthetic() || ModifierUtil.isAbstract(method)));
            methodSignatures = Collections.unmodifiableMap(methods.toMap(m -> m + "", m -> m));
            methodProxyList = Collections.unmodifiableMap(methods.toMap(m -> m + "", m -> new ArrayList<>()));
            methodAssertList = Collections.unmodifiableMap(methods.toMap(m -> m + "", m -> new ArrayList<>()));
            redefine();
        }

        private void redefine() {
            try {
                backup(mockClass);
                CtClass ct = getCtClass(mockClass);

                for (Method method : methodSignatures.values()) {

                    CtMethod ctMethod = getCtMethod(method, ct);

                    String bean = ModifierUtil.isStatic(method) ? "null" : "$0";
                    String src = "Either either = MethodValueFactory.invoke($class,\"" + method + "\"," + bean + ",$args);\r\n"
                            + "if(either.isRight()) return ($r)either.getRight();";
                    if (ctMethod.isEmpty()) { // 接口或者抽象方法
                        continue;
                    }
                    ctMethod.insertBefore(src);
                    String recordCode = "MethodValueFactory.record( $class,\"" + method + "\",\"" + bean + "\",$args,($w)$_);";
                    ctMethod.insertAfter(recordCode);
                    CtClass ctClass = getCtClass(Throwable.class);
                    String catchCode = " MethodValueFactory.record( $class,\"" + method + "\",\"" + bean + "\",$args,$e); throw $e;";
                    ctMethod.addCatch(catchCode, ctClass);
                }
                instrumentation.redefineClasses(new ClassDefinition(mockClass, toBytecode(ct)));
            } catch (Exception e) {
                throw new MockException(e);
            }
        }

        public void detach() {
            this.methodProxyList.values().forEach(List::clear);
            this.methodAssertList.values().forEach(List::clear);
        }

        public void putMethodProxy(MethodFilter methodFilter, MethodProxy<?> methodProxy) {
            methodSignatures.forEach((n, m) -> {
                if (methodFilter.apply(m)) {
                    methodProxyList.get(n).add(0, methodProxy);
                }
            });
        }

        public void putMethodAssert(MethodFilter methodFilter, MethodAssert methodAssert) {
            methodSignatures.forEach((n, m) -> {
                if (methodFilter.apply(m)) {
                    methodAssertList.get(n).add(0, methodAssert);
                }
            });
        }

        public Either<?, ?> invoke(String methodSig, Object bean, Object[] args) throws Throwable {
            Method method = methodSignatures.get(methodSig);
            if (method != null) {

                for (MethodProxy<?> methodProxy : methodProxyList.get(methodSig)) {
                    Object methodProxyValue = methodProxy.apply(bean, method, args);
                    Either<Void, Object> returnValue = MethodValueFactory.adjustReturnValue(methodProxyValue, mockClass, method.getGenericReturnType());
                    if (returnValue.isRight()) {
                        return returnValue;
                    }
                }

            }

            return Either.none();
        }


        public void record(String methodSig, Object bean, Object[] args, Object originReturn) {
            Method method = methodSignatures.get(methodSig);

            if (method != null) {
                methodAssertList.get(methodSig).forEach(methodAssert -> {
                    try {
                        methodAssert.apply(method, bean, args, originReturn);
                    } catch (Throwable throwable) {
                        AbstractRecorder.assertThrow.add(throwable);
                        throw throwable;
                    }
                });

            }
        }

    }

}
