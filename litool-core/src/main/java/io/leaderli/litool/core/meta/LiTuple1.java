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
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

/**
 * A tuple of one element which can be seen as cartesian product of one component.
 *
 * @param <T1> type of the 1st element
 * @author Daniel Dietrich
 */
public final class LiTuple1<T1> implements LiTuple, Comparable<LiTuple1<T1>>, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The 1st element of this tuple.
     */
    public final T1 _1;

    /**
     * Constructs a tuple of one element.
     *
     * @param t1 the 1st element
     */
    public LiTuple1(T1 t1) {
        this._1 = t1;
    }

    public static <T1> Comparator<LiTuple1<T1>> comparator(Comparator<? super T1> t1Comp) {
        return (Comparator<LiTuple1<T1>> & Serializable) (t1, t2) -> {
            return t1Comp.compare(t1._1, t2._1);

            // all components are equal
        };
    }

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public boolean notIncludeNull() {
        return _1 != null;
    }

    @Override
    public int compareTo(LiTuple1<T1> that) {
        return LiTuple1.compareTo(this, that);
    }

    @SuppressWarnings("unchecked")
    private static <U1 extends Comparable<? super U1>> int compareTo(LiTuple1<?> o1, LiTuple1<?> o2) {
        final LiTuple1<U1> t1 = (LiTuple1<U1>) o1;
        final LiTuple1<U1> t2 = (LiTuple1<U1>) o2;

        return t1._1.compareTo(t2._1);

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
    public LiTuple1<T1> update1(T1 value) {
        return new LiTuple1<>(value);
    }

    /**
     * Maps the components of this tuple using a mapper function.
     *
     * @param mapper the mapper function
     * @param <U1>   new type of the 1st component
     * @return A new Tuple of same arity.
     * @throws NullPointerException if {@code mapper} is null
     */
    public <U1> LiTuple1<U1> map(Function<? super T1, ? extends U1> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return LiTuple.of(mapper.apply(_1));
    }

    /**
     * Transforms this tuple to an object of type U.
     *
     * @param f   Transformation which creates a new object of type U based on this tuple's contents.
     * @param <U> type of the transformation result
     * @return An object of type U
     * @throws NullPointerException if {@code f} is null
     */
    public <U> U apply(Function<? super T1, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(_1);
    }

    /**
     * Append a value to this tuple.
     *
     * @param <T2> type of the value to append
     * @param t2   the value to append
     * @return a new Tuple with the value appended
     */
    public <T2> LiTuple2<T1, T2> append(T2 t2) {
        return LiTuple.of(_1, t2);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T2>  the type of the 2nd value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws NullPointerException if {@code tuple} is null
     */
    public <T2> LiTuple2<T1, T2> concat(LiTuple1<T2> tuple) {
        Objects.requireNonNull(tuple, "tuple is null");
        return LiTuple.of(_1, tuple._1);
    }

    @Override
    public int hashCode() {
        return LiTuple.hash(_1);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof LiTuple1)) {
            return false;
        } else {
            final LiTuple1<?> that = (LiTuple1<?>) o;
            return Objects.equals(this._1, that._1);
        }
    }

    @Override
    public String toString() {
        return "(" + _1 + ")";
    }

}
