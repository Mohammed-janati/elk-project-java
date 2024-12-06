package image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.client.RestHighLevelClient;

import elk.connection;
import elk.indexfct;
import image.KeywordAnalyticsController.Keyword;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;



import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

public class inputs {

    @FXML
    private  TextField input,title,tags,user,passw,Ttags,Tprice,Tid;
    @FXML
    private TextField Ttitle;
    @FXML
    private  TextArea description,Tdescription;
    @FXML
    private  Label i1,error,chose,priceLabel;
 
    @FXML
    private  ScrollPane sc;
    @FXML
    private  FlowPane pane;
    @FXML
    private  VBox list,keywordsVBox;
    @FXML
    private  Button search,filen,edit;
    @FXML
    private  ComboBox<String> category;

    private  ArrayList<Map<String, Object>> result = new ArrayList<>();
    int b=0;
    String a;
    
    connection con = new connection();
    RestHighLevelClient client = con.CONNECT();
    indexobj i=new indexobj();
    
    Label titleLabel = null;
    Label descriptionLabel = null;
   
    String id = null; 
    Button deleteButton = null;
    
    private Stage stage;
	 private Scene scene;
	 private Parent root;
    
    
    
    public void initialize() {
        // Set a fixed height for the ScrollPane’s viewport
        sc.setPrefViewportHeight(600); // Adjust as desired for visible viewport height
        sc.setFitToWidth(true); // Ensure the FlowPane width fits the ScrollPane
        sc.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scrollbar appears only when needed
        sc.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Disable horizontal scrollbar
        category.getItems().addAll("image", "livre", "service");
    }

    public void auth(ActionEvent e) throws IOException {
    	if(user.getText()!=null && !user.getText().isEmpty() && 
    		passw.getText()!=null && !passw.getText().isEmpty() ) {
    		
    boolean b=indexfct.auth(user.getText(), passw.getText());
    if(b) {
    	this.switchToScene2(e);
    }else {
    	error.setVisible(true);
    }
    	
    	}
    	
    	
    }
   
	 
 	 public void switchToScene1(ActionEvent event) throws IOException {
	  root = FXMLLoader.load(getClass().getResource("/view.fxml"));
	  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	  scene = new Scene(root);
	  stage.setScene(scene);
	  stage.setTitle("welcome to search engine"); 
	  stage.show();
	 }
 	 public void switchToScene5(ActionEvent event) throws IOException {
 		 root = FXMLLoader.load(getClass().getResource("/view5.fxml"));
 		 stage = (Stage)((Node)event.getSource()).getScene().getWindow();
 		 scene = new Scene(root);
 		 stage.setScene(scene);
 		 stage.setTitle("welcome to search engine"); 
 		 stage.show();
 	 }
	 
	 public void switchToScene2(ActionEvent event) throws IOException {
		 category.getItems().addAll("livre", "service");
	  Parent root = FXMLLoader.load(getClass().getResource("/view2.fxml"));
	  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	  scene = new Scene(root);
	  stage.setScene(scene);
	  stage.setTitle("welcome admin"); 
	  stage.show();
	 }
	 
	 public void switchToScene3(ActionEvent event) throws IOException {
		 
		 Parent root = FXMLLoader.load(getClass().getResource("/view3.fxml"));
		 stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		 scene = new Scene(root);
		 stage.setScene(scene);
		  stage.setTitle("add a record"); 
		 stage.show();
	 }
	 
