package me.david.sploty4.dom.html.nodes;

import lombok.Getter;
import me.david.sploty4.dom.html.Node;

public class DocTypeNode extends Node {

    @Getter private final String doctype;

    public DocTypeNode(String doctype, long id) {
        super("Doctype", id);
        this.doctype = doctype;
    }

    public DocTypeNode(String doctype, long id, Node parent) {
        super("Doctype", id, parent);
        this.doctype = doctype;
    }



}
