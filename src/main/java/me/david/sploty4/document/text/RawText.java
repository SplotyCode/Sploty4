package me.david.sploty4.document.text;


import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import me.david.sploty4.document.Document;
import me.david.sploty4.gui.tab.TabHandler;
import me.david.sploty4.io.Connection;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class RawText implements Document {

    private String content;

    @Override
    public Node render(TabHandler tab) {
        TextArea area = new TextArea();
        area.setEditable(false);
        area.setText(content);
        return area;
    }

    @Override
    public void load(TabHandler tab, Connection connection) {
        try {
            System.out.println(connection.getInputStream().getClass().getSimpleName());
            content = IOUtils.toString(connection.getInputStream(), connection.getCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {

    }
}
