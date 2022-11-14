package io.leaderli.litool.core.event;


import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.meta.LiTuple2;

/**
 * A event with source , used to trigger specific
 * behavior on the particular type
 */
public class LiEventTuple<T1, T2> {


    public final LiTuple2<T1, T2> tuple;

    public LiEventTuple(T1 t1, T2 t2) {
        this.tuple = LiTuple.of(t1, t2);
    }

    public T1 _1() {

        return tuple._1;
    }

    public T2 _2() {

        return tuple._2;
    }


    @Override
    public final String toString() {

        return getClass().getSimpleName() + tuple;
    }

}
