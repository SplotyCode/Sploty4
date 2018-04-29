package me.david.sploty4.dom.css.selectors.simple;

import lombok.NoArgsConstructor;
import me.david.sploty4.document.text.HtmlDocument;
import me.david.sploty4.dom.css.selectors.SimpleSelector;
import me.david.sploty4.dom.html.StyleableNode;

public class IDSelector extends SimpleSelector {

    public IDSelector(String select) {
        super('#', select);
    }
    public IDSelector() {
        super('#');
    }

    @Override
    public boolean valid(StyleableNode node, HtmlDocument document) {
        return node.hasAttribute("id") && node.getStandardAttribute("id").getValue().equalsIgnoreCase(select);
    }
}
