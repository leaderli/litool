package io.leaderli.litool.core.meta;

import java.util.Objects;
import java.util.function.BinaryOperator;

/**
 * LiBox 是一个用于包装实例的类，便于在 lambda 表达式中更新值。
 *
 * @param <T> 包装的实例的类型
 */
public class LiBox<T> implements LiValue {

    private T value;

    /**
     * 默认构造函数
     */
    private LiBox() {

    }

    /**
     * 构造函数，初始化 value
     *
     * @param value 待包装的实例
     */
    public LiBox(T value) {
        this.value = value;
    }

    /**
     * 返回一个值为 null 的 LiBox 实例
     *
     * @param <T> 包装的实例的类型
     * @return 值为 null 的 LiBox 实例
     */
    public static <T> LiBox<T> none() {
        return new LiBox<>();
    }

    /**
     * 返回一个包装了 value 的 LiBox 实例
     *
     * @param value 待包装的实例
     * @param <T>   包装的实例的类型
     * @return 包装了 value 的 LiBox 实例
     */
    public static <T> LiBox<T> of(T value) {
        return new LiBox<>(value);
    }

    /**
     * 更新实例 value 的值
     *
     * @param value 新的值
     */
    public void value(T value) {
        this.value = value;
    }

    /**
     * 返回实例的值
     *
     * @return 实例的值
     */
    public T value() {
        return this.value;
    }

    /**
     * 重置实例的值为 null
     *
     * @return 返回当前实例
     */
    public LiBox<T> reset() {
        this.value = null;
        return this;
    }

    /**
     * 返回一个新的包装了 value 的 Lino 实例
     *
     * @return 包装了 value 的 Lino 实例
     */
    public Lino<T> lino() {
        return Lino.of(this.value);
    }

    /**
     * 判断实例的值是否存在
     *
     * @return 如果实例的值不为 null 则返回 true，否则返回 false
     */
    @Override
    public boolean present() {
        return this.value != null;
    }

    /**
     * 返回对象的名称
     *
     * @return 返回字符串 "box"
     */
    @Override
    public String name() {
        return "box";
    }

    /**
     * 通过传入的函数更新实例的值
     *
     * @param function 函数对象，通过调用 BiFunction.apply(Object, Object) 来更新实例的值，
     *                 如果 function 或者它的参数为 null，则不会调用此函数。
     * @param right    函数的第二个参数，function 的第一个参数为实例的值
     * @see java.util.function.BiFunction
     */
    public void apply(BinaryOperator<T> function, T right) {
        if (function != null && value != null && right != null) {
            this.value = function.apply(value, right);
        }
    }

    /**
     * 返回 LiBox 实例的哈希值
     *
     * @return LiBox 实例的哈希值
     */
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    /**
     * 判断两个 LiBox 实例是否相等
     *
     * @param o 另一个 LiBox 实例
     * @return 如果两个 LiBox 实例的实例值相等则返回 true，否则返回 false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LiBox<?> liBox = (LiBox<?>) o;
        return Objects.equals(value, liBox.value);
    }

    /**
     * 返回 LiBox 实例的字符串表示形式
     *
     * @return LiBox 实例的字符串表示形式
     */
    @Override
    public String toString() {
        return "{" + value + "}";
    }
}
