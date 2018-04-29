package me.david.sploty4.dom.css.selectors.attribute;

import lombok.NoArgsConstructor;
import me.david.sploty4.document.text.HtmlDocument;
import me.david.sploty4.dom.css.selectors.AttributeSelector;
import me.david.sploty4.dom.html.StyleableNode;

@NoArgsConstructor
public class HasAttribute extends AttributeSelector {

    public HasAttribute(boolean ignoreCase, String attributeName, String value) {
        super(ignoreCase, Character.MIN_VALUE, attributeName, value);
    }

    @Override
    public boolean valid(StyleableNode node, HtmlDocument document) {
        return node.hasAttribute(attributeName);
    }
}
