package io.leaderli.litool.core.meta;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/9/25 11:17 AM
 */
public interface Either<L, R> extends LiValue, Supplier<R> {


    /**
     * Constructs a {@link Right}
     *
     * @param right The value.
     * @param <L>   Type of left value.
     * @param <R>   Type of right value.
     * @return A new {@code Right} instance.
     */
    static <L, R> Either<L, R> right(R right) {
        return new Right<>(right);
    }

    /**
     * Constructs a {@link Right}
     *
     * @param <L> Type of left value.
     * @param <R> Type of right value.
     * @return A new {@code Left} instance.
     */
    static <L, R> Either<L, R> none() {
        return new Left<>(null);
    }

    /**
     * Constructs a {@link Left}
     *
     * @param left The value.
     * @param <L>  Type of left value.
     * @param <R>  Type of right value.
     * @return A new {@code Left} instance.
     */
    static <L, R> Either<L, R> left(L left) {
        return new Left<>(left);
    }

    static <T> T fold(Either<T, T> either) {
        return either.fold(l -> l, r -> r);
    }

    /**
     * Narrows a widened {@code Either<? extends L, ? extends R>} to {@code Either<L, R>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param either A {@code Either}.
     * @param <L>    Type of left value.
     * @param <R>    Type of right value.
     * @return the given {@code either} instance as narrowed type {@code Either<L, R>}.
     */
    @SuppressWarnings("unchecked")
    static <L, R> Either<L, R> narrow(Either<? extends L, ? extends R> either) {
        return (Either<L, R>) either;
    }

    /**
     * @return the lino of left value if {@link  #isLeft()}  otherwise return {@link Lino#none()}
     */
    default Lino<L> getLeftLino() {
        if (isLeft()) {
            return Lino.of(getLeft());
        }
        return Lino.none();
    }

    /**
     * Returns whether this Either is a Left.
     *
     * @return true, if this is a Left, false otherwise
     */
    boolean isLeft();


    /**
     * different with {@link  #getRight()}, it's will return null when {@link  #isLeft()}
     * replace throw{@link  NoSuchElementException}
     *
     * @return the right value
     */
    @Override
    default R get() {
        if (isRight()) {
            return getRight();
        }
        return null;
    }

    /**
     * Returns the left value.
     *
     * @return The left value.
     * @throws NoSuchElementException if this is a {@code Right}.
     */
    L getLeft();

    /**
     * Gets the right value if this is a {@code Right} or throws if this is a {@code Left}.
     *
     * @return the right value
     * @throws NoSuchElementException if this is a {@code Left}.
     */
    R getRight();

    /**
     * Returns whether this Either is a Right.
     *
     * @return true, if this is a Right, false otherwise
     */
    boolean isRight();

    /**
     * @return the lino of right value if {@link  #isRight()} ()}  otherwise return {@link Lino#none()}
     */
    default Lino<R> getRightLino() {
        if (isRight()) {
            return Lino.of(getRight());
        }
        return Lino.none();
    }


    default Lino<Either<L, R>> lino() {
        return Lino.of(this);
    }

    /**
     * Folds either the left or the right side of this disjunction.
     *
     * @param leftMapper  maps the left value if this is a Left
     * @param rightMapper maps the right value if this is a Right
     * @param <U>         type of the folded value
     * @return A value of type U
     */
    default <U> U fold(Function<? super L, ? extends U> leftMapper, Function<? super R, ? extends U> rightMapper) {
        Objects.requireNonNull(leftMapper, "leftMapper is null");
        Objects.requireNonNull(rightMapper, "rightMapper is null");
        if (isRight()) {
            return rightMapper.apply(getRight());
        } else {
            return leftMapper.apply(getLeft());
        }
    }


    /**
     * Converts a {@code Left} to a {@code Right} vice versa by wrapping the value in a new type.
     *
     * @return a new {@code Either}
     */
    default Either<R, L> swap() {
        if (isRight()) {
            return new Left<>(getRight());
        } else {
            return new Right<>(getLeft());
        }
    }


    @Override
    default boolean present() {
        return isRight();
    }

    /**
     * The {@code Right} version of an {@code Either}.
     *
     * @param <L> left component type
     * @param <R> right component type
     * @author Daniel Dietrich
     */
    class Right<L, R> implements Either<L, R> {

        private static final long serialVersionUID = 1L;

        private final R value;

        /**
         * Constructs a {@code Right}.
         *
         * @param value a right value
         */
        Right(R value) {
            this.value = value;
        }

        @Override
        public L getLeft() {
            throw new NoSuchElementException("getLeft() on Right");
        }

        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public R getRight() {
            return value;
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }

        @Override
        public boolean equals(Object obj) {
            return (obj == this) || (obj instanceof Either.Right && Objects.equals(value, ((Right<?, ?>) obj).value));
        }

        @Override
        public String toString() {
            return name() + "(" + value + ")";
        }

        @Override
        public String name() {
            return "Right";
        }
    }

    /**
     * The {@code Left} version of an {@code Either}.
     *
     * @param <L> left component type
     * @param <R> right component type
     * @author Daniel Dietrich
     */
    final class Left<L, R> implements Either<L, R> {


        private final L value;

        /**
         * Constructs a {@code Left}.
         *
         * @param value a left value
         */
        Left(L value) {
            this.value = value;
        }

        @Override
        public L getLeft() {
            return value;
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public R getRight() {
            throw new NoSuchElementException("get() on Left");
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }

        @Override
        public boolean equals(Object obj) {
            return (obj == this) || (obj instanceof Either.Left && Objects.equals(value, ((Left<?, ?>) obj).value));
        }

        @Override
        public String toString() {
            return name() + "(" + value + ")";
        }

        @Override
        public String name() {
            return "Left";
        }
    }
}
