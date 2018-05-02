package me.david.sploty4.document.other;

import javafx.scene.Node;
import me.david.sploty4.Sploty;
import me.david.sploty4.document.Document;
import me.david.sploty4.gui.tab.TabHandler;
import me.david.sploty4.io.Connection;
import org.apache.commons.io.IOUtils;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.StyleClassedTextArea;

import java.io.IOException;

public class ViewSourceDocument implements Document {

    StyleClassedTextArea textArea = new StyleClassedTextArea();
    String content;

    @Override
    public Node render(TabHandler tab) {
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.replaceText(content);
        textArea.setParagraphGraphicFactory(LineNumberFactory.get(textArea));
        return textArea;
    }

    @Override
    public void load(TabHandler tab, Connection connection) {
        try {
            content = IOUtils.toString(connection.getInputStream(), connection.getCharset());
        } catch (IOException e) {
            Sploty.getLogger().exception(e, "Failed reading content");
        }
    }

    @Override
    public void close() {

    }
}
