package me.david.sploty4.gui.about;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public class AboutHandler {

    private HashMap<String, AboutPage> pages = new HashMap<>();

    public AboutHandler(){
        pages.put("blank", new AboutBlank());
        pages.put("newtab", new AboutNewTab());
    }

    public Pane handle(String about){
        AboutPage page = pages.get(about);
        if(page == null){
            Platform.runLater(() -> new Alert(Alert.AlertType.WARNING, "Die interne seite " + about + " konnte nicht gefunden werden!", ButtonType.CLOSE).show());
            return null;
        }
        return page.handle();
    }
}
