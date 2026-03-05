import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javafx.scene.control.*;


public class SignUpLogic {

    private final String url = "jdbc:mysql://165.227.235.122/ceb96_CI536Login";
    private final String dbUser = "ceb96_CI536Login";
    private final String dbPassword = "4V9o&G$?!ro)chO%H[";

    public String registerUser(String username, String password, String confirmPassword) {

        if(username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            return "Please fill all fields!";
        }

        if(!password.equals(confirmPassword)) {
            return "Passwords do not match!";
        }

        String hashedPassword = hashPassword(password);

        try (Connection con = DriverManager.getConnection(url, dbUser, dbPassword)) {

            String checkQuery = "SELECT * FROM login WHERE username=?";
            PreparedStatement psCheck = con.prepareStatement(checkQuery);
            psCheck.setString(1, username);

            ResultSet rs = psCheck.executeQuery();
            if(rs.next()) {
                return "Username already taken!";
            }


            String insertQuery = "INSERT INTO login (username, password) VALUES (?, ?)";
            PreparedStatement psInsert = con.prepareStatement(insertQuery);
            psInsert.setString(1, username);
            psInsert.setString(2, hashedPassword);
            psInsert.executeUpdate();

            return "Sign Up successful!";

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error!";
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for(byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}