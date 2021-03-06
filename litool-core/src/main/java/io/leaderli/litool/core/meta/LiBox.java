package io.leaderli.litool.core.meta;

import java.util.Objects;

/**
 * @author leaderli
 * @since 2022/6/16
 * <p>
 * 装箱一个实例，方便在lambda表达式中更新值
 */
public class LiBox<T> implements LiValue {

    private T value;


    private LiBox() {

    }

    public LiBox(T value) {
        this.value = value;
    }

    /**
     * @param <T> 泛型
     * @return 返回一个  {@code value = null } 的实例
     */
    public static <T> LiBox<T> none() {
        return new LiBox<>();
    }

    public static <T> LiBox<T> of(T value) {
        return new LiBox<>(value);
    }

    public void value(T value) {
        this.value = value;
    }

    public T value() {
        return this.value;
    }

    public LiBox<T> reset() {
        this.value = null;
        return this;
    }

    public Lino<T> lino() {
        return Lino.of(this.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LiBox<?> liBox = (LiBox<?>) o;
        return Objects.equals(value, liBox.value);
    }

    @Override
    public String toString() {
        return "LiBox{" +
                "value=" + value +
                '}';
    }

    @Override
    public boolean present() {
        return this.value != null;
    }

    @Override
    public String name() {
        return "box";
    }
}
