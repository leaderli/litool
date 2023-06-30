package io.leaderli.litool.core.test;

import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ModifierUtil;
import io.leaderli.litool.core.type.ReflectUtil;

import java.lang.reflect.Field;
import java.util.function.Function;

/**
 * @param <T> 类型
 * @author leaderli
 * @since 2022/8/17 7:16 PM
 */
public class CartesianObject<T> {

    private final Class<T> cls;
    private final T instance;
    private final Function<Field, Object[]> fieldValueProvider;


    /**
     * @param cls                类
     * @param fieldValueProvider 类的属性的可能的值提供函数
     */
    public CartesianObject(Class<T> cls, Function<Field, Object[]> fieldValueProvider) {
        this.cls = cls;
        this.instance = ReflectUtil.newInstance(cls).get();
        this.fieldValueProvider = fieldValueProvider;
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
                .filter(f -> !ModifierUtil.isFinal(f))
                .filter(f -> {
                    Object[] apply = fieldValueProvider.apply(f);
                    return apply != null && apply.length > 0;
                });


        if (fields.absent()) {
            return Lira.of(instance);
        }
        Object[][] fieldsValues = fields
                .map(fieldValueProvider)
                .toArray(Object[].class);

        Object[][] objectsValues = CollectionUtils.cartesian(fieldsValues);

        return Lira.of(objectsValues).map(fieldsValue -> {

            T obj = ReflectUtil.newInstance(cls).get();

            int i = 0;
            for (Field field : fields) {

                ReflectUtil.setFieldValue(obj, field, fieldsValue[i++]);
            }
            return obj;
        });
    }


}