	 public void switchToScene4(ActionEvent event) throws IOException {
		 
		 Parent root = FXMLLoader.load(getClass().getResource("/view4.fxml"));
		 stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		 scene = new Scene(root);
		 stage.setScene(scene);
		 stage.setTitle("authentication"); 
		 stage.show();
	 }
	/* public void switchToScene6(ActionEvent event) throws IOException {
		    try {
		        // Load the new FXML file for the scene
		        Parent root = FXMLLoader.load(getClass().getResource("/view6.fxml"));
		        
		        // Get the current stage and set a new scene
		        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		        scene = new Scene(root);
		        stage.setScene(scene);
		        stage.setTitle("authentication");
		        
		        // Retrieve the document by ID
		        Map<String, Object> document = indexfct.getDocumentById(id);
		        if (document != null) {
		            // Safely update UI components with document data
		            if (document.containsKey("tags") && document.get("tags") instanceof String[]) {
		                Ttags.setText(String.join(",", (String[]) document.get("tags")));  // If tags are stored as an array
		            }
		            if (document.containsKey("title") && document.get("title") instanceof String[]) {
		            	Ttitle.setText(String.join(",", (String[]) document.get("title")));  // If tags are stored as an array
		            }
		            if (document.containsKey("description")) {
		                Tdescription.setText((String) document.get("description"));
		            }
		            if (document.containsKey("category")) {
		                category.setValue((String) document.get("category"));
		            }
		            if (document.containsKey("price")) {
		                Tprice.setText(String.valueOf(document.get("price")));
		            }
		            Tid.setText(id);
		        } else {
		            System.err.println("Document not found for ID: " + id);
		        }
		        
		        // Show the new scene
		        stage.show();
		    } catch (IOException e) {
		        System.err.println("Error loading FXML or setting scene: " + e.getMessage());
		        e.printStackTrace();
		    }
		}*/

    
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
    		
