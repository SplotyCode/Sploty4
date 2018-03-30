package me.david.sploty4.setting.settings;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import me.david.sploty4.Sploty;
import me.david.sploty4.setting.Setting;
import me.david.sploty4.storage.FileSerializer;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class GeneralSettings extends Setting {

    private URL defaultsite;
    private boolean scriptblock;
    private File downloadDirectory;

    public GeneralSettings() {
        super("General", new File(Sploty.getDirectory(), "settings/general.txt"));
    }

    @Override
    public void render(Tab tab, Stage stage) {
        VBox vBox = new VBox();

        HBox siteBox = new HBox();
        TextField site = new TextField(defaultsite.toString());
        site.textProperty().addListener((observableValue, s, t1) -> {
            try {
                defaultsite = new URL(site.getText());
            } catch (MalformedURLException e) {
                new Alert(Alert.AlertType.ERROR, "Please enter a Valid Url");
            }
        });
        siteBox.getChildren().addAll(new Label("Standart Seite: "), site);

        HBox scriptBox = new HBox();
        CheckBox script = new CheckBox();
        script.setDisable(true);
        script.setSelected(scriptblock);
        scriptBox.getChildren().addAll(new Label("Block Javascript: "), script);

        HBox downloadBox = new HBox();
        Label folder = new Label(downloadDirectory.getAbsolutePath());
        folder.setFont(Font.font(null, FontWeight.BOLD, 12));
        Button button = new Button("Select Folder");
        button.setOnAction((event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Choose Default download Folder");
            chooser.setInitialDirectory(downloadDirectory);
            File directory = chooser.showDialog(stage);
            if(directory != null) {
                downloadDirectory = directory;
                folder.setText(downloadDirectory.getAbsolutePath());
            }
        }));
        downloadBox.getChildren().addAll(new Label("Default download Folder: "), folder, button);

        vBox.getChildren().addAll(siteBox, scriptBox, downloadBox);
        tab.setContent(vBox);
    }

    @Override
    public void read(FileSerializer serializer) throws IOException {
        defaultsite = new URL(serializer.readString());
        scriptblock = serializer.readBoolean();
        downloadDirectory = new File(serializer.readString());
    }

    @Override
    public void write(FileSerializer serializer) throws IOException {
        serializer.writeString(defaultsite.toString());
        serializer.writeBoolean(scriptblock);
        serializer.writeString(downloadDirectory.getAbsolutePath());
    }

    public URL getDefaultsite() {
        return defaultsite;
    }

    public boolean isScriptblock() {
        return scriptblock;
    }


    public File getDownloadDirectory() {
        return downloadDirectory;
    }

    public void setDownloadDirectory(File downloadDirectory) {
        this.downloadDirectory = downloadDirectory;
    }

    public void setDefaultsite(URL defaultsite) {
        this.defaultsite = defaultsite;
    }

    public void setScriptblock(boolean scriptblock) {
        this.scriptblock = scriptblock;
    }
}
