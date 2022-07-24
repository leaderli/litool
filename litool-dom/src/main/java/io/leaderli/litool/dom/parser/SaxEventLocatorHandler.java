package io.leaderli.litool.dom.parser;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.LocatorDefaultHandler;
import io.leaderli.litool.dom.sax.*;
import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public class SaxEventLocatorHandler<T extends SaxBean> extends LocatorDefaultHandler {


    private final List<SaxEvent> saxEventList = new ArrayList<>();


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        this.saxEventList.add(new StartEvent(this.locator, qName));

        for (int i = 0; i < attributes.getLength(); i++) {
            String name = attributes.getQName(i);
            String value = attributes.getValue(i);
            this.saxEventList.add(new AttributeEvent(this.locator, name, value));
        }

    }


    @Override
    public void endElement(String uri, String localName, String qName) {
        this.saxEventList.add(new EndEvent(this.locator, qName));
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        String trim = new String(ch, start, length).trim();

        if (StringUtils.isEmpty(trim)) {
            return;
        }


        // 多个连续 body 片段合为一起
        Lira.of(this.saxEventList)
                .last()
                .cast(BodyEvent.class)
                .or(() -> {
                    BodyEvent bodyEvent = new BodyEvent(this.locator);
                    this.saxEventList.add(bodyEvent);
                    return bodyEvent;
                })
                .ifPresent(body -> body.append(trim));
    }


    public List<SaxEvent> getSaxEventList() {
        return new ArrayList<>(this.saxEventList);
    }
}
