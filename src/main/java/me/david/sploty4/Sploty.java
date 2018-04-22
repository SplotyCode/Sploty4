package me.david.sploty4;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import me.david.sploty4.constants.AppConstants;
import me.david.sploty4.document.DocumentHandler;
import me.david.sploty4.features.DownloadManager;
import me.david.sploty4.features.History;
import me.david.sploty4.gui.GuiManager;
import me.david.sploty4.setting.SettingManager;
import me.david.sploty4.setting.settings.GeneralSettings;
import me.david.sploty4.storage.FileSerializer;
import me.david.sploty4.storage.SQLite;
import me.david.sploty4.storage.StorageHelper;
import me.david.sploty4.util.FXUtil;
import me.david.sploty4.util.SplotyLogger;

import java.io.File;
import java.net.URL;
import java.security.Security;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class Sploty extends Application {

    public static void main(String[] args){
        launch(args);
    }

    private static File directory;
    private static Sploty instance;
    private static GuiManager guiManager;
    private static SettingManager settingManager = new SettingManager();
    private static SplotyLogger logger;
    private Executor siteExecutor = Executors.newFixedThreadPool(3);

    public static final Image SPLOTY_ICON = FXUtil.getImage("/Sploty.png");

    private DocumentHandler documentHandler;
    private DownloadManager downloadManager;
    private History history;


    public static File getDirectory() {
        return directory;
    }

    @Override
    public void start(Stage stage) throws Exception {
        instance = this;
        GeneralSettings settings = new GeneralSettings();
        settings.setDefaultsite(new URL("https://google.com/"));
        settings.setDownloadDirectory(new File("/home/david/Downloads/"));
        settings.setScriptblock(true);
        FileSerializer serializer = new FileSerializer();
        settings.write(serializer);
        serializer.writeFile(new File("/home/david/testi.txt"));
        System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

        directory = new StorageHelper().getSplotyFolder();
        logger = new SplotyLogger(Level.ALL);
        logger.printBanner();
        logger.info("Starting Sploty4 " + AppConstants.VERSION + "...   ");
        settingManager.load();
        SQLite.INSTANCE.connect();
        documentHandler = new DocumentHandler();
        downloadManager = new DownloadManager();
        history = new History();
        //System.getProperties().list(System.out);
        guiManager = new GuiManager(stage);
        logger.info("Successfully started Sploty!");
    }

    public static GuiManager getGuiManager() {
        return guiManager;
    }

    public static Sploty getInstance() {
        return instance;
    }

    public DocumentHandler getDocumentHandler() {
        return documentHandler;
    }

    public static SettingManager getSettingManager() {
        return settingManager;
    }

    public History getHistory() {
        return history;
    }

    public DownloadManager getDownloadManager() {
        return downloadManager;
    }

    public Executor getSiteExecutor() {
        return siteExecutor;
    }

    public static SplotyLogger getLogger() {
        return logger;
    }
}
