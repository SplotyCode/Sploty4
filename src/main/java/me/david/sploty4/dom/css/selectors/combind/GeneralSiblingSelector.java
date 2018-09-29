package me.david.sploty4.dom.css.selectors.combind;

import lombok.NoArgsConstructor;
import me.david.sploty4.document.text.HtmlDocument;
import me.david.sploty4.dom.css.CSSSelector;
import me.david.sploty4.dom.css.selectors.CombinedSelector;
import me.david.sploty4.dom.html.Node;
import me.david.sploty4.dom.html.StyleableNode;

@NoArgsConstructor
public class GeneralSiblingSelector extends CombinedSelector {

    public GeneralSiblingSelector(CSSSelector one, CSSSelector two) {
        super(one, two, '~');
    }

    @Override
    public boolean valid(StyleableNode node, HtmlDocument document) {
        if (!two.valid(node, document) || node.getParent() == null) return false;
        int index = node.getParent().getChilds().indexOf(node);
        for (int i = index; i < node.getParent().getChilds().size(); i++) {
            Node child = node.getParent().getChilds().get(i);
            if (!(child instanceof StyleableNode)) continue;
            if (one.valid((StyleableNode) child, document)) return true;
        }
        return false;
    }
}
