import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.sql.*;

public class SearchLogic {
    private final String url = "jdbc:mysql://165.227.235.122/ceb96_CI536Database";
    private final String dbUser = "ceb96_CI536Login";
    private final String dbPassword = "4V9o&G$?!ro)chO%H[";
    private Connection con;

    public void SearchStatement (String query, VBox resultsBox) throws SQLException {
        System.out.println("Searched for " + query);
        String CSearch = "SELECT * FROM cars WHERE model LIKE ? OR make LIKE ? OR CONCAT(make, ' ',model) LIKE ?";
        try (Connection con = DriverManager.getConnection(url, dbUser, dbPassword)){
            PreparedStatement psSearch = con.prepareStatement(CSearch);
            String Squery = "%" + query + "%";
            psSearch.setString(1, Squery);
            psSearch.setString(2, Squery);
            psSearch.setString(3, Squery);
            ResultSet rs = psSearch.executeQuery();
            while(rs.next()) {
                String model = rs.getString("model");
                String make = rs.getString("make");
                int year = rs.getInt("year");
                int miles = rs.getInt("miles");
                double price = rs.getDouble("price");

                Label result = new Label(make + " " + model + " Year: " + year + " Miles: " + miles + " Price:£" + price);
                resultsBox.getChildren().add(result);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println("Database error");
        }

    }
}
