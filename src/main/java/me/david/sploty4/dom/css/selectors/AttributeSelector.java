package me.david.sploty4.dom.css.selectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.david.sploty4.dom.css.CSSSelector;

@AllArgsConstructor
public abstract class AttributeSelector extends CSSSelector {

    @Getter @Setter protected boolean ignoreCase;
    @Getter protected final char operator;
    @Getter @Setter protected String attributeName, value;


}
