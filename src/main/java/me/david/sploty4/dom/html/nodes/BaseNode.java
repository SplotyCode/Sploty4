package me.david.sploty4.dom.html.nodes;

import me.david.sploty4.dom.html.StyleableNode;

public class BaseNode extends StyleableNode implements Cloneable {

    public BaseNode() {
        super("Base");
    }

    @Override
    public BaseNode clone() {
        return (BaseNode) super.clone();
    }
}
