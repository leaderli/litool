package io.leaderli.litool.dom.sax;

/**
 * @author leaderli
 * @since 2022/7/9 10:32 AM
 * <p>
 * 用于指定名称 {@link #tag} 的复杂成员变量
 */
public abstract class SaxBean implements SaxEventHandler {

    public final String tag;
    protected String id = "";

    protected SaxBean(String tag) {
        this.tag = tag;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }
}
