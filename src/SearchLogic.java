import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.sql.*;
import java.util.Objects;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class SearchLogic {
    private final String url = "jdbc:mysql://165.227.235.122/ceb96_CI536Database";
    private final String dbUser = "ceb96_CI536Login";
    private final String dbPassword = "4V9o&G$?!ro)chO%H[";

    public void SearchStatement(String query, VBox resultsBox) throws SQLException {
        System.out.println("Connecting to database for: " + query);

        // it will always clear the previous results on the UI thread now
        Platform.runLater(() -> resultsBox.getChildren().clear());

        if (Objects.equals(HomePageUI.type, "Cars")) {
            // Updated SQL to match the phpMyAdmin screen: note the column names
            String CSearch = "SELECT * FROM cars WHERE make LIKE ? OR model LIKE ? OR CONCAT(make, ' ', model) LIKE ?";

            try (Connection con = DriverManager.getConnection(url, dbUser, dbPassword)) {
                PreparedStatement psSearch = con.prepareStatement(CSearch);
                String Squery = "%" + query + "%";

                psSearch.setString(1, Squery);
                psSearch.setString(2, Squery);
                psSearch.setString(3, Squery);

                ResultSet rs = psSearch.executeQuery();
                boolean foundAnything = false;

                while (rs.next()) {
                    foundAnything = true;

                    String make = rs.getString("make");
                    String model = rs.getString("model");
                    int year = rs.getInt("year");
                    int miles = rs.getInt("miles");
                    double price = rs.getDouble("price");

                    System.out.println("Found in DB: " + make + " " + model);

                    HBox rRow = createStyledCard(year + " " + make + " " + model, miles + " miles", price, resultsBox);
                    Platform.runLater(() -> resultsBox.getChildren().add(rRow));
                }

                if(!foundAnything) System.out.println("Database returned 0 rows for Cars.");

            } catch (SQLException e) {
                System.out.println("SQL Error in Cars search!");
                e.printStackTrace();
            }
        } else {
            // Parts Search Section
            String PSearch = "SELECT * FROM parts WHERE name LIKE ? OR brand LIKE ? OR CONCAT(name, ' ', brand) LIKE ?";
            try (Connection con = DriverManager.getConnection(url, dbUser, dbPassword)) {
                PreparedStatement psSearch = con.prepareStatement(PSearch);
                String Squery = "%" + query + "%";

                psSearch.setString(1, Squery);
                psSearch.setString(2, Squery);
                psSearch.setString(3, Squery);

                ResultSet rs = psSearch.executeQuery();
                boolean foundAnything = false;

                while (rs.next()) {
                    foundAnything = true;
                    String name = rs.getString("name");
                    String brand = rs.getString("brand");
                    String condition = rs.getString("part_condition");
                    double price = rs.getDouble("price");

                    System.out.println("Found in DB: " + name);

                    HBox rRow = createStyledCard(name, brand + " | Condition: " + condition, price, resultsBox);
                    Platform.runLater(() -> resultsBox.getChildren().add(rRow));
                }

                if(!foundAnything) System.out.println("Database returned 0 rows for Parts.");

            } catch (SQLException e) {
                System.out.println("SQL Error in Parts search!");
                e.printStackTrace();
            }
        }
    }

    private HBox createStyledCard(String title, String subtitle, double price, VBox resultsBox) {
        HBox card = new HBox(20);
        card.setPrefWidth(700);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-radius: 10; " +
                        "-fx-border-color: #ddd; " +
                        "-fx-padding: 20; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );

        VBox infoBox = new VBox(5);
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Label subLabel = new Label(subtitle);
        subLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

        Label priceLabel = new Label("£" + String.format("%.2f", price));
        priceLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #6b21a8; -fx-font-weight: bold;");

        infoBox.getChildren().addAll(titleLabel, subLabel, priceLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addToBasket = new Button("Add To Basket");
        addToBasket.setStyle(
                "-fx-background-color: #6b21a8; -fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-padding: 10 20; -fx-background-radius: 5; -fx-cursor: hand;"
        );

        addToBasket.setOnAction(e -> {
            Basket.basketItems.add(title + " - £" + price);
            System.out.println("Added to basket: " + title);
        });

        card.getChildren().addAll(infoBox, spacer, addToBasket);
        return card;
    }
}