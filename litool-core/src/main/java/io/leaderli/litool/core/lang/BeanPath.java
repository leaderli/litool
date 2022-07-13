package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.util.LiStringConvert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/11
 */
public class BeanPath {
    // TODO

    private static final int BEGIN = 0;
    private static final int KEY = 1;
    private static final int INDEX = 2;
    private static final char ARR_BEGIN = '[';
    private static final char ARR_END = ']';
    private static final char VARIABLE_SPLIT = '.';


    private final List<Function<Lino<Object>, Lino<Object>>> path = new ArrayList<>();

    public static BeanPath of(String expression) {

        Objects.requireNonNull(expression, " expression is null");

        BeanPath beanPath = new BeanPath();


        int state = BEGIN;

        StringBuilder temp = new StringBuilder();
        for (char ch : expression.toCharArray()) {

            switch (state) {
                case BEGIN:

                    LiAssertUtil.assertFalse(ch == VARIABLE_SPLIT || ch == ARR_END, "'.key'  and   '[index].' and    ']key'  and '[index]]'   is not valid expression");
                    if (ch == ARR_BEGIN) {
                        state = INDEX;
                    } else {
                        temp.append(ch);
                        state = KEY;
                    }

                    break;

                case KEY:


                    if (ch == VARIABLE_SPLIT) {
                        beanPath.setKeyFunction(temp);
                        temp = new StringBuilder();
                        state = BEGIN;
                    } else if (ch == ARR_BEGIN) {
                        beanPath.setKeyFunction(temp);
                        temp = new StringBuilder();
                        state = INDEX;
                    } else if (ch == ARR_END) {
                        throw new IllegalStateException("key] it's not a valid expression");
                    } else {
                        temp.append(ch);
                    }
                    break;

                case INDEX:

                    if (ch >= '0' && ch <= '9') {

                        temp.append(ch);
                    } else if (ch == ARR_END) {

                        int index = LiStringConvert.parser(temp.toString(), -1);

                        beanPath.path.add(m -> m.cast(List.class).toLira(Object.class).get(index));
                        temp = new StringBuilder();

                        state = BEGIN;

                    } else {
                        throw new IllegalStateException("[index] only support number");
                    }
                    break;
                default:
                    throw new UnsupportedOperationException(String.format("state %d not support", state));

            }


        }

        if (state == KEY) {

            beanPath.setKeyFunction(temp);
            state = BEGIN;
        }

        LiAssertUtil.assertTrue(state == BEGIN, "expression is not complete");
        return beanPath;
    }

    private void setKeyFunction(StringBuilder key) {
        path.add(map -> map.cast(Map.class).map(m -> m.get(key.toString())));
    }

    public Lino<Object> parser(Object obj) {
        Lino<Object> of = Lino.of(obj);

        for (Function<Lino<Object>, Lino<Object>> linoLinoFunction : path) {

            of = linoLinoFunction.apply(of);
        }

        return of;
    }
}
