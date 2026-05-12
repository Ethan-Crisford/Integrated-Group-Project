import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.sql.*;
import java.util.Objects;
import java.io.File;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

public class SearchLogic {
    private final String url = "jdbc:mysql://165.227.235.122/ceb96_CI536Database";
    private final String dbUser = "ceb96_CI536Login";
    private final String dbPassword = "4V9o&G$?!ro)chO%H[";

    public void SearchStatement(String query, VBox resultsBox) throws SQLException {
        System.out.println("Connecting to database for: " + query);
        Platform.runLater(() -> resultsBox.getChildren().clear());

        if (Objects.equals(HomePageUI.type, "Cars")) {
            String CSearch = "SELECT * FROM cars WHERE make LIKE ? OR model LIKE ? OR CONCAT(make, ' ', model) LIKE ?";
            try (Connection con = DriverManager.getConnection(url, dbUser, dbPassword)) {
                PreparedStatement psSearch = con.prepareStatement(CSearch);
                String Squery = "%" + query + "%";
                psSearch.setString(1, Squery);
                psSearch.setString(2, Squery);
                psSearch.setString(3, Squery);

                ResultSet rs = psSearch.executeQuery();
                while (rs.next()) {
                    HBox rRow = createStyledCard(
                            rs.getInt("year") + " " + rs.getString("make") + " " + rs.getString("model"),
                            rs.getInt("miles") + " miles",
                            rs.getDouble("price"),
                            rs.getString("image_path")
                    );
                    Platform.runLater(() -> resultsBox.getChildren().add(rRow));
                }
            } catch (SQLException e) { e.printStackTrace(); }
        } else {
            String PSearch = "SELECT * FROM parts WHERE name LIKE ? OR brand LIKE ? OR CONCAT(name, ' ', brand) LIKE ?";
            try (Connection con = DriverManager.getConnection(url, dbUser, dbPassword)) {
                PreparedStatement psSearch = con.prepareStatement(PSearch);
                String Squery = "%" + query + "%";
                psSearch.setString(1, Squery);
                psSearch.setString(2, Squery);
                psSearch.setString(3, Squery);

                ResultSet rs = psSearch.executeQuery();
                while (rs.next()) {
                    HBox rRow = createStyledCard(
                            rs.getString("name"),
                            rs.getString("brand") + " | Condition: " + rs.getString("part_condition"),
                            rs.getDouble("price"),
                            rs.getString("image_path")
                    );
                    Platform.runLater(() -> resultsBox.getChildren().add(rRow));
                }
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    private HBox createStyledCard(String title, String subtitle, double price, String imgPath) {
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
        } catch (Exception e) { e.printStackTrace(); }

        Rectangle clip = new Rectangle(120, 80);
        clip.setArcWidth(10);
        clip.setArcHeight(10);
        imageView.setClip(clip);

        // FAVORITE STAR BUTTON
        Button favBtn = new Button("☆");
        favBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #888; -fx-font-size: 20px; -fx-cursor: hand; -fx-padding: 0;");

        favBtn.setOnAction(e -> {
            if (favBtn.getText().equals("☆")) {
                saveItemToDb(title, subtitle, price, imgPath);
                favBtn.setText("★");
                favBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #ffcc00; -fx-font-size: 20px; -fx-padding: 0;");
            }
        });


        VBox infoBox = new VBox(5);

        // Title and Star in one row
        HBox titleRow = new HBox(10);
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");
        titleRow.getChildren().addAll(titleLabel, favBtn);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Label subLabel = new Label(subtitle);
        subLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

        Label priceLabel = new Label("£" + String.format("%.2f", price));
        priceLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #6b21a8; -fx-font-weight: bold;");

        infoBox.getChildren().addAll(titleRow, subLabel, priceLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addToBasket = new Button("Add To Basket");
        addToBasket.setStyle("-fx-background-color: #6b21a8; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 5; -fx-cursor: hand;");
        addToBasket.setOnAction(e -> Basket.basketItems.add(title + " - £" + price));

        card.getChildren().addAll(imageView, infoBox, spacer, addToBasket);
        return card;
    }

    private void saveItemToDb(String title, String details, double price, String imgPath) {
        String sql = "INSERT INTO saved_items (user_id, item_name, item_details, price, image_path) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection(url, dbUser, dbPassword)) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Session.userId);
            ps.setString(2, title);
            ps.setString(3, details);
            ps.setDouble(4, price);
            ps.setString(5, imgPath);
            ps.executeUpdate();
            System.out.println("DEBUG: Item saved to favorites for User ID: " + Session.userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}