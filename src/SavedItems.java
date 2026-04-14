import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SavedItems {
    public void start(Stage stage) {
        Label Title = new Label("Saved Items");
        HBox hBox = new HBox(Title);
        hBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(hBox, 400, 400);
        stage.setTitle("Saved Items");
        stage.setScene(scene);
        stage.show();
    }
}
