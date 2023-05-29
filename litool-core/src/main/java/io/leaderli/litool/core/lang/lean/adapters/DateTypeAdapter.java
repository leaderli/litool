package io.leaderli.litool.core.lang.lean.adapters;

import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.lang.lean.TypeAdapter;

import java.util.Date;

public class DateTypeAdapter implements TypeAdapter<Date> {
    @Override
    public Date read(Object source, Lean lean) {
        if (source instanceof Date) {
            return (Date) source;
        }
        if (source instanceof Long) {
            return new Date((Long) source);
        }
        return null;
    }
}
