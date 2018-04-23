package me.david.sploty4.dom.html.readers;

import me.david.sploty4.dom.html.DomHtmlParser;
import me.david.sploty4.dom.DomReader;

public class CommentReader implements DomReader<DomHtmlParser> {

    @Override
    public void readNext(char c, DomHtmlParser parser) {
        if(parser.isLocked()){
            if(parser.skipIfFollow("-->")) {
                parser.setLocked(null);
            }
        }else if(parser.skipIfFollow("<!--")){
            parser.stopCurrent();
            parser.setLocked(this);
        }
    }

    @Override public void parseDone() {}

}
