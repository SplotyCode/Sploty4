package me.david.sploty4.gui;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import me.david.sploty4.Sploty;
import me.david.sploty4.constants.AppConstants;
import me.david.sploty4.gui.menu.MenuManager;
import me.david.sploty4.gui.shortCurts.MainShortcuts;
import me.david.sploty4.gui.tab.BrowserTab;
import me.david.sploty4.gui.tab.TabList;
import me.david.sploty4.setting.settings.GeneralSettings;

public class Window {

    private Stage stage;
    private Scene scene;
    private MenuManager menuBar = new MenuManager(this);
    private MainShortcuts shortcurts;
    private TabList tabBar;

    public Window(Stage stage, BrowserTab... tabs){
        this.stage = stage;
        tabBar = new TabList(this);
        tabBar.getTabs().addAll(tabs);
        if(tabs.length == 0) tabBar.getTabs().add(new BrowserTab(tabBar, ((GeneralSettings) Sploty.getSettingManager().getSettingByClass(GeneralSettings.class)).getDefaultsite().toExternalForm()));
        VBox box = new VBox();
        box.getChildren().addAll(menuBar, tabBar);
        scene = new Scene(box, 400, 350);
        shortcurts = new MainShortcuts(this);
        scene.setFill(Color.OLDLACE);
        stage.setScene(scene);
        stage.setTitle("Sploty4 WebBrowser - " + AppConstants.VERSION);
        stage.setFullScreenExitHint("Press Esc to leave Fullscreen");
        stage.setMaximized(true);
        stage.getIcons().add(Sploty.SPLOTY_ICON);
        stage.setOnCloseRequest(windowEvent -> Sploty.getGuiManager().getWindows().remove(this));
        stage.show();
        stage.requestFocus();
    }

    public BrowserTab getCurrentTab(){
        return tabBar.getCurrentTab();
    }

    public MainShortcuts getShortcurts() {
        return shortcurts;
    }

    public Stage getStage() {
        return stage;
    }

    public Scene getScene() {
        return scene;
    }

    public MenuManager getMenuBar() {
        return menuBar;
    }

    public TabList getTabBar() {
        return tabBar;
    }
}
