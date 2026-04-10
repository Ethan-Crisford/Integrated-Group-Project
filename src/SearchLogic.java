import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.util.Objects;

public class SearchLogic {
    private final String url = "jdbc:mysql://165.227.235.122/ceb96_CI536Database";
    private final String dbUser = "ceb96_CI536Login";
    private final String dbPassword = "4V9o&G$?!ro)chO%H[";
    private Connection con;

    public void SearchStatement (String query, VBox resultsBox) throws SQLException {
        System.out.println("Searched for " + query);
        if (Objects.equals(HomePageUI.type, "Cars")) {
            String CSearch = "SELECT * FROM cars WHERE model LIKE ? OR make LIKE ? OR CONCAT(make, ' ',model) LIKE ?";
            try (Connection con = DriverManager.getConnection(url, dbUser, dbPassword)) {
                PreparedStatement psSearch = con.prepareStatement(CSearch);
                String Squery = "%" + query + "%";
                psSearch.setString(1, Squery);
                psSearch.setString(2, Squery);
                psSearch.setString(3, Squery);
                ResultSet rs = psSearch.executeQuery();
                while (rs.next()) {
                    String model = rs.getString("model");
                    String make = rs.getString("make");
                    int year = rs.getInt("year");
                    int miles = rs.getInt("miles");
                    double price = rs.getDouble("price");

                    HBox Rrow = new  HBox(10);
                    Label result = new Label(make + " | " + model + " | Year: " + year + " | Miles: " + miles + " | Price: £" + price + " |");

                    String item = make + " | " + model + " | Year: " + year + " | Miles: " + miles + " | Price: £" + price + " |";
                    Button addToBasket = new Button("Add To Basket");
                    addToBasket.setOnAction(e -> {
                        Basket.basketItems.add(item);
                    });

                    Rrow.getChildren().addAll(result,addToBasket);
                    resultsBox.getChildren().add(Rrow);

                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Database error");
            }
        }
        else{
            String CSearch = "SELECT * FROM parts WHERE name LIKE ? OR brand LIKE ? OR CONCAT(name, ' ',brand) LIKE ?";
            try (Connection con = DriverManager.getConnection(url, dbUser, dbPassword)) {
                PreparedStatement psSearch = con.prepareStatement(CSearch);
                String Squery = "%" + query + "%";
                psSearch.setString(1, Squery);
                psSearch.setString(2, Squery);
                psSearch.setString(3, Squery);
                ResultSet rs = psSearch.executeQuery();
                while (rs.next()) {
                    Integer id = rs.getInt("id");
                    String name = rs.getString("name");
                    String category = rs.getString("category");
                    Double price = rs.getDouble("price");
                    String partCondition = rs.getString("part_condition");
                    String brand = rs.getString("brand");

                    HBox Rrow = new  HBox(10);
                    Label result = new Label(name + " | " + category + " | Price:£" + price + " | Condition: " + partCondition + " | brand: " + brand + " |");

                    String item = name + " | " + category + " | Price:£" + price + " | Condition: " + partCondition + " | brand: " + brand + " |";
                    Button addToBasket = new Button("Add To Basket");
                    addToBasket.setOnAction(e -> {
                        Basket.basketItems.add(item);
                    });

                    Rrow.getChildren().addAll(result,addToBasket);
                    resultsBox.getChildren().add(Rrow);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Database error");
            }
        }
    }
}
