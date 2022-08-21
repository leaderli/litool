package io.leaderli.litool.core.test;

import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ModifierUtil;
import io.leaderli.litool.core.type.ReflectUtil;

import java.lang.reflect.Field;

/**
 * @author leaderli
 * @since 2022/8/17 7:16 PM
 */
public class CartesianObject<T> {

    public final Class<T> cls;
    public final T instance;
    public final CartesianContext context;

    public CartesianObject(Class<T> cls, CartesianContext context) {
        this.cls = cls;
        this.instance = ReflectUtil.newInstance(cls).get();
        this.context = context;
    }

    /**
     * @return 所有成员变量有区分度的取值集合的笛卡尔集组成的所有实例
     */

    public Lira<T> cartesian() {

        if (cls == null || cls.getSuperclass() != Object.class) {
            return Lira.none();
        }


        // 无默认构造器
        if (instance == null) {
            return Lira.none();
        }

        Lira<Field> fields = ReflectUtil.getFields(cls)
                .filter(f -> !ModifierUtil.isFinal(f));

        Object[][] objects = fields
                .map(field -> CartesianUtil.cartesian(field, context))
                .toArray(Object[].class);

        Object[][] cartesian = CollectionUtils.cartesian(objects);

        return Lira.of(cartesian).map(arr -> {

            T obj = ReflectUtil.newInstance(cls).get();

            int i = 0;
            for (Field field : fields) {

                ReflectUtil.setFieldValue(obj, field, arr[i++]);
            }
            return obj;
        });
    }


}
