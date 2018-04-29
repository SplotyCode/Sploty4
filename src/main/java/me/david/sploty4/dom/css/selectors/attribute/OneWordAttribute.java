package me.david.sploty4.dom.css.selectors.attribute;

import lombok.NoArgsConstructor;
import me.david.sploty4.document.text.HtmlDocument;
import me.david.sploty4.dom.css.selectors.AttributeSelector;
import me.david.sploty4.dom.html.StyleableNode;

@NoArgsConstructor
public class OneWordAttribute extends AttributeSelector {

    public OneWordAttribute(boolean ignoreCase, String attributeName, String value) {
        super(ignoreCase, '~', attributeName, value);
    }

    @Override
    public boolean valid(StyleableNode node, HtmlDocument document) {
        String nodeValue = node.getRawAttribute(attributeName);
        if (ignoreCase) nodeValue = nodeValue.toLowerCase();
        String[] split = nodeValue.split(" ");
        for (String word : split)
            if (word.equals(value))
                return true;
        return false;
    }
}
