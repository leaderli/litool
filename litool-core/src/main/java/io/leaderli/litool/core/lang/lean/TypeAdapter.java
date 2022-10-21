package io.leaderli.litool.core.lang.lean;

import java.lang.reflect.Type;

/**
 * @author leaderli
 * @since 2022/9/24 2:47 PM
 */
@FunctionalInterface
public interface TypeAdapter<T> {


    /**
     * @param source the source
     * @param lean   the lean, that can provide adapter by {@link  Lean#getAdapter(Type)}
     *               for custom TypeAdapter do some trick thing
     * @return create target by source
     */
    T read(Object source, Lean lean);
}
