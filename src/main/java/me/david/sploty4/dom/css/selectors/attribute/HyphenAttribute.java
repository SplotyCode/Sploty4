package me.david.sploty4.dom.css.selectors.attribute;

import lombok.NoArgsConstructor;
import me.david.sploty4.document.text.HtmlDocument;
import me.david.sploty4.dom.css.selectors.AttributeSelector;
import me.david.sploty4.dom.html.StyleableNode;

@NoArgsConstructor
public class HyphenAttribute extends AttributeSelector {

    public HyphenAttribute(boolean ignoreCase, String attributeName, String value) {
        super(ignoreCase, '|', attributeName, value);
    }

    @Override
    public boolean valid(StyleableNode node, HtmlDocument document) {
        String nodeValue = node.getRawAttribute(attributeName).split("-")[0];
        return ignoreCase?nodeValue.equalsIgnoreCase(value):nodeValue.equals(value);
    }
}
