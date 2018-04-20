package me.david.sploty4.dom.atriute;

import me.david.sploty4.dom.DomParseException;

public class ToogleAttribute extends ValueAtriute<Boolean> {

    private boolean value;

    public ToogleAttribute(String name, String rawValue) {
        super(name);
        rawValue = rawValue.toLowerCase();
        if(rawValue.equals("on")) value = true;
        else if(rawValue.equals("off")) value = false;
        else throw new DomParseException("Invalid Toggle value: '" + rawValue + "'");
    }

    public ToogleAttribute(String name, Boolean value){
        super(name);
        this.value = value;
    }

    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public Boolean getValue() {
        return value;
    }
}
