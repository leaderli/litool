package io.leaderli.litool.core.meta;

import io.leaderli.litool.core.exception.LiThrowableFunction;
import io.leaderli.litool.core.exception.LiThrowableSupplier;
import io.leaderli.litool.core.meta.reactor.IntermediateRaSubscriber;
import io.leaderli.litool.core.meta.reactor.LinkSubscription;
import io.leaderli.litool.core.meta.reactor.RaPublisher;
import io.leaderli.litool.core.meta.reactor.RaSubscriber;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @param <T> 泛型
 *            <p>
 *            链式执行，每个节点执行成功后才会执行下一个节点，若某一个节点执行结果为失败，
 *            则调用 {@link #error(Runnable)}。
 *            执行动作是响应式的
 * @author leaderli
 * @since 2022/7/16
 */
public interface LiLink<T> extends LiValue {


    /**
     * @param t   实例
     * @param <T> 泛型
     * @return 返回一个新的实例
     */
    static <T> LiLink<T> of(T t) {

        return new RaLinkBegin<>();
    }

    LiLink<T> then(Function<T, Object> supplier);

    LiLink<T> throwable_then(LiThrowableFunction<T, Object> function);

    LiLink<T> then(Supplier<Object> supplier);

    LiLink<T> throwable_then(LiThrowableSupplier<Object> function);

    void error(Runnable runnable);

    void error(Consumer<T> runnable);

}

abstract class RaLink<T> implements LiLink<T>, RaPublisher<T> {

    private boolean err = false;


    @Override
    public LiLink<T> then(Function<T, Object> supplier) {
//        lira = lira.filter(supplier);
        return this;
    }

    @Override
    public LiLink<T> throwable_then(LiThrowableFunction<T, Object> function) {
//        lira = lira.filter(
//                t -> {
//                    try {
//                        return function.apply(t);
//                    } catch (Throwable e) {
//                        LiConstant.getWhenThrow().accept(e);
//                        return false;
//                    }
//                }
//        );
        return this;
    }

    @Override
    public LiLink<T> then(Supplier<Object> supplier) {
//        lira = lira.filter(t -> supplier.get());
        return this;
    }

    @Override
    public LiLink<T> throwable_then(LiThrowableSupplier<Object> supplier) {
//        lira = lira.filter(t -> {
//            try {
//                return supplier.get();
//            } catch (Throwable e) {
//                LiConstant.getWhenThrow().accept(e);
//                return false;
//            }
//        });
        return this;
    }

    @Override
    public void error(Runnable runnable) {
        err = true;
//        lira.first().ifAbsent(runnable);
    }

    @Override
    public void error(Consumer<T> runnable) {
        err = true;
//        lira.first().ifAbsent(() -> runnable.accept(value));
    }

    @Override
    public boolean present() {
        return true;
    }

    @Override
    public String name() {
        return "link";
    }


}

class RaLinkBegin<T> extends RaLink<T> {

    @Override
    public void subscribe(RaSubscriber<? super T> actualSubscriber) {
        actualSubscriber.onSubscribe(new LinkBeginRaSubscription<>(actualSubscriber));

    }

}

class LinkBeginRaSubscription<T> implements LinkSubscription<T> {
    private final T[] arr;

    private final RaSubscriber<? super T> actualSubscriber;

    public LinkBeginRaSubscription(RaSubscriber<? super T> actualSubscriber) {
    }


    @Override
    public void request(T t) {

    }
}

class RaLinkFilter<T> extends RaLink<T> {
    private final RaPublisher<T> prevPublisher;


    private final Function<? super T, Object> filter;

    public RaLinkFilter(RaPublisher<T> prevPublisher, Function<? super T, Object> filter) {
        this.prevPublisher = prevPublisher;
        this.filter = filter;
    }


    @Override
    public void subscribe(RaSubscriber<? super T> actualSubscriber) {
        prevPublisher.subscribe(new LinkFilterRaSubscriber<>(actualSubscriber, filter));

    }

    private static class LinkFilterRaSubscriber<T> extends IntermediateRaSubscriber<T, T> {
        private final Function<? super T, Object> filter;

        public LinkFilterRaSubscriber(RaSubscriber<? super T> actualSubscriber, Function<? super T, Object> filter) {
            super(actualSubscriber);
            this.filter = filter;
        }

        @Override
        public void next(Lino<T> t) {
            t.filter(filter).nest(f -> this.actualSubscriber.next(Lino.narrow(f)));

        }
    }
}

