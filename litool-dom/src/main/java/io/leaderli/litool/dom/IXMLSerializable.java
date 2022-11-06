package io.leaderli.litool.dom;

import org.dom4j.dom.DOMDocument;
import org.dom4j.dom.DOMElement;

public interface IXMLSerializable {

    void writeToXML(DOMDocument document, DOMElement parent) throws XMLSerializationException;

    void readFromXML(DOMElement element) throws XMLSerializationException;
}
