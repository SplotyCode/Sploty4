package me.david.sploty4.document.text;

import me.david.sploty4.document.Document;
import me.david.sploty4.dom.DomErrorReporter;
import me.david.sploty4.dom.Node;
import me.david.sploty4.dom.parser.DomHtmlParser;
import me.david.sploty4.features.ProblemConclusion;
import me.david.sploty4.gui.tab.TabHandler;
import me.david.sploty4.io.Connection;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class HtmlDocument implements Document {

    private Node html;
    private DomHtmlParser parser;
    private DomErrorReporter errorReporter = new DomErrorReporter();

    @Override
    public javafx.scene.Node render(TabHandler tab) {
        return null;
    }

    @Override
    public void load(TabHandler tab, Connection connection) {
        String content = "";
        try {
            content = IOUtils.toString(connection.getInputStream(), connection.getCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        parser = new DomHtmlParser();
        parser.setErrorReporter(errorReporter);
        parser.parse(content);
        html = parser.getBase();

        //ignore infos??
        if(!errorReporter.getErrors().isEmpty())
            new ProblemConclusion(errorReporter).build();
    }

    @Override
    public void close() {

    }
}