    		jdbc.getConnection();
    		jdbc.checkAndInsertOrUpdate(a,selected);
    		
    		
    	}
    }
    
    
    
    
    
    public  void getimage(ActionEvent e) {
        
        list.setVisible(false);
        pane.setVisible(true);
        pane.getChildren().clear();
       

        try {
            if (!a.isEmpty()) {
                result = indexfct.searchimage( a);

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
    
    
    
    
    public  void gettextadmin(ActionEvent e) throws IOException {
    	a = input.getText();
    	String selected=category.getValue();
    	if(selected==null || selected.isEmpty()) {
    		
    		i1.setVisible(true);
    		i1.setText("please select a category");}
    	else {
    	pane.setVisible(false);
    	list.setVisible(true);
    	list.getChildren().clear();
    	
    	if(!a.isEmpty()) {
    		result=indexfct.searchtxt(selected,a);
    		 if (result.isEmpty()) {
                 i1.setVisible(true);
                 i1.setText("no item found");
    		 }else {
    			
    		
				    for (Map<String, Object> person : result) {
				        // Create a container for each item (e.g., a VBox for title, description, and delete button)
				        VBox textContainer = new VBox();
				        textContainer.setSpacing(2); // Add spacing between title and description
				        textContainer.setStyle("-fx-padding: 5;");

				        // Reset variables for each iteration
				       
				      

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

				            if (key.equals("id")) {
				                id = value.toString(); // Save the ID for the delete button
				            }
				            
				            if (key.equals("price")) {
	                            priceLabel = new Label("Price: $" + value);
	                            priceLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #007B00;"); // Green text for price
	                        }
				        }

				        // Create the delete button if the ID is available
				        if (id != null) {
				            deleteButton = new Button("Delete");
				            deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 5; -fx-font-size: 12px; -fx-border-radius: 3; -fx-background-radius: 3;");
				            deleteButton.setUserData(id); // Store the ID in the button's userData property
				            deleteButton.setOnAction(event -> {
				                String documentId = (String) deleteButton.getUserData();
				                System.out.println("Delete button clicked for ID: " + documentId);
				                // Add your delete logic here
				                try {
									indexfct.delete(id);
									
									this.switchToScene2(e);
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
				            });
				           /* edit = new Button("edit");
				            edit.setStyle("-fx-background-color: blue; -fx-text-fill: white; -fx-padding: 5; -fx-font-size: 12px; -fx-border-radius: 3; -fx-background-radius: 3;");
				            edit.setUserData(id); // Store the ID in the button's userData property
				           
				            edit.setOnAction(event -> {
				                String documentId = (String) edit.getUserData();
				                System.out.println("edit button clicked for ID: " + documentId);
				                // Add your delete logic here
				                try {
									
									
									//this.switchToScene6(e);
				                	
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
				            });
				            */
				            
				            
				        }
				        

				        // Add the title and description to the text container
				        if (titleLabel != null) {
				            textContainer.getChildren().add(titleLabel);
				        }
				        if (descriptionLabel != null) {
				            textContainer.getChildren().add(descriptionLabel);
				        }
				        if (priceLabel != null) {
	                        textContainer.getChildren().add(priceLabel);
	                    }
				        

				        // Combine the text container and delete button in an HBox
				        HBox itemContainer = new HBox();
				        itemContainer.setSpacing(10); // Add spacing between text and button
				        itemContainer.setStyle("-fx-padding: 5; -fx-border-color: #ccc; -fx-border-width: 1; -fx-background-color: #f9f9f9; -fx-border-radius: 5; -fx-background-radius: 5;");
				        itemContainer.getChildren().add(textContainer);

				        if (deleteButton != null) {
				            // Add spacing and alignment for the delete button
				            HBox.setHgrow(textContainer, Priority.ALWAYS); // Allow the text to take up remaining space
				            deleteButton.setAlignment(Pos.CENTER_RIGHT);
				            itemContainer.getChildren().add(deleteButton);
				        }
				     /*   if (edit != null) {
				        	// Add spacing and alignment for the delete button
				        	HBox.setHgrow(textContainer, Priority.ALWAYS); // Allow the text to take up remaining space
				        	edit.setAlignment(Pos.CENTER_RIGHT);
				        	itemContainer.getChildren().add(edit);
				        }*/

				        // Add itemContainer to the main list container
				        list.getChildren().add(itemContainer);
				    }



    		 }//end checking result existance
    		 
    		 
    	}
    	
    	
    	
    	}
    }
    Path targetPath;
    File selectedc;
   
    public void imagechoser(ActionEvent e) throws IOException {
    	FileChooser fc=new FileChooser();
    	 fc.getExtensionFilters().add(
    	            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
    	        );
    	 
    	selectedc=fc.showOpenDialog(null);
    	
    	if(selectedc!=null) {
    		String projectDir = System.getProperty("user.dir");
    		
            Path targetFolder = Path.of(projectDir, "src", "main", "resources", "images");
            
            // Create the target folder if it doesn't exist
            if (!Files.exists(targetFolder)) {
                Files.createDirectories(targetFolder);
            }
            
            // Define the target file path
             targetPath = targetFolder.resolve(selectedc.getName());
            i.url= targetPath.toString();
            
            b=1;
            // Copy the selected file to the target folder
      
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
    	
        	
    	
        	if(selected!="image" ) {
        		indexfct.insert(i);
        		
        	}
        	
        	else {
        		if(selectedc!=null) {
        	System.out.println(selectedc.toPath());
        	System.out.println(targetPath);
        	Files.copy(selectedc.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);  
        	indexfct.insert(i);
        	
        	this.switchToScene2(e);
        	}else {
        		chose.setVisible(true);
        	}
        		}
        	
        	
        	
    	}
    	
    	
    	
    	
    	
    	
    	
    }
    @FXML
    CheckBox filtre;
    
    
    public void gettext(ActionEvent e, String cat) throws IOException {

        pane.setVisible(false);
        list.setVisible(true);

        if (!a.isEmpty()) {
            result = indexfct.searchtxt(cat, a);
            if (filtre.isSelected()) {
             
            result.sort((map1, map2) -> {
                // Get the price values and handle potential null values or type mismatches
                Object price1 = map1.get("price");
                Object price2 = map2.get("price");

                // Convert price to Double for comparison
                Double value1 = price1 instanceof Number ? ((Number) price1).doubleValue() : 0.0;
                Double value2 = price2 instanceof Number ? ((Number) price2).doubleValue() : 0.0;

                return value2.compareTo(value1);
            });
            
            } 
            if (result.isEmpty()) {
                i1.setVisible(true);
                i1.setText("no item found");
            } else {
                for (Map<String, Object> person : result) {
                    // Create a container for each item (e.g., a VBox for title, description, and price)
                    VBox itemContainer = new VBox();
                    itemContainer.setSpacing(5); // Add spacing between title, description, and price
                    itemContainer.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-width: 1; -fx-background-color: #f9f9f9; -fx-border-radius: 5; -fx-background-radius: 5;");

                    Label titleLabel = null;
                    Label descriptionLabel = null;
                    Label priceLabel = null;

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

                        if (key.equals("price")) {
                            priceLabel = new Label("Price: $" + value);
                            priceLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #007B00;"); // Green color for price
                        }
                    }

                    // Add the title, description, and price to the item container
                    if (titleLabel != null) {
                        itemContainer.getChildren().add(titleLabel);
                    }
                    if (descriptionLabel != null) {
                        itemContainer.getChildren().add(descriptionLabel);
                    }
                    if (priceLabel != null) {
                        itemContainer.getChildren().add(priceLabel);
                    }

                    // Add itemContainer to the main list container
                    list.getChildren().add(itemContainer);
                }
            }
        }
    }

 
 
 public void displayanal(ActionEvent e) throws IOException {
	    String selected = category.getValue();
	    if (selected == null || selected.isEmpty()) {
	        i1.setVisible(true);
	        i1.setText("Please select a category");
	    } else {
	        List<KeywordAnalyticsController.Keyword> topKeywords = KeywordAnalyticsController.getTopTrendingKeywords(selected);

	        // Clear the VBox
	        keywordsVBox.getChildren().clear();

	        // Create a Bar Chart
	        CategoryAxis xAxis = new CategoryAxis();
	        xAxis.setLabel("Keyword");

	        NumberAxis yAxis = new NumberAxis();
	        yAxis.setLabel("Count");

	        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
	        barChart.setTitle("Top Trending Keywords for " + selected);

	        // Create a data series
	        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
	        dataSeries.setName("Keyword Counts");

	        // Populate the data series with keyword data
	        for (KeywordAnalyticsController.Keyword keyword : topKeywords) {
	            dataSeries.getData().add(new XYChart.Data<>(keyword.getWord(), keyword.getCount()));
	        }

	        // Add the data series to the chart
	        barChart.getData().add(dataSeries);

	        // Add the Bar Chart to the VBox
	        keywordsVBox.getChildren().add(barChart);
	    }
	}
 
 
 
 
 
 
 
 
 
 
 
 public void importData(ActionEvent event) throws IOException {
     // Open file chooser
     FileChooser fileChooser = new FileChooser();
     fileChooser.setTitle("Open Excel File");
     fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls"));
     File file = fileChooser.showOpenDialog(null);

     if (file != null) {
         // Process the selected Excel file
         importExcelToElasticsearch(file);
         this.switchToScene2(event);
     }
 }
 
 
 private void importExcelToElasticsearch(File file) {
     try (FileInputStream fis = new FileInputStream(file);
          Workbook workbook = new XSSFWorkbook(fis)) {

         Sheet sheet =  workbook.getSheetAt(0); // Read the first sheet
         BulkRequest bulkRequest = new BulkRequest();

         for (Row row : sheet) {
             // Skip header row
             if (row.getRowNum() == 0) {
            	 System.out.println("row null");
            	 continue;
             }

             // Read columns from the row
             String title = row.getCell(0).getStringCellValue();
             String tags = row.getCell(1).getStringCellValue();
             String category = row.getCell(2).getStringCellValue();
             String description = row.getCell(3).getStringCellValue();
             double price = row.getCell(4).getNumericCellValue(); // Assuming price is in column 5

             indexobj b=new indexobj();
             b.setCat(category);
             b.setTags(tags);
             b.setTitle(title);
            b.description=description;
             b.price=(float) price;
             
           indexfct.insert(b);
         }
         
         System.out.println("done");
     } catch (IOException e) {
         e.printStackTrace();
     }
 }
 
 
 
 
 
 
 
 public void update(ActionEvent e) throws IOException {
 	
 	
 	//check category entred
 	String selected=category.getValue();
 	if(selected==null || selected.isEmpty()) {
 		
 		i1.setVisible(true);
 		i1.setText("please select a category");
 	}else {
 		
 		i.setCat(selected);
 		i.description=Tdescription.getText();
     	i.tags=Ttags.getText();
     	i.title=Ttitle.getText();
     	i.price=Integer.parseInt(Tprice.getText());
     	i.cat=selected;
     	String idq=Tid.getText();
     	indexfct.update(idq, i);
 	
     	this.switchToScene2(e);
 	
     	
     	
     	
     	
 	}
 	
 	
 }
 

 
 
 
 
 
 
 
 

}


//file:///C:/Users/hp/eclipse-workspace/image/src/main/resources/images/image-pomme-dm16119.png
