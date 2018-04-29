package me.david.sploty4.dom.css.selectors;

import lombok.Getter;
import me.david.sploty4.dom.css.selectors.attribute.*;
import me.david.sploty4.dom.css.selectors.combind.AdjacentSiblingSelector;
import me.david.sploty4.dom.css.selectors.combind.ChildSelector;
import me.david.sploty4.dom.css.selectors.combind.DescendantSelector;
import me.david.sploty4.dom.css.selectors.combind.GeneralSiblingSelector;
import me.david.sploty4.dom.css.selectors.simple.ClassSelector;
import me.david.sploty4.dom.css.selectors.simple.IDSelector;
import me.david.sploty4.dom.css.selectors.simple.StarSelector;
import me.david.sploty4.dom.css.selectors.simple.TypeSelector;
import me.david.sploty4.dom.css.selectors.special.RootSelector;

public final class CSSSelectors {

    @Getter private static final SimpleSelector[] simpleSelectors = new SimpleSelector[] {
            new ClassSelector(), new IDSelector(),
            new StarSelector()
    };

    @Getter private static final AttributeSelector[] attributeSelectors = new AttributeSelector[] {
            new BeginWithAttribute(), new ConstainsAttribute(),
            new EndWithAttribute(), new EqualsAttribute(),
            new HyphenAttribute(), new OneWordAttribute()
    };

    @Getter private static final SpecialSelector[] specialSelectors = new SpecialSelector[] {
            new RootSelector()
    };

    @Getter private static final CombinedSelector[] combinedSelectors = new CombinedSelector[] {
            new AdjacentSiblingSelector(), new ChildSelector(),
            new GeneralSiblingSelector()
    };

    @Getter private static final TypeSelector typeSelector = new TypeSelector();
    @Getter private static final HasAttribute hasAttributeSelector = new HasAttribute();
    @Getter private static final DescendantSelector descendantSelector = new DescendantSelector();

}
