package io.leaderli.litool.runner.executor;

import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.TypeAlias;
import io.leaderli.litool.runner.xml.EntryElement;
import io.leaderli.litool.runner.xml.ResponseElement;

import java.util.Map;

/**
 * @author leaderli
 * @since 2022/8/9 4:48 PM
 */
public class ResponseElementExecutor extends BaseElementExecutor<ResponseElement> {

    public ResponseElementExecutor(ResponseElement requestElement) {
        super(requestElement);
    }


    @Override
    public void execute(Context context) {
        Map<String, Object> response = element.entryList.lira().toMap(EntryElement::getKey, e -> TypeAlias.parser(e.getType(), null, e.getDef()));
        context.origin_request_or_response.putAll(response);

    }
}
