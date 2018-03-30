package me.david.sploty4.setting.settings;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import me.david.sploty4.Sploty;
import me.david.sploty4.gui.fields.NumberTextField;
import me.david.sploty4.setting.Setting;
import me.david.sploty4.storage.FileComponent;
import me.david.sploty4.storage.FileSerializer;
import me.david.sploty4.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;

public class ConnectionSettings extends Setting {

    private long timeoutHtml, timeOutImage, timeOutCss, timeOutJavascript, timeOutOther;
    private ArrayList<ProxyProfile> proxyProfiles;

    public ConnectionSettings() {
        super("Connection", new File(Sploty.getDirectory(), "settings/connection.txt"));
    }

    @Override
    public void read(FileSerializer serializer) throws IOException {
        timeoutHtml = serializer.readLong();
        timeOutImage = serializer.readLong();
        timeOutCss = serializer.readLong();
        timeOutJavascript = serializer.readLong();
        timeOutOther = serializer.readLong();
        int profiles = serializer.readVarInt();
        proxyProfiles = new ArrayList<>();
        for(int i = 0;i < profiles;i++) {
            ProxyProfile proxyProfile = new ProxyProfile();
            proxyProfile.read(serializer);
            proxyProfiles.add(proxyProfile);
        }
    }

    @Override
    public void write(FileSerializer serializer) throws IOException {
        serializer.writeLong(timeoutHtml);
        serializer.writeLong(timeOutImage);
        serializer.writeLong(timeOutCss);
        serializer.writeLong(timeOutJavascript);
        serializer.writeLong(timeOutOther);
        serializer.writeVarInt(proxyProfiles.size());
        for(ProxyProfile proxyProfile : proxyProfiles) proxyProfile.write(serializer);
    }

