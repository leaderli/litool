package io.leaderli.litool.dom.sax;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public abstract class NoBodyAndAttributeSaxBean extends NoAttributeSaxBean {

    public NoBodyAndAttributeSaxBean(String tag) {
        super(tag);
    }

    @Override
    public void body(BodyEvent bodyEvent) {
    }

}
