import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class StartPage extends Application {

    @Override
    public void start(Stage stage) {

        Label title = new Label("Welcome");
        title.setFont(Font.font("Tahoma", 36));
        title.setStyle("-fx-text-fill: #6A0DAD;");

        Label subtitle = new Label("Please choose an option");

        Button loginButton = new Button("Log In");
        loginButton.setPrefWidth(200);
        loginButton.setOnAction(this::openLogin);

        Button signupButton = new Button("Sign Up");
        signupButton.setPrefWidth(200);
        signupButton.setOnAction(this::SignUp);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #EDE7F5;");

        root.getChildren().addAll(title, subtitle, loginButton, signupButton);

        Scene scene = new Scene(root, 400, 350);

        stage.setTitle("User System");
        stage.setScene(scene);
        stage.show();
    }

    private void openLogin(ActionEvent event) {


        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        currentStage.close();

        Stage loginStage = new Stage();
        LogInUI loginPage = new LogInUI();
        loginPage.start(loginStage);
    }
    private void SignUp(ActionEvent event) {


        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        currentStage.close();

        Stage SignUpStage = new Stage();
        SignUpUI SignUp = new SignUpUI();
        SignUp.start(SignUpStage);
    }

    public static void main(String[] args) {
        launch();
    }
}
