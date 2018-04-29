package me.david.sploty4.dom.css.selectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.david.sploty4.document.text.HtmlDocument;
import me.david.sploty4.dom.css.CSSSelector;
import me.david.sploty4.dom.html.StyleableNode;

@AllArgsConstructor
@NoArgsConstructor
public abstract class CombinedSelector extends CSSSelector {

    @Getter @Setter protected CSSSelector one, two;
    @Getter private char bindChar;

    @Override public abstract boolean valid(StyleableNode node, HtmlDocument document);

}
