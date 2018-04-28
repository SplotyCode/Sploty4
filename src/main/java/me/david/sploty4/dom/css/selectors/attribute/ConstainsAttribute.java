package me.david.sploty4.dom.css.selectors.attribute;

import me.david.sploty4.document.text.HtmlDocument;
import me.david.sploty4.dom.css.selectors.AttributeSelector;
import me.david.sploty4.dom.html.StyleableNode;

public class ConstainsAttribute extends AttributeSelector {

    public ConstainsAttribute(boolean ignoreCase, String attributeName, String value) {
        super(ignoreCase, '*', attributeName, value);
    }

    @Override
    public boolean valid(StyleableNode node, HtmlDocument document) {
        String nodeValue = node.getRawAttribute(attributeName);
        if (ignoreCase) nodeValue = nodeValue.toLowerCase();
        return value.contains(nodeValue);
    }
}
