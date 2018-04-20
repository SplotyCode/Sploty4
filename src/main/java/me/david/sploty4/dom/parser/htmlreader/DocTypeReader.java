package me.david.sploty4.dom.parser.htmlreader;

import me.david.sploty4.dom.nodes.DocTypeNode;
import me.david.sploty4.dom.parser.DomHtmlParser;
import me.david.sploty4.dom.parser.DomReader;
import me.david.sploty4.util.StringUtil;

public class DocTypeReader implements DomReader<DomHtmlParser> {

    private String doctype = "";

    @Override
    public void readNext(char c, DomHtmlParser parser) {
        if(parser.isLocked()){
            if(c == '>'){
                parser.getCurrentParent().getChilds().add(new DocTypeNode(doctype));
                parser.setLocked(null);
                doctype = "";
            }else doctype += c;
        }else {
            if (parser.skipIfFollowIgnoreCase("<!doctype ")) {
                parser.setLocked(this);
                parser.stopCurrent();
            }else if(StringUtil.isNoWhiteSpace(c))
                parser.disableReader(this);
        }
    }

    @Override public void parseDone() {}
}
