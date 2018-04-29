package me.david.sploty4.dom.css.selectors.combind;

import me.david.sploty4.document.text.HtmlDocument;
import me.david.sploty4.dom.css.CSSSelector;
import me.david.sploty4.dom.css.selectors.CombinedSelector;
import me.david.sploty4.dom.html.Node;
import me.david.sploty4.dom.html.StyleableNode;

public class ChildSelector extends CombinedSelector {

    public ChildSelector(CSSSelector one, CSSSelector two) {
        super(one, two, '>');
    }

    @Override
    public boolean valid(StyleableNode node, HtmlDocument document) {
        return two.valid(node, document) &&
                node.getParent() != null &&
                node.getParent() instanceof StyleableNode &&
                one.valid((StyleableNode) node.getParent(), document);
    }
}
