package io.leaderli.litool.core.meta;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * wraps an instance for easy updating of values in lambda expressions
 *
 * @author leaderli
 * @since 2022/6/16
 */
public class LiBox<T> implements LiValue {

private T value;


private LiBox() {

}

public LiBox(T value) {
    this.value = value;
}

/**
 * @param <T> the type of value
 * @return return an instance that {@code value = null }
 */
public static <T> LiBox<T> none() {
    return new LiBox<>();
}

/**
 * @param value the  instance libox wraps
 * @param <T>   the type of value
 * @return return an instance that  contains value
 */
public static <T> LiBox<T> of(T value) {
    return new LiBox<>(value);
}

/**
 * @param value update instance value
 */
public void value(T value) {
    this.value = value;
}

/**
 * @return return instance value
 */
public T value() {
    return this.value;
}

/**
 * @return this and set instance to null
 */
public LiBox<T> reset() {
    this.value = null;
    return this;
}

/**
 * @return {@code Lino.of(this.value)}
 */
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

/**
 * @param function update {{@link #value}} by call {@link BiFunction#apply(Object, Object)}，
 *                 if function is null or it's parameter contains null , the function will
 *                 not be called
 * @param right    the second parameter of  function, the first parameter is {@link #value}
 * @see java.util.function.BiFunction
 */
public void apply(BiFunction<T, T, T> function, T right) {

    if (function != null && value != null && right != null) {
        this.value = function.apply(value, right);
    }
}
}
