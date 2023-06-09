package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.Lira;

import java.util.Iterator;
import java.util.List;

/**
 * 一个固定长度的列表，不包含 null 或重复元素。
 * <p>
 * 当添加符合条件的元素时，元素会添加到 {@link #elements} 的头部，并且所有元素向右移动一位。
 *
 * @param <T> {@link #elements} 的类型
 */
public class LiFixedSizeSet<T> implements ToList<T, List<T>>, ToArray<T>, Iterable<T> {

    /**
     * 列表的长度
     */
    public final int size;
    private final T[] elements;

    private final boolean allowReorderIndex;

    /**
     * @param size -
     */
    public LiFixedSizeSet(int size) {
        this(size, false);
    }

    /**
     * @param size              -
     * @param allowReorderIndex 是否允许add时，碰到重复元素时重新调整顺序
     */
    @SuppressWarnings("unchecked")
    public LiFixedSizeSet(int size, boolean allowReorderIndex) {
        LiAssertUtil.assertTrue(size > 0, IllegalArgumentException::new, "size is less than 1");
        this.size = size;
        this.elements = (T[]) new Object[size];
        this.allowReorderIndex = allowReorderIndex;
    }


    /**
     * 当元素不为 null，且不包含在列表中时，将新元素添加到列表中。
     * 添加成功后，所有元素向右移动一位，最后一位元素将被覆盖。
     *
     * @param element 待添加元素
     * @see #contains(Object)
     */
    public void add(T element) {
        if (element == null) {
            return;
        }
        if (contains(element)) {
            if (allowReorderIndex) {
                remove(element);
            } else {
                return;
            }
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


    /**
     * @param type 转换为指定类型的数组
     * @return 新数组
     */
    @Override
    public T[] toArray(Class<T> type) {
        return Lira.of(elements).toArray(type);
    }

    /**
     * @return 转换成 ArrayList
     */
    @Override
    public List<T> toList() {
        return Lira.of(elements).get();
    }

    @Override
    public Iterator<T> iterator() {
        return Lira.of(elements).iterator();
    }
}
