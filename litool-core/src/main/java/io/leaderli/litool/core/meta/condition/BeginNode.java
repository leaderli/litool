package io.leaderli.litool.core.meta.condition;

/**
 * 用于开始响应式调用，并保存最终结果
 *
 * @param <T> 源数据泛型
 * @param <R> 结果数据泛型
 */
class BeginNode<T, R> extends Node<T, R> {
    private final T value;


    public BeginNode(T value) {
        this.value = value;
    }

    @Override
    public void subscribe(Subscriber<T, R> actualSubscriber) {


        actualSubscriber.onSubscribe(new IntermediateSubscriber<T, R>(actualSubscriber) {

            @Override
            public void request() {

                if (value == null) {
                    actualSubscriber.onComplete(null);
                } else {
                    actualSubscriber.next(value);
                }

            }
        });
    }

}
