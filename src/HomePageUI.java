import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.sql.SQLException;
import java.util.Objects;

public class HomePageUI {
    private TextField searchInput;
    private ComboBox<String> typeSelector;
    private HBox featuredContent;
    private VBox Results;
    private VBox searchArea; // Global reference for switching layouts
    private BorderPane root;
    public static String type;

    public void show(Stage stage) {
        // Root Layout
        root = new BorderPane();
        root.setPadding(new Insets(20));

        // Header + Navbar
        HBox navbar = new HBox(20);
        navbar.setPadding(new Insets(10, 0, 30, 0));
        navbar.setAlignment(Pos.CENTER_LEFT);

        Label logo = new Label("AutoPartHub");
        logo.setFont(Font.font("Tahoma", 24));
        logo.setTextFill(Color.web("#6A0DAD"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button BasketBtn = new Button("\uD83D\uDED2");
        BasketBtn.setOnAction(this::ToBasket);

        Button btnGarage = new Button("My Garage");
        btnGarage.setOnAction(this::OpenMyGarage);

        Button btnLogout = new Button("Logout");
        btnLogout.setOnAction(this::Logout);

        Button SavedItems = new Button("⭐");
        SavedItems.setOnAction(this::SavedItems);

        navbar.getChildren().addAll(logo, spacer, SavedItems, BasketBtn, btnGarage, btnLogout);
        root.setTop(navbar);

        // Search Section
        searchArea = new VBox(15);
        searchArea.setAlignment(Pos.CENTER);
        searchArea.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 40; -fx-border-radius: 10;");

        Label headline = new Label("Find Cars & Parts In One Place");
        headline.setFont(Font.font("Tahoma", 20));

        HBox searchBar = new HBox(10);
        searchBar.setAlignment(Pos.CENTER);

        typeSelector = new ComboBox<>();
        typeSelector.getItems().addAll("Cars", "Parts");
        typeSelector.setValue("Cars");

        searchInput = new TextField();
        searchInput.setPromptText("Search BMW, Brake Pads, etc...");
        searchInput.setPrefWidth(350);

        Button searchBtn = new Button("Search");
        searchBtn.setStyle("-fx-background-color: #6A0DAD; -fx-text-fill: white; -fx-font-weight: bold;");
        searchBtn.setOnAction(this::search);

        searchBar.getChildren().addAll(typeSelector, searchInput, searchBtn);
        searchArea.getChildren().addAll(headline, searchBar);

        root.setCenter(searchArea);

        // Featured Items
        featuredContent = new HBox(20);
        featuredContent.setPadding(new Insets(20, 0, 0, 0));
        featuredContent.setAlignment(Pos.CENTER);
        featuredContent.getChildren().addAll(
                createFeatureCard("2021 Audi A3", "£22,000"),
                createFeatureCard("Brembo Brake Kit", "£120.00"),
                createFeatureCard("Castrol Engine Oil", "£35.00")
        );

        root.setBottom(featuredContent);


        Results = new VBox(15);
        Results.setPadding(new Insets(20));
        Results.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(root, 900, 700);
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

    private void search(ActionEvent event) {
        try {
            Results.getChildren().clear();
            type = typeSelector.getValue();
            String query = searchInput.getText();


            SearchLogic searchLogic = new SearchLogic();
            searchLogic.SearchStatement(query, Results);

            // ScrollPane logic
            ScrollPane scrollPane = new ScrollPane(Results);
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefHeight(400);
            scrollPane.setStyle("-fx-background-color:transparent; -fx-background:transparent;");

            // Immediately switches the layout to show the search bar and the scrolling results
            // I removed the 'isEmpty' check because it was firing just before the DB could finish will remove these notes later.
            VBox mainLayout = new VBox(20, searchArea, scrollPane);
            root.setCenter(mainLayout);
            root.setBottom(null); // Hide featured items to show search results

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void Logout(ActionEvent event) {
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        currentStage.close();
        new StartPage().start(new Stage());
    }

    private void OpenMyGarage(ActionEvent event) {
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        currentStage.close();
        new MyGarage().start(new Stage());
    }

    private void ToBasket(ActionEvent event) {
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        currentStage.close();
        new Basket().start(new Stage());
    }

    private void SavedItems(ActionEvent event) {
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        currentStage.close();
        new SavedItems().start(new Stage());
    }
}