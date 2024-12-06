package image;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import image.KeywordAnalyticsController.Keyword;


public class KeywordAnalyticsController {

    // Function to populate the view with top 5 trending keywords per category
	

    // Function to query the top 5 trending keywords for each category
    public static List<Keyword> getTopTrendingKeywords(String category) {
        List<Keyword> keywords = new ArrayList<>();
        String query = "SELECT word, category, count FROM keyword WHERE category=? ORDER BY count DESC LIMIT 5";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/keyword", "root", "");
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the category parameter
            statement.setString(1, category);

            // Execute the query
            try (ResultSet resultSet = statement.executeQuery()) {

                // Loop through the result set and create Keyword objects
                while (resultSet.next()) {
                    String word = resultSet.getString("word");
                    String cat = resultSet.getString("category");
                    int count = resultSet.getInt("count");
                   
                    keywords.add(new Keyword(word, cat, count));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return keywords;
    }


// Keyword class to store information about each keyword
public static class Keyword {
    private String word;
    private String category;
    private int count;

    public Keyword(String word, String category, int count) {
        this.word = word;
        this.category = category;
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public String getCategory() {
        return category;
    }

    public int getCount() {
        return count;
    }
}

}
