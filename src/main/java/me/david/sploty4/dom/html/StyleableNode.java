package me.david.sploty4.dom.html;

import com.google.common.collect.Lists;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.david.sploty4.dom.css.CSSRule;
import me.david.sploty4.layout.Layout;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class StyleableNode extends Node {

    @Getter @Setter private List<CSSRule> styleRules;

    private Layout layout;

    public StyleableNode(String name) {
        super(name);
        styleRules = new ArrayList<>();
    }

    public StyleableNode(String name, Node parent) {
        super(name, parent);
        styleRules = new ArrayList<>();
    }

    public StyleableNode(String name, List<CSSRule> styleRules) {
        super(name);
        this.styleRules = styleRules;
    }

    public StyleableNode(String name, Node parent, List<CSSRule> styleRules) {
        super(name, parent);
        this.styleRules = styleRules;
    }

    public void layout() {
        layout.layout(this);
    }

    public StyleableNode getFirstChild(){
        for(Node node : getChilds())
            if(node instanceof StyleableNode)
                return (StyleableNode) node;
        return null;
    }

    public StyleableNode getLastChild(){
        for(Node node : Lists.reverse(getChilds()))
            if(node instanceof StyleableNode)
                return (StyleableNode) node;
        return null;
    }
}
