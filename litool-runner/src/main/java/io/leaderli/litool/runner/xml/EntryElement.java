package io.leaderli.litool.runner.xml;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.dom.sax.BodyEvent;
import io.leaderli.litool.dom.sax.SaxBean;

import java.util.Objects;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public class EntryElement implements SaxBean {

    private String label;
    private String key;
    private String def = "";
    private String type = "str";


    public void setLabel(String label) {
        this.label = label;
    }


    @Override
    public void body(BodyEvent bodyEvent) {


        this.key = bodyEvent.description();
    }

    @Override
    public String name() {
        return "entry";
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntryElement that = (EntryElement) o;
        return Objects.equals(getKey(), that.getKey());
    }

    @Override
    public String toString() {
        return "EntryElement{" +
                "label='" + getLabel() + '\'' +
                ", key='" + getKey() + '\'' +
                ", def='" + getDef() + '\'' +
                ", type='" + getType() + '\'' +
                '}';
    }

    public String getLabel() {
        return label;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }


}
