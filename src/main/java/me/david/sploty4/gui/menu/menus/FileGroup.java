package me.david.sploty4.gui.menu.menus;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import me.david.sploty4.Sploty;
import me.david.sploty4.gui.Window;
import me.david.sploty4.gui.menu.MenuGroup;
import me.david.sploty4.gui.tab.BrowserTab;

public class FileGroup extends MenuGroup {

    public FileGroup(Window window) {
        super(window, "File");
        add("New Tab", event -> window.getTabBar().getTabs().add(new BrowserTab(window.getTabBar(), "about:newtab")), new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN));
        add("New Window", event -> Sploty.getGuiManager().getWindows().add(new Window(new Stage())), new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        add("Settings", event -> Sploty.getGuiManager().openSettings(window.getStage()));
        add("Exit", event -> {
            Platform.exit();
            System.exit(0);
        }, new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
    }
}
