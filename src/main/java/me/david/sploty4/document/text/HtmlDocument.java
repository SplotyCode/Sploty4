package me.david.sploty4.document.text;

import me.david.sploty4.document.Document;
import me.david.sploty4.dom.Node;
import me.david.sploty4.dom.parser.DomHtmlParser;
import me.david.sploty4.gui.tab.TabHandler;
import me.david.sploty4.io.Connection;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class HtmlDocument implements Document {

    private Node html;
    private DomHtmlParser parser;

    @Override
    public javafx.scene.Node render(TabHandler tab) {
        return null;
    }

    @Override
    public void load(TabHandler tab, Connection connection) {
        String content = "";
        try {
            content = IOUtils.toString(connection.getInputStream(), connection.getEncoding());
        } catch (IOException e) {
            e.printStackTrace();
        }
        parser = new DomHtmlParser();
        parser.parse(content);
        html = parser.getBase();
    }

    @Override
    public void close() {

    }
}
