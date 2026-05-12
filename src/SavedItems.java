import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.File;
import java.sql.*;

public class SavedItems {
    private final String url = "jdbc:mysql://165.227.235.122/ceb96_CI536Database";
    private final String dbUser = "ceb96_CI536Login";
    private final String dbPassword = "4V9o&G$?!ro)chO%H[";
    private VBox savedContainer;

    public void start(Stage stage) {
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setStyle("-fx-background-color: #f4f4f4;");

        Button backBtn = new Button("Back");
        backBtn.setStyle("-fx-background-color: #6b21a8; -fx-text-fill: white; -fx-cursor: hand;");
        backBtn.setOnAction(e -> {
            stage.close();
            new HomePageUI().start(new Stage());
        });

        HBox backBox = new HBox(backBtn);
        backBox.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("Saved Items");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");

        savedContainer = new VBox(15);
        savedContainer.setAlignment(Pos.TOP_CENTER);
        savedContainer.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(savedContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(600);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        mainLayout.getChildren().addAll(backBox, title, scrollPane);

        loadSavedItems();

        Scene scene = new Scene(mainLayout, 800, 700);
        stage.setTitle("Saved Items");
        stage.setScene(scene);
        stage.show();
    }

    private void loadSavedItems() {
        savedContainer.getChildren().clear();


        String sql = "SELECT savedID, item_name, item_details, price, image_path FROM saved_items WHERE user_id = ?";

        try (Connection con = DriverManager.getConnection(url, dbUser, dbPassword)) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Session.userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("savedID");
                String name = rs.getString("item_name");
                String details = rs.getString("item_details");
                double price = rs.getDouble("price");
                String imgPath = rs.getString("image_path");

                HBox card = createSavedCard(name, details, price, imgPath, id);
                savedContainer.getChildren().add(card);
            }

            if (savedContainer.getChildren().isEmpty()) {
                Label noItems = new Label("No saved items found.");
                noItems.setStyle("-fx-text-fill: #888; -fx-font-size: 16px;");
                savedContainer.getChildren().add(noItems);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private HBox createSavedCard(String title, String subtitle, double price, String imgPath, int id) {
        HBox card = new HBox(20);
        card.setPrefWidth(700);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-radius: 10; " +
                        "-fx-border-color: #ddd; " +
                        "-fx-padding: 15; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );

        ImageView imageView = new ImageView();
        try {
            if (imgPath != null && !imgPath.isEmpty()) {
                File file = new File(imgPath);
                if (file.exists()) {
                    imageView.setImage(new Image(file.toURI().toString(), 120, 80, true, true));
                } else {
                    imageView.setImage(new Image("https://via.placeholder.com/120x80.png?text=No+Photo"));
                }
            }
        } catch (Exception e) {
            imageView.setImage(new Image("https://via.placeholder.com/120x80.png?text=Error"));
        }

        Rectangle clip = new Rectangle(120, 80);
        clip.setArcWidth(10);
        clip.setArcHeight(10);
        imageView.setClip(clip);

        // Same layout as MyGarage
        VBox infoBox = new VBox(5);
        Label titleLabel = new Label(title); // This will show Make/Model
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Label subLabel = new Label(subtitle); // This will show Mileage
        subLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

        Label priceLabel = new Label("£" + String.format("%.2f", price));
        priceLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #6b21a8; -fx-font-weight: bold;");

        infoBox.getChildren().addAll(titleLabel, subLabel, priceLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox actionBox = new VBox(10);
        actionBox.setAlignment(Pos.CENTER);


        Button addToBasket = new Button("Add To Basket");
        addToBasket.setStyle("-fx-background-color: #6b21a8; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5; -fx-cursor: hand;");
        addToBasket.setOnAction(e -> {
            Basket.basketItems.add(title + "|" + String.format("%.2f", price) + "|" + imgPath);
        });

        Button removeBtn = new Button("Unsave");
        removeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #ef4444; -fx-font-size: 12px; -fx-cursor: hand;");
        removeBtn.setOnAction(e -> unsaveItem(id));

        actionBox.getChildren().addAll(addToBasket, removeBtn);

        card.getChildren().addAll(imageView, infoBox, spacer, actionBox);
        return card;
    }

    private void unsaveItem(int id) {
        try (Connection con = DriverManager.getConnection(url, dbUser, dbPassword)) {

            String sql = "DELETE FROM saved_items WHERE savedID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            loadSavedItems();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

