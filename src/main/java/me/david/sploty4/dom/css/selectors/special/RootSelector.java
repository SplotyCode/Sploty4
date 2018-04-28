package me.david.sploty4.dom.css.selectors.special;

import me.david.sploty4.document.text.HtmlDocument;
import me.david.sploty4.dom.css.DomCSSParser;
import me.david.sploty4.dom.css.selectors.SpecialSelector;
import me.david.sploty4.dom.html.StyleableNode;
import me.david.sploty4.dom.html.nodes.BaseNode;

public class RootSelector extends SpecialSelector {

    @Override
    public boolean startSelector(char c, DomCSSParser parser) {
        return parser.skipIfFollowIgnoreCase(":root");
    }

    @Override
    public boolean valid(StyleableNode node, HtmlDocument document) {
        return node instanceof BaseNode;
    }

}
