package me.david.sploty4.gui.tab;

import javafx.collections.ListChangeListener;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import me.david.sploty4.Sploty;
import me.david.sploty4.gui.Window;

public class TabList extends TabPane {

    public static final String DRAG_KEY = "SPLOTY_TAB_DRAG";
    private Window window;

    public TabList(Window window){
        this.window = window;
        setPrefHeight(20000);
        setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        getTabs().addListener((ListChangeListener<? super Tab>) change -> {
            if(getTabs().isEmpty()) {
                window.getStage().close();
            }
        });
        setOnDragOver(event -> {
            final Dragboard dragboard = event.getDragboard();
            if (dragboard.hasString()
                    && DRAG_KEY.equals(dragboard.getString())
                    && Sploty.getGuiManager().getDraggingTab() != null
                    && Sploty.getGuiManager().getDraggingTab().getTabPane() != this) {
                event.acceptTransferModes(TransferMode.MOVE);
                event.consume();
            }
        });
        /*setOnDragDropped(event -> {
            final Dragboard dragboard = event.getDragboard();
            if (dragboard.hasString()
                    && DRAG_KEY.equals(dragboard.getString())
                    && Sploty.getGuiManager().getDraggingTab() != null
                    && Sploty.getGuiManager().getDraggingTab().getTabPane() != this) {
                final Tab tab = Sploty.getGuiManager().getDraggingTab();
                tab.getTabPane().getTabs().remove(tab);
                getTabs().add(tab);
                event.setDropCompleted(true);
                Sploty.getGuiManager().setDraggingTab(null);
                Platform.runLater(() -> getSelectionModel().select(tab));
                event.consume();
            }
        });*/
    }

    public BrowserTab getCurrentTab(){
        return (BrowserTab) getSelectionModel().getSelectedItem();
    }

    public Window getWindow() {
        return window;
    }
}
