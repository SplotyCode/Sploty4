package me.david.sploty4.debugger;

import javafx.collections.ListChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.david.sploty4.Sploty;
import org.controlsfx.control.CheckComboBox;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class Console extends DebugTab {

    private CheckComboBox<Level> comboBox = new CheckComboBox<>();
    private HBox toolBox = new HBox();
    private BorderPane main = new BorderPane();
    private CheckBox javaScirpt = new CheckBox("JavaScript"), splotyLog = new CheckBox("Sploty Logging");

    private TextArea textArea = new TextArea("(No Messages)");
    private List<MessageEntry> messages = new ArrayList<>();

    public Console() {
        super("Console");
        comboBox.getItems().addAll(Level.INFO, Level.WARNING, Level.CONFIG);
        comboBox.getCheckModel().check(Level.INFO);
        comboBox.getCheckModel().check(Level.WARNING);
        comboBox.getCheckModel().check(Level.CONFIG);

        comboBox.getCheckModel().getCheckedItems().addListener((ListChangeListener<? super Level>)  change -> {
            generateMessages();
        });

        javaScirpt.setOnAction(event -> generateMessages());
        splotyLog.setOnAction(event -> generateMessages());


        /*TableColumn messageCol = new TableColumn("Message");
        messageCol.setCellValueFactory(new PropertyValueFactory<>("message"));
        TableColumn extraCol = new TableColumn("Extra");
        extraCol.setCellValueFactory(new PropertyValueFactory<>("extra"));
        messageTable.getColumns().addAll(messageCol, extraCol);*/

        Sploty.getLogger().getLogger().addHandler(new Handler() {
            @Override
            public void publish(LogRecord logRecord) {
                /*System.out.println("--------");
                for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
                    System.out.println(element.getFileName() + " at " + element.getLineNumber());
                }

                System.out.println("--------");*/
                StackTraceElement element = Thread.currentThread().getStackTrace()[6];
                MessageEntry entry = new MessageEntry(logRecord.getMessage(), logRecord.getLevel(), MessageType.LOGGING,  element.getFileName() + ":" + element.getLineNumber(), logRecord.getMillis());
                messages.add(entry);
                if (((entry.getType() == MessageType.LOGGING && splotyLog.isSelected()) || (entry.getType() == MessageType.JAVASCRIPT && javaScirpt.isSelected())) && comboBox.getItemBooleanProperty(entry.getLevel()).getValue()) {
                    textArea.appendText(format.format(entry.time) + "[" + entry.getType().name() + "] [" + entry.level.getName() + "] " + entry.getMessage() + " | " + entry.getExtra() + "\n");
                }
            }
            @Override public void flush() {}
            @Override public void close() throws SecurityException {}
        });

        //TODO reload image
        Button reload = new Button("Reload");
        reload.setOnAction(event -> generateMessages());

        textArea.setWrapText(false);
        textArea.setEditable(false);

        toolBox.getChildren().addAll(comboBox, splotyLog, javaScirpt, reload);
        main.setTop(toolBox);
        main.setCenter(textArea);
        setContent(main);
        generateMessages();
    }

    private DateFormat format = new SimpleDateFormat("HH:mm:ss");

    private void generateMessages() {
        StringBuilder builder = new StringBuilder();
        for (MessageEntry entry : messages) {
            if (((entry.getType() == MessageType.LOGGING && splotyLog.isSelected()) || (entry.getType() == MessageType.JAVASCRIPT && javaScirpt.isSelected())) && comboBox.getItemBooleanProperty(entry.level).getValue()) {
                builder.append(format.format(entry.time)).
                        append("[").
                        append(entry.getType().name()).
                        append("] [").
                        append(entry.level.getName()).
                        append("] ").
                        append(entry.getMessage()).
                        append(" | ").
                        append(entry.getExtra()).
                        append("\n");
            }
        }
        textArea.setText(builder.toString());
        textArea.setVisible(true);
        textArea.requestFocus();
        textArea.requestLayout();
    }

    @AllArgsConstructor
    public class MessageEntry {

        @Getter private final String message;
        @Getter private final Level level;
        @Getter private final MessageType type;
        @Getter private final String extra;
        @Getter private final long time;

    }

    private enum MessageType {

        LOGGING,
        JAVASCRIPT

    }

}
