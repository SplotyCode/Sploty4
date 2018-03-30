package me.david.sploty4.gui.menu;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import me.david.sploty4.gui.Window;

public class MenuGroup extends Menu {

    private Window window;

    public MenuGroup(Window window, String name) {
        super(name);
        this.window = window;
    }

    public void add(String name, EventHandler<ActionEvent> event){
        MenuItem item = new MenuItem(name);
        item.setOnAction(event);
        getItems().add(item);
    }

    public void add(String name, EventHandler<ActionEvent> event, KeyCombination shortcurt){
        MenuItem item = new MenuItem(name);
        item.setMnemonicParsing(true);
        item.setAccelerator(shortcurt);
        item.setOnAction(event);
        getItems().add(item);
    }

    public void addToggle(String name, boolean def, ChangeListener<Boolean> event){
        CheckMenuItem item = new CheckMenuItem(name);
        item.setSelected(def);
        item.selectedProperty().addListener(event);
        getItems().add(item);
    }

    public void addToggle(String name, boolean def, ChangeListener<Boolean> event, KeyCombination shortcurt){
        CheckMenuItem item = new CheckMenuItem(name);
        item.setSelected(def);
        item.setMnemonicParsing(true);
        item.setAccelerator(shortcurt);
        item.selectedProperty().addListener(event);
        getItems().add(item);
    }

    public Window getWindow() {
        return window;
    }
}
