package me.david.sploty4.dom.css;

import me.david.sploty4.document.text.HtmlDocument;
import me.david.sploty4.dom.html.StyleableNode;

public abstract class CSSSelector {

    public abstract boolean valid(StyleableNode node, HtmlDocument document);

}
