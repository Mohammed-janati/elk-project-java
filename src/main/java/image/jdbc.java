package image;
import java.sql.*;
public class jdbc {
	

	

	    // Function to establish database connection
	    public static Connection getConnection() {
	        String url = "jdbc:mysql://localhost:3306/keyword";
	        String user = "root";
	        String password = "";

	        try {
	            Connection connection = DriverManager.getConnection(url, user, password);
	            return connection;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	    // Function to check if the word exists and either increment count or insert a new entry
	    public static void checkAndInsertOrUpdate(String word, String category) {
	        String selectQuery = "SELECT count FROM keyword WHERE word = ? AND category = ?";
	        String updateQuery = "UPDATE keyword SET count = count + 1 WHERE word = ? AND category = ?";
	        String insertQuery = "INSERT INTO keyword (word, category, count) VALUES (?, ?, 1)";

	        try (Connection connection = getConnection();
	             PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
	             PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
	             PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {

	            // Check if the word exists
	            selectStmt.setString(1, word);
	            selectStmt.setString(2, category);
	            ResultSet resultSet = selectStmt.executeQuery();

	            if (resultSet.next()) {
	                // Word exists, increment count
	                updateStmt.setString(1, word);
	                updateStmt.setString(2, category);
	                updateStmt.executeUpdate();
	            } else {
	                // Word doesn't exist, insert new entry
	                insertStmt.setString(1, word);
	                insertStmt.setString(2, category);
	                insertStmt.executeUpdate();
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	  

}
