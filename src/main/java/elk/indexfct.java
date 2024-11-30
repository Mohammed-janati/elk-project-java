package elk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import image.indexobj;

public class indexfct {
	private static RestHighLevelClient client;
	static SearchRequest searchRequest;
	 static SearchSourceBuilder searchSourceBuilder ;
	 
	 
	 
	 
	static {
		client=connection.CONNECT();
		 searchRequest = new SearchRequest();
		 searchSourceBuilder= new SearchSourceBuilder();
		 searchRequest.indices("images_rsh");
	}
	

	public static ArrayList<Map<String, Object>> searchtxt(String category,String w) throws IOException {
		
	    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
	    
	    boolQuery.must(QueryBuilders.matchQuery("title",w));
	    boolQuery.must(QueryBuilders.termQuery("type", "text"));
	    boolQuery.must(QueryBuilders.termQuery("category", category));
	   
	    boolQuery.should(QueryBuilders.multiMatchQuery(w,"title","description","tags"));
	    
	    searchSourceBuilder.query(boolQuery);
	    searchRequest.source(searchSourceBuilder);

	    
	    Map<String, Object> map=null;
		 ArrayList<Map<String, Object>> array=new ArrayList<Map<String, Object>>();
		  
		 try {
		      
		      SearchResponse searchResponse =client.search(searchRequest, RequestOptions.DEFAULT);
		     if (searchResponse.getHits().getTotalHits().value > 0) {
		         SearchHit[] searchHit = searchResponse.getHits().getHits();
		         for (SearchHit hit : searchHit) {
		             map = hit.getSourceAsMap();  
		             array.add(map);
		         }
		         return array;
		     }
		 } catch (IOException e) {
		     e.printStackTrace();
		 }
		 
		 return array;
	}
	
	
	
	
	public static ArrayList<Map<String, Object>>  searchimage(String mustKeyword, String shouldKeyword) {
		
		 
		 
		 
		 BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
	        
	        // Add a must clause (required)
		 boolQuery.must(QueryBuilders.termQuery("type", "image"));
	        if (mustKeyword != null && !mustKeyword.isEmpty()) {
	            boolQuery.must(QueryBuilders.multiMatchQuery(mustKeyword, "title", "tags", "description"));
	        }
	        
	        // Add a should clause (optional)
	        if (shouldKeyword != null && !shouldKeyword.isEmpty()) {
	            boolQuery.should(QueryBuilders.multiMatchQuery(shouldKeyword,"tags"));
	        }

	        searchSourceBuilder.query(boolQuery);
	        searchSourceBuilder.fetchSource(new String[] {"url"}, null); 
	        searchRequest.source(searchSourceBuilder);
		 
		 
		 
		 
		 
		 
		 Map<String, Object> map=null;
		 ArrayList<Map<String, Object>> array=new ArrayList<Map<String, Object>>();
		  
		 try {
		     SearchResponse searchResponse = null;
		     searchResponse =client.search(searchRequest, RequestOptions.DEFAULT);
		     if (searchResponse.getHits().getTotalHits().value > 0) {
		         SearchHit[] searchHit = searchResponse.getHits().getHits();
		         for (SearchHit hit : searchHit) {
		             map = hit.getSourceAsMap();  
		             array.add(map);
		            
		         }
		         return array;
		     }
		 } catch (IOException e) {
		     e.printStackTrace();
		 }
		return array;
	}


	public static void insert(indexobj i) {
		try {
		IndexRequest request = new IndexRequest("images_rsh"); // Specify the index name
		 Map<String, Object> document = new HashMap<>();
		 
		 
		 
		 
		 
	     // Create a document as a map or JSON string
	    
	     document.put("title",i.title);
	     document.put("tags", i.tags);
	     document.put("type", i.type);
	     document.put("category", i.cat);
	     document.put("url", i.url);
	     document.put("description", i.description);
	     if(i.cat=="image") {
	    	 document.put("type", "image");
	     }else {
	    	 document.put("type", "text");
	     }
	     

	     // Convert the map to JSON and set the request's source
	     request.source(document, XContentType.JSON);

	     // Execute the insert request
	     IndexResponse response = client.index(request, RequestOptions.DEFAULT);

	     System.out.println("donne");
	     

	 } catch (IOException e) {
	     e.printStackTrace();
	 }
		}


}
