package io.leaderli.litool.core.event;


import io.leaderli.litool.core.meta.Lino;

/**
 * 所有的 event 发生时都伴随着一个事件对象
 */
public class LiEventObject<T> {


    private final T source;

    public LiEventObject(T source) {
        this.source = source;
    }

    public final Lino<T> getSource() {
        return Lino.of(source);
    }


    public final String toString() {

        return getClass().getName() + "[source=" + source + "]";
    }

}
