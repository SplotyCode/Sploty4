package me.david.sploty4.dom.html.nodes;

import me.david.sploty4.dom.html.Node;

public class BaseNode extends Node implements Cloneable {

    public BaseNode() {
        super("Base");
    }

    @Override
    public BaseNode clone() {
        return (BaseNode) super.clone();
    }
}
