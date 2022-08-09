package io.leaderli.litool.runner.xml;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.dom.sax.EndEvent;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.SaxBeanVisitor;
import io.leaderli.litool.runner.TypeAlias;

import java.util.Map;
import java.util.Objects;

/**
 * @author leaderli
 * @since 2022/7/23
 */
public class ResponseElement implements SaxBeanVisitor {


    public EntryList entryList = new EntryList();

    public void addEntry(EntryElement element) {
        entryList.lira().iterator().forEachRemaining(entry -> {
            LiAssertUtil.assertFalse(Objects.equals(entry.getKey(), element.getKey()), "duplicate key of " + element.getKey());
            LiAssertUtil.assertFalse(Objects.equals(entry.getLabel(), element.getLabel()), "duplicate label of " + element.getLabel());
        });
        this.entryList.add(element);
    }

    @Override
    public void end(EndEvent endEvent) {
        LiAssertUtil.assertFalse(entryList.lira().size() == 0, "the entryList of response is empty");
        SaxBeanVisitor.super.end(endEvent);
    }

    @Override
    public String name() {
        return "response";
    }


    @Override
    public void visit(Context context) {
        Map<String, Object> response = this.entryList.lira().toMap(EntryElement::getKey, e -> TypeAlias.parser(e.getType(), null, e.getDef()));
        context.origin_request_or_response.putAll(response);
    }
}
