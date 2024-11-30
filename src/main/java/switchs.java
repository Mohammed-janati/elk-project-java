import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import image.inputs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class switchs {
	  @FXML
	    private TextField input;
	    @FXML
	    private Label i1;
	    
	    @FXML
	    private FlowPane pane;
	    @FXML
	    private VBox list;
	    @FXML
	    private Button search;
	    @FXML
	    private ComboBox<String> category;

	    private ArrayList<Map<String, Object>> result = new ArrayList<>();
	    
	    String a;
    private Stage stage;
	 private Scene scene;
	 private Parent root;
	 
	 public void switchToScene1(ActionEvent event) throws IOException {
	  root = FXMLLoader.load(getClass().getResource("/view.fxml"));
	  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	  scene = new Scene(root);
	  stage.setScene(scene);
	  stage.show();
	 }
	 
	 public void switchToScene2(ActionEvent event) throws IOException {
	  Parent root = FXMLLoader.load(getClass().getResource("/view2.fxml"));
	  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	  scene = new Scene(root);
	  stage.setScene(scene);
	  stage.show();
	 
}
	 
	 public void getcat(ActionEvent e) throws IOException {
		 inputs i= new inputs();
	    	a = input.getText();
	    	String selected=category.getValue();
	    	if(selected==null || selected.isEmpty()) {
	    		
	    		i1.setVisible(true);
	    		i1.setText("please select a category");
	    	}else {
	    		if(selected=="image") {
	    			pane.getChildren().clear(); 
	    	        list.getChildren().clear(); 
	    			i.getimage(e);
	    			
	    		}else {
	    			pane.setVisible(false);
	    			list.setVisible(true);

	    	    	pane.getChildren().clear(); 
	    	        list.getChildren().clear(); 
	    	        i.gettext(e, selected);
	    		}
	    		
	    	}
	    }
	 
	 
	 public void initialize() {
	    
	      
	        category.getItems().addAll("image", "livre", "service");
	    }
}


	