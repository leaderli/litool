package io.leaderli.litool.dom.sax;

import io.leaderli.litool.core.lang.TupleMap;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.type.ClassUtil;

/**
 * @author leaderli
 * @since 2022/7/15
 */
public class SupportTagBuilder<T extends SupportTag> {
    private final TupleMap<String, Class<T>> pairs;

    private SupportTagBuilder(TupleMap<String, Class<T>> pairs) {
        this.pairs = pairs;
    }

    public static <T extends SupportTag> TupleMap<String, Class<T>> build(TupleMap<String, Class<T>> pairs, Class<? extends T> cls) {
        return of(pairs).add(cls).build();
    }

    public TupleMap<String, Class<T>> build() {
        return this.pairs;
    }

    public SupportTagBuilder<T> add(Class<? extends T> cls) {

        Lino.of(cls)
                .throwable_map(Class::newInstance, t -> {
                    throw new UnsupportedOperationException(cls + " cannot instance", t);
                })
                .ifPresent(sax -> pairs.putKeyValue(sax.tagName(), ClassUtil.narrow(cls)));
        return this;
    }

    public static <T extends SupportTag> SupportTagBuilder<T> of(TupleMap<String, Class<T>> pairs) {
        return new SupportTagBuilder<>(pairs);
    }
}
