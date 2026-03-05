import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.sql.SQLException;

public class LogInUI extends Application {

    private TextField usernameField;
    private PasswordField passwordField;
    private TextField visiblePasswordField;
    private Button eyeButton;
    private Label messageLabel;
    private boolean showingPassword = false;
    private LogInService loginService;

    @Override
    public void start(Stage stage) {
        try {
            loginService = new LogInService(
                    "jdbc:mysql://165.227.235.122/ceb96_CI536Login",
                    "ceb96_CI536Login",
                    "4V9o&G$?!ro)chO%H["
            );
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database connection failed!");
        }


        Label titleLabel = new Label("User Login");
        titleLabel.setFont(Font.font("Tahoma", 36));
        titleLabel.setStyle("-fx-text-fill: #6A0DAD;");

        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setPrefWidth(250);

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefWidth(250);

        visiblePasswordField = new TextField();
        visiblePasswordField.setPromptText("Password");
        visiblePasswordField.setPrefWidth(250);
        visiblePasswordField.setVisible(false);
        visiblePasswordField.setManaged(false);

        StackPane passwordStack = new StackPane(passwordField, visiblePasswordField);
        passwordStack.setPrefWidth(250);

        eyeButton = new Button("🐵");
        eyeButton.setFocusTraversable(false);
        eyeButton.setOnAction(this::passwordVisable);

        HBox passwordBox = new HBox(5, passwordStack, eyeButton);
        passwordBox.setAlignment(Pos.CENTER);

        HBox.setHgrow(passwordStack, Priority.ALWAYS);
        passwordStack.setMaxWidth(Double.MAX_VALUE);

        Button confirmButton = new Button("Confirm");
        confirmButton.setPrefWidth(250);
        confirmButton.setOnAction(this::LoginLogic);

        messageLabel = new Label("");
        messageLabel.setFont(Font.font("Tahoma", 14));
        messageLabel.setStyle("-fx-text-fill: red;");

        VBox root = new VBox(20, titleLabel, usernameField, passwordBox, confirmButton, messageLabel);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #EDE7F5;");

        Scene scene = new Scene(root, 450, 400);
        stage.setScene(scene);
        stage.setTitle("Login System");
        stage.show();
    }

    private void passwordVisable(javafx.event.ActionEvent event) {
        if (showingPassword) {
            passwordField.setText(visiblePasswordField.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);

            visiblePasswordField.setVisible(false);
            visiblePasswordField.setManaged(false);

            eyeButton.setText("🐵");
            showingPassword = false;
        }
        else {
            visiblePasswordField.setText(passwordField.getText());
            visiblePasswordField.setVisible(true);
            visiblePasswordField.setManaged(true);

            passwordField.setVisible(false);
            passwordField.setManaged(false);

            eyeButton.setText("🙈");
            showingPassword = true;
        }
    }

    private void LoginLogic(javafx.event.ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.isVisible() ? passwordField.getText() : visiblePasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter username and password!");
            messageLabel.setStyle("-fx-text-fill: red;");
            return;
        }
        try {
            boolean success = loginService.login(username, password);
            if (success) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Login successful!");
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Invalid username or password!");
            }
        }
        catch (SQLException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Database error!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
