package me.david.sploty4.document;

import javafx.scene.Node;
import me.david.sploty4.gui.tab.TabHandler;
import me.david.sploty4.io.Connection;
import org.apache.commons.io.IOUtils;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.StyleClassedTextArea;

import java.io.IOException;

public class ViewSource implements Document {

    StyleClassedTextArea textArea = new StyleClassedTextArea();
    String content;

    @Override
    public Node render(TabHandler tab) {
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.replaceText(0, 0, content);
        textArea.setParagraphGraphicFactory(LineNumberFactory.get(textArea));
        return textArea;
    }

    @Override
    public void load(TabHandler tab, Connection connection) {
        try {
            content = IOUtils.toString(connection.getInputStream(), connection.getCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {

    }
}
