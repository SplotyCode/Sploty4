package me.david.sploty4.dom.css.selectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.david.sploty4.document.text.HtmlDocument;
import me.david.sploty4.dom.css.CSSSelector;
import me.david.sploty4.dom.html.StyleableNode;

@AllArgsConstructor
public abstract class CombinedSelector extends CSSSelector {

    @Getter @Setter private CSSSelector one, two;

    @Override public abstract boolean valid(StyleableNode node, HtmlDocument document);

}
