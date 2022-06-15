package io.leaderli.litool.core.meta;

/**
 * @author leaderli
 * @since 2022/6/16
 * <p>
 * 基于函数式编程思维，所有对其包含的value的值的操作，都是类型安全的。
 * <p>
 * 当且仅当value的值 {@link #isPresent()} 时，才会实际对其进行方法调用
 */
public interface Lino<T> extends LiValue {


    /**
     * @param value 值
     * @param <T>   泛型
     * @return 窄化一个宽泛的泛型， {@code <? extends T> } 转换为  {@code  <T> }
     */
    @SuppressWarnings("unchecked")
    static <T> Lino<T> narrow(Lino<? extends T> value) {

        return (Lino<T>) value;

    }

    /**
     * @param <T> 泛型
     * @return {@link None#INSTANCE}
     */
    static <T> Lino<T> none() {
        @SuppressWarnings("unchecked") final None<T> none = (None<T>) None.INSTANCE;
        return none;
    }

    /**
     * @param value 值
     * @param <T>   泛型
     * @return 返回一个实例，
     * 当 {@code value == null} 时返回 {@link #none()}
     * 否则返回 {@link Some}
     */
    static <T> Lino<T> of(T value) {
        if (value == null) {
            return none();
        }
        return new Some<>(value);
    }


    class Some<T> implements Lino<T> {

        private final T value;

        public Some(T value) {
            this.value = value;
        }

        @Override
        public boolean isPresent() {
            return true;
        }

        @Override
        public String name() {
            return "Some";
        }

        @Override
        public String toString() {
            return name() + "(" + value + ")";
        }
    }

    class None<T> implements Lino<T> {
        private static final None<?> INSTANCE = new None<>();

        private None() {
        }


        @Override
        public boolean isPresent() {
            return false;
        }

        @Override
        public String name() {
            return "None";
        }

        @Override
        public String toString() {
            return name() + "()";
        }
    }
}
