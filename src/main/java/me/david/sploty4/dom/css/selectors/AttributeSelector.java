package me.david.sploty4.dom.css.selectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.david.sploty4.dom.css.CSSSelector;

@AllArgsConstructor
@NoArgsConstructor
public abstract class AttributeSelector extends CSSSelector {

    @Getter @Setter protected boolean ignoreCase;
    @Getter protected char operator;
    @Getter @Setter protected String attributeName, value;


}
