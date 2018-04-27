package me.david.sploty4.dom.css.selectors.normal;

import me.david.sploty4.document.SyntaxException;
import me.david.sploty4.document.text.HtmlDocument;
import me.david.sploty4.dom.css.selectors.SimpleSelector;
import me.david.sploty4.dom.html.StyleableNode;
import me.david.sploty4.util.StringUtil;

public class StarSelector extends SimpleSelector {

    public StarSelector(String select) {
        super('*', null);
        if (!StringUtil.isEmpty(select)) throw new SyntaxException("Star Selector has no name attribute");
    }

    @Override
    public boolean valid(StyleableNode node, HtmlDocument document) {
        return true;
    }
}
