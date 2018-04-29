package me.david.sploty4.dom.css.selectors.combind;

import me.david.sploty4.document.text.HtmlDocument;
import me.david.sploty4.dom.css.CSSSelector;
import me.david.sploty4.dom.css.selectors.CombinedSelector;
import me.david.sploty4.dom.html.Node;
import me.david.sploty4.dom.html.StyleableNode;

public class AdjacentSiblingSelector extends CombinedSelector {

    public AdjacentSiblingSelector(CSSSelector one, CSSSelector two) {
        super(one, two, '+');
    }

    @Override
    public boolean valid(StyleableNode node, HtmlDocument document) {
        Node other = node.getNodeBefore();
        return two.valid(node, document) &&
                other != null &&
                other instanceof StyleableNode &&
                one.valid((StyleableNode) other, document);
    }
}
