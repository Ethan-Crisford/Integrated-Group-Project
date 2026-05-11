import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Random;

public class Checkout
{
    public void start(Stage stage)
    {
        Label emailPrompt = new Label("Enter your email:");
        TextField emailField = new TextField();
        emailField.setPromptText("example@gmail.com");
        Label errorLabel = new Label("");
        emailField.setMaxWidth(200);

        Button backButton = new Button("Back");
        //backButton.setOnAction(this::back);

        backButton.setOnAction(event ->
        {
            Basket basket = new Basket();
            basket.start(stage);
        });

        Button confirmButton = new Button("Confirm");

        VBox root = new VBox(15);
        VBox topBox = new VBox(backButton);
        topBox.setAlignment(Pos.TOP_LEFT);

        VBox contentBox = new VBox(15);
        contentBox.setAlignment(Pos.CENTER);

        contentBox.getChildren().addAll(
                emailPrompt,
                errorLabel,
                emailField,
                confirmButton
        );

        root.getChildren().addAll(
                topBox,
                contentBox
        );

        Scene scene = new Scene(root, 500, 300);

        // Button click
        confirmButton.setOnAction(event ->
        {

            String email = emailField.getText();

            // Email input validation using a regular expression (regex) instead of just checking for @ symbol
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
            {
                emailField.setStyle("-fx-border-color: red;");
                errorLabel.setStyle("-fx-text-fill: red;");
                errorLabel.setText("Please enter a valid email address.");
                return;
            }
            else
            {
                emailField.setStyle("");
                errorLabel.setText("");
            }

            // Generate order number using .random import
            Random random = new Random();
            int orderNumber = 100000 + random.nextInt(900000);

            // Thank you screen
            Label thankYou = new Label("Thank you for your order!");
            Label orderLabel = new Label("Order Number: #" + orderNumber);

            Label emailLabel = new Label(
                    "A confirmation email has been sent to: "
            );

            Label userEmail = new Label(email);

            Button homeButton = new Button("Continue shopping");

            homeButton.setOnAction(e -> {
                HomePageUI home = new HomePageUI();
                home.start(stage);
            });

            VBox thankYouRoot = new VBox(15);
            thankYouRoot.setAlignment(Pos.CENTER);

            thankYouRoot.getChildren().addAll(
                    thankYou,
                    orderLabel,
                    emailLabel,
                    userEmail,
                    homeButton
            );

            Scene thankYouScene = new Scene(thankYouRoot, 500, 300);

            stage.setScene(thankYouScene);
        });

        stage.setTitle("Checkout");
        stage.setScene(scene);
        stage.show();
    }
}