package io.leaderli.litool.runner.event;

import io.leaderli.litool.core.event.ILiEventListener;
import io.leaderli.litool.core.meta.LiTuple;

/**
 * @author leaderli
 * @since 2022/8/16 8:21 AM
 */
@FunctionalInterface
public interface EchoListener extends ILiEventListener<EchoEvent, LiTuple<Integer, String>> {
}
