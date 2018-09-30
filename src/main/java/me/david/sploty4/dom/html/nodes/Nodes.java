package me.david.sploty4.dom.html.nodes;

import me.david.sploty4.dom.html.Node;

import java.util.HashMap;
import java.util.Map;

public enum Nodes {

    TITLE(new TitleNode());

    private static Map<String, Nodes> map = new HashMap<>();

    static {
        for (Nodes node : values())
            map.put(node.name().toLowerCase(), node);
    }

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

    public static Node byName(String name) {
        Nodes objNode = map.get(name);
        if (objNode == null) {
            return new Node(name);
        }
        return objNode.getNewNode();
    }
}
