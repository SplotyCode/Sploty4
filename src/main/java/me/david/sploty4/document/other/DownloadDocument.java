package me.david.sploty4.document.other;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import me.david.sploty4.Sploty;
import me.david.sploty4.document.Document;
import me.david.sploty4.gui.tab.TabHandler;
import me.david.sploty4.io.Connection;
import me.david.sploty4.setting.settings.GeneralSettings;
import me.david.sploty4.util.FileUtil;

import java.io.File;

public class DownloadDocument implements Document {

    private Connection connection;
    private File defaultFile = ((GeneralSettings) Sploty.getSettingManager().getSettingByClass(GeneralSettings.class)).getDownloadDirectory();
    private File specialFolder = defaultFile;

    @Override
    public Pane render(TabHandler tab) {
        HBox hBox = new HBox();
        VBox location = new VBox();
        VBox name = new VBox();
        Button download = new Button("Download");
        VBox downloadSection = new VBox();

        ToggleGroup loc = new ToggleGroup();
        RadioButton def = new RadioButton("Use Default Folder");
        def.setSelected(true);
        def.setToggleGroup(loc);
        RadioButton specific = new RadioButton("Select Folder");
        specific.setToggleGroup(loc);
        Text folder = new Text(specialFolder.getAbsolutePath());
        folder.setDisable(true);
        Button changeDir = new Button("Change Directory");
        changeDir.setDisable(true);
        specific.setOnAction((event -> {
            folder.setDisable(!specific.isSelected());
            changeDir.setDisable(!specific.isSelected());
        }));
        changeDir.setOnAction(event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Choose Default download Folder");
            chooser.setInitialDirectory(specialFolder);
            File directory = chooser.showDialog(tab.getStage());
            if(directory != null) {
                specialFolder = directory;
                folder.setText(specialFolder.getAbsolutePath());
            }
        });
        location.getChildren().addAll(def, specific, folder, changeDir);

        ToggleGroup nameGroup = new ToggleGroup();
        RadioButton normal = new RadioButton("'" + connection.getFileName() + "' (Default)");
        normal.setSelected(true);
        normal.setToggleGroup(nameGroup);
        RadioButton userBased = new RadioButton("Custom");
        userBased.setToggleGroup(nameGroup);
        TextField field = new TextField(connection.getFileName());
        userBased.setOnAction((event -> field.setDisable(!userBased.isSelected())));
        field.setDisable(true);
        name.getChildren().addAll(normal, userBased, field);

        ProgressBar bar = new ProgressBar();
        bar.setProgress(0.0001);
        download.setOnAction((ActionEvent event) -> {
            File finalfile;
            File directory = def.isSelected()?defaultFile:specialFolder;
            String fileName;
            if(normal.isSelected()) fileName = connection.getFileName();
            else fileName = field.getText();
            if(!FileUtil.isValidFileName(fileName)){
                new Alert(Alert.AlertType.ERROR, "The Filename '" + fileName + "' is invalid!", ButtonType.CLOSE).show();
                return;
            }
            finalfile = new File(directory, fileName);
            finalfile = FileUtil.createFile(finalfile);
            final File finalFinalFile = finalfile;
            new Thread(() ->
                Sploty.getInstance().getDownloadManager().download(connection.getInputStream(), finalFinalFile, connection.getLength(),
                    (percentage, done) -> {
                        Platform.runLater(() -> {
                            bar.setProgress(percentage/100);
                            if(done)
                                new Alert(Alert.AlertType.INFORMATION, "Finished downloading '" + finalFinalFile.getName() + "'!").show();
                        });
                    }, message -> {
                        Platform.runLater(() -> new Alert(Alert.AlertType.WARNING, "Failed to Download: '" + message + "'!").show());
                    }
                )
            ).start();
        });
        downloadSection.getChildren().addAll(download, bar);
        hBox.getChildren().addAll(location, name, downloadSection);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    @Override
    public void load(TabHandler tab, Connection connection) {
        this.connection = connection;
    }

    @Override
    public void close() {}

}
