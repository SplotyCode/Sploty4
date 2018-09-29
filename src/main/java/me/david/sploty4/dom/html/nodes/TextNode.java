package me.david.sploty4.dom.html.nodes;

import me.david.sploty4.dom.html.Node;
import me.david.sploty4.dom.html.attriute.Attribute;
import me.david.sploty4.dom.html.attriute.StandardAttribute;

import java.util.List;
import java.util.Set;

public class TextNode extends Node {

    public TextNode(String name) {
        super(name);
    }

    public TextNode(String name, Node parent) {
        super(name, parent);
    }

    @Override
    public List<Node> getChilds() {
        throw new InternalError("Method blocked for Text Nodes!");
    }

    @Override
    public Attribute getAttribute(String name) {
        throw new InternalError("Method blocked for Text Nodes!");
    }

    @Override
    public Set<Attribute> getAttributes() {
        throw new InternalError("Method blocked for Text Nodes!");
    }

    @Override
    public boolean hasAttribute(String name) {
        throw new InternalError("Method blocked for Text Nodes!");
    }

    @Override
    public StandardAttribute getStandardAttribute(String name) {
        throw new InternalError("Method blocked for Text Nodes!");
    }

    @Override
    public String getRawAttribute(String name) {
        throw new InternalError("Method blocked for Text Nodes!");
    }

    @Override
    public void setChilds(List<Node> childs) {
        throw new InternalError("Method blocked for Text Nodes!");
    }

}
