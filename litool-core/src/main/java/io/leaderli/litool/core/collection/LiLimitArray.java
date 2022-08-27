package io.leaderli.litool.core.collection;

/**
 * a fixed-length  list, which excluding null or duplicate element。
 * when add  element that match the rules, it will add to the head of {@link #elements}
 * after all {@link #elements} move one right
 *
 * @param <T> the type of {@link  #elements}
 */
public class LiLimitArray<T> {

public final int size;
private final Object[] elements;

public LiLimitArray(int size) {
    this.size = size;
    elements = new Object[size];
}


/**
 * 当元素不为 null ， 且不包含该元素时，新增一个元素，原所有元素会右移一位，最后一位元素会直接被覆盖
 * when the element is not null and not {@link  #contains(Object)}, the
 * element will add to {@link  #elements} header after move all elements  one right
 *
 * @param t the element
 * @see #contains(Object)
 */
public void add(T t) {
    if (t == null || contains(t) || size == 0) {
        return;
    }
    System.arraycopy(elements, 0, elements, 1, size - 1);
    elements[0] = t;
}

/**
 * Return {@code true} if {@link  #elements} contains element
 *
 * @param t the element
 * @return {@code true} if {@link  #elements} contains element
 * <p>
 * 当  {@code t == null} 时 返回 false
 */
public boolean contains(T t) {
    if (t == null) {
        return false;
    }
    for (int i = 0; i < size; i++) {
        if (t.equals(elements[i])) {
            return true;
        }
    }
    return false;
}

/**
 * remove found element
 * <p>
 * Return {@code true} if  remove element
 *
 * @param t the element
 * @return {@code true} if  remove element
 */
public boolean remove(T t) {
    if (t == null) {
        return false;
    }
    for (int i = 0; i < size; i++) {

        if (t.equals(elements[i])) {
            fastRemove(i);
            return true;
        }
    }
    return false;
}

private void fastRemove(int index) {
    int numMoved = size - index - 1;
    if (numMoved > 0)
        System.arraycopy(elements, index + 1, elements, index, numMoved);
    elements[size - 1] = null;
}


}
