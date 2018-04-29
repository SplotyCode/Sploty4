package me.david.sploty4.dom.html;

import lombok.Getter;
import lombok.Setter;
import me.david.sploty4.document.SyntaxException;
import me.david.sploty4.dom.html.attriute.Attribute;
import me.david.sploty4.dom.html.attriute.StandardAttribute;
import me.david.sploty4.dom.html.attriute.ValueAttribute;
import me.david.sploty4.dom.html.nodes.TagHelper;
import me.david.sploty4.objects.IgnorePrint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Node implements Cloneable {

    @Getter private final String name;
    @Getter private final long id;

    @IgnorePrint private Node parent;
    private List<Node> childs;

    private Set<Attribute> attributes;

    public Node(String name, long id) {
        this.name = name;
        this.id = id;
        childs = new ArrayList<>();
        attributes = new HashSet<>();
    }

    public Node(String name, long id, Node parent) {
        this.name = name;
        this.id = id;
        this.parent = parent;
        childs = new ArrayList<>();
        attributes = new HashSet<>();
    }

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
        Node before = null;
        for (final Node node : parent.getChilds()) {
            if(node.getId() == id) return before;
            before = node;
        }
        return null;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (!getName().equals(node.getName())) return false;
        if (getParent() != null ? !getParent().equals(node.getParent()) : node.getParent() != null) return false;
        if (getChilds() != null ? !getChilds().equals(node.getChilds()) : node.getChilds() != null) return false;
        return getAttributes() != null ? getAttributes().equals(node.getAttributes()) : node.getAttributes() == null;
    }
}
