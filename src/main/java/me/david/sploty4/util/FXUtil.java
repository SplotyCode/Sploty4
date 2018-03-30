package me.david.sploty4.util;

import javafx.application.Platform;
import javafx.scene.control.Labeled;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;

public final class FXUtil {

    public static void setImage(Labeled label, String image){
        Platform.runLater(() -> {
            InputStream is = FXUtil.class.getResourceAsStream(image);
            label.setGraphic(new ImageView(new Image(is)));
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static Image getImage(String loc){
        InputStream is = FXUtil.class.getResourceAsStream(loc);
        Image image = new Image(is);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
