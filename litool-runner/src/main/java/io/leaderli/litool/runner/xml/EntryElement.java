package io.leaderli.litool.runner.xml;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.LiConstant;
import io.leaderli.litool.core.text.StringConvert;
import io.leaderli.litool.dom.sax.BodyEvent;
import io.leaderli.litool.dom.sax.EndEvent;
import io.leaderli.litool.runner.TypeAlias;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public class EntryElement extends SaxBeanWithID {

    private String label;
    private String key;
    private String def = "";
    private String type = "str";

    public EntryElement() {
        super("");
    }

    @Override
    public void body(BodyEvent bodyEvent) {


        String key = bodyEvent.description();
        LiAssertUtil.assertTrue(key.matches(LiConstant.ATTRIBUTE_NAME_RULE), String.format("the entry key %s is not match %s", key, LiConstant.ATTRIBUTE_NAME_RULE));
        this.key = key;
    }

    @Override
    public void end(EndEvent endEvent) {
        super.end(endEvent);
        StringConvert.parser(TypeAlias.getType(this.type), def).assertNotNone(String.format("the def value %s cannot satisfied the entry type %s", def, type));
    }

    @Override
    public String tag() {
        return "entry";
    }

    @Override
    public String toString() {
        return "EntryElement{" +
                "id='" + getId() + '\'' +
                ", label='" + label + '\'' +
                ", key='" + key + '\'' +
                ", def='" + def + '\'' +
                ", type='" + type + '\'' +
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
