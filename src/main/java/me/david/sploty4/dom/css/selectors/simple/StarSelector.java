package me.david.sploty4.dom.css.selectors.simple;

import lombok.NoArgsConstructor;
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

    public StarSelector() {super('*');}

    @Override
    public boolean valid(StyleableNode node, HtmlDocument document) {
        return true;
    }
}
