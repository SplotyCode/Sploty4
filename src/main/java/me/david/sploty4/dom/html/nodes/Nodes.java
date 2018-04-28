package me.david.sploty4.dom.html.nodes;

import me.david.sploty4.dom.html.DomHtmlParser;
import me.david.sploty4.dom.html.Node;

public enum Nodes {

    ;

    private final Node node;

    Nodes(Node node){
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public Node getNewNode() {
        return node.clone();
    }

    public static Node byName(String name, DomHtmlParser parser){
        for(Nodes node : values())
            if(node.getNode().getName().equals(name))
                return node.getNewNode();
        parser.setCurrentTagID(parser.getCurrentTagID()+1);
        return new Node(name, parser.getCurrentTagID());
    }
}
