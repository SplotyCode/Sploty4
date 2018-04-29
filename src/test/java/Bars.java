import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Bars extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        /*DndTabPane tabPane = new DndTabPane();
        tabPane.getTabs().addAll(new Tab("hey"));
        tabPane.getTabs().addAll(new Tab("hey2"));
        tabPane.getTabs().addAll(new Tab("hey3"),
            new Tab("hey3"), new Tab("hey3"), new Tab("hey3"), new Tab("hey3"),
            new Tab("hey3"), new Tab("hey3"), new Tab("hey3"), new Tab("hey3"),
            new Tab("hey3"), new Tab("hey3"), new Tab("hey3"), new Tab("hey3"),
            new Tab("hey3"), new Tab("hey3"), new Tab("hey3"), new Tab("hey3"),
            new Tab("hey3"), new Tab("hey3"), new Tab("hey3"), new Tab("hey3"),
            new Tab("hey3"), new Tab("hey3"), new Tab("hey3"), new Tab("hey3"),
            new Tab("hey3"), new Tab("hey3"), new Tab("hey3"), new Tab("hey3"),
            new Tab("hey3"), new Tab("hey3"), new Tab("hey3"), new Tab("hey3"),
            new Tab("hey3"), new Tab("hey3"), new Tab("hey3"), new Tab("hey3"),
            new Tab("hey3"), new Tab("hey3"), new Tab("hey3"), new Tab("hey3"),
            new Tab("hey3"), new Tab("hey3"), new Tab("hey3"), new Tab("hey3"),
            new Tab("hey3"), new Tab("hey3"), new Tab("hey3"), new Tab("hey3")
        );
        StackPane containerPane = new StackPane(tabPane);

// We need to create the skin manually, could also be your custom skin.
        DnDTabPaneSkin skin = new DnDTabPaneSkin(tabPane);

// Setup the dragging.
        DndTabPaneFactory.setup(DndTabPaneFactory.FeedbackType.OUTLINE, containerPane, skin);

// Set the skin.
        tabPane.setSkin(skin);

        stage.setScene(new Scene(containerPane, 800, 400));
        stage.show();*/
    }
}
