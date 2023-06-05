package io.leaderli.litool.core.type;

/**
 * 该类用于封装一个对象和一个优先级，方便进行优先级排序
 *
 * @param <T> 对象类型
 */
public class ObjectPriority<T> {


    public final T object;
    public final int priority;

    public ObjectPriority(T object, int priority) {
        this.object = object;
        this.priority = priority;
    }

    @Override
    public int hashCode() {
        return object == null ? 0 : object.hashCode();
    }

    public T getObject() {
        return object;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof ObjectPriority) {
            Object compare = ((ObjectPriority<?>) obj).object;
            if (object == null) {
                return compare == null;
            }
            return object.equals(compare);
        }
        return false;
    }

    @Override
    public String toString() {
        return object + ":" + priority;
    }
}
