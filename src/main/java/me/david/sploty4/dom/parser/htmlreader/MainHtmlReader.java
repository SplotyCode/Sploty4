package me.david.sploty4.dom.parser.htmlreader;

import me.david.sploty4.document.SyntaxException;
import me.david.sploty4.dom.Node;
import me.david.sploty4.dom.atriute.Attribute;
import me.david.sploty4.dom.atriute.AttributeHelper;
import me.david.sploty4.dom.atriute.StandartAttribute;
import me.david.sploty4.dom.atriute.ToogleAttribute;
import me.david.sploty4.dom.nodes.Nodes;
import me.david.sploty4.dom.nodes.TextNode;
import me.david.sploty4.dom.parser.DomHtmlParser;
import me.david.sploty4.dom.parser.DomReader;
import me.david.sploty4.util.StringUtil;

import java.rmi.UnexpectedException;

public class MainHtmlReader implements DomReader<DomHtmlParser> {

    private State state = State.TEXT;

    private String name = "", value = "", text = "";
    private char endChar;

    private Node atributeNode = null;

    @Override
    public void readNext(char c, DomHtmlParser parser) {
        switch (state){
            case TEXT:
                if(c == '<') {
                    parser.getCurrentParent().getChilds().add(new TextNode(text, parser.getCurrentParent()));
                    state = State.TAGNAME;
                    text = "";
                }
                else text += c;
                break;
            case TAGNAME:
                if(c == ' '){
                    atributeNode = Nodes.byName(name.toLowerCase());
                    atributeNode.setParent(parser.getCurrentParent());
                    name = "";
                    state = State.AFTER_TAGNAME;
                    parser.getCurrentParent().getChilds().add(atributeNode);
                    parser.setCurrentParent(atributeNode);
                    atributeNode = null;
                }else if(c == '>') {
                    Node node = Nodes.byName(name.toLowerCase());
                    node.setParent(parser.getCurrentParent());
                    parser.getCurrentParent().getChilds().add(node);
                    parser.setCurrentParent(node);
                    name = "";
                    state = State.TEXT;
                } else if (c == '/'){
                    state = State.CLOSESTART;
                } else name += c;
                break;
            case AFTER_TAGNAME:
                if (c == '>') {
                    atributeNode = null;
                    state = State.TEXT;
                } else if (c == '/'){
                    state = State.AUTOCLOSE;
                } else if (StringUtil.isNoWhiteSpace(c)) {
                    state = State.ATRIBUTE_NAME;
                    parser.rehandle();
                }
                break;
            case ATRIBUTE_NAME:
                if (StringUtil.isWhiteSpace(c)) {
                    state = State.AFTER_ATRIBUTE_NAME;
                } else if (c == '>'){
                    parser.getCurrentParent().getAttributes().add(new Attribute(name.toLowerCase()));
                    name = "";
                } else if (c == '/'){
                    state = State.AUTOCLOSE;
                } else name += c;
                break;
            case AFTER_ATRIBUTE_NAME:
                if (c == '>'){
                    parser.getCurrentParent().getAttributes().add(new Attribute(name.toLowerCase()));
                    name = "";
                } else if (c == '/'){
                    state = State.AUTOCLOSE;
                } else if (c == '='){
                    state = State.AFTERQUALS;
                } else if (StringUtil.isNoWhiteSpace(c)){
                    parser.getCurrentParent().getAttributes().add(new Attribute(name.toLowerCase()));
                    name = "";
                    state = State.AFTER_TAGNAME;
                    parser.rehandle();
                }
                break;
            case AFTERQUALS:
                if (c == '"' || c == '\''){
                    endChar = c;
                    state = State.VALUE;
                } else if (StringUtil.isNoWhiteSpace(c)) {
                    state = State.VALUE;
                    endChar = Character.MIN_VALUE;
                }
                break;
            case VALUE:
                if (c == endChar || (endChar == Character.MIN_VALUE &&
                        (StringUtil.isWhiteSpace(c) || c == '>' || c == '/'))) {
                    name = name.toLowerCase();
                    value = value.toLowerCase();
                    if (AttributeHelper.isBoolean(name.toLowerCase()))
                        parser.getCurrentParent().getAttributes().add(new ToogleAttribute(name, value));
                    else parser.getCurrentParent().getAttributes().add(new StandartAttribute(name, value));
                    state = c == '/'?State.AUTOCLOSE:State.AFTER_TAGNAME;
                    if (AttributeHelper.isSelftClosing(name)) throw new SyntaxException("Attribute '" + name + "' for tag '" + parser.getCurrentParent().getName() + "' should not have a value!");
                } else value += c;
                break;
            case AUTOCLOSE:
                if (c == '>') {
                    /* Go one element back */
                    parser.setCurrentParent(parser.getCurrentParent().getParent());
                } else if (StringUtil.isNoWhiteSpace(c)){
                    throw new SyntaxException("Expected > but not '" + c + "'!");
                }
                break;
            case CLOSESTART:
                if (StringUtil.isNoWhiteSpace(c)) {
                    parser.rehandle();
                    state = State.CLOSENAME;
                }
                break;
            case CLOSENAME:
                if (StringUtil.isWhiteSpace(c)) {
                    state = State.CLOSEFINISHED;
                } else name += c;
                break;
            case CLOSEFINISHED:
                if (c == '>') {
                    if(name.toLowerCase().equals(parser.getCurrentParent().getName())) throw new SyntaxException("Closed '" + name + "' without closing '" + parser.getCurrentParent().getName() + "'!");
                    parser.setCurrentParent(parser.getCurrentParent().getParent());
                    name = "";
                } else if (StringUtil.isNoWhiteSpace(c)) throw new SyntaxException("Expected > but not ' + c + '!");
                break;
        }
    }

    @Override
    public void parseDone() {
        if (StringUtil.isEmpty(text) ||
           StringUtil.isEmpty(name) ||
           StringUtil.isEmpty(value) ||
           atributeNode == null){
            //todo: logger...
            System.out.println("[WARN] MainHtmlReader is not cleaned up... But parse id done text: '" + text + "' name: '" + name + "' value: '" + value + "' attrNode: '" + atributeNode);
        }
    }

    private enum State {

        TAGNAME,
        AFTER_TAGNAME,
        ATRIBUTE_NAME,
        AFTER_ATRIBUTE_NAME,
        AFTERQUALS,
        VALUE,
        TEXT,
        AUTOCLOSE,
        CLOSESTART,
        CLOSENAME,
        CLOSEFINISHED

    }
}
