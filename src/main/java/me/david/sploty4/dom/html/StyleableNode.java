package me.david.sploty4.dom.html;

import lombok.Getter;
import lombok.Setter;
import me.david.sploty4.dom.css.CSSRule;

import java.util.ArrayList;
import java.util.List;

public class StyleableNode extends Node {

    @Getter @Setter private List<CSSRule> styleRules;

    public StyleableNode(String name, long id) {
        super(name, id);
        styleRules = new ArrayList<>();
    }

    public StyleableNode(String name, long id, Node parent) {
        super(name, id, parent);
        styleRules = new ArrayList<>();
    }

    public StyleableNode(String name, long id, List<CSSRule> styleRules) {
        super(name, id);
        this.styleRules = styleRules;
    }

    public StyleableNode(String name, long id, Node parent, List<CSSRule> styleRules) {
        super(name, id, parent);
        this.styleRules = styleRules;
    }

    public StyleableNode getFirstChild(){
        for(Node node : getChilds())
            if(node instanceof StyleableNode)
                return (StyleableNode) node;
        return null;
    }

    public StyleableNode getLastChild(){
        for(Node node : getChilds())
            if(node instanceof StyleableNode)
                return (StyleableNode) node;
        return null;
    }
}
