package me.david.sploty4.dom.html;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.david.sploty4.document.SyntaxException;
import me.david.sploty4.dom.html.attriute.Attribute;
import me.david.sploty4.dom.html.attriute.StandardAttribute;
import me.david.sploty4.dom.html.attriute.ValueAttribute;
import me.david.sploty4.dom.html.nodes.TagHelper;
import me.david.sploty4.gui.tab.TabHandler;
import me.david.sploty4.objects.IgnorePrint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode
public class Node implements Cloneable {

    @Getter private final String name;

    @IgnorePrint private Node parent;
    protected List<Node> childs;

    private Set<Attribute> attributes;

    public Node(String name) {
        this.name = name;
        childs = new ArrayList<>();
        attributes = new HashSet<>();
    }

    public Node(String name, Node parent) {
        this.name = name;
        this.parent = parent;
        childs = new ArrayList<>();
        attributes = new HashSet<>();
    }

    public void postDom(TabHandler tab) {}

    public Set<Attribute> getAttributes() {
        return attributes;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChilds() {
        return childs;
    }

    public void setChilds(List<Node> childs) {
        this.childs = childs;
    }

    public boolean hasAttribute(String name) {
        for(Attribute attribute : attributes)
            if(attribute.getName().equals(name))
                return true;
        return false;
    }

    public Attribute getAttribute(String name) {
        for(Attribute attribute : attributes)
            if(attribute.getName().equals(name))
                return attribute;
        return null;
    }

    public StandardAttribute getStandardAttribute(String name) {
        try {
            for(Attribute attribute : attributes)
                if(attribute.getName().equals(name))
                    return (StandardAttribute) attribute;
        } catch (ClassCastException ex) {
            throw new SyntaxException("Not right value type for '" + name + "'!");
        }
        return null;
    }

    public Node getNodeBefore() {
        if (parent == null) return null;
        return parent.getChilds().get(parent.getChilds().indexOf(this)  - 1);
    }

    public List<Node> getParents() {
        List<Node> parents = new ArrayList<>();
        if(parent == null) return parents;

        Node current = parent;
        while (current != null) {
            parents.add(current);
            current = current.getParent();
        }
        return parents;
    }

    public String getRawAttribute(String name) {
        for(Attribute attribute : attributes)
            if(attribute instanceof ValueAttribute)
                return ((ValueAttribute) attribute).getStringValue();
        return null;
    }

    @Override
    public Node clone() {
        try {
            return (Node) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    public boolean canSelfClose(){
        return TagHelper.canSelfClose(name);
    }

}
