package me.david.sploty4.dom.html.nodes;

import me.david.sploty4.dom.html.Node;
import me.david.sploty4.gui.tab.TabHandler;

public class TitleNode extends Node {

    public TitleNode() {
        super("title");
    }

    @Override
    public void postDom(TabHandler tab) {
        StringBuilder builder = new StringBuilder();
        for (Node node : childs) {
            if (node instanceof TextNode) {
                builder.append(node.getName());
            }
        }
        tab.setTitle(builder.toString());
    }
}
