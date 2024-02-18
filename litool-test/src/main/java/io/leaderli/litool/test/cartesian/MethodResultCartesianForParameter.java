package io.leaderli.litool.test.cartesian;

import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.collection.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 该类用于构建多条测试案例中某个方法的根据参数不同，所有返回值的笛卡尔集合情况
 */
public class MethodResultCartesianForParameter {
    private final Object[] defaultResults;

    /**
     * 根据方法参数，通过调用tuple第一个参数的函数，来确定当前tuple是否为参数对应的方法结果。
     */
    private final Map<Object[], Object[]> valuesOfParameter = new LinkedHashMap<>();

    /**
     * 默认值使用null
     *
     * @see #MethodResultCartesianForParameter(Object[])
     */
    public MethodResultCartesianForParameter() {
        this((Object) null);
    }

    /**
     * @param defaultResults 当指定参数的结果不存在时返回
     */
    public MethodResultCartesianForParameter(Object... defaultResults) {
        this.defaultResults = defaultResults;
    }


    /**
     * @param key    方法可能的请求参数
     * @param values 该参数列表可能的返回值
     */
    public void add(Object[] key, Object... values) {
        valuesOfParameter.put(key, values);
    }

    /**
     * @return 返回根据方法参数不同的所有可能可能结果的笛卡尔积。例如
     * 假如方法参数可能存在两种情况
     * <pre>{@code
     *     1 ==> 1,2
     *     2 ==> a,b
     *    那么则会生成四条案例
     *
     *     {[1]=1, [2]=a}
     *     {[1]=1, [2]=b}
     *     {[1]=2, [2]=a}
     *     {[1]=2, [2]=b}
     *     }
     * </pre>
     */
    public Object[] catesian() {

        Object[][] keys = ArrayUtils.toArray(Object[].class, valuesOfParameter.keySet());
        Object[][] values = ArrayUtils.toArray(Object[].class, valuesOfParameter.values());
        List<MethodResultForParameter> result = new ArrayList<>();
        Object[][] cartesian = CollectionUtils.cartesian(values);
        for (Object[] objects : cartesian) {
            for (Object defaultResult : defaultResults) {
                MethodResultForParameter methodResultForParameter = new MethodResultForParameter(defaultResult);
                result.add(methodResultForParameter);
                for (int i = 0; i < keys.length; i++) {
                    methodResultForParameter.put(keys[i], objects[i]);
                }
            }
        }

        if (result.isEmpty()) {

            for (Object defaultResult : defaultResults) {
                MethodResultForParameter methodResultForParameter = new MethodResultForParameter(defaultResult);
                result.add(methodResultForParameter);
            }
        }

        return result.toArray();
    }


}
