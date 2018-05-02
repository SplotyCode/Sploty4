package me.david.sploty4.gui.tab;

import javafx.collections.ListChangeListener;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import me.david.sploty4.gui.Window;
import me.david.sploty4.gui.tab.drag.DraggableBar;

public class TabList extends DraggableBar {

    //public static final String DRAG_KEY = "SPLOTY_TAB_DRAG";
    private Window window;

    public TabList(Window window, Pane dragPane){
        super(dragPane);
        this.window = window;
        setPrefHeight(20000);
        setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        getTabs().addListener((ListChangeListener<? super Tab>) change -> {
            if(getTabs().isEmpty()) {
                window.getStage().close();
            }
        });
    }

    public BrowserTab getCurrentTab(){
        return (BrowserTab) getSelectionModel().getSelectedItem();
    }

    public Window getWindow() {
        return window;
    }
}
