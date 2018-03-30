package me.david.sploty4.dom;

public class RenderedNode extends Node {

    public RenderedNode(String name) {
        super(name);
    }

    public RenderedNode(String name, Node parent) {
        super(name, parent);
    }

    public RenderedNode getFirstChild(){
        for(Node node : getChilds())
            if(node instanceof RenderedNode)
                return (RenderedNode) node;
        return null;
    }

    public RenderedNode getLastChild(){
        for(Node node : getChilds())
            if(node instanceof RenderedNode)
                return (RenderedNode) node;
        return null;
    }
}
