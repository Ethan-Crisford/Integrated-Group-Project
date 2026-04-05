

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class HomePageUI {

    public void show(Stage stage) {
        // Root Layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Header + Navbar
        HBox navbar = new HBox(20);
        navbar.setPadding(new Insets(10, 0, 30, 0));
        navbar.setAlignment(Pos.CENTER_LEFT);

        Label logo = new Label("AutoPartHub");
        logo.setFont(Font.font("Tahoma", 24));
        logo.setTextFill(Color.web("#6A0DAD")); // Matching your login UI purple

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button Basket = new Button ("\uD83D\uDED2");
        Basket.setOnAction(this::ToBasket);

        Button btnGarage = new Button("My Garage");
        btnGarage.setOnAction(this::OpenMyGarage);

        Button btnLogout = new Button("Logout");
        btnLogout.setOnAction(this::Logout);

        Button SavedItems = new Button("⭐");
        SavedItems.setOnAction(this::SavedItems);

        navbar.getChildren().addAll(logo, spacer, SavedItems,Basket, btnGarage, btnLogout);

        root.setTop(navbar);

        // Our USP
        VBox searchArea = new VBox(15);
        searchArea.setAlignment(Pos.CENTER);
        searchArea.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 40; -fx-border-radius: 10;");

        Label headline = new Label("Find Cars or Parts in One Place");
        headline.setFont(Font.font("Tahoma", 20));

        HBox searchBar = new HBox(10);
        searchBar.setAlignment(Pos.CENTER);

        ComboBox<String> typeSelector = new ComboBox<>();
        typeSelector.getItems().addAll("Cars", "Parts");
        typeSelector.setValue("Cars");

        TextField searchInput = new TextField();
        searchInput.setPromptText("Search BMW, Brake Pads, etc...");
        searchInput.setPrefWidth(350);

        Button searchBtn = new Button("Search");
        searchBtn.setStyle("-fx-background-color: #6A0DAD; -fx-text-fill: white;");

        searchBar.getChildren().addAll(typeSelector, searchInput, searchBtn);
        searchArea.getChildren().addAll(headline, searchBar);

        root.setCenter(searchArea);

        // 3. Featured Section (Bottom)
        HBox featuredContent = new HBox(20);
        featuredContent.setPadding(new Insets(20, 0, 0, 0));
        featuredContent.setAlignment(Pos.CENTER);

        // Example placeholders for "Featured Items"
        featuredContent.getChildren().addAll(
                createFeatureCard("2021 Audi A3", "£22,000"),
                createFeatureCard("Brembo Brake Kit", "£120.00"),
                createFeatureCard("Castrol Engine Oil", "£35.00")
        );

        root.setBottom(featuredContent);

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("AutoPartHub - Home");
        stage.setScene(scene);
        stage.show();
    }


    private VBox createFeatureCard(String title, String price) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-color: white;");
        card.setPrefWidth(180);
        card.getChildren().addAll(new Label(title), new Label(price));
        return card;
    }

    private void Logout(ActionEvent event) {
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        currentStage.close();
        StartPage startPage = new StartPage();
        startPage.start(new Stage());
    }

    private void OpenMyGarage(ActionEvent event) {
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        currentStage.close();
        MyGarage mygarage = new MyGarage();
        mygarage.start(new Stage());
    }

    private void ToBasket(ActionEvent event) {
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        currentStage.close();
        Basket basket = new Basket();
        basket.start(new Stage());
    }

    private void SavedItems(ActionEvent event) {
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        currentStage.close();
        SavedItems saved = new SavedItems();
        saved.start(new Stage());
    }
}
