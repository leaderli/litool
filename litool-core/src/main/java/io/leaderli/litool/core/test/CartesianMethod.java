package io.leaderli.litool.core.test;

import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.meta.Lira;

import java.lang.reflect.Method;

/**
 * @author leaderli
 * @since 2022/8/17 7:16 PM
 */
public class CartesianMethod {

    public final Method method;
    public final CartesianContext context;

    public CartesianMethod(Method method, CartesianContext context) {
        this.method = method;
        this.context = context;
    }

    /**
     * @return 所有成员变量有区分度的取值集合的笛卡尔集组成的所有实例
     */

    public Lira<Object[]> cartesian() {

        if (method == null) {
            return Lira.none();
        }

        Object[][] objects = Lira.of(method.getParameters())
                .map(parameter -> CartesianUtil.cartesian(parameter, context))
                .toArray(Object[].class);

        Object[][] cartesian = CollectionUtils.cartesian(objects);

        return Lira.of(cartesian);

    }


}
