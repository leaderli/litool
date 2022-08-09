package io.leaderli.litool.runner.xml;

import io.leaderli.litool.core.collection.ImmutableMap;
import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.dom.sax.EndEvent;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.SaxBeanVisitor;
import io.leaderli.litool.runner.TypeAlias;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author leaderli
 * @since 2022/7/23
 */
public class RequestElement implements SaxBeanVisitor {


    public final EntryList entryList = new EntryList();


    public void addEntry(EntryElement element) {

        entryList.lira().iterator().forEachRemaining(entry -> {
            LiAssertUtil.assertFalse(Objects.equals(entry.getKey(), element.getKey()), "duplicate key of " + element.getKey());
            LiAssertUtil.assertFalse(Objects.equals(entry.getLabel(), element.getLabel()), "duplicate label of " + element.getLabel());
        });
        this.entryList.add(element);
    }

    @Override
    public void end(EndEvent endEvent) {
        LiAssertUtil.assertFalse(entryList.lira().size() == 0, "the entryList of request is empty");

        SaxBeanVisitor.super.end(endEvent);
    }

    @Override
    public String name() {
        return "request";
    }

    @Override
    public void visit(Context context) {
        Map<String, Object> parserRequest = new HashMap<>();
        entryList.lira().forEach(entry -> {

            String text = entry.getKey();
            String value = (String) context.origin_request_or_response.getOrDefault(text, entry.getDef());
            Class<?> type = TypeAlias.getType(entry.getType());
            Object parserValue = TypeAlias.parser(entry.getType(), value, entry.getDef());

            parserRequest.put(text, parserValue);
        });

        context.origin_request_or_response.clear();
        context.setReadonly_request(ImmutableMap.of(parserRequest));

    }

    public EntryList getEntryList() {
        return entryList;
    }
}
