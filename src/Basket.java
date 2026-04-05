import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Basket {
    public void start(Stage stage) {
        Label Title = new Label("Basket");
        HBox hBox = new HBox(Title);
        hBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(hBox, 400, 400);
        stage.setTitle("Basket");
        stage.setScene(scene);
        stage.show();
    }
}
