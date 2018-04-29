package me.david.sploty4.dom.css.selectors.simple;

import lombok.NoArgsConstructor;
import me.david.sploty4.document.text.HtmlDocument;
import me.david.sploty4.dom.css.selectors.SimpleSelector;
import me.david.sploty4.dom.html.StyleableNode;

public class TypeSelector extends SimpleSelector implements Cloneable {

    public TypeSelector(String select) {
        super(Character.MIN_VALUE, select.toLowerCase());
    }

    public TypeSelector() {
        super(Character.MIN_VALUE);
    }

    @Override
    public boolean valid(StyleableNode node, HtmlDocument document) {
        return node.getName().equals(select);
    }

}
