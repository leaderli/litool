package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.function.Chain;
import io.leaderli.litool.core.function.Filter;

import java.util.ArrayList;
import java.util.List;

public class FilterChain<T> implements Filter<T>, Chain<Filter<T>> {

    private final List<Filter<T>> chain = new ArrayList<>();

    public FilterChain() {

    }

    public FilterChain(Filter<T> filter) {
        add(filter);
    }

    @Override
    public Boolean apply(T t) {
        for (Filter<T> filter : chain) {
            if (!filter.apply(t)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public FilterChain<T> addHead(Filter<T> filter) {
        if (filter instanceof FilterChain) {
            ((FilterChain<Object>) filter).chain.forEach(f -> addHead((Filter<T>) f));
        } else {
            chain.add(0, filter);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public FilterChain<T> add(Filter<T> filter) {
        if (filter instanceof FilterChain) {
            ((FilterChain<Object>) filter).chain.forEach(f -> add((Filter<T>) f));
        } else {
            chain.add(filter);
        }
        return this;
    }

}
