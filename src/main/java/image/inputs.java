package image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Map;

import org.elasticsearch.client.RestHighLevelClient;

import elk.connection;
import elk.indexfct;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class inputs {

    @FXML
    private  TextField input,title,tags;
    @FXML
    private  TextArea description;
    @FXML
    private  Label i1;
    @FXML
    private  ScrollPane sc;
    @FXML
    private  FlowPane pane;
    @FXML
    private  VBox list;
    @FXML
    private  Button search,file;
    @FXML
    private  ComboBox<String> category;

    private  ArrayList<Map<String, Object>> result = new ArrayList<>();
    int b=0;
    String a;
    connection con = new connection();
    RestHighLevelClient client = con.CONNECT();
    indexobj i=new indexobj();
    public void initialize() {
        // Set a fixed height for the ScrollPane’s viewport
        sc.setPrefViewportHeight(600); // Adjust as desired for visible viewport height
        sc.setFitToWidth(true); // Ensure the FlowPane width fits the ScrollPane
        sc.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scrollbar appears only when needed
        sc.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Disable horizontal scrollbar
        category.getItems().addAll("image", "livre", "service");
    }

    
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
	  Parent root = FXMLLoader.load(getClass().getResource("/view3.fxml"));
	  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	  scene = new Scene(root);
	  stage.setScene(scene);
	  stage.show();
	 }
    
    
    public void getcat(ActionEvent e) throws IOException {
    	a = input.getText();
    	String selected=category.getValue();
    	if(selected==null || selected.isEmpty()) {
    		
    		i1.setVisible(true);
    		i1.setText("please select a category");
    	}else {
    		if(selected=="image") {
    			pane.getChildren().clear(); 
    	        list.getChildren().clear(); 
    			this.getimage(e);
    			i1.setVisible(false);
    			
    		}else {
    			pane.setVisible(false);
    			list.setVisible(true);
    			i1.setVisible(false);
    	    	pane.getChildren().clear(); 
    	        list.getChildren().clear(); 
    			this.gettext(e, selected);
    		}
    		
    	}
    }
    
    
    
    
    
    public  void getimage(ActionEvent e) {
        
        list.setVisible(false);
        pane.setVisible(true);
        pane.getChildren().clear();
       

        try {
            if (!a.isEmpty()) {
                result = indexfct.searchimage( a, null);

                if (result.isEmpty()) {
                    i1.setVisible(true);
                    i1.setText("no item found");
                    pane.setVisible(false);
                } else {
                    i1.setVisible(false);
                    pane.setVisible(true);
                    pane.getChildren().clear(); // Clear previous search results
                    
                    
                        for (Map<String, Object> person : result) {
                           
                                person.forEach((key, value) -> {
                                    if ("url".equals(key) && value != null) {
                                        String imageUrl = value.toString();
                                        try {
                                        	imageUrl=imageUrl.replace("C:","file://");
                                        	
                                        	
                                        	
                                            Image image = new Image(imageUrl);
                                            ImageView imageView = new ImageView(image);
                                            
                                            imageView.setFitWidth(250);
                                            imageView.setFitHeight(250);
                                            imageView.setPreserveRatio(true);

                                            pane.getChildren().add(imageView); 
                                        	// Add image to FlowPane
                                        } catch (Exception imgEx) {
                                            System.out.println("Image load error: " + imgEx.getMessage());
                                        }
                                    }
                                });
                            
                        }
                    
                }
            } else {
                i1.setVisible(false);
            }
        } catch (Exception e1) {
            System.out.println(e1.getMessage());
        }
    }
    
    
    
    
    
    public  void gettext(ActionEvent e,String cat) throws IOException {
    	
    	pane.setVisible(false);
    	list.setVisible(true);
    	
    	if(!a.isEmpty()) {
    		result=indexfct.searchtxt(cat,a);
    		 if (result.isEmpty()) {
                 i1.setVisible(true);
                 i1.setText("no item found");
    		 }else {
    		
    		
    			 for (Map<String, Object> person : result) {
    				    // Create a container for each item (e.g., a VBox for title and description)
    				    VBox itemContainer = new VBox();
    				    itemContainer.setSpacing(2);// Add spacing between title and description
    				    itemContainer.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-width: 1; -fx-background-color: #f9f9f9; -fx-border-radius: 5; -fx-background-radius: 5;");

    				    Label titleLabel = null;
    				    Label descriptionLabel = null;

    				    for (Map.Entry<String, Object> entry : person.entrySet()) {
    				        String key = entry.getKey();
    				        Object value = entry.getValue();

    				        if (key.equals("title")) {
    				            titleLabel = new Label(value.toString());
    				            titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333;");
    				            titleLabel.setWrapText(true);
    				        }

    				        if (key.equals("description")) {
    				            descriptionLabel = new Label(value != null ? value.toString() : "No description available");
    				            descriptionLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
    				            descriptionLabel.setWrapText(true);
    				        }
    				    }

    				    // Add the title and description to the item container
    				    if (titleLabel != null) {
    				        itemContainer.getChildren().add(titleLabel);
    				    }
    				    if (descriptionLabel != null) {
    				        itemContainer.getChildren().add(descriptionLabel);
    				    }

    				    // Add itemContainer to the main list container
    				    list.getChildren().add(itemContainer);
    				}

    		 }//end checking result existance
    		 
    		 
    	}else {}
    	
    	
    	
    	
    }
    
    public void imagechoser(ActionEvent e) throws IOException {
    	FileChooser fc=new FileChooser();
    	 fc.getExtensionFilters().add(
    	            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
    	        );
    	 
    	File selected=fc.showOpenDialog(null);
    	
    	if(selected!=null) {
    		String projectDir = System.getProperty("user.dir");
    		
            Path targetFolder = Path.of(projectDir, "src", "main", "resources", "images");
            
            // Create the target folder if it doesn't exist
            if (!Files.exists(targetFolder)) {
                Files.createDirectories(targetFolder);
            }
            
            // Define the target file path
            Path targetPath = targetFolder.resolve(selected.getName());
            i.url= targetPath.toString();
            
            b=1;
            // Copy the selected file to the target folder
            Files.copy(selected.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);  
            }
    
    }
    public void submit(ActionEvent e) throws IOException {
    	
    	
    	//check category entred
    	String selected=category.getValue();
    	if(selected==null || selected.isEmpty()) {
    		
    		i1.setVisible(true);
    		i1.setText("please select a category");
    	}else {
    		
    		i.setCat(selected);
    		i.description=description.getText();
        	i.tags=tags.getText();
        	i.title=title.getText();
    	}
    	
    	indexfct.insert(i);
    	
    	this.switchToScene1(e);
    	
    	
    	
    	
    	
    	
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}





//file:///C:/Users/hp/eclipse-workspace/image/src/main/resources/images/image-pomme-dm16119.png
