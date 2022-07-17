package io.leaderli.litool.core.collection;

/**
 * 一个简单的定长的 List , 其中不包括 null 或者重复元素
 *
 * @param <T> 泛型
 */
public class LiLimitArray<T> {

    public final int size;
    private final Object[] data;

    public LiLimitArray(int size) {
        this.size = size;
        data = new Object[size];
    }


    /**
     * 当元素不为 null ， 且不包含该元素时，新增一个元素，原所有元素会右移一位，最后一位元素会直接被覆盖
     *
     * @param t 新元素
     * @see #contains(Object)
     */
    public void add(T t) {
        if (t == null || contains(t)) {
            return;
        }
        System.arraycopy(data, 0, data, 1, size - 1);
        data[0] = t;
    }

    /**
     * @param t 元素
     * @return 元素是否存在
     * <p>
     * 当  {@code t == null} 时 返回 false
     */
    public boolean contains(T t) {
        if (t == null) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (t.equals(data[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 删除查找到的元素，同时将右边所有元素左移一位
     *
     * @param t 需要删除的元素
     * @return 是否删除了元素
     */
    public boolean remove(T t) {
        if (t == null) {
            return false;
        }
        for (int i = 0; i < size; i++) {

            if (t.equals(data[i])) {
                fastRemove(i);
                return true;
            }
        }
        return false;
    }

    private void fastRemove(int index) {
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(data, index + 1, data, index,
                    numMoved);
        data[size - 1] = null;
    }


}
