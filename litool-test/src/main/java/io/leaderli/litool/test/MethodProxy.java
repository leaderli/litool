package io.leaderli.litool.test;

import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.ReflectUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MethodProxy<T> {

    MethodProxy<?> NONE = of(null);

    static <T> MethodProxy<T> of(T value) {
        return (m, args) -> value;
    }

    static <T> MethodProxy<T> error(Throwable throwable) {
        return (m, args) -> {
            throw throwable;
        };
    }

    /**
     * @return 根据返回类型生成一个默认值
     */
    static MethodProxy<?> of() {
        return (m, args) -> ReflectUtil.newInstance(m.getReturnType()).assertNotNone().get();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    static MethodProxy<?> quick(Object... items) {
        return (m, args) -> {
            Class<?> returnType = m.getReturnType();
            if (ClassUtil.isAssignableFromOrIsWrapper(Collection.class, returnType) || returnType.isArray()) {
                return Lean.INSTANCE.fromBean(items, returnType);
//                Object o = BeanCreator.create(returnType).build().create();
//                Collection list = (Collection) ((?) o).assertNotNone().get();
//                list.addAll(Arrays.asList(items));
//                return list;
            } else if (ClassUtil.isAssignableFromOrIsWrapper(Map.class, returnType)) {
                Map map = (Map) Lean.INSTANCE.fromBean(null, returnType);
                for (int i = 0; i < items.length - 1; i = i + 2) {
                    map.put(items[i], items[i + 1]);
                }
                return map;
            }
            Object bean = Lean.INSTANCE.fromBean(new Object(), returnType);
            if (bean != null) {
                List<Field> fields = ReflectUtil.getFields(returnType).get();
                for (int i = 0; i < items.length; i++) {
                    ReflectUtil.setFieldValue(bean, fields.get(i), items[i]);
                }
            }
            return bean;
        };
    }

    /**
     * 增加多线程支持，避免相互影响
     */
    static MethodProxy<?> threadLocal(MethodProxy<?> methodProxy) {

        Thread currentThread = Thread.currentThread();
        return (m, args) -> {
            if (Thread.currentThread() == currentThread) {
                return methodProxy.apply(m, args);
            }
            return LiMock.SKIP_MARK;
        };
    }

    T apply(Method method, Object[] args) throws Throwable;

}
