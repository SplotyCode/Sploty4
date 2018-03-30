package me.david.sploty4.document.archive;

import javafx.scene.control.TreeCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import me.david.sploty4.document.Document;
import me.david.sploty4.gui.tab.TabHandler;
import me.david.sploty4.io.Connection;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipArchiveDocument implements Document {

    private ZipInputStream stream;

    @Override
    public Pane render(TabHandler tab) {
        ZipEntry entry;
        HBox box = new HBox();
        try {
            while ((entry = stream.getNextEntry()) != null) {
                box.getChildren().add(new Text(entry.getName()));
                stream.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return box;
    }

    @Override
    public void load(TabHandler tab, Connection connection) {
        stream = new ZipInputStream(connection.getInputStream());
    }

    @Override
    public void close() {
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
