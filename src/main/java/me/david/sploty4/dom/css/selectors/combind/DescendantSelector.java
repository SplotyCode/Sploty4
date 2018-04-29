package me.david.sploty4.dom.css.selectors.combind;

import me.david.sploty4.document.text.HtmlDocument;
import me.david.sploty4.dom.css.CSSSelector;
import me.david.sploty4.dom.css.selectors.CombinedSelector;
import me.david.sploty4.dom.html.Node;
import me.david.sploty4.dom.html.StyleableNode;

public class DescendantSelector extends CombinedSelector {

    public DescendantSelector(CSSSelector one, CSSSelector two) {
        super(one, two, ' ');
    }

    @Override
    public boolean valid(StyleableNode node, HtmlDocument document) {
        if (two.valid(node, document)) return false;
        for (final Node parent : node.getParents())
            if (parent instanceof StyleableNode && one.valid((StyleableNode) parent, document))
                return true;
        return false;
    }
}
