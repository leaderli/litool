
package io.leaderli.litool.core.meta;


import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * LiTuple是一个由两个元素组成的元组，可以看作是两个组件的笛卡尔积。
 *
 * @param <T1> 第一个元素的类型
 * @param <T2> 第二个元素的类型*
 */
public final class LiTuple<T1, T2> implements Comparable<LiTuple<T1, T2>>, Either<T1, T2>, Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 元组的第一个元素的值
     */
    public final transient T1 _1;
    /**
     * 元组的第二个元素的值
     */
    public final transient T2 _2;


    /**
     * 构造一个包含两个元素的元组。
     *
     * @param t1 第一个元素
     * @param t2 第二个元素
     */
    public LiTuple(T1 t1, T2 t2) {
        this._1 = t1;
        this._2 = t2;
    }

    /**
     * 从 {@link Map.Entry} 创建一个 {@code Tuple2}。
     *
     * @param <T1>  第一个元素的类型（entry 的 key）
     * @param <T2>  第二个元素的类型（entry 的 value）
     * @param entry 要转换的 {@link Map.Entry}
     * @return 包含给定 {@code entry} 的 key 和 value 的新 {@code Tuple2}
     */
    public static <T1, T2> LiTuple<T1, T2> of(Map.Entry<? extends T1, ? extends T2> entry) {
        Objects.requireNonNull(entry, "entry is null");
        return new LiTuple<>(entry.getKey(), entry.getValue());
    }

    /**
     * 将宽泛的 {@code Tuple2<? extends T1, ? extends T2>} 缩小为 {@code Tuple2<T1, T2>}。
     * 这是合法的，因为不可变/只读的元组是协变的。
     *
     * @param t    要缩小类型的 {@code Tuple2}
     * @param <T1> 元组的第一个元素的类型
     * @param <T2> 元组的第二个元素的类型
     * @return 将给定对象实例缩小为类型 {@code Tuple2<T1, T2>}
     */
    @SuppressWarnings("unchecked")
    public static <T1, T2> LiTuple<T1, T2> narrow(LiTuple<? extends T1, ? extends T2> t) {
        return (LiTuple<T1, T2>) t;
    }

    /**
     * 检查元组中是否包含 null 值。
     *
     * @return 如果元组中的所有元素都不为 null，则返回 true；否则返回 false
     */
    public boolean notIncludeNull() {
        return _1 != null && _2 != null;
    }

    /**
     * 调用静态方法LiTuple.compareTo()，比较两个LiTuple对象的大小
     *
     * @param that 另一个LiTuple对象
     * @return 返回比较结果，1代表大于，0代表等于，-1代表小于
     */
    @Override
    public int compareTo(LiTuple<T1, T2> that) {
        return LiTuple.compareTo(this, that);
    }

    @SuppressWarnings("unchecked")
    private static <U1 extends Comparable<? super U1>, U2 extends Comparable<? super U2>> int compareTo(LiTuple<?,
            ?> o1
            , LiTuple<?, ?> o2) {
        final LiTuple<U1, U2> t1 = (LiTuple<U1, U2>) o1;
        final LiTuple<U1, U2> t2 = (LiTuple<U1, U2>) o2;

        final int check1 = t1._1.compareTo(t2._1);
        if (check1 != 0) {
            return check1;
        }

        return t1._2.compareTo(t2._2);

    }

    /**
     * @return 返回元组类的第一个元素。
     */
    public T1 _1() {
        return _1;
    }

    /**
     * @param value 更新后的值
     * @return 返回一个新的元组类对象，其第一个元素为更新后的值。
     */
    public LiTuple<T1, T2> update1(T1 value) {
        return new LiTuple<>(value, _2);
    }

    /**
     * @return 返回元组类的第二个元素。
     */
    public T2 _2() {
        return _2;
    }

    /**
     * @param value 更新后的值
     * @return 返回一个新的元组类对象，其第二个元素为更新后的值。
     */
    public LiTuple<T1, T2> update2(T2 value) {
        return new LiTuple<>(_1, value);
    }

    /**
     * 将元组类转换为Map.Entry类型。
     *
     * @return 返回一个Map.Entry对象，该对象的键为元组类的第一个元素，值为元组类的第二个元素。
     */
    public Map.Entry<T1, T2> toEntry() {
        return new AbstractMap.SimpleEntry<>(_1, _2);
    }

    /**
     * 使用映射函数对元组类进行映射。
     *
     * @param mapper 映射函数
     * @param <U1>   第一个元素的新类型
     * @param <U2>   第二个元素的新类型
     * @return 返回一个新的元组类对象，其第一个元素和第二个元素分别为经过映射函数处理后的值。
     * @throws NullPointerException 如果映射函数为null，则抛出NullPointerException异常。
     */
    public <U1, U2> LiTuple<U1, U2> map(BiFunction<? super T1, ? super T2, LiTuple<U1, U2>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return mapper.apply(_1, _2);
    }

    /**
     * 使用函数对元组的每个组件进行映射。
     *
     * @param f1   第一个组件的映射函数
     * @param f2   第二个组件的映射函数
     * @param <U1> 第一个组件映射后的新类型
     * @param <U2> 第二个组件映射后的新类型
     * @return 返回一个新的元组，其组件分别为经过映射函数处理后的值。
     * @throws NullPointerException 如果映射函数为null，则抛出NullPointerException异常。
     */
    public <U1, U2> LiTuple<U1, U2> map(Function<? super T1, ? extends U1> f1, Function<? super T2, ? extends U2> f2) {
        Objects.requireNonNull(f1, "f1 is null");
        Objects.requireNonNull(f2, "f2 is null");
        return LiTuple.of(f1.apply(_1), f2.apply(_2));
    }

    /**
     * 创建一个包含两个元素的元组。
     *
     * @param <T1> 元组的第一个元素的类型
     * @param <T2> 元组的第二个元素的类型
     * @param t1   元组的第一个元素的值
     * @param t2   元组的第二个元素的值
     * @return 包含两个元素的元组
     */
    public static <T1, T2> LiTuple<T1, T2> of(T1 t1, T2 t2) {
        return new LiTuple<>(t1, t2);
    }

    /**
     * 使用函数将元组的第一个组件映射为新的值。
     *
     * @param <U>    第一个组件映射后的新类型
     * @param mapper 映射函数
     * @return 返回一个新的元组，其第一个组件为经过映射函数处理后的值。
     * @throws NullPointerException 如果映射函数为null，则抛出NullPointerException异常。
     */
    public <U> LiTuple<U, T2> map1(Function<? super T1, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_1);
        return LiTuple.of(u, _2);
    }

    /**
     * 使用函数将元组的第二个组件映射为新的值。
     *
     * @param <U>    第二个组件映射后的新类型
     * @param mapper 映射函数
     * @return 返回一个新的元组，其第二个组件为经过映射函数处理后的值。
     * @throws NullPointerException 如果映射函数为null，则抛出NullPointerException异常。
     */
    public <U> LiTuple<T1, U> map2(Function<? super T2, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_2);
        return LiTuple.of(_1, u);
    }

    /**
     * 将元组转换为类型为U的对象。
     *
     * @param f   将元组转换为类型为U的函数。
     * @param <U> 转换后的类型。
     * @return 返回转换后的对象。
     * @throws NullPointerException 如果函数为null，则抛出NullPointerException异常。
     */
    public <U> U apply(BiFunction<? super T1, ? super T2, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(_1, _2);
    }

    @Override
    public int hashCode() {
        return LiTuple.hash(_1, _2);
    }

    /**
     * 返回两个给定值的有序 hash 值。
     *
     * @param o1 要 hash 的第一个值
     * @param o2 要 hash 的第二个值
     * @return 与 {@link Objects#hash(Object...)} 方法返回相同的结果
     */
    static int hash(Object o1, Object o2) {
        int result = 1;
        result = 31 * result + Objects.hash(o1);
        result = 31 * result + Objects.hash(o2);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof LiTuple)) {
            return false;
        } else {
            final LiTuple<?, ?> that = (LiTuple<?, ?>) o;
            return Objects.equals(this._1, that._1)
                    && Objects.equals(this._2, that._2);
        }
    }

    @Override
    public String toString() {
        return "(" + _1 + ", " + _2 + ")";
    }

    /**
     * 获取元组的右组件。
     *
     * @return 返回右组件的值。
     * @throws NoSuchElementException 如果左组件不为null或右组件为null，则抛出NoSuchElementException异常。
     */
    @Override
    public T2 getRight() {
        if (isLeft()) {
            throw new NoSuchElementException();
        }
        return _2;
    }

    /**
     * 判断元组的右组件是否存在。
     *
     * @return 返回true，如果右组件存在，否则返回false。
     */
    @Override
    public boolean isRight() {
        return _2 != null || _1 == null;
    }

    /**
     * 判断元组的左组件是否存在。
     *
     * @return 返回true，如果左组件存在，否则返回false。
     */
    @Override
    public boolean isLeft() {
        return _2 == null && _1 != null;
    }

    /**
     * 获取元组的左组件。
     *
     * @return 返回左组件的值。
     * @throws NoSuchElementException 如果右组件不为null或左组件为null，则抛出NoSuchElementException异常。
     */
    @Override
    public T1 getLeft() {
        if (isRight()) {
            throw new NoSuchElementException();
        }
        return _1;
    }

    /**
     * 交换元组的组件。
     *
     * @return 返回一个新的元组，其第一个组件为原元组的第二个组件，第二个组件为原元组的第一个组件。
     */
    @Override
    public LiTuple<T2, T1> swap() {
        return LiTuple.of(_2, _1);
    }

    /**
     * 判断元组的左组件是否存在。
     *
     * @return 返回true，如果左组件存在，否则返回false。
     */
    public boolean hasLeft() {
        return _1 != null;
    }

    /**
     * 判断元组的右组件是否存在。
     *
     * @return 返回true，如果右组件存在，否则返回false。
     */
    public boolean hasRight() {
        return _2 != null;
    }

    @Override
    public String name() {
        return "tuple";
    }


}
