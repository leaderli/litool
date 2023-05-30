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
 * 一个类似于xpath的工具，用于方便地检索bean属性
 *
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
     * @param filters 用于在解析'[index]'之前过滤可迭代对象的过滤器
     */
    @SafeVarargs
    public BeanPath(Function<Lino<Object>, Object>... filters) {

        Collections.addAll(filters_stack, filters);

    }


    /**
     * 该方法用于解析类似于xpath的表达式，将表达式解析为对应的key和数组下标
     * 表达式的合法格式如下：
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
     * 表达式的非法格式如下：
     *
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
     * @param expression 待解析的xpath-like表达式
     * @throws BeginIllegalStateException       表达式以变量分隔符或数组结束符开始时抛出
     * @throws KeyEndIllegalStateException      表达式中key的结束符后跟着变量分隔符或数组开始、结束符时抛出
     * @throws ArrayEndIllegalStateException    表达式中数组结束符后跟着非变量分隔符或数组开始、结束符时抛出
     * @throws NotCompleteIllegalStateException 表达式未解析完时抛出
     * @throws IllegalStateException            表达式中包含非法字符时抛出
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
     * 解析器，根据指定的路径{@link #path}来解析对象bean并返回解析后的结果
     *
     * @param bean 要解析的对象bean
     * @return 经过解析后的结果
     */
    public Lino<Object> parse(Object bean) {

        for (Function<Object, Object> function : Lira.of(path)) {

            if (bean == null) {
                return Lino.none();
            }
            bean = function.apply(bean);

        }

        return Lino.of(bean);
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

    /**
     * @param bean 要查找的对象
     * @param key  map的key或者对象的属性名
     * @return map的值或者对象的属性值
     */
    public static Lino<Object> simple(Object bean, String key) {
        BeanPath beanPath = new BeanPath();
        beanPath.setKeyFunction(key);
        return beanPath.parse(bean);
    }


    /**
     * 解析器，根据指定的路径{@link #path}来解析对象bean并返回解析后的结果
     *
     * @param bean       要解析的对象bean
     * @param expression 表达式
     * @return 经过解析后的结果
     * @see #build(String)
     * @see #parse(Object)
     */
    public static Lino<Object> parse(Object bean, String expression) {

        BeanPath beanPath = new BeanPath();
        beanPath.build(expression);
        return beanPath.parse(bean);
    }

    /**
     * 解析器，根据指定的路径{@link #path}来解析对象bean并返回解析后的结果
     *
     * @param bean       要解析的对象bean
     * @param expression 表达式
     * @param filters    表达式中的 List 类型的过滤器
     * @return 经过解析后的结果
     * @see #build(String)
     * @see #parse(Object)
     * @see #setArrFunction(int)
     */
    @SafeVarargs
    public static Lino<Object> parse(Object bean, String expression, Function<Lino<Object>, Object>... filters) {

        BeanPath beanPath = new BeanPath(filters);
        beanPath.build(expression);
        return beanPath.parse(bean);
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
