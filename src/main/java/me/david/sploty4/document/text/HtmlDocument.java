package me.david.sploty4.document.text;

import me.david.sploty4.document.Document;
import me.david.sploty4.dom.Node;
import me.david.sploty4.dom.parser.DomHtmlParser;
import me.david.sploty4.gui.tab.TabHandler;
import me.david.sploty4.io.Connection;

public class HtmlDocument implements Document {

    private Node html;
    private DomHtmlParser parser;

    @Override
    public javafx.scene.Node render(TabHandler tab) {
        return null;
    }

    @Override
    public void load(TabHandler tab, Connection connection) {

    }

    @Override
    public void close() {

    }
}
