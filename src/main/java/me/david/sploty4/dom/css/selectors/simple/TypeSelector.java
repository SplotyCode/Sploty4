package me.david.sploty4.dom.css.selectors.normal;

import me.david.sploty4.document.text.HtmlDocument;
import me.david.sploty4.dom.css.selectors.SimpleSelector;
import me.david.sploty4.dom.html.StyleableNode;

public class TypeSelector extends SimpleSelector {

    public TypeSelector(String select) {
        super(Character.MIN_VALUE, select.toLowerCase());
    }

    @Override
    public boolean valid(StyleableNode node, HtmlDocument document) {
        return node.getName().equals(select);
    }
}
