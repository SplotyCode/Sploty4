package me.david.sploty4.gui;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import me.david.sploty4.Sploty;
import me.david.sploty4.constants.AppConstants;

public class InformationGui {

    public InformationGui(Window window){
        Stage stage = new Stage();
        HBox hBox = new HBox();
        VBox vBox = new VBox();
        Text text = new Text("Sploty4");
        text.setFill(Color.BLUEVIOLET);
        text.setFont(Font.font(null, FontWeight.BOLD, 22));
        vBox.getChildren().addAll(text, new Text("Version: " + AppConstants.VERSION), new Text("Icons by smashicons, Roundicons and Pixel Buddha from flaticon.com"));
        hBox.getChildren().addAll(new ImageView(Sploty.SPLOTY_ICON), vBox);
        Scene scene = new Scene(hBox, 450, 250);
        scene.setFill(Color.OLDLACE);
        stage.setScene(scene);
        stage.initOwner(window.getStage());
        stage.setTitle("About Sploty");
        stage.getIcons().add(Sploty.SPLOTY_ICON);
        stage.show();
        stage.requestFocus();
    }
}
