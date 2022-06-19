package io.leaderli.litool.core.meta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public interface LiCase<T, E> {

    /**
     * @param lino 实例
     * @param <T>  实例泛型
     * @param <E>  转换后的泛型
     * @return 新的 LiCase 实例
     */
    static <T, E> LiCase<T, E> of(Lino<T> lino) {
        if (lino == null || lino.notPresent()) {
            return none();
        }
        return new Some<>(lino);
    }

    /**
     * @param <T> 实例泛型
     * @param <E> 转换后的泛型
     * @return 全局唯一的空 LiCase
     */
    static <T, E> LiCase<T, E> none() {
        @SuppressWarnings("unchecked") final LiCase<T, E> none = (None<T, E>) None.INSTANCE;
        return none;
    }


    /**
     * @param filter  判断函数
     * @param mapping 转换函数
     * @return 当满足条件时执行转换函数
     * @see io.leaderli.litool.core.util.LiBoolUtil#parse(Object)
     */
    LiCase<T, E> if_map(Function<? super T, Object> filter, Function<? super T, ? extends E> mapping);


    /**
     * @param keyType 是否继承的类型
     * @param mapping 转换函数
     * @param <F>     转换函数的第二个泛型
     * @return 当 {@link Lino} 的实际值类型满足 keyType 时执行转换函数
     */
    <F> LiCase<T, E> case_map(Class<F> keyType, Function<? super F, ? extends E> mapping);


    /**
     * @param keyType 是否继承的类型
     * @param m1      转换函数1
     * @param m2      转换函数2
     * @param <F1>    转换函数1的第二个泛型,转换函数2的第一个泛型
     * @param <F2>    转换函数2的第二个泛型
     * @return 当 {@link Lino} 的实际值类型满足 keyType 时执行两个转换函数，最终得到 F2泛型的 {@link Lino}
     */
    <F1, F2> LiCase<T, E> case_map(Class<F1> keyType, Function<? super F1, ? extends F2> m1, Function<? super F2, ? extends E> m2);

    /**
     * @return return first match case_map function
     */
    Lino<E> lino();


    final class Some<T, E> implements LiCase<T, E> {

        private final Lino<T> lino;

        private final List<Function<Lino<T>, ? extends Lino<E>>> mappings = new ArrayList<>();

        private Some(Lino<T> lino) {
            this.lino = lino;
        }


        public LiCase<T, E> if_map(Function<? super T, Object> filter, Function<? super T, ? extends E> mapping) {


            this.mappings.add(ln -> ln.filter(filter).map(mapping));

            return this;
        }


        public <R> LiCase<T, E> case_map(Class<R> keyType, Function<? super R, ? extends E> mapping) {

            this.mappings.add(ln -> Lino.narrow(ln.cast(keyType).map(mapping)));
            return this;
        }


        public <F1, F2> LiCase<T, E> case_map(Class<F1> keyType, Function<? super F1, ? extends F2> m1, Function<? super F2, ? extends E> m2) {
            this.mappings.add(ln -> Lino.narrow(ln.cast(keyType).map(m1)).map(m2));
            return this;
        }


        public Lino<E> lino() {
            for (Function<Lino<T>, ? extends Lino<E>> mapping : this.mappings) {

                Lino<E> apply = mapping.apply(lino);

                if (apply.isPresent()) {
                    return apply;
                }
            }

            return Lino.none();
        }

    }


    final class None<T, E> implements LiCase<T, E> {

        private static final None<?, ?> INSTANCE = new None<>();

        @Override
        public LiCase<T, E> if_map(Function<? super T, Object> filter, Function<? super T, ? extends E> mapping) {
            return this;
        }

        @Override
        public <R> LiCase<T, E> case_map(Class<R> keyType, Function<? super R, ? extends E> mapping) {
            return this;
        }

        @Override
        public <F1, F2> LiCase<T, E> case_map(Class<F1> keyType, Function<? super F1, ? extends F2> m1, Function<? super F2, ? extends E> m2) {
            return this;
        }

        @Override
        public Lino<E> lino() {
            return Lino.none();
        }
    }
}