    @Override
    public void render(Tab tab, Stage stage) {
        VBox vBox = new VBox();
        Text text = new Text("TimeOuts (in seconds)");
        text.setFont(Font.font(null, FontWeight.BOLD, 24));

        HBox html = new HBox();
        NumberTextField htmlc = new NumberTextField(timeoutHtml/1000+"");
        htmlc.setOnAction((event -> timeoutHtml = Long.valueOf(htmlc.getText())*1000));
        html.getChildren().addAll(new Label("Html: "), htmlc);

        HBox css = new HBox();
        NumberTextField cssc = new NumberTextField(timeOutCss/1000+"");
        cssc.setOnAction((event -> timeOutCss = Long.valueOf(cssc.getText())*1000));
        css.getChildren().addAll(new Label("Css: "), cssc);

        HBox js = new HBox();
        NumberTextField jsc = new NumberTextField(timeOutJavascript/1000+"");
        jsc.setOnAction((event -> timeOutJavascript = Long.valueOf(cssc.getText())*1000));
        js.getChildren().addAll(new Label("Javascript: "), jsc);

        HBox image = new HBox();
        NumberTextField imagec = new NumberTextField(timeOutImage/1000+"");
        htmlc.setOnAction((event -> timeOutImage = Long.valueOf(imagec.getText())*1000));
        image.getChildren().addAll(new Label("Images: "), imagec);

        HBox other = new HBox();
        NumberTextField otherc = new NumberTextField(timeOutOther/1000+"");
        otherc.setOnAction((event -> timeOutOther = Long.valueOf(otherc.getText())*1000));
        other.getChildren().addAll(new Label("Other: "), otherc);

        Text proxy = new Text("Proxys");
        proxy.setFont(Font.font(null, FontWeight.BOLD, 24));

        TableView<ProxyProfile> proxyTable = new TableView<>();
        proxyTable.setItems(FXCollections.observableArrayList(proxyProfiles));
        proxyTable.setEditable(true);

        TableColumn<ProxyProfile, Proxy.Type> type = new TableColumn<>("Type");
        type.setCellFactory((param) -> new ComboBoxTableCell<>(new StringConverter<Proxy.Type>() {
            @Override public String toString(Proxy.Type type) {return type.name();}
            @Override public Proxy.Type fromString(String s) {return Proxy.Type.valueOf(s);}
        }, Proxy.Type.values()));
        type.setCellValueFactory(typea -> new SimpleObjectProperty<>(typea.getValue().getType()));

        TableColumn<ProxyProfile, Boolean> enabled = new TableColumn<>("Enabled");
        enabled.setCellValueFactory(
                param -> new SimpleBooleanProperty(param.getValue().isEnabled()){{
                    addListener((observableValue, aBoolean, t1) -> {
                        for(ProxyProfile profile : proxyProfiles) profile.setEnabled(false);
                        param.getValue().setEnabled(t1);
                        proxyTable.setItems(FXCollections.observableArrayList(proxyProfiles));
                        proxyTable.refresh();
                    });
                }}
        );
        enabled.setCellFactory(CheckBoxTableCell.forTableColumn(enabled));

        TableColumn<ProxyProfile, String> nameColum = new TableColumn<>("Name");
        nameColum.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()){{
            addListener((observableValue, aBoolean, t1) -> {
                param.getValue().setName(t1);
                proxyTable.setItems(FXCollections.observableArrayList(proxyProfiles));
                proxyTable.refresh();
            });
        }});
        nameColum.setCellFactory(param -> new TextFieldTableCell<>(new DefaultStringConverter()));

        TableColumn<ProxyProfile, String> addressColum = new TableColumn<>("Address");
        addressColum.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAddress()){{
            addListener((observableValue, aBoolean, t1) -> {
                param.getValue().setAddress(t1);
                proxyTable.setItems(FXCollections.observableArrayList(proxyProfiles));
                proxyTable.refresh();
            });
        }});
        addressColum.setCellFactory(param -> new TextFieldTableCell<>(new DefaultStringConverter()));

        TableColumn<ProxyProfile, String> portColum = new TableColumn<>("Port");
        portColum.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getPort())){{
            addListener((observableValue, aBoolean, t1) -> {
                param.getValue().setPort(Integer.valueOf(t1));
                proxyTable.setItems(FXCollections.observableArrayList(proxyProfiles));
                proxyTable.refresh();
            });
        }});
        portColum.setCellFactory(param -> new TextFieldTableCell<>(new DefaultStringConverter()));

        proxyTable.getColumns().addAll(enabled, nameColum, portColum, type);

        HBox add = new HBox();
        TextField name = new TextField();
        TextField address = new TextField();
        NumberTextField port = new NumberTextField();
        ComboBox<Proxy.Type> proxyType = new ComboBox<>();
        proxyType.getItems().addAll(Proxy.Type.values());
        proxyType.setValue(Proxy.Type.HTTP);
        Button button = new Button("Create");
        button.setOnAction((event -> {
            if(StringUtil.isEmpty(name.getText())){
                error("Please give a valid Name");
                return;
            }
            for(ProxyProfile profile : proxyProfiles)
                if(profile.name.equals(name.getText())){
                    error("The Name already exists");
                    return;
                }
            try {
                new URL(address.getText());
            } catch (MalformedURLException e) {
                error("Butte gebe eine richtige Addresse an!");
                return;
            }
            proxyProfiles.add(new ProxyProfile(address.getText(), false, name.getText(), Integer.valueOf(port.getText()), proxyType.getValue()));
            proxyTable.setItems(FXCollections.observableArrayList(proxyProfiles));
            proxyTable.refresh();
        }));

        Text addText = new Text("Add");
        addText.setFill(Color.GREEN);
        addText.setFont(Font.font(null, FontWeight.BOLD, 16));
        add.getChildren().addAll(
                new Label("Name: "), name,
                new Label("Address: "), address,
                new Label("Port: "), port,
                new Label("Type: "), proxyType,
                button
        );

        vBox.getChildren().addAll(text, html, css, js, image, other, proxy, proxyTable, addText, add);
        tab.setContent(vBox);
    }

    public Proxy getCurrentProxy(){
        for(ProxyProfile profile : proxyProfiles)
            if(profile.isEnabled())
                return new Proxy(profile.type, new InetSocketAddress(profile.address, profile.port));
        return Proxy.NO_PROXY;
    }

    private void error(String error){
        new Alert(Alert.AlertType.ERROR, error, ButtonType.CLOSE).show();
    }



    public static class ProxyProfile implements FileComponent {

        private String address, name;
        private boolean enabled;
        private int port;
        private Proxy.Type type;

        public ProxyProfile() {}
        public ProxyProfile(String address, boolean enabled, String name, int port, Proxy.Type type) {
            this.address = address;
            this.name = name;
            this.port = port;
            this.type = type;
        }

        @Override
        public void read(FileSerializer serializer) throws IOException {
            address = serializer.readString();
            name = serializer.readString();
            port = serializer.readVarInt();
            type = serializer.readEnum(Proxy.Type.class);
            enabled = serializer.readBoolean();
        }

        @Override
        public void write(FileSerializer serializer) throws IOException {
            serializer.writeString(address);
            serializer.writeString(name);
            serializer.writeVarInt(port);
            serializer.writeEnum(type);
            serializer.writeBoolean(enabled);
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public Proxy.Type getType() {
            return type;
        }

        public void setType(Proxy.Type type) {
            this.type = type;
        }
    }

    public long getTimeoutHtml() {
        return timeoutHtml;
    }

    public void setTimeoutHtml(long timeoutHtml) {
        this.timeoutHtml = timeoutHtml;
    }

    public long getTimeOutImage() {
        return timeOutImage;
    }

    public void setTimeOutImage(long timeOutImage) {
        this.timeOutImage = timeOutImage;
    }

    public long getTimeOutCss() {
        return timeOutCss;
    }

    public void setTimeOutCss(long timeOutCss) {
        this.timeOutCss = timeOutCss;
    }

    public long getTimeOutJavascript() {
        return timeOutJavascript;
    }

    public void setTimeOutJavascript(long timeOutJavascript) {
        this.timeOutJavascript = timeOutJavascript;
    }

    public long getTimeOutOther() {
        return timeOutOther;
    }

    public void setTimeOutOther(long timeOutOther) {
        this.timeOutOther = timeOutOther;
    }

    public ArrayList<ProxyProfile> getProxyProfiles() {
        return proxyProfiles;
    }

    public void setProxyProfiles(ArrayList<ProxyProfile> proxyProfiles) {
        this.proxyProfiles = proxyProfiles;
    }
}
