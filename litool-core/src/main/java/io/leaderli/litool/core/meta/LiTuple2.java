/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2020 Vavr, http://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.leaderli.litool.core.meta;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A tuple of two elements which can be seen as cartesian product of two components.
 *
 * @param <T1> type of the 1st element
 * @param <T2> type of the 2nd element
 * @author Daniel Dietrich
 */
public final class LiTuple2<T1, T2> implements LiTuple, Comparable<LiTuple2<T1, T2>>, Either<T1, T2>, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The 1st element of this tuple.
     */
    public final transient T1 _1;

    /**
     * The 2nd element of this tuple.
     */
    public final transient T2 _2;

    /**
     * Constructs a tuple of two elements.
     *
     * @param t1 the 1st element
     * @param t2 the 2nd element
     */
    public LiTuple2(T1 t1, T2 t2) {
        this._1 = t1;
        this._2 = t2;
    }

    public static <T1, T2> Comparator<LiTuple2<T1, T2>> comparator(Comparator<? super T1> t1Comp,
                                                                   Comparator<? super T2> t2Comp) {
        return (Comparator<LiTuple2<T1, T2>> & Serializable) (t1, t2) -> {
            final int check1 = t1Comp.compare(t1._1, t2._1);
            if (check1 != 0) {
                return check1;
            }

            return t2Comp.compare(t1._2, t2._2);

            // all components are equal
        };
    }

    public static <K, V> LiTuple2<K, V> of(Map.Entry<K, V> entry) {
        return LiTuple.of(entry.getKey(), entry.getValue());
    }

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public boolean notIncludeNull() {
        return _1 != null && _2 != null;
    }

    @Override
    public int compareTo(LiTuple2<T1, T2> that) {
        return LiTuple2.compareTo(this, that);
    }

    @SuppressWarnings("unchecked")
    private static <U1 extends Comparable<? super U1>, U2 extends Comparable<? super U2>> int compareTo(LiTuple2<?,
            ?> o1
            , LiTuple2<?, ?> o2) {
        final LiTuple2<U1, U2> t1 = (LiTuple2<U1, U2>) o1;
        final LiTuple2<U1, U2> t2 = (LiTuple2<U1, U2>) o2;

        final int check1 = t1._1.compareTo(t2._1);
        if (check1 != 0) {
            return check1;
        }

        return t1._2.compareTo(t2._2);

        // all components are equal
    }

    /**
     * Getter of the 1st element of this tuple.
     *
     * @return the 1st element of this Tuple.
     */
    public T1 _1() {
        return _1;
    }

    /**
     * Sets the 1st element of this tuple to the given {@code value}.
     *
     * @param value the new value
     * @return a copy of this tuple with a new value for the 1st element of this Tuple.
     */
    public LiTuple2<T1, T2> update1(T1 value) {
        return new LiTuple2<>(value, _2);
    }

    /**
     * Getter of the 2nd element of this tuple.
     *
     * @return the 2nd element of this Tuple.
     */
    public T2 _2() {
        return _2;
    }

    /**
     * Sets the 2nd element of this tuple to the given {@code value}.
     *
     * @param value the new value
     * @return a copy of this tuple with a new value for the 2nd element of this Tuple.
     */
    public LiTuple2<T1, T2> update2(T2 value) {
        return new LiTuple2<>(_1, value);
    }

    /**
     * Converts the tuple to java.util.Map.Entry {@code Tuple}.
     *
     * @return A  java.util.Map.Entry where the first element is the key and the second
     * element is the value.
     */
    public Map.Entry<T1, T2> toEntry() {
        return new AbstractMap.SimpleEntry<>(_1, _2);
    }
    /**
     * Maps the components of this tuple using a mapper function.
     *
     * @param mapper the mapper function
     * @param <U1>   new type of the 1st component
     * @param <U2>   new type of the 2nd component
     * @return A new Tuple of same arity.
     * @throws NullPointerException if {@code mapper} is null
     */
    public <U1, U2> LiTuple2<U1, U2> map(BiFunction<? super T1, ? super T2, LiTuple2<U1, U2>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return mapper.apply(_1, _2);
    }

    @Override
    public T1 getLeft() {
        if (isRight()) {
            throw new NoSuchElementException();
        }
        return _1;
    }

    /**
     * Maps the components of this tuple using a mapper function for each component.
     *
     * @param f1   the mapper function of the 1st component
     * @param f2   the mapper function of the 2nd component
     * @param <U1> new type of the 1st component
     * @param <U2> new type of the 2nd component
     * @return A new Tuple of same arity.
     * @throws NullPointerException if one of the arguments is null
     */
    public <U1, U2> LiTuple2<U1, U2> map(Function<? super T1, ? extends U1> f1, Function<? super T2, ? extends U2> f2) {
        Objects.requireNonNull(f1, "f1 is null");
        Objects.requireNonNull(f2, "f2 is null");
        return LiTuple.of(f1.apply(_1), f2.apply(_2));
    }

    /**
     * Maps the 1st component of this tuple to a new value.
     *
     * @param <U>    new type of the 1st component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 1st component
     */
    public <U> LiTuple2<U, T2> map1(Function<? super T1, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_1);
        return LiTuple.of(u, _2);
    }

    /**
     * Maps the 2nd component of this tuple to a new value.
     *
     * @param <U>    new type of the 2nd component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 2nd component
     */
    public <U> LiTuple2<T1, U> map2(Function<? super T2, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_2);
        return LiTuple.of(_1, u);
    }

    @Override
    public boolean isLeft() {
        return _2 == null && _1 != null;
    }

    /**
     * Transforms this tuple to an object of type U.
     *
     * @param f   Transformation which creates a new object of type U based on this tuple's contents.
     * @param <U> type of the transformation result
     * @return An object of type U
     * @throws NullPointerException if {@code f} is null
     */
    public <U> U apply(BiFunction<? super T1, ? super T2, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(_1, _2);
    }

    @Override
    public int hashCode() {
        return LiTuple.hash(_1, _2);
    }
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof LiTuple2)) {
            return false;
        } else {
            final LiTuple2<?, ?> that = (LiTuple2<?, ?>) o;
            return Objects.equals(this._1, that._1)
                    && Objects.equals(this._2, that._2);
        }
    }

    @Override
    public T2 getRight() {
        if (isLeft()) {
            throw new NoSuchElementException();
        }
        return _2;
    }

    @Override
    public String toString() {
        return "(" + _1 + ", " + _2 + ")";
    }

    @Override
    public String name() {
        return "tuple2";
    }

    @Override
    public boolean isRight() {
        return _2 != null || _1 == null;
    }


    /**
     * Swaps the elements of this {@code Tuple}.
     *
     * @return A new Tuple where the first element is the second element of this Tuple
     * and the second element is the first element of this Tuple.
     */
    @Override
    public LiTuple2<T2, T1> swap() {
        return LiTuple.of(_2, _1);
    }


// -- Object


}
