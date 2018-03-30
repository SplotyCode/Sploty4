package me.david.sploty4.setting;

import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import me.david.sploty4.setting.settings.*;
import me.david.sploty4.storage.FileSerializer;

import java.io.IOException;
import java.util.ArrayList;

public class SettingManager {

    private ArrayList<Setting> settings = new ArrayList<>();

    public SettingManager() {}

    public void load(){
        register(new GeneralSettings());
        register(new ConnectionSettings());
        register(new RamCacheSettings());
        register(new DiskCacheSettings());
        //register(new ConnectionSettings());
    }

    public void buildSettings(Stage stage){
        BorderPane box = new BorderPane();
        TabPane tap = new TabPane();
        tap.setSide(Side.LEFT);
        tap.setRotateGraphic(true);
        tap.setTabMinHeight(100);
        tap.setTabMinWidth(50);
        tap.setTabMaxWidth(50);
        tap.setTabMaxHeight(100);
        tap.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        for(Setting setting : settings){
            Tab t = new Tab();
            Group label = new Group(new Label(setting.getTitle()));
            label.setRotate(90);
            t.setGraphic(new StackPane(label));
            setting.render(t, stage);
            tap.getTabs().add(t);
        }
        HBox buttons = new HBox();
        buttons.setAlignment(Pos.BASELINE_RIGHT);

        Button save = new Button("Save");
        save.setDefaultButton(true);
        save.setOnAction((event -> {
            saveAll();
            stage.close();
        }));

        Button cancel = new Button("Cancel");
        cancel.setCancelButton(true);
        cancel.setOnAction((event -> {
            readAll();
            stage.close();
        }));

        Button session = new Button("This Session");
        session.setOnAction((event -> {
            readAll();
            stage.close();
        }));

        buttons.getChildren().addAll(save, session, cancel);
        box.setCenter(tap);
        box.setBottom(buttons);
        stage.setScene(new Scene(box, 400, 320));
        stage.getScene().setFill(Color.OLDLACE);
    }

    private void register(Setting setting){
        try {
            setting.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        settings.add(setting);
    }

    private void saveAll(){
        for(Setting setting : settings){
            FileSerializer serializer = new FileSerializer();
            try {
                setting.write(serializer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            serializer.writeFile(setting.getFile());
        }
    }

    private void readAll(){
        for(Setting setting : settings){
            FileSerializer serializer = new FileSerializer();
            serializer.readFile(setting.getFile());
            try {
                setting.read(serializer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Setting getSettingByClass(Class<? extends Setting> setting){
        for(Setting set : settings)
            if(set.getClass() == setting)
                return set;
        return null;
    }

}
