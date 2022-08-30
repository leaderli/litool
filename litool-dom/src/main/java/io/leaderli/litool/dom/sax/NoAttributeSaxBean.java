package io.leaderli.litool.dom.sax;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public abstract class NoAttributeSaxBean extends SaxBean {
    protected NoAttributeSaxBean(String tag) {
        super(tag);
    }

    @Override
    public void attribute(AttributeEvent attributeEvent) {
    }
}
