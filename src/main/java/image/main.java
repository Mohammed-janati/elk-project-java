package image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class main extends Application{

	

	    public void start(Stage primaryStage) {
	        try {
	            // Load the FXML file
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view.fxml")); // Replace with your FXML file name
	            Parent root = loader.load();
	            
	          
	         
	            
	            
	            // Create a scene with the loaded FXML
	            Scene scene = new Scene(root,900,400);
	            primaryStage.setTitle("JavaFX Scene Builder Example");
	            primaryStage.setScene(scene);
	            primaryStage.show();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	   
	    
	public static void main(String[] args) {
		
		  launch(args);
		
		
		
		
		
	/*	
		/*new insert(client);*/
		
		
		
		
	/*	*/
	}

}
