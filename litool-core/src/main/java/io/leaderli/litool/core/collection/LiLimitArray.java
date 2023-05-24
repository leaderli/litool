package io.leaderli.litool.core.collection;

import java.util.ArrayList;

/**
 * 一个固定长度的列表，不包含 null 或重复元素。
 * <p>
 * 当添加符合条件的元素时，元素会添加到 {@link #elements} 的头部，并且所有元素向右移动一位。
 *
 * @param <T> {@link #elements} 的类型
 */
public class LiLimitArray<T> implements ToList<T, ArrayList<T>>, ToArray<T> {

    public final int size;
    private final T[] elements;

    @SuppressWarnings("unchecked")
    public LiLimitArray(int size) {
        this.size = size;
        elements = (T[]) new Object[size];
    }


    /**
     * 当元素不为 null，且不包含在列表中时，将新元素添加到列表中。
     * 添加成功后，所有元素向右移动一位，最后一位元素将被覆盖。
     *
     * @param element 待添加元素
     * @see #contains(Object)
     */
    public void add(T element) {
        if (element == null || contains(element) || size == 0) {
            return;
        }
        System.arraycopy(elements, 0, elements, 1, size - 1);
        elements[0] = element;
    }

    /**
     * 判断列表中是否包含指定元素。
     *
     * @param element 待查找元素
     * @return 如果找到元素返回 true，否则返回 false。
     * 当 {@code element == null} 时，返回 false。
     */
    public boolean contains(T element) {
        if (element == null) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (element.equals(elements[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 移除已找到的元素。
     *
     * @param element 待移除元素
     * @return 如果成功移除元素返回 true，否则返回 false。
     */
    public boolean remove(T element) {
        if (element == null) {
            return false;
        }
        for (int i = 0; i < size; i++) {

            if (element.equals(elements[i])) {
                fastRemove(i);
                return true;
            }
        }
        return false;
    }

    private void fastRemove(int index) {
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[size - 1] = null;
    }


    @Override
    public T[] toArray(Class<T> type) {
        return ArrayUtils.convertToTargetArray(type, (Object[]) elements);
    }

    @Override
    public ArrayList<T> toList() {
        return CollectionUtils.toList(elements);
    }
}
