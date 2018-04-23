package me.david.sploty4.dom.html.attriute;

public abstract class ValueAtriute<T> extends Attribute {

    public ValueAtriute(String name) {
        super(name);
    }

    public abstract void setValue(T value);
    public abstract T getValue();
}
