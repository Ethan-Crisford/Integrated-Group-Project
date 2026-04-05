import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MyGarage{
    public void start(Stage stage) {
        Label Title = new Label("My Garage");
        HBox hBox = new HBox(Title);
        hBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(hBox, 400, 400);
        stage.setTitle("My Garage");
        stage.setScene(scene);
        stage.show();
    }
}
