package io.leaderli.litool.runner.executor;

import io.leaderli.litool.core.collection.ImmutableMap;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.TypeAlias;
import io.leaderli.litool.runner.xml.RequestElement;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/8/9 4:48 PM
 */
public class RequestElementExecutor extends BaseElementExecutor<RequestElement> {

    public RequestElementExecutor(RequestElement requestElement) {
        super(requestElement);
    }

    @Override
    public void visit(Context context) {
        Map<String, Object> parserRequest = new HashMap<>();
        element.entryList.lira().forEach(entry -> {

            String text = entry.getKey();
            String value = (String) context.origin_request_or_response.getOrDefault(text, entry.getDef());
            Class<?> type = TypeAlias.getType(entry.getType());
            Object parserValue = TypeAlias.parser(entry.getType(), value, entry.getDef());

            parserRequest.put(text, parserValue);
        });

        context.origin_request_or_response.clear();
        context.setReadonly_request(ImmutableMap.of(parserRequest));

    }
}
