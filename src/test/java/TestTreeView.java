import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

public class TestTreeView extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        TabPane pane = new TabPane();
        pane.getTabs().add(new TreePane());
        stage.setScene(new Scene(pane));
        stage.setTitle("tesad");
        stage.show();
    }

    private static class TreePane extends Tab {

        private TreePane() {
            setContent(new TreeView<>());
        }
    }
}
