package me.david.sploty4.gui.about;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AboutNewTab implements AboutPage {

    @Override
    public Pane handle() {
        VBox pane = new VBox();
        pane.getChildren().addAll(new Text("New tab Page"), new Text("WorkInProgress"));
        return pane;
    }

}
