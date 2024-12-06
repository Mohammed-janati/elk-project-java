package elk;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation.Bucket;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;




import image.indexobj;

public class indexfct {
	private static RestHighLevelClient client;
	static SearchRequest searchRequest;
	 static SearchSourceBuilder searchSourceBuilder ;
	 private static String index;
	 
	 
	 
	 
	static {
		client=connection.CONNECT();
		
		
	}
	
	public static boolean auth(String user,String passw) throws IOException {
		
		 searchRequest = new SearchRequest();
		
		
		searchRequest.indices("auth");
		 
		 SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		    	
	if (user != null && !user.isEmpty()) {
		    boolQuery.must(QueryBuilders.matchQuery("user",user));
	}
	if (passw != null && !passw.isEmpty()) {
		boolQuery.must(QueryBuilders.matchQuery("passw",passw));
	}
		   

	searchSourceBuilder.query(boolQuery);
	searchRequest.source(searchSourceBuilder);
	SearchResponse searchResponse =client.search(searchRequest, RequestOptions.DEFAULT);
	
	if (searchResponse.getHits().getTotalHits().value > 0) {
		System.out.println("found");
		return true;
	}else return false;
	
	}
	
	
	

	public static ArrayList<Map<String, Object>> searchtxt(String category,String w) throws IOException {
		 index="images_rsh";
		 searchRequest = new SearchRequest();
		
		 searchRequest.indices(index);
	    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
	    
	    boolQuery.must(QueryBuilders.multiMatchQuery(w,"title","tags"));
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
		             map.put("id", hit.getId());
		             System.out.println(map);
		             array.add(map);
		         }
		         return array;
		     }
		 } catch (IOException e) {
		     e.printStackTrace();
		 }
		 
		 return array;
	}
	
	
	
	
	public static ArrayList<Map<String, Object>>  searchimage(String mustKeyword) {
		
		 index="images_rsh";
		 searchRequest = new SearchRequest();
		 searchSourceBuilder= new SearchSourceBuilder();
		 searchRequest.indices(index);
		 
		 
		 BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
	        
	        // Add a must clause (required)
		 boolQuery.must(QueryBuilders.termQuery("type", "image"));
		 
	        if (mustKeyword != null && !mustKeyword.isEmpty()) {
	        	boolQuery.must(QueryBuilders.matchQuery("tags",mustKeyword));
	            boolQuery.should(QueryBuilders.multiMatchQuery(mustKeyword, "title", "tags"));
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
		            System.out.println(map);
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
	     String[] o=i.tags.split(",");
	     
	     document.put("tags",o);
	     document.put("type", i.type);
	     document.put("category", i.cat);
	     document.put("url", i.url);
	     document.put("description", i.description);
	     document.put("price", i.price);
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
	
	
	public static Map<String, Object> getDocumentById(String documentId) throws IOException {
	    SearchRequest searchRequest = new SearchRequest("images_rsh");  // Specify the index
	    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	    searchSourceBuilder.query(QueryBuilders.termQuery("_id", documentId));
	    searchRequest.source(searchSourceBuilder);

	    SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
	    Map<String, Object> document = null;

	    if (searchResponse.getHits().getTotalHits().value > 0) {
	        SearchHit hit = searchResponse.getHits().getAt(0);  // Retrieve the first (and only) result
	        document = hit.getSourceAsMap();
	    }

	    return document;
	}

	
	public static void delete(String id) throws IOException {
DeleteRequest deleteRequest = new DeleteRequest(index, id);
        
        // Execute the request
        DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
	}



	public static void gere_log(String i,String c) {
		 
	    try {
	        // Create a unique ID based on search_word and category
	        String docId = i + "_" + c; // Ensure this generates unique IDs for each combination

	        // Check if the document exists
	        GetRequest getRequest = new GetRequest("search_logs", docId);
	        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);

	        if (getResponse.isExists()) {
	            // Document exists, increment the count field
	            UpdateRequest updateRequest = new UpdateRequest("search_logs", docId);

	            // Use a script to increment the count field
	            String script = "ctx._source.count += 1; ctx._source.timestamp = params.timestamp";
	            Map<String, Object> params = new HashMap<>();
	            params.put("timestamp", Instant.now().toString());

	            updateRequest.script(new Script(ScriptType.INLINE, "painless", script, params));

	            client.update(updateRequest, RequestOptions.DEFAULT);
	            System.out.println("Document exists. Incremented count.");
	        } else {
	            // Document does not exist, insert it as a new document
	            IndexRequest indexRequest = new IndexRequest("search_logs")
	                    .id(docId); // Set the ID to ensure uniqueness
	            Map<String, Object> document = new HashMap<>();
	            document.put("search_word", i);
	            document.put("category", c);
	            document.put("count", 1); // Initialize count to 1
	            document.put("timestamp", Instant.now().toString());

	            indexRequest.source(document, XContentType.JSON);
	            client.index(indexRequest, RequestOptions.DEFAULT);

	            System.out.println("New document inserted.");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

		}

	
	
	
	public static void update(String documentId,indexobj i) throws IOException {
	    // Specify the index name
	    String index = "images_rsh";

	    // Create the UpdateRequest
	    UpdateRequest updateRequest = new UpdateRequest(index, documentId);

	    // Create a map for the fields you want to update
	    Map<String, Object> updatedFields = new HashMap<>();
	    updatedFields.put("title", i.title);
	    updatedFields.put("description", i.description);
	    updatedFields.put("tags", i.tags);  // Assuming tags is a string, adjust if it's an array
	    updatedFields.put("category", i.cat);
	    updatedFields.put("price", i.price);

	    // Set the source of the update (i.e., the fields that should be updated)
	    updateRequest.doc(updatedFields);

	    // Send the update request
	    try {
	        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
	        System.out.println("Document updated successfully. Status: " + updateResponse.status());
	    } catch (ElasticsearchException e) {
	        if (e.status() == RestStatus.NOT_FOUND) {
	            System.out.println("Document not found for ID: " + documentId);
	        } else {
	            e.printStackTrace();
	        }
	    }
	}
	
	
	 /*public static Map<String, Long> fetchTrendingKeywordsByCategory(String category) {
	        Map<String, Long> trendingKeywords = new HashMap<>();

	        try {
	            // Build the aggregation for the specific category
	            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
	                    .size(0) // We don't need document hits
	                    .aggregation(AggregationBuilders
	                            .filter("category_filter", QueryBuilders.termQuery("category.keyword", category)) // Filter by category
	                            .subAggregation(
	                                    AggregationBuilders
	                                            .terms("trending_keywords")
	                                            .field("search_word.keyword")
	                                            .size(5) // Get top 5 trending keywords
	                                           
	                            ));

	            // Build the search request
	            SearchRequest searchRequest = new SearchRequest("search_logs")
	                    .source(sourceBuilder);

	            // Execute the search
	            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

	            // Extract the "category_filter" aggregation
	            FilterAggregationBuilder categoryFilterAgg = response.getAggregations().get("category_filter");

	            // Get the "trending_keywords" aggregation from the category filter aggregation
	            Terms trendingKeywordsAgg = ((Bucket) categoryFilterAgg).getAggregations().get("trending_keywords");

	            // Process the terms aggregation buckets
	            for (Terms.Bucket bucket : trendingKeywordsAgg.getBuckets()) {
	                trendingKeywords.put(bucket.getKeyAsString(), bucket.getDocCount());
	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        return trendingKeywords;
	    }

	public static Map<String, Map<String, Long>> fetchTrendingKeywordsByCategory() {
	    Map<String, Map<String, Long>> trendingKeywordsByCategory = new HashMap<>();

	    try {
	        // Build the aggregation query with category filter
	        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
	                .size(0) // We don't need document hits
	                .aggregation(AggregationBuilders.terms("by_category")
	                        .field("category.keyword") // Assuming 'category' is a keyword field
	                        .subAggregation(AggregationBuilders.terms("trending_keywords")
	                                .field("search_word.keyword") // Trending keyword aggregation
	                                .size(10)) // Limit to top 10 trending keywords per category
	                );

	        // Build the search request
	        SearchRequest searchRequest = new SearchRequest("search_logs")
	                .source(sourceBuilder);

	        // Execute the search
	        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

	        // Parse the results and populate the map
	        Terms categories = response.getAggregations().get("by_category");
	        for (Terms.Bucket categoryBucket : categories.getBuckets()) {
	            String category = categoryBucket.getKeyAsString();
	            Terms trendingKeywords = categoryBucket.getAggregations().get("trending_keywords");

	            System.out.println("Category: " + category);

	            Map<String, Long> categoryKeywords = new HashMap<>();
	            for (Terms.Bucket keywordBucket : trendingKeywords.getBuckets()) {
	                String keyword = keywordBucket.getKeyAsString();
	                long count = keywordBucket.getDocCount();

	                // Print each keyword and its count to console
	                System.out.println("    Keyword: " + keyword + ", Count: " + count);

	                categoryKeywords.put(keyword, count);
	            }

	            // Add the category and its trending keywords to the result map
	            trendingKeywordsByCategory.put(category, categoryKeywords);
	            System.out.println("Category Keywords Map: " + categoryKeywords);
	            System.out.println(); // Print a blank line for readability between categories
	        }

	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return trendingKeywordsByCategory;
	}
*/









}
