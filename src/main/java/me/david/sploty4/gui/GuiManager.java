package me.david.sploty4.gui;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import me.david.sploty4.Sploty;
import me.david.sploty4.gui.about.AboutHandler;
import me.david.sploty4.gui.tab.BrowserTab;
import me.david.sploty4.setting.SettingManager;

import java.util.ArrayList;

public class GuiManager {

    private ArrayList<Window> windows = new ArrayList<Window>(){
        @Override
        public boolean remove(Object o) {
            if(size() == 1){
                Platform.exit();
                System.exit(0);
            }
            return super.remove(o);
        }
    };
    private ObjectProperty<BrowserTab> draggingTab = new SimpleObjectProperty<>();
    private AboutHandler aboutHandler = new AboutHandler();

    public GuiManager(Stage stage) {
        windows.add(new Window(stage));
    }

    public void openSettings(Stage stage){
        Stage settings = new Stage();
        Sploty.getSettingManager().buildSettings(settings);
        //settings.initOwner(stage);
        settings.setMaximized(true);
        settings.setTitle("Settings");
        settings.getIcons().add(Sploty.SPLOTY_ICON);
        settings.setFullScreenExitHint("Press Esc to leave Fullscreen");
        settings.show();
        settings.requestFocus();
    }

    public ArrayList<Window> getWindows() {
        return windows;
    }

    public BrowserTab getDraggingTab() {
        return draggingTab.get();
    }

    public AboutHandler getAboutHandler() {
        return aboutHandler;
    }

    public void setDraggingTab(BrowserTab draggingTab) {
        this.draggingTab.set(draggingTab);
    }
}
