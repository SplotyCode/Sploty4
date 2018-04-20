package me.david.sploty4.dom.nodes;

import me.david.sploty4.dom.Node;

public class BaseNode extends Node implements Cloneable {

    public BaseNode() {
        super("Base");
    }

    @Override
    public BaseNode clone() {
        return (BaseNode) super.clone();
    }
}
