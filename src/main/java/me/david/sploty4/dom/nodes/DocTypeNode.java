package me.david.sploty4.dom.nodes;

import lombok.Getter;
import me.david.sploty4.dom.Node;

public class DocTypeNode extends Node {

    @Getter private final String doctype;

    public DocTypeNode(String doctype) {
        super("Doctype");
        this.doctype = doctype;
    }

}
