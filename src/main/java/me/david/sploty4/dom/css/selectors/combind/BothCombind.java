package me.david.sploty4.dom.css.selectors.combind;

import me.david.sploty4.document.text.HtmlDocument;
import me.david.sploty4.dom.css.CSSSelector;
import me.david.sploty4.dom.css.selectors.CombinedSelector;
import me.david.sploty4.dom.html.StyleableNode;

public class BothCombind extends CombinedSelector {

    public BothCombind(CSSSelector one, CSSSelector two) {
        super(one, two, ',');
    }

    @Override
    public boolean valid(StyleableNode node, HtmlDocument document) {
        return getOne().valid(node, document) && getTwo().valid(node, document);
    }
}
