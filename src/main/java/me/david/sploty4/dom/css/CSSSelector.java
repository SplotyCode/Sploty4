package me.david.sploty4.dom.css;

import me.david.sploty4.document.text.HtmlDocument;
import me.david.sploty4.dom.css.selectors.SimpleSelector;
import me.david.sploty4.dom.html.StyleableNode;

public abstract class CSSSelector implements Cloneable {

    public abstract boolean valid(StyleableNode node, HtmlDocument document);

    @Override
    public CSSSelector clone() {
        try {
            return (CSSSelector) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

}
