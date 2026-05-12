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
        scrollPane.setPrefHeight(500);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        mainLayout.getChildren().addAll(backBox, title, scrollPane);

        // Load items from database
        loadSavedItems();

        Scene scene = new Scene(mainLayout, 750, 650);
        stage.setTitle("Saved Items");
        stage.setScene(scene);
        stage.show();
    }

    private void loadSavedItems() {
        savedContainer.getChildren().clear();


        String sql = "SELECT * FROM saved_items WHERE user_id = ?";

        try (Connection con = DriverManager.getConnection(url, dbUser, dbPassword)) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Session.userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("item_name");
                String details = rs.getString("item_details");
                double price = rs.getDouble("price");
                String imgPath = rs.getString("image_path");
                int id = rs.getInt("SavedID");

                HBox card = createSavedCard(name, details, price, imgPath, id);
                savedContainer.getChildren().add(card);
            }
        } catch (SQLException e) {
            // If the table doesn't exist yet, show a friendly message
            Label errorLabel = new Label("No saved items found.");
            savedContainer.getChildren().add(errorLabel);
            e.printStackTrace();
        }
    }

    private HBox createSavedCard(String title, String subtitle, double price, String imgPath, int id) {
        HBox card = new HBox(20);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-padding: 15; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );

        // Image section
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

        imageView.setFitWidth(120);
        imageView.setFitHeight(80);
        Rectangle clip = new Rectangle(120, 80);
        clip.setArcWidth(15);
        clip.setArcHeight(15);
        imageView.setClip(clip);

        // Info section
        VBox info = new VBox(5);
        Label lblTitle = new Label(title);
        lblTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Label lblSub = new Label(subtitle);
        lblSub.setStyle("-fx-text-fill: #666;");
        Label lblPrice = new Label("£" + String.format("%.2f", price));
        lblPrice.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #6b21a8;");
        info.getChildren().addAll(lblTitle, lblSub, lblPrice);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Buttons section
        VBox actions = new VBox(10);
        actions.setAlignment(Pos.CENTER);

        Button removeBtn = new Button("Unsave");
        removeBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand;");
        removeBtn.setOnAction(e -> unsaveItem(id));

        actions.getChildren().add(removeBtn);

        card.getChildren().addAll(imageView, info, spacer, actions);
        return card;
    }

    private void unsaveItem(int id) {
        try (Connection con = DriverManager.getConnection(url, dbUser, dbPassword)) {
            String sql = "DELETE FROM saved_items WHERE savedID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            loadSavedItems(); // Refresh
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
