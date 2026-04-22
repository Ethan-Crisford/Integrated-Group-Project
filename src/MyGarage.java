import javafx.application.Application;
import javafx.event.ActionEvent;
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
        Button back = new Button("Back");
        Button AddItem = new Button("Add Item");
        AddItem.setOnAction(this::AddItem);
        back.setOnAction(this::back);
        back.setAlignment(Pos.TOP_LEFT);
        HBox hBox = new HBox(back,Title);
        hBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(hBox, 400, 400);
        stage.setTitle("My Garage");
        stage.setScene(scene);
        stage.show();
    }

    private void back(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
        HomePageUI home = new HomePageUI();
        home.show(new Stage());
    }

    private void AddItem(ActionEvent event) {

    }
}
