package io.leaderli.litool.dom;


import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.resource.ResourceUtil;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.dom.DOMDocument;
import org.dom4j.dom.DOMDocumentFactory;
import org.dom4j.dom.DOMElement;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.List;

/**
 * operate xml
 * <p>
 * using library dom4j and jaxen
 */
public class LiDomUtil {


    @SuppressWarnings("java:S106")
    private static final PrintStream LOGGER = System.out;

    public static DOMElement getDOMRootByPath(String path) throws DocumentException {
        return (DOMElement) getDOMDocumentByPath(path).getRootElement();

    }

    public static DOMDocument getDOMDocumentByPath(String path) throws DocumentException {
        return (DOMDocument) getSAXReader().read(ResourceUtil.getResourceAsStream(path));

    }

    private static SAXReader getSAXReader() {
        SAXReader saxReader = new SAXReader(DOMDocumentFactory.getInstance(), false);
        try {
            saxReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return saxReader;
    }

    public static DOMElement getDOMRootByInputStream(InputStream inputStream) throws DocumentException {
        return (DOMElement) getDOMDocumentByInputStream(inputStream).getRootElement();

    }

    public static DOMDocument getDOMDocumentByInputStream(InputStream inputStream) throws DocumentException {
        return (DOMDocument) getSAXReader().read(inputStream);

    }

    public static DOMElement getDOMRootByString(String xml) throws DocumentException {
        return (DOMElement) getDOMDocumentByString(xml).getRootElement();

    }

    public static DOMDocument getDOMDocumentByString(String xml) throws DocumentException {
        return (DOMDocument) getSAXReader().read(new StringReader(xml));

    }

    /**
     * @param element a {@code DOMElement}
     * @return all child node of element
     */
    public static List<DOMElement> selectNodes(DOMElement element) {

        return Lira.of(element.selectNodes("child::*")).cast(DOMElement.class).getRaw();

    }

    /**
     * @param element a {@code DOMElement}
     * @param xpath   -
     * @return the child node query by xpath from element
     */
    public static List<DOMElement> selectNodes(DOMElement element, String xpath) {

        return Lira.of(element.selectNodes(xpath)).cast(DOMElement.class).getRaw();
    }

    /**
     * @param element a {@code DOMElement}
     * @param xpath   -
     * @return the first child node query by xpath from element
     */
    public static DOMElement selectSingleNode(DOMElement element, String xpath) {

        return (DOMElement) element.selectSingleNode(xpath);
    }

    public static Lira<DOMElement> elements(DOMElement element) {

        return Lira.of(element.elements()).cast(DOMElement.class);
    }

    public static DOMElement element(DOMElement element, String name) {
        return (DOMElement) element.element(name);
    }

    public static void prettyPrint(Node node) {
        try {
            //document
            StringWriter writer = new StringWriter();
            XMLWriter xmlWriter = new XMLWriter(writer, OutputFormat.createPrettyPrint());
            xmlWriter.write(node);
            xmlWriter.close();
            LOGGER.println(writer);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param node the xml node
     * @return the pretty format content of node
     */
    public static String pretty(Node node) {
        StringWriter writer = new StringWriter();
        XMLWriter xmlWriter = new XMLWriter(writer, OutputFormat.createPrettyPrint());
        try {
            xmlWriter.write(node);
            xmlWriter.close();
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
