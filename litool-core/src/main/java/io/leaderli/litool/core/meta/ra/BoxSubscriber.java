package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;

/**
 * @author leaderli
 * @since 2022/9/2 1:08 PM
 */
public class BoxSubscriber<T> {


    public static <T> Lino<T> subscribe(Lira<T> lira) {
        LiBox<T> box = LiBox.none();
        lira.subscribe(new ConsumerSubscriber<>(box::value));
        return box.lino();
    }
}
