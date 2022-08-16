package io.leaderli.litool.core.bit;

/**
 * @author leaderli
 * @since 2022/8/16
 */
public class BitPermission<T> {

    private final Class<T> status_class;

    // 存储目前的权限状态
    private int state;

    public BitPermission(Class<T> status_class) {
        this.status_class = status_class;
    }

    /**
     * 重新设置权限
     */
    public void init(int permission) {
        state = permission;
    }

    /**
     * 添加一项或多项权限
     */
    public void enable(int permission) {
        state |= permission;
    }

    /**
     * 删除一项或多项权限
     */
    public void disable(int permission) {
        state &= ~permission;
    }

    /**
     * 是否拥某些权限
     */
    public boolean have(int permission) {
        return (state & permission) == permission;
    }

    /**
     * 是否禁用了某些权限
     */
    public boolean miss(int permission) {
        return (state & permission) == 0;
    }

    /**
     * 是否仅仅拥有某些权限
     */
    public boolean only(int permission) {
        return state == permission;
    }


    public boolean none() {
        return state == 0;
    }

    public boolean any() {
        return !none();
    }

    @Override
    public String toString() {
        return status_class.getSimpleName() + ":" + BitStatus.of(status_class).beauty(state);
    }
}

