package io.leaderli.litool.runner.xml;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.LiConstant;
import io.leaderli.litool.core.text.StringConvert;
import io.leaderli.litool.dom.sax.BodyEvent;
import io.leaderli.litool.dom.sax.EndEvent;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.TypeAlias;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public class EntryElement implements SaxBean {

    private String label;
    private String key;
    private String def = "";
    private String type = "str";

    @Override
    public void body(BodyEvent bodyEvent) {


        String key = bodyEvent.description();
        LiAssertUtil.assertTrue(key.matches(LiConstant.ENTRY_NAME_RULE), String.format("the entry key %s is not match %s", key, LiConstant.ENTRY_NAME_RULE));
        this.key = key;
    }

    @Override
    public void end(EndEvent endEvent) {


        StringConvert.parser(TypeAlias.getType(this.type), def).assertNotNone(String.format("the def value %s cannot satisfied the entry type %s", def, type));
        SaxBean.super.end(endEvent);

    }

    @Override
    public String name() {
        return "entry";
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

    public void setLabel(String label) {
        this.label = label;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

        LiAssertUtil.assertTrue(TypeAlias.support(type), String.format("the entry type  %s is unsupported ", type));
        this.type = type;
    }

}
