package io.leaderli.litool.dom;

import org.xml.sax.Locator;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author leaderli
 * @since 2022/7/14
 */
public class LocatorDefaultHandler extends DefaultHandler {
protected Locator locator;

@Override
public void setDocumentLocator(Locator locator) {
    this.locator = locator;
}
}
