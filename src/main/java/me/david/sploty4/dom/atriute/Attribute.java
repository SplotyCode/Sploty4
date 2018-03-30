package me.david.sploty4.dom.atriute;

public abstract class Attribute {

    private final String name;

    protected Attribute(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
