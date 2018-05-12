package me.david.sploty4.document.text;

import javafx.scene.control.TextArea;
import me.david.sploty4.Sploty;
import me.david.sploty4.document.Document;
import me.david.sploty4.dom.DomErrorReporter;
import me.david.sploty4.dom.html.Node;
import me.david.sploty4.dom.html.DomHtmlParser;
import me.david.sploty4.features.ProblemConclusion;
import me.david.sploty4.gui.tab.TabHandler;
import me.david.sploty4.io.Connection;
import me.david.sploty4.objects.PrettyPrint;
import me.david.sploty4.objects.Timer;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class HtmlDocument implements Document {

    private Node html;
    private DomHtmlParser parser;
    private DomErrorReporter errorReporter = new DomErrorReporter();

    @Override
    public javafx.scene.Node render(TabHandler tab) {
        TextArea area = new TextArea();
        area.setEditable(false);
        area.setText(new PrettyPrint(html, "").prettyPrint());
        return area;
    }

    @Override
    public void load(TabHandler tab, Connection connection) {
        String content = "";
        try {
            content = IOUtils.toString(connection.getInputStream(), connection.getCharset());
        } catch (IOException e) {
            Sploty.getLogger().exception(e, "Failed to read Connection Stream...");
        }
        parser = new DomHtmlParser();
        parser.setErrorReporter(errorReporter);
        //TODO: replace timer with a real profiler (multiple section etc)
        Timer timer = new Timer().start();
        parser.parse(content);
        Sploty.getLogger().info("Html parsing took: " + timer.getDelay() + "ms");
        html = parser.getBase();

        //ignore infos??
        if(!errorReporter.getErrors().isEmpty())
            new ProblemConclusion(errorReporter).build();
    }

    @Override
    public void close() {

    }
}
