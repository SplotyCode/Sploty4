package me.david.sploty4.document;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import me.david.sploty4.gui.tab.TabHandler;
import me.david.sploty4.io.Connection;

public interface Document {

    Node render(TabHandler tab);
    void load(TabHandler tab, Connection connection);
    void close();

}
