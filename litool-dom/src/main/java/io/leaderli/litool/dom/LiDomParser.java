package io.leaderli.litool.dom;

import io.leaderli.litool.core.type.ComponentType;
import org.dom4j.Node;

/**
 * @author leaderli
 * @since 2022/7/6 8:43 AM
 */
public interface LiDomParser<T, E extends Node> extends ComponentType<E> {


    T parse(E node);


}
