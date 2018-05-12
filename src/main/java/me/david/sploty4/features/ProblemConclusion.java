package me.david.sploty4.features;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.david.sploty4.Sploty;
import me.david.sploty4.dom.DomErrorReporter;
import me.david.sploty4.dom.error.ErrorEntry;
import me.david.sploty4.dom.error.StackErrorEntry;
import me.david.sploty4.util.StringUtil;

import java.io.PrintWriter;
import java.io.StringWriter;

@NoArgsConstructor
@AllArgsConstructor
public class ProblemConclusion {

    @Getter
    @Setter
    private DomErrorReporter errorReporter = null;

    public void build() {
        Platform.runLater(() -> {
            BorderPane box = new BorderPane();
            Stage stage = new Stage();
            stage.setTitle("Problem with loading the Page!");
            stage.getIcons().add(Sploty.SPLOTY_ICON);
            Scene scene = new Scene(box, 800, 600);
            stage.setScene(scene);

            TableView<ErrorEntry> table = new TableView<>();
            TableColumn typeCol = new TableColumn("Type");
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            TableColumn messageCol = new TableColumn("Message");
            messageCol.setCellValueFactory(new PropertyValueFactory<>("Message"));
            TableColumn actionCol = new TableColumn("Action");
            actionCol.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
            actionCol.setCellFactory(new ProblemCellFactory());

            table.getColumns().addAll(typeCol, messageCol, actionCol);
            table.setItems(FXCollections.observableList(errorReporter.getErrors()));
            box.setCenter(table);
            box.setTop(new Text("Problem while loading the page... (" + errorReporter.getErrors().size() + " entry's)"));
            stage.show();
            stage.requestFocus();
        });
    }

    private class ProblemCellFactory implements Callback<TableColumn<ErrorEntry, String>, TableCell<ErrorEntry, String>> {

        @Override
        public TableCell<ErrorEntry, String> call(TableColumn<ErrorEntry, String> errorEntryStringTableColumn) {
            final TableCell<ErrorEntry, String> cell = new TableCell<ErrorEntry, String>() {
                final Button btn = new Button("More Details");

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        btn.setOnAction(event -> {
                            ErrorEntry error = getTableView().getItems().get(getIndex());
                            showMore(error);
                        });
                        if (getTableView().getItems().get(getIndex()) instanceof StackErrorEntry)
                            setGraphic(btn);
                        setText(null);
                    }
                }
            };
            return cell;
        }
    }

    private void showMore(ErrorEntry entry){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("More Details");
        alert.setHeaderText("More details about this Report");
        alert.setContentText("Message: " + entry.getMessage() + "\nType: " + entry.getType().name());

        String exceptionText = StringUtil.fromException(((StackErrorEntry) entry).getThrowable());

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(new Label("The exception stacktrace was:"), 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.show();
    }


}
