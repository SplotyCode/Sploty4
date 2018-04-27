package me.david.sploty4.dom.css.selectors;

import me.david.sploty4.dom.css.CSSSelector;
import me.david.sploty4.dom.css.DomCSSParser;

public abstract class SpecialSelector extends CSSSelector {


    public abstract boolean startSelector(char c, DomCSSParser parser);

}
