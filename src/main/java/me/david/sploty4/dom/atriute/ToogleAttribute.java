package me.david.sploty4.dom.atriute;

import me.david.sploty4.dom.DomParseException;

public class ToogleAttribute extends ValueAtriute<Boolean> {

    private boolean value;

    public ToogleAttribute(String name, String rawValue) {
        super(name, rawValue);
        rawValue = rawValue.toLowerCase();
        if(rawValue.equals("on")) value = true;
        else if(rawValue.equals("off")) value = false;
        else throw new DomParseException("Invalid Toggle value: '" + rawValue + "'");
    }

    @Override
    public Boolean setValue() {
        return null;
    }

    @Override
    public Boolean getValue() {
        return null;
    }
}
