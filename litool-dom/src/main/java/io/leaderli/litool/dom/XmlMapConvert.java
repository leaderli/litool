package io.leaderli.litool.dom;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ClassUtil;
import org.dom4j.Attribute;
import org.dom4j.dom.DOMElement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/8/15 5:30 PM
 */
public class XmlMapConvert {

public static final String $NAME = "$name";
public static final String $BODY = "$body";
public static final String $CHILD = "$child";

public static Map<String, Object> read(DOMElement dom) {

    Map<String, Object> map = new LinkedHashMap<>();
    for (Attribute attribute : dom.attributes()) {
        map.put(attribute.getName(), attribute.getValue());
    }
    map.put($NAME, dom.getTagName());
    map.put($BODY, dom.getTextTrim());
    List<Map<String, Object>> children = new ArrayList<>();

    for (DOMElement child : LiDomUtil.elements(dom)) {
        children.add(read(child));
    }
    if (!children.isEmpty()) {
        map.put($CHILD, children);
    }
    return map;

}

public static DOMElement write(Map<String, Object> map) {
    String tag = (String) map.remove($NAME);
    String body = (String) map.remove($BODY);
    DOMElement element = new DOMElement(tag);
    element.setText(body);
    Lira<Map<String, Object>> children = Lino.of(map.remove($CHILD)).toLira()
            .cast(String.class, Object.class);

    for (Map<String, Object> child : children) {
        element.appendChild(write(child));
    }
    ClassUtil.filterCanCast(map, String.class, String.class)
            .forEach(element::setAttribute);
    return element;
}
}
