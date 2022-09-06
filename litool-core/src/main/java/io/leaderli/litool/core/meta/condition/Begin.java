package io.leaderli.litool.core.meta.condition;

import io.leaderli.litool.core.meta.Lino;

import java.util.function.Consumer;

/**
 * 用于开始响应式调用，并保存最终结果
 *
 * @param <T> 源数据泛型
 * @param <R> 结果数据泛型
 */
class Begin<T, R> extends If<T, R> {
    private final Lino<T> lino;

    public Begin(Lino<T> lino) {
        this.lino = lino;
    }

    @Override
    public void subscribe(Subscriber<T, R> subscriber) {

        subscriber.onSubscribe(new Subscription<R>() {


            private Consumer<? super R> completeConsumer;

            @Override
            public void request(Consumer<? super R> completeConsumer) {

                this.completeConsumer = completeConsumer;
                subscriber.next(lino.get(), null);
            }

            @Override
            public void onComplete(R value) {
                this.completeConsumer.accept(value);
            }
        });
    }

}
