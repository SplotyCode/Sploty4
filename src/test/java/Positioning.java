import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Positioning extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("test");
        Pane pane = new Pane();
        Button button = new Button("test");
        button.setLayoutX(100);
        pane.getChildren().add(button);
        stage.setScene(new Scene(pane, 1200, 600));
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
