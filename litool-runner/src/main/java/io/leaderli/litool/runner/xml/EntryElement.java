package io.leaderli.litool.runner.xml;

import io.leaderli.litool.dom.sax.BodyEvent;
import io.leaderli.litool.dom.sax.SaxBean;

import java.util.Objects;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public class EntryElement implements SaxBean {

    public String label;
    public String key;
    public String def = "";
    public String type = "str";


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
        return Objects.hash(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntryElement that = (EntryElement) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public String toString() {
        return "EntryElement{" +
                "label='" + label + '\'' +
                ", key='" + key + '\'' +
                ", def='" + def + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
