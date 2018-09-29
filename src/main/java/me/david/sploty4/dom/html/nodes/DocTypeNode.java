package me.david.sploty4.dom.html.nodes;

import lombok.Getter;
import me.david.sploty4.dom.html.Node;

public class DocTypeNode extends Node {

    @Getter private final String doctype;

    public DocTypeNode(String doctype) {
        super("Doctype");
        this.doctype = doctype;
    }

    public DocTypeNode(String doctype, Node parent) {
        super("Doctype", parent);
        this.doctype = doctype;
    }



}
