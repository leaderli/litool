package io.leaderli.litool.core.meta;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Either类型表示一个值可以是两种类型之一，左边或右边。
 *
 * @param <L> 左边值的类型
 * @param <R> 右边值的类型
 */
public interface Either<L, R> extends LiValue, Supplier<R> {


    /**
     * 构造一个{@link Right}对象。
     *
     * @param right 右边的值
     * @param <L>   左边值的类型
     * @param <R>   右边值的类型
     * @return 一个新的{@code Right}实例
     */
    static <L, R> Either<L, R> right(R right) {
        return new Right<>(right);
    }


    /**
     * 构造一个{@link Left}对象，其值为null
     *
     * @param <L> 左边值的类型
     * @param <R> 右边值的类型
     * @return 一个{@link Left}对象，其值为null
     */
    static <L, R> Either<L, R> none() {
        return new Left<>(null);
    }

    /**
     * 构造一个{@link Left}对象。
     *
     * @param left 左边的值
     * @param <L>  左边值的类型
     * @param <R>  右边值的类型
     * @return 一个新的{@code Left}实例
     */
    static <L, R> Either<L, R> left(L left) {
        return new Left<>(left);
    }


    /**
     * 将类型为{@code Either<? extends L, ? extends R>}的Either缩小为类型{@code Either<L, R>}，以进行类型安全的强制转换。
     *
     * @param either {@code Either}实例
     * @param <L>    左边值的类型
     * @param <R>    右边值的类型
     * @return 给定的{@code either}实例作为缩小类型{@code Either<L，R>}的实例。
     */
    @SuppressWarnings("unchecked")
    static <L, R> Either<L, R> narrow(Either<? extends L, ? extends R> either) {
        return (Either<L, R>) either;
    }

    /**
     * 如果此Either为{@code Right}，则获取其右侧值或返回null（不会抛出异常）。
     *
     * @return 右边的值，或者返回null
     */
    @Override
    default R get() {
        if (isRight()) {
            return getRight();
        }
        return null;
    }

    /**
     * 如果此Either为{@code Right}，则返回其右侧值。
     *
     * @return 右边的值
     * @throws NoSuchElementException 如果为{@code Left}则抛出此异常
     */
    R getRight();

    /**
     * 返回此Either是否为{@code Right}。
     *
     * @return 如果为{@code Right}则返回true，否则返回false
     */
    boolean isRight();

    /**
     * @return 如果该要素为左值，则返回左值，否则返回{@link Lino#none()}
     */
    default Lino<L> getLeftLino() {
        if (isLeft()) {
            return Lino.of(getLeft());
        }
        return Lino.none();
    }

    /**
     * 返回此Either是否为{@code Left}。
     *
     * @return 如果为{@code Left}则返回true，否则返回false
     */
    boolean isLeft();

    /**
     * 如果此Either为{@code Left}，则返回其左侧值。
     *
     * @return 左边的值
     * @throws NoSuchElementException 如果为{@code Right}则抛出此异常
     */
    L getLeft();

    /**
     * @return 如果该要素为右值，则返回右值，否则返回{@link Lino#none()}
     */
    default Lino<R> getRightLino() {
        if (isRight()) {
            return Lino.of(getRight());
        }
        return Lino.none();
    }


    /**
     * @return 返回其 Lino
     */
    default Lino<Either<L, R>> lino() {
        return Lino.of(this);
    }

    /**
     * 将左侧或右侧折叠到这个二进制分支中的一个。
     *
     * @param leftMapper  如果是左侧，则映射左侧的值
     * @param rightMapper 如果是右侧，则映射右侧的值
     * @param <U>         折叠值的类型
     * @return 类型U的值
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
     * 将{@code Left}转换为{@code Right}，将{@code Right}转换为{@code Left}，并将值包装在新类型中。
     *
     * @return 新的{@code Either}
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
     * 表示 {@code Either} 的 {@code Right} 版本。
     *
     * @param <L> 左侧元素类型
     * @param <R> 右侧元素类型
     */
    class Right<L, R> implements Either<L, R> {


        private final R value;


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
     * 表示 {@code Either} 的 {@code Left} 版本。
     *
     * @param <L> 左侧元素类型
     * @param <R> 右侧元素类型
     */
    final class Left<L, R> implements Either<L, R> {


        private final L value;


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
