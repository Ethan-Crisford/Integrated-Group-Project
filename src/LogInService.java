import java.sql.*;

public class LogInService {
    private Connection con;

    public LogInService(String dbUrl, String dbUser, String dbPass) throws SQLException {
        con = DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }

    public boolean login(String username, String password) throws SQLException {
        String query = "SELECT * FROM login WHERE username = ? AND password = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
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
