package me.david.sploty4.setting;

import javafx.scene.control.Tab;
import javafx.stage.Stage;
import me.david.sploty4.storage.FileComponent;
import me.david.sploty4.storage.FileSerializer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public abstract class Setting implements FileComponent {

    private final String title;
    private final File file;

    public Setting(String title) {
        this.title = title;
        file = null;
    }

    public Setting(String title, File file) {
        this.title = title;
        this.file = file;
    }

    public void load() throws IOException {
        if(file == null)return;
        if(!file.exists()){
            file.getParentFile().mkdirs();
            InputStream is = getClass().getResourceAsStream("/default/" + file.getName());
            Files.copy(is, file.getAbsoluteFile().toPath());
            is.close();
        }
        read(new FileSerializer().readFile(file));
    }

    public abstract void render(Tab tab, Stage stage);

    public String getTitle() {
        return title;
    }

    public File getFile() {
        return file;
    }
}
