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
import me.david.sploty4.storage.FileSerializer;
import me.david.sploty4.util.FileUtil;

import java.io.File;
import java.io.IOException;
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
    private File sessionFile = new File(Sploty.getDirectory(), "session.rawBin");

    public GuiManager(Stage stage) {
        if (sessionFile.exists()) {
            FileSerializer serializer = new FileSerializer();
            serializer.readFile(sessionFile);
            int size = serializer.readVarInt();
            for (int i = 0; i < size;i++)
                windows.add(new Window(stage, serializer));
        } else windows.add(new Window(stage));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            boolean needSave = false;
            for (Window window : windows)
                if (!window.getTabBar().getTabs().isEmpty()) {
                    needSave = true;
                    break;
                }
            if (needSave) {
                if (!sessionFile.exists()) FileUtil.createFile(sessionFile);
                FileSerializer serializer = new FileSerializer();
                serializer.writeVarInt(windows.size());
                try {
                    for (Window window : windows)
                        window.write(serializer);
                } catch (IOException ex) {
                    Sploty.getLogger().exception(ex, "Failed writing to Session File");
                }
                serializer.writeFile(sessionFile);
            }
        }, "Session Save"));
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
