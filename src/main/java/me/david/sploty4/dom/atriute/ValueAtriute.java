package me.david.sploty4.dom.atriute;

public abstract class ValueAtriute<T> extends Attribute {

    public ValueAtriute(String name, String rawValue) {
        super(name);
    }

    public abstract T setValue();
    public abstract T getValue();
}
