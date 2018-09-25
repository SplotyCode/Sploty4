package me.david.sploty4.gui;

import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.StackPane;
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
import me.david.sploty4.storage.FileComponent;
import me.david.sploty4.storage.FileSerializer;

import java.io.IOException;

public class Window implements FileComponent {

    private Stage stage;
    private Scene scene;
    private MenuManager menuBar = new MenuManager(this);
    private MainShortcuts shortcurts;
    private TabList tabBar;
    private StackPane tabsPane;

    public Window(Stage stage, BrowserTab... tabs){
        this.stage = stage;
        tabsPane = new StackPane();
        tabBar = new TabList(this, tabsPane);
        tabBar.getTabs().addAll(tabs);
        if(tabs.length == 0) tabBar.getTabs().add(new BrowserTab(tabBar, ((GeneralSettings) Sploty.getSettingManager().getSettingByClass(GeneralSettings.class)).getDefaultsite().toExternalForm()));
        buildGui();
    }

    public Window(Stage stage, FileSerializer serializer) {
        this.stage = stage;
        tabsPane = new StackPane();
        tabBar = new TabList(this, tabsPane);
        try {
            read(serializer);
        } catch (IOException e) {
            Sploty.getLogger().exception(e, "Failed reading from Session File");
        }
        if(tabBar.getTabs().size() == 0) tabBar.getTabs().add(new BrowserTab(tabBar, ((GeneralSettings) Sploty.getSettingManager().getSettingByClass(GeneralSettings.class)).getDefaultsite().toExternalForm()));
        buildGui();
    }

    private void buildGui() {
        tabsPane.getChildren().addAll(tabBar);
        VBox box = new VBox();
        box.getStylesheets().add("/themes/external/flatred.css");
        box.getChildren().addAll(menuBar, tabsPane);
        scene = new Scene(box, 400, 350);
        shortcurts = new MainShortcuts(this);
        //scene.setFill(Color.OLDLACE);
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

    @Override
    public void read(final FileSerializer serializer) throws IOException {
        int size = serializer.readVarInt(), selected = serializer.readVarInt();

        for (int i = 0;i < size;i++)
            tabBar.getTabs().add(new BrowserTab(tabBar, serializer.readString()));

        tabBar.getSelectionModel().select(selected);
    }

    @Override
    public void write(final FileSerializer serializer) throws IOException {
        serializer.writeVarInt(tabBar.getTabs().size());
        serializer.writeVarInt(tabBar.getSelectionModel().getSelectedIndex());

        for (final Tab tab : tabBar.getTabs())
            serializer.writeString(((BrowserTab) tab).getCurrentUrl());
    }
}
