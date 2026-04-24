import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MyGarage{
    public void start(Stage stage) {
        VBox main = new VBox(10);;
        main.setAlignment(Pos.TOP_CENTER);

        Button BackButton = new Button ("Back");
        BackButton.setOnAction(this::Back);
        HBox Back = new HBox(BackButton);
        Back.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("My Garage");
        title.setAlignment(Pos.CENTER);
        main.getChildren().addAll(Back, title);

        stage.setScene(new Scene(main, 400,400));
        stage.setTitle("Garage");
        stage.show();
    }

    private void Back(ActionEvent event)
    {
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        currentStage.close();
        HomePageUI home = new HomePageUI();
        home.show(new Stage());
    }
}
