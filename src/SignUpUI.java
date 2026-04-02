import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class SignUpUI extends Application {

    private TextField usernameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Label messageLabel;

    private SignUpLogic logic;

    @Override
    public void start(Stage stage) {

        logic = new SignUpLogic();

        Label title = new Label("Sign Up");
        title.setFont(Font.font("Tahoma", 32));
        title.setStyle("-fx-text-fill: #6A0DAD;");

        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(250);

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(250);

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.setMaxWidth(250);

        Button submitButton = new Button("Sign Up");
        submitButton.setPrefWidth(250);
        submitButton.setOnAction(this::handleSignUp);

        messageLabel = new Label();
        messageLabel.setFont(Font.font(14));

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.getChildren().addAll(title, usernameField, passwordField, confirmPasswordField, submitButton, messageLabel);

        root.setStyle("-fx-background-color: #EDE7F5;");

        Scene scene = new Scene(root, 400, 400);
        stage.setTitle("Sign Up");
        stage.setScene(scene);
        stage.show();
    }

    private void handleSignUp(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String result = logic.registerUser(username, password, confirmPassword);

        messageLabel.setText(result);
    }

    public static void main(String[] args) {
        launch();
    }
}