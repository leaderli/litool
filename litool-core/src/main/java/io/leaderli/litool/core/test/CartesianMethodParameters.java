package io.leaderli.litool.core.test;

import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.meta.Lira;

import java.lang.reflect.Method;

/**
 * @author leaderli
 * @since 2022/8/17 7:16 PM
 */
public class CartesianMethodParameters {

    public final Method method;
    public final CartesianContext context;

    public CartesianMethodParameters(Method method, CartesianContext context) {
        this.method = method;
        this.context = context;
    }

    /**
     * @return the parameters cartesian lira
     */
    public Lira<Object[]> cartesian() {

        if (method == null) {
            return Lira.none();
        }

        if (method.getParameters().length == 0) {
            return Lira.of(new Object[1][0]);
        }
        Lira<Object[]> map = Lira.of(method.getParameters())
                .map(parameter -> CartesianUtil.cartesian(parameter, context));


        Object[][] parametersValues = map.toArray(Object[].class);
        Object[][] cartesian = CollectionUtils.cartesian(parametersValues);

        return Lira.of(cartesian);

    }


}
