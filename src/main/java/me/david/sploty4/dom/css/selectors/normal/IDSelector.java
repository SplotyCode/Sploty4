package me.david.sploty4.dom.css.selectors.normal;

import me.david.sploty4.document.text.HtmlDocument;
import me.david.sploty4.dom.css.selectors.SimpleSelector;
import me.david.sploty4.dom.html.StyleableNode;

public class IDSelector extends SimpleSelector {

    public IDSelector(String select) {
        super('#', select);
    }

    @Override
    public boolean valid(StyleableNode node, HtmlDocument document) {
        return node.hasAttribute("id") && node.getStandartAttribute("id").getValue().equalsIgnoreCase(select);
    }
}
