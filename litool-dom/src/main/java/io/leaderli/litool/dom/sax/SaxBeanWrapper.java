package io.leaderli.litool.dom.sax;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public class SaxBeanWrapper implements Runnable {

    public static final Runnable EMPTY = () -> {
    };
    public final SaxBean sax;
    /**
     * 用于在 {@link EndEvent} 中回调
     */
    private final Runnable runnable;

    private SaxBeanWrapper(SaxBean sax, Runnable runnable) {
        this.sax = sax;
        this.runnable = runnable;
    }

    public static SaxBeanWrapper of(SaxBean sax, Runnable runnable) {
        return new SaxBeanWrapper(sax, runnable);
    }

    public static SaxBeanWrapper of(SaxBean sax) {
        return new SaxBeanWrapper(sax, EMPTY);
    }

    @Override
    public void run() {
        this.runnable.run();
    }
}
