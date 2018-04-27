package me.david.sploty4.dom.css;

import me.david.sploty4.Sploty;
import me.david.sploty4.dom.DefaultStringDomParser;
import me.david.sploty4.dom.DomErrorReporter;
import me.david.sploty4.dom.DomReader;

import java.util.ArrayList;
import java.util.List;

public class DomCSSParser extends DefaultStringDomParser<List<CSSRule>, DomCSSParser> {

    private List<CSSRule> cssRules;

    @Override
    public List<CSSRule> parse(String input) {
        cssRules = new ArrayList<>();
        if (getErrorReporter() == null) {
            setErrorReporter(new DomErrorReporter());
            Sploty.getLogger().warn("Error Reporter was not set in CSS Parser");
        }
        while (getIndex() < getContent().length()){
            char c = getContent().charAt(getIndex());
            if(isLocked()) getLocked().readNext(c, this);
            else for(DomReader<DomCSSParser> reader : getActivReaders()) {
                try {
                    reader.readNext(c, this);
                }catch (Throwable throwable){
                    Sploty.getLogger().exception(throwable, "Exception was thrown (Index: " + getIndex() + " Line: " + getLine() + "): ");
                    getErrorReporter().report(throwable);
                }
                if(isSkipThis()) {
                    setSkipThis(false);
                    break;
                }
            }
            if(isRehandle()) setRehandle(false);
            else {
                if(c == '\n') setLine(getLine()+1);
                setIndex(getIndex()+1);
            }
        }
        for(DomReader<DomCSSParser> reader : getReaders())
            reader.parseDone();
        return cssRules;
    }

    @Override
    public DomReader<DomCSSParser>[] getReaders() {
        return new DomReader[0];
    }
}
