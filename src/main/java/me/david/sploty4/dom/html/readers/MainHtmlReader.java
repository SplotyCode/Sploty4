package me.david.sploty4.dom.html.readers;

import me.david.sploty4.Sploty;
import me.david.sploty4.document.SyntaxException;
import me.david.sploty4.dom.html.Node;
import me.david.sploty4.dom.html.attriute.Attribute;
import me.david.sploty4.dom.html.attriute.AttributeHelper;
import me.david.sploty4.dom.html.attriute.StandardAttribute;
import me.david.sploty4.dom.html.attriute.ToggleAttribute;
import me.david.sploty4.dom.html.nodes.Nodes;
import me.david.sploty4.dom.html.nodes.TextNode;
import me.david.sploty4.dom.html.DomHtmlParser;
import me.david.sploty4.dom.DomReader;
import me.david.sploty4.util.StringUtil;

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
                    if(!StringUtil.isEmpty(text)) parser.getCurrentParent().getChilds().add(new TextNode(text, parser.getCurrentParent()));
                    state = State.TAGNAME;
                    text = "";
                }
                else if(StringUtil.isNoSpecialSpace(c)) text += c;
                break;
            case TAGNAME:
                if(c == ' '){
                    atributeNode = Nodes.byName(name.toLowerCase(), parser);
                    atributeNode.setParent(parser.getCurrentParent());
                    name = "";
                    state = State.AFTER_TAGNAME;
                    parser.getCurrentParent().getChilds().add(atributeNode);
                    if(!atributeNode.canSelfClose())
                        parser.setCurrentParent(atributeNode);
                }else if(c == '>') {
                    Node node = Nodes.byName(name.toLowerCase(), parser);
                    node.setParent(parser.getCurrentParent());
                    parser.getCurrentParent().getChilds().add(node);
                    if(!node.canSelfClose())
                        parser.setCurrentParent(node);
                    name = "";
                    state = State.TEXT;
                } else if (c == '/'){
                    state = State.CLOSESTART;
                } else name += c;
                break;
            case AFTER_TAGNAME:
                if (c == '>') {
                    state = State.TEXT;
                    if(!atributeNode.canSelfClose())
                        parser.setCurrentParent(atributeNode);
                    atributeNode = null;
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
                } else if (c == '=') {
                    state = State.AFTERQUALS;
                }else if (c == '>'){
                    atributeNode.getAttributes().add(new Attribute(name.toLowerCase()));
                    name = "";
                } else if (c == '/'){
                    state = State.AUTOCLOSE;
                } else name += c;
                break;
            case AFTER_ATRIBUTE_NAME:
                if (c == '>'){
                    atributeNode.getAttributes().add(new Attribute(name.toLowerCase()));
                    name = "";
                } else if (c == '/'){
                    state = State.AUTOCLOSE;
                } else if (c == '='){
                    state = State.AFTERQUALS;
                } else if (StringUtil.isNoWhiteSpace(c)){
                    atributeNode.getAttributes().add(new Attribute(name.toLowerCase()));
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
                //System.out.println(endChar + " " + c);
                if (c == endChar || (endChar == Character.MIN_VALUE &&
                        (StringUtil.isWhiteSpace(c) || c == '>' || c == '/'))) {
                    name = name.toLowerCase();
                    value = value.toLowerCase();
                    if (AttributeHelper.isBoolean(name.toLowerCase()))
                        atributeNode.getAttributes().add(new ToggleAttribute(name, value));
                    else atributeNode.getAttributes().add(new StandardAttribute(name, value));
                    name = value = "";
                    state = c == '/'?State.AUTOCLOSE:State.AFTER_TAGNAME;
                    if (AttributeHelper.isSelftClosing(name)) throw new SyntaxException("Attribute '" + name + "' for tag '" + parser.getCurrentParent().getName() + "' should not have a value!");
                } else value += c;
                break;
            case AUTOCLOSE:
                if (c == '>') {
                    /* Go one element back */
                    if(atributeNode != null && !atributeNode.canSelfClose()) {
                        parser.setCurrentParent(parser.getCurrentParent().getParent());
                    }
                    atributeNode = null;
                    state = State.TEXT;
                } else if (StringUtil.isNoWhiteSpace(c)){
                    throw new SyntaxException("Expected > but not '" + c + "'!(0)");
                }
                break;
            case CLOSESTART:
                if (StringUtil.isNoWhiteSpace(c)) {
                    parser.rehandle();
                    state = State.CLOSENAME;
                    name = "";
                }
                break;
            case CLOSENAME:
                if (StringUtil.isWhiteSpace(c) || c ==  '>') {
                    parser.rehandle();
                    state = State.CLOSEFINISHED;
                } else name += c;
                break;
            case CLOSEFINISHED:
                if (c == '>') {
                    if(!name.toLowerCase().equals(parser.getCurrentParent().getName())) throw new SyntaxException("Closed '" + name + "' without closing '" + parser.getCurrentParent().getName() + "'!");
                    if(!parser.getCurrentParent().canSelfClose()) parser.setCurrentParent(parser.getCurrentParent().getParent());
                    name = "";
                    state = State.TEXT;
                } else if (StringUtil.isNoWhiteSpace(c)) throw new SyntaxException("Expected > but not '" + c + "'!(1)");
                break;
        }
    }

    @Override
    public void parseDone() {
        if (!StringUtil.isEmpty(text) ||
           !StringUtil.isEmpty(name) ||
           !StringUtil.isEmpty(value) ||
           atributeNode != null){
            Sploty.getLogger().warn("MainHtmlReader is not cleaned up... But parse id done! Text: '" + text + "' name: '" + name + "' value: '" + value + "' attrNode: '" + atributeNode + "'");
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
