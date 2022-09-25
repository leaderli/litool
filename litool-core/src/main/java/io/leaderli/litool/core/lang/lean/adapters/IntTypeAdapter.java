package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.lean.TypeAdapter;
import io.leaderli.litool.core.text.StringConvert;

/**
 * @author leaderli
 * @since 2022/9/25
 */
public class IntTypeAdapter implements TypeAdapter<Integer> {
    @Override
    public Integer read(Object obj) {

        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        if (obj instanceof String) {
            return StringConvert.parser(Integer.class, (String) obj).assertNotNone(obj + " cannot convert to int").get();
        }
        throw new IllegalArgumentException(obj + " cannot convert to int");
    }

}
