package io.leaderli.litool.runner.event;

import io.leaderli.litool.core.event.LiEventObject;
import io.leaderli.litool.dom.sax.SaxBean;

/**
 * @author leaderli
 * @since 2022/8/22 4:42 PM
 */
public class VisitorEvent extends LiEventObject<SaxBean> {
public VisitorEvent(SaxBean source) {
    super(source);
}
}
