package me.david.sploty4.dom.html.attriute;

public abstract class ValueAttribute<T> extends Attribute {

    public ValueAttribute(String name) {
        super(name);
    }

    public abstract void setValue(T value);
    public abstract T getValue();
    public abstract String getStringValue();

}
