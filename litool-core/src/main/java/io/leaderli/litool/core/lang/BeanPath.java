package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.collection.IterableItr;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StringConvert;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.core.type.ReflectUtil;

import java.util.*;
import java.util.function.Function;

/**
 * An xpath-like tool for easy retrieval of bean properties
 *
 * @author leaderli
 * @since 2022/7/11
 */
public class BeanPath {

    private static final short BEGIN = 0;
    private static final short KEY_IN = 1;
    private static final short KEY_END = 2;
    private static final short ARR_BEGIN = 3;
    private static final short ARR_IN = 4;
    private static final short ARR_END = 5;

    private static final char CHAR_ARR_BEGIN = '[';
    private static final char CHAR_ARR_END = ']';
    private static final char CHAR_VARIABLE_SPLIT = '.';


    private final List<Function<Object, Object>> path = new ArrayList<>();
    private final Deque<Function<Lino<Object>, Object>> filters_stack = new ArrayDeque<>();


    /**
     * @param filters the filter use to filter iterable obj before parse'[index]'
     */
    @SafeVarargs
    public BeanPath(Function<Lino<Object>, Object>... filters) {

        Collections.addAll(filters_stack, filters);

    }

    public static Lino<Object> parse(Object obj, String expression) {

        BeanPath beanPath = new BeanPath();
        beanPath.build(expression);
        return beanPath.parse(obj);
    }

    /**
     * @param obj the find obj
     * @param key the key of map or the field name of obj
     * @return the map value or field value
     */
    public static Lino<Object> simple(Object obj, String key) {
        BeanPath beanPath = new BeanPath();
        beanPath.setKeyFunction(key);
        return beanPath.parse(obj);
    }

    /**
     * an valid expression can be as follow
     * <ul>
     *     <li>key1</li>
     *     <li>key1.key2</li>
     *     <li>key1[0]</li>
     *     <li>key1[0].key2</li>
     *     <li>key1[0][0]</li>
     *     <li>[0]</li>
     *     <li>[0]</li>
     *     <li>[0].key</li>
     * </ul>
     * illegal expression can be as follow
     * <ul>
     *     <li>{@code  }</li>
     *     <li>.</li>
     *     <li>key1.</li>
     *     <li>key1..a</li>
     *     <li>[</li>
     *     <li>]</li>
     *     <li>[]</li>
     *     <li>a[</li>
     *     <li>a]</li>
     *     <li>a[]</li>
     *     <li>a[a]</li>
     * </ul>
     *
     * @param expression the xpath-like expression
     */
    private void build(String expression) {

        if (StringUtils.isBlank(expression)) {
            throw new IllegalStateException(" expression is null");
        }


        int state = BEGIN;

        StringBuilder temp = new StringBuilder();
        for (char ch : expression.toCharArray()) {

            switch (state) {
                case BEGIN:

                    if (ch == CHAR_VARIABLE_SPLIT || ch == CHAR_ARR_END) {
                        throw new BeginIllegalStateException();
                    }

                    if (ch == CHAR_ARR_BEGIN) {
                        state = ARR_BEGIN;
                    } else {
                        temp.append(ch);
                        state = KEY_IN;
                    }
                    break;

                case KEY_IN:


                    if (ch == CHAR_VARIABLE_SPLIT) {
                        this.setKeyFunction(temp.toString());
                        temp = new StringBuilder();
                        state = KEY_END;
                    } else if (ch == CHAR_ARR_BEGIN) {
                        this.setKeyFunction(temp.toString());
                        temp = new StringBuilder();
                        state = ARR_BEGIN;
                    } else if (ch == CHAR_ARR_END) {
                        throw new IllegalStateException("key] it's not a valid " +
                                "expression");
                    } else {
                        temp.append(ch);
                    }
                    break;
                case KEY_END:
                    if (ch == CHAR_VARIABLE_SPLIT || ch == CHAR_ARR_BEGIN || ch == CHAR_ARR_END) {
                        throw new KeyEndIllegalStateException();
                    }
                    temp.append(ch);
                    state = KEY_IN;
                    break;
                case ARR_BEGIN:

                    if (ch >= '0' && ch <= '9' || ch == '-') {

                        temp.append(ch);
                        state = ARR_IN;
                    } else {
                        throw new IllegalStateException("[index] only support number");
                    }
                    break;

                case ARR_IN:
                    if (ch >= '0' && ch <= '9') {
                        temp.append(ch);
                    } else if (ch == CHAR_ARR_END) {

                        String number = temp.toString();
                        StringConvert.parser(Integer.class, number)
                                .ifPresent(this::setArrFunction)
                                .assertNotNone("[index] only support number:" + number);

                        temp = new StringBuilder();
                        state = ARR_END;

                    } else {
                        throw new IllegalStateException("[index] only support number");
                    }
                    break;
                case ARR_END:
                    if (ch == CHAR_VARIABLE_SPLIT) {
                        state = KEY_IN;
                    } else if (ch == CHAR_ARR_BEGIN) {
                        state = ARR_BEGIN;
                    } else {

                        throw new ArrayEndIllegalStateException();

                    }
                    break;
                default:
                    throw new UnsupportedOperationException(String.format("state " +
                            "%d not support", state));

            }


        }

        if (state == KEY_IN && temp.length() > 0) {

            setKeyFunction(temp.toString());
            state = BEGIN;
        }

        if (state != BEGIN && state != ARR_END) {
            throw new NotCompleteIllegalStateException();
        }
    }

    /**
     * @param obj 数据源
     * @return 根据 {@link #path} 找到的数据
     */
    public Lino<Object> parse(Object obj) {

        for (Function<Object, Object> function : Lira.of(path)) {

            if (obj == null) {
                return Lino.none();
            }
            obj = function.apply(obj);

        }

        return Lino.of(obj);
    }

    private void setKeyFunction(String key) {

        path.add(obj -> {
            if (obj instanceof Map) {

                return ((Map<?, ?>) obj).get(key);
            }
            return ReflectUtil.getFieldValue(obj, key).get();
        });
    }

    private void setArrFunction(int index) {
        if (filters_stack.isEmpty()) {

            path.add(obj -> Lira.of(IterableItr.of(obj)).get(index).get());
        } else {
            Function<Lino<Object>, Object> pop = filters_stack.pop();
            path.add(obj -> Lira.of(IterableItr.of(obj))
                    .filter(v -> pop.apply(Lino.of(v)))
                    .get(index)
                    .get());
        }
    }

    @SafeVarargs
    public static Lino<Object> parse(Object obj, String expression, Function<Lino<Object>, Object>... filters) {

        BeanPath beanPath = new BeanPath(filters);
        beanPath.build(expression);
        return beanPath.parse(obj);
    }


    public static class BeginIllegalStateException extends IllegalStateException {

        public BeginIllegalStateException() {
            super("expression cannot start with  '.','[',']' ");
        }
    }

    public static class KeyEndIllegalStateException extends IllegalStateException {

        public KeyEndIllegalStateException() {
            super("expression after . cannot union with  '.','[',']' ");
        }
    }

    public static class ArrayEndIllegalStateException extends IllegalStateException {

        public ArrayEndIllegalStateException() {
            super("expression after ] only can union with  '.','[' ");
        }
    }

    public static class NotCompleteIllegalStateException extends IllegalStateException {

        public NotCompleteIllegalStateException() {
            super("expression is not complete");
        }
    }
}
