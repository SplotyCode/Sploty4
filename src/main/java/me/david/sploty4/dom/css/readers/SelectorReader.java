package me.david.sploty4.dom.css.readers;

import me.david.sploty4.dom.DomReader;
import me.david.sploty4.dom.css.CSSSelector;
import me.david.sploty4.dom.css.DomCSSParser;
import me.david.sploty4.dom.css.selectors.CSSSelectors;
import me.david.sploty4.dom.css.selectors.SimpleSelector;
import me.david.sploty4.dom.css.selectors.SpecialSelector;
import me.david.sploty4.dom.css.selectors.simple.TypeSelector;
import me.david.sploty4.util.StringUtil;

public class SelectorReader implements DomReader<DomCSSParser> {

    private enum State {

        START,
        ATTR_BEFOREKEY,
        ATTR_KEY,
        ATTR_AFTERKEY,
        ATTR_OPERATOR,
        ATTR_VALUE,
        ATTR_AFTERVALUE,
        NAME,
        AFTER_NAME

    }

    private State state = State.START;
    private CSSSelector current = null;

    @Override
    public void readNext(char c, DomCSSParser parser) throws RuntimeException {
        if (!parser.isLocked()) parser.setLocked(this);

        switch (state) {
            case START:
                if (StringUtil.isWhiteSpace(c)) return;
                for (SimpleSelector simpleSelector : CSSSelectors.getSimpleSelectors()) {
                    if (c == simpleSelector.getIndicator()) {
                        current = simpleSelector.clone();
                        state = State.NAME;
                        return;
                    }
                }
                for (SpecialSelector specialSelector : CSSSelectors.getSpecialSelectors()) {
                    if (specialSelector.startSelector(c, parser)) {
                        current = specialSelector.clone();
                        state = State.AFTER_NAME;
                        return;
                    }
                }
                if (c == '[') {
                    state = State.ATTR_BEFOREKEY;
                } else {
                    current = new TypeSelector();
                    state = State.NAME;
                }
                break;
        }
    }

    @Override
    public void parseDone() {

    }
}
