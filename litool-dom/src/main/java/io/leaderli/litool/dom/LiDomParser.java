package io.leaderli.litool.dom;

import org.dom4j.Node;

/**
 * @author leaderli
 * @since 2022/7/6 8:43 AM
 */
public interface LiDomParser<T> {


    T parse(Node node);

}
