package me.david.sploty4.gui.tab;

import javafx.stage.Stage;

public interface TabHandler {

    void setTitle(String title);
    String getTitle();

    BrowserTab getTab();
    Stage getStage();

}
