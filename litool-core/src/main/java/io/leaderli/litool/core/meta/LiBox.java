package io.leaderli.litool.core.meta;

import java.util.Objects;

/**
 * @author leaderli
 * @since 2022/6/16
 * <p>
 * 装箱一个实例，方便在lambda表达式中更新值
 */
public class LiBox<T> {

    private T value;


    private LiBox() {

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

    public LiBox(T value) {
        this.value = value;
    }

    public void value(T value) {
        this.value = value;
    }

    public T value() {
        return this.value;
    }

    public Lino<T> lino() {
        return Lino.of(this.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LiBox<?> liBox = (LiBox<?>) o;
        return Objects.equals(value, liBox.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "LiBox{" +
                "value=" + value +
                '}';
    }
}
