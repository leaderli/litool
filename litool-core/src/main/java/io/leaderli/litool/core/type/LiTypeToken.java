package io.leaderli.litool.core.type;

/**
 * @author leaderli
 * @since 2022/9/24 11:45 AM
 */
public interface LiTypeToken<T> {

    LiTypeToken<Void> TOKEN = new LiTypeToken<Void>() {
    };

    @SuppressWarnings("unchecked")
    static <T> LiTypeToken<T> of() {
        return (LiTypeToken<T>) TOKEN;
    }
}
