package io.leaderli.litool.core.test.cartesian;

import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ModifierUtil;
import io.leaderli.litool.core.type.ReflectUtil;

import java.lang.reflect.Field;

/**
 * @author leaderli
 * @since 2022/8/17 7:16 PM
 */
public class Cartesian<T> {

    public final Class<T> cls;
    public final T instance;

    public Cartesian(Class<T> cls) {
        this.cls = cls;
        this.instance = ReflectUtil.newInstance(cls).get();
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
                .map(this::cartesian)
                .toArray(Object[].class);

        for (Field field : fields) {

            System.out.println(ReflectUtil.findAnnotations(field));
            System.out.println(ReflectUtil.findAnnotationsWithMark(field, Valuable.class));

        }
//        System.out.println(objects.length);
//        System.out.println(ArrayUtils.toString(objects));

        Object[][] cartesian = CollectionUtils.cartesian(objects);

//        System.out.println(ArrayUtils.toString(cartesian));
        return Lira.of(cartesian).map(arr -> {

            T obj = ReflectUtil.newInstance(cls).get();

            int i = 0;
            for (Field field : fields) {

                ReflectUtil.setFieldValue(obj, field, arr[i++]);
            }
            return obj;
        });
    }


    private Object[] cartesian(Field field) {

        Class<?> type = field.getType();


        Lino<IntValues> values = ReflectUtil.getAnnotation(field, IntValues.class);
        if (values.absent()) {
            return new Object[]{ReflectUtil.getFieldValue(instance, field)};
        } else {


            values.get().value();
        }

        return null;

    };

    ;


}
