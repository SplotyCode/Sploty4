package me.david.sploty4.dom.css.selectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.david.sploty4.dom.css.CSSSelector;

@AllArgsConstructor
public abstract class SimpleSelector extends CSSSelector {

    @Getter @Setter protected char indicator;
    @Getter @Setter protected String select;

}
