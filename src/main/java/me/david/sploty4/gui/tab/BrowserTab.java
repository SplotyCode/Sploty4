package me.david.sploty4.gui.tab;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import me.david.sploty4.Sploty;
import me.david.sploty4.document.Document;
import me.david.sploty4.document.ErrorDocument;
import me.david.sploty4.document.ViewSource;
import me.david.sploty4.document.text.RawText;
import me.david.sploty4.features.DownloadManager;
import me.david.sploty4.features.History;
import me.david.sploty4.gui.Window;
import me.david.sploty4.io.Connection;
import me.david.sploty4.util.FXUtil;
import me.david.sploty4.util.ListUtil;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BrowserTab extends Tab implements TabHandler {

    private TabList list;
    private Label label;
    private Document document;
    private Connection connection;
    private BorderPane main, addressBar;
    private HBox toolBox = new HBox();
    private TextField urlBar = new TextField();
    private Button download = new Button();
    private Button go = new Button();
    private HBox left = new HBox();
    private Button secure = new Button();
    private Button undoBut = new Button();
    private Button redoBut = new Button();
    private List<String> undo = new ArrayList<String>(){
        @Override
        public boolean add(String o) {
            boolean result = super.add(o);
            undoBut.setDisable(size()==0);
            updateMenus();
            return result;
        }
        @Override
        public String remove(int o) {
            String result = super.remove(o);
            undoBut.setDisable(size()==0);
            updateMenus();
            return result;
        }
    };
    private List<String> redo = new ArrayList<String>(){
        @Override
        public boolean add(String o) {
            boolean result = super.add(o);
            redoBut.setDisable(size()==0);
            updateMenus();
            return result;
        }
        @Override
        public String remove(int o) {
            String result = super.remove(o);
            redoBut.setDisable(size()==0);
            updateMenus();
            return result;
        }
    };
    private ContextMenu redoMenu = new ContextMenu();
    private ContextMenu undoMenu = new ContextMenu();
    private String currentUrl;
    private History.HistoryEntry currentHistory;

    private void updateMenus(){
        redoMenu.getItems().clear();
        undoMenu.getItems().clear();
        for(String str : redo)
            redoMenu.getItems().add(new MenuItem(str));
        for(String str : undo)
            undoMenu.getItems().add(new MenuItem(str));
    }

    public BrowserTab(TabList list, String url) {
        this.list = list;
        label = new Label();
        setGraphic(label);
        main = new BorderPane();
        addressBar = new BorderPane();
        addressBar.setCenter(urlBar);
        setContextMenu(new TabContextMenu(this));
        urlBar.setOnAction(event -> openNew(urlBar.getText(), false));

        DownloadManager downloadManager = Sploty.getInstance().getDownloadManager();
        FXUtil.setImage(download, downloadManager.isError()?"/icons/downloaderror.png":downloadManager.isActive()?"/icons/downloadsinakktiv.png":downloadManager.isError()?"/icons/downloaderror.png":"/icons/downloads.png");
        downloadManager.addListener((DownloadManager.ActivityListener) isActive -> FXUtil.setImage(download, isActive?"/icons/downloadsinakktiv.png":"/icons/downloads.png"));
        downloadManager.addListener((DownloadManager.ShowErrorListenr) show -> FXUtil.setImage(download, show?"/icons/downloaderror.png":downloadManager.isActive()?"/icons/downloadsinakktiv.png":"/icons/downloads.png"));
        download.setOnAction((event) -> downloadManager.openDownloads(download));
        FXUtil.setImage(go, "/icons/forward.png");
        go.setOnAction(event -> openNew(urlBar.getText(), false));
        toolBox.getChildren().addAll(go, download);
        addressBar.setRight(toolBox);

        FXUtil.setImage(undoBut, "/icons/undo.png");
        undoBut.setOnAction((event -> {
            String site = undo.get(undo.size()-1);
            undo.remove(undo.size()-1);
            Sploty.getLogger().info("undo: open=" + site + " addredo=" + currentUrl);
            redo.add(currentUrl);
            openNew(site, true);
        }));
        redoBut.setOnAction((event -> {
            String site = redo.get(redo.size()-1);
            Sploty.getLogger().info("redo: open=" + site + " addredo=" + currentUrl);
            redo.remove(redo.size()-1);
            undo.add(currentUrl);
            openNew(site, true);
        }));
        undoBut.setContextMenu(undoMenu);
        redoBut.setContextMenu(redoMenu);
        FXUtil.setImage(redoBut, "/icons/redo.png");
        undoBut.setDisable(true);
        redoBut.setDisable(true);
        left.getChildren().addAll(undoBut, redoBut, secure);

        addressBar.setLeft(left);
        addressBar.setTop(null);

        main.setTop(addressBar);
        setContent(main);
        setOnClosed(event -> {
            if(list.getTabs().size() == 0)
                list.getWindow().getStage().close();
            event.consume();
        });

        label.setOnDragDetected(event -> {
            Dragboard dragboard = label.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(TabList.DRAG_KEY);
            dragboard.setContent(clipboardContent);
            Sploty.getGuiManager().setDraggingTab(this);
            event.consume();
        });
        label.setOnDragDone(event -> {
            Point2D point = new Point2D(event.getScreenX(), event.getScreenY());
            Dragboard dragboard = event.getDragboard();
            BrowserTab tab = Sploty.getGuiManager().getDraggingTab();
            if(!event.isDropCompleted()
                && dragboard.hasString()
                && TabList.DRAG_KEY.equals(dragboard.getString())
                && tab != null) {
                for(Window window : Sploty.getGuiManager().getWindows()){
                    TabList tabBar = window.getTabBar();
                    Rectangle2D tabRect = getAbsoluteRect(tabBar);
                    if(tabRect.contains(point)){
                        int tabInsertIndex = 0;
                        if(!tabBar.getTabs().isEmpty()) {
                            Rectangle2D firstTabRect = getAbsoluteRect(tabBar.getTabs().get(0));
                            if(firstTabRect.getMaxY()+60 < point.getY() || firstTabRect.getMinY() > point.getY()) {
                                continue;
                            }
                            Rectangle2D lastTabRect = getAbsoluteRect(tabBar.getTabs().get(tabBar.getTabs().size() - 1));
                            if(point.getX() < (firstTabRect.getMinX() + firstTabRect.getWidth() / 2)) {
                                tabInsertIndex = 0;
                            } else if(point.getX() > (lastTabRect.getMaxX() - lastTabRect.getWidth() / 2)) {
                                tabInsertIndex = tabBar.getTabs().size();
                            } else {
                                for(int i = 0; i < tabBar.getTabs().size() - 1; i++) {
                                    Tab leftTab = tabBar.getTabs().get(i);
                                    Tab rightTab = tabBar.getTabs().get(i + 1);
                                    if(leftTab instanceof DraggableTab && rightTab instanceof DraggableTab) {
                                        Rectangle2D leftTabRect = getAbsoluteRect(leftTab);
                                        Rectangle2D rightTabRect = getAbsoluteRect(rightTab);
                                        if(betweenX(leftTabRect, rightTabRect, point.getX())) {
                                            tabInsertIndex = i + 1;
                                        }
                                    }
                                }
                            }
                            System.out.println(tabInsertIndex);
                            tabBar.getTabs().remove(this);
                            ListUtil.replace(tabBar.getTabs(), tabInsertIndex, this);
                        }
                        return;
                    }
                }
                tab.getTabPane().getTabs().remove(tab);
                Window window = new Window(new Stage(), tab);
                Sploty.getGuiManager().getWindows().add(window);
            }
        });
        Sploty.getInstance().getSiteExecutor().execute(() -> load(url));
    }

    private Rectangle2D getAbsoluteRect(Control node) {
        return new Rectangle2D(node.localToScene(node.getLayoutBounds().getMinX(), node.getLayoutBounds().getMinY()).getX() + node.getScene().getWindow().getX(),
                node.localToScene(node.getLayoutBounds().getMinX(), node.getLayoutBounds().getMinY()).getY() + node.getScene().getWindow().getY(),
                node.getWidth(),
                node.getHeight());
    }

    /* Do not call this when open a fresh tab */
    public void openNew(String url, boolean noUndo){
        if(!url.equals(currentUrl) && !noUndo) {
            undo.add(currentUrl);
            currentHistory = Sploty.getInstance().getHistory().put(url);
        }
        currentUrl = url;
        Sploty.getInstance().getSiteExecutor().execute(() -> load(url));
    }

    public void reload(){
        Sploty.getInstance().getSiteExecutor().execute(() -> load(currentUrl));
    }

    private void load(String url){
        secure.setContextMenu(null);
        boolean viewSource = false;
        if(url.startsWith("view-source:")){
            viewSource = true;
            url = url.substring(12);
        }
        if(url.startsWith("about:")) {
            String name = url.substring(6).toLowerCase();
            main.setCenter(Sploty.getGuiManager().getAboutHandler().handle(name));
            name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
            setTitle(name);
            currentUrl = url;
            String finalUrl1 = viewSource?"view-source:":"" + url;
            Platform.runLater(() -> urlBar.setText(finalUrl1));
            FXUtil.setImage(secure, "/icons/alert/success.png");
            secure.setTooltip(new Tooltip("Secure -> Local File"));
        } else {
            if(url.startsWith("file://")){
                connection = new Connection(new File(url.substring(7)));
            } else {
                if(!url.startsWith("https://") && !url.startsWith("http://")) {
                    url = "http://" + url;
                }
                setTitle(url);
                currentUrl = url;
                final String finalUrl = viewSource?"view-source:":"" + url;
                Platform.runLater(() -> urlBar.setText(finalUrl));
                try {
                    connection = new Connection(new URL(url));
                } catch (MalformedURLException e) {
                    Sploty.getLogger().exception(e, "Failed parsing the address...");
                    return;
                }
            }
            connection.connect();
            boolean success = connection.isLocal() || !connection.getUrl().getProtocol().equals("http");
            secure.setTooltip(new Tooltip(success?"Secure -> SSL Verification":"UnSecure -> HTTP Verification"));
            if(success){
                if(connection.isLocal()){
                    secure.setTooltip(new Tooltip("Local File!"));
                }else {
                    ContextMenu menu = new ContextMenu();
                    HttpsURLConnection ssl = (HttpsURLConnection) connection.getConnection();
                    if (ssl == null || connection.getError() < -99 && connection.getError() > -200) {
                        success = false;
                        menu.getItems().add(new MenuItem("Something wrong with that connection..."));
                    } else {
                        try {
                            menu.getItems().addAll(new MenuItem("CipherSuite: " + (ssl.getCipherSuite() == null ? "None (usually not good :) )" : ssl.getCipherSuite())),
                                    new MenuItem("PeerPrincipal: " + ssl.getPeerPrincipal().getName()));
                            menu.getItems().add(new MenuItem("Certificates: "));
                            for (Certificate cert : ssl.getServerCertificates())
                                menu.getItems().add(new MenuItem(cert.getType() + " -> " + cert.getPublicKey().getFormat() + " -> " + cert.getPublicKey().getAlgorithm()));
                        } catch (SSLPeerUnverifiedException e) {
                            Sploty.getLogger().exception(e, "Failed to read SSL Details");
                        }
                    }
                    secure.setContextMenu(menu);
                }
            }
            FXUtil.setImage(secure, success?"/icons/alert/success.png":"/icons/alert/warning.png");
            if(viewSource) document = new ViewSource();
            else document = connection.getError() == 200 || connection.getError() == 304?Sploty.getInstance().getDocumentHandler().handleFile(connection):new ErrorDocument();
            document.load(this, connection);
            Node pane = document.render(this);
            Platform.runLater(() -> {
                main.setCenter(pane);
                list.getWindow().getTabBar().requestLayout();
            });
        }
    }

    public Label getLabel() {
        return label;
    }

    private Rectangle2D getAbsoluteRect(Tab tab) {
        Control node = ((BrowserTab) tab).getLabel();
        return getAbsoluteRect(node);
    }

    private boolean betweenX(Rectangle2D r1, Rectangle2D r2, double xPoint) {
        double lowerBound = r1.getMinX() + r1.getWidth() / 2;
        double upperBound = r2.getMaxX() - r2.getWidth() / 2;
        return xPoint >= lowerBound && xPoint <= upperBound;
    }

    @Override
    public void setTitle(String title) {
        if(currentHistory != null) currentHistory.setTitle(title);
        Platform.runLater(() -> label.setText(title));
    }

    @Override
    public String getTitle() {
        return label.getText();
    }

    @Override
    public BrowserTab getTab() {
        return this;
    }

    @Override
    public Stage getStage() {
        return list.getWindow().getStage();
    }

    public TextField getUrlBar() {
        return urlBar;
    }

    public Button getDownload() {
        return download;
    }

    public List<String> getUndo() {
        return undo;
    }

    public List<String> getRedo() {
        return redo;
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    public TabList getList() {
        return list;
    }
}
