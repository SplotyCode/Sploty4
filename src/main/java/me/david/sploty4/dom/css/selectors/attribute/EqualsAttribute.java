package me.david.sploty4.dom.css.selectors.attribute;

import me.david.sploty4.document.text.HtmlDocument;
import me.david.sploty4.dom.css.selectors.AttributeSelector;
import me.david.sploty4.dom.html.StyleableNode;

public class EqualsAttribute extends AttributeSelector {

    public EqualsAttribute(boolean ignoreCase, String attributeName, String value) {
        super(ignoreCase, Character.MIN_VALUE, attributeName, value);
    }

    @Override
    public boolean valid(StyleableNode node, HtmlDocument document) {
        String nodeValue = node.getRawAttribute(attributeName);
        return ignoreCase?nodeValue.equalsIgnoreCase(value):nodeValue.equals(value);
    }
}
