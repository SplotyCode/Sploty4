package me.david.sploty4.document;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import me.david.sploty4.gui.tab.TabHandler;
import me.david.sploty4.io.Connection;
import me.david.sploty4.util.ErrorUtil;

import java.io.IOException;
import java.net.HttpURLConnection;

public class ErrorDocument implements Document {

    private String message, messageServer;
    private boolean local;
    private int code;

    @Override
    public Pane render(TabHandler tab) {
        VBox main = new VBox();
        Text title = new Text("Error - " + code);
        title.setFont(Font.font(null, FontWeight.BOLD, 70));
        if(local) main.getChildren().addAll(title, new Text("Info: Local File System"), new Text("Error: " + message), new Text("ErrorCode: " + code));
        else main.getChildren().addAll(title, new Text("Reporter: " + (code < 0?"Client":"Server")), new Text("ErrorCode: " + code), new Text("ClientMessage: " + message), new Text("ServerMessage: " + messageServer));
        main.setAlignment(Pos.CENTER);
        main.setSpacing(8);
        return main;
    }

    @Override
    public void load(TabHandler tab, Connection connection) {
        code = connection.getError();
        message = ErrorUtil.getErrorMessage(code);
        try {
            if(connection.getConnection() == null) local = true;
            else messageServer = ((HttpURLConnection) connection.getConnection()).getResponseMessage();
        } catch (IOException e) {
            messageServer = "None";
        }
    }

    @Override
    public void close() {

    }
}
