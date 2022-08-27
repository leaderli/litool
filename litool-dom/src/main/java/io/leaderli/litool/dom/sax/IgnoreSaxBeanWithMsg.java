package io.leaderli.litool.dom.sax;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public class IgnoreSaxBeanWithMsg extends IgnoreSaxBean {


public final List<String> msgs = new ArrayList<>();


@Override
public void end(EndEvent endEvent) {

    int begin = endEvent.locator.getLineNumber();
    int end = endEvent.locator.getLineNumber();
    this.msgs.add(String.format("ignore <%s> between line %d - %d ", endEvent.name, begin, end));
}

}
