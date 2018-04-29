package me.david.sploty4.dom.css.selectors.combind;

import me.david.sploty4.document.text.HtmlDocument;
import me.david.sploty4.dom.css.CSSSelector;
import me.david.sploty4.dom.css.selectors.CombinedSelector;
import me.david.sploty4.dom.html.Node;
import me.david.sploty4.dom.html.StyleableNode;

public class GeneralSiblingSelector extends CombinedSelector {

    public GeneralSiblingSelector(CSSSelector one, CSSSelector two) {
        super(one, two, '~');
    }

    @Override
    public boolean valid(StyleableNode node, HtmlDocument document) {
        if (!two.valid(node, document) || node.getParent() == null) return false;
        for (Node child : node.getParent().getChilds()) {
            if (!(child instanceof StyleableNode)) continue;
            if (child.getId() == node.getId()) return false;
            if (one.valid((StyleableNode) child, document)) return true;
        }
        return false;
    }
}
