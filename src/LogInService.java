import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class LogInService {
    private Connection con;
    private SignUpLogic signUpLogic;
    private LogInUI logInUI;

    public LogInService(String dbUrl, String dbUser, String dbPass) throws SQLException {
        con = DriverManager.getConnection(dbUrl, dbUser, dbPass);
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

        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean login(String username, String Password) throws SQLException {
        String query = "SELECT * FROM login WHERE username = ? AND password = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, hashPassword(Password));
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public void close() throws SQLException {
        if (con != null && !con.isClosed()) {
            con.close();
        }
    }
}
