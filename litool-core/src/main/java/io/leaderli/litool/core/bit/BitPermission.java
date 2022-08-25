package io.leaderli.litool.core.bit;

/**
 * Use binary to express permissions, use bitwise operations to modify permissions
 *
 * @author leaderli
 * @since 2022/8/16
 */
public class BitPermission<T> {

    /**
     * Constant class used to represent permission state
     * <p>
     * eg:
     * <pre>
     *    Modifier.FINAL
     * </pre>
     */
    private final Class<T> permission_constant_class;

    /**
     * A int to hold the permission token, save permission with binary position. 1 means permission, 0 means no permission
     */
    private int permissions;


    public BitPermission(Class<T> permission_constant_class) {
        this.permission_constant_class = permission_constant_class;
    }

    /**
     * Set the permissions
     *
     * @param permissions {@link #permissions}
     */
    public void set(int permissions) {
        this.permissions = permissions;
    }

    /**
     * Add one or more permission
     *
     * @param permissions {@link #permissions}
     */
    public void enable(int permissions) {
        this.permissions |= permissions;
    }

    /**
     * Remove one or more permission
     *
     * @param permissions {@link #permissions}
     */
    public void disable(int permissions) {
        this.permissions &= ~permissions;
    }

    /**
     * Have the permissions
     *
     * @param permissions {@link #permissions}
     * @return have the permissions
     */
    public boolean have(int permissions) {
        return (this.permissions & permissions) == permissions;
    }

    /**
     * Don't have the permissions
     *
     * @param permissions {@link #permissions}
     * @return don't have the permissions
     */
    public boolean miss(int permissions) {
        return (this.permissions & permissions) == 0;
    }

    /**
     * only have the permissions
     *
     * @param permissions {@link #permissions}
     * @return {@code this.permissions == permissions}
     */
    public boolean only(int permissions) {
        return this.permissions == permissions;
    }


    /**
     * Don't have any permission
     *
     * @return {@code permissions == 0 }
     */
    public boolean none() {
        return permissions == 0;
    }

    /**
     * @return {@code permissions != 0 }
     */
    public boolean any() {
        return permissions != 0;
    }

    @Override
    public String toString() {
        return permission_constant_class.getSimpleName() + ":" + BitStatus.of(permission_constant_class).beauty(permissions);
    }
}

