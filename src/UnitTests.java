import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

class UnitTests {
	private final static String BASE_URL = "http://localhost:4567/";
	private final static String COMMAND = "java -jar ./runTodoManagerRestAPI-1.5.5.jar";
	private final static int OK_CODE = 200;
	private final static int NOT_FOUND = 404;
	
	// parameters to use
	private final static String TITLE_S = "title";
	private final static String DONESTATUS_S = "doneStatus";
	private final static String DESCRIPTION_S = "description";
	private final static String TITLE1 = "TestTODO1";
	private final static String TITLE2 = "TestTODO2";
	private final static String STATUS1 = "true";
	private final static String STATUS2 = "false";
	private final static String DESCRIPTION = "none";
	private final static String PARAM1 = ("{" 
			+ TITLE_S + ": " + TITLE1 + "," 
			+ DONESTATUS_S + ": " + STATUS1 + ","
			+ DESCRIPTION_S + ": " + DESCRIPTION
			+ "}");
	private final static String PARAM2 = ("{" 
			+ TITLE_S + ": " + TITLE2 + "," 
			+ DONESTATUS_S + ": " + STATUS2 + ","
			+ DESCRIPTION_S + ": " + DESCRIPTION
			+ "}");
	

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Runtime rt = Runtime.getRuntime();
		rt.exec(COMMAND);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		shutDown();
		Runtime rt = Runtime.getRuntime();
		rt.exec(COMMAND);
	}

	@BeforeEach
	void setUp() throws Exception {
		if(testConnection()) {
			System.out.println("Set-up\tSystem is ready");
		} else {
			System.out.println("Set-up\tError connecting to localhost:4567");
			Assert.fail();
		}
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	// =====================/todos====================
	@Test
	void testGetInvalidTodos() throws JSONException {
		JSONObject obj = sendRequest("POST", BASE_URL, "todos", PARAM1);
		String id = (String) obj.get("id");
		
		// Delete
		sendRequest("DELETE", BASE_URL, "todos/"+id, "");
		
		//Get with invalid filter
		String filer = "id=" + id;
		obj = sendRequest("GET", BASE_URL, "todos?" + filer, "");
		JSONArray todos = obj.getJSONArray("todos");
		
		assertEquals(0, todos.length());
		
		System.out.println("\n");
	}
	
	@Test
	void testPostNewTodos() throws JSONException {
		JSONObject obj = sendRequest("POST", BASE_URL, "todos", PARAM1);
		
		assertEquals(STATUS1, obj.get(DONESTATUS_S));
		assertEquals(TITLE1, obj.get(TITLE_S));
		String id = (String) obj.get("id");
		
		// Check there is at least an element in todos
		obj = sendRequest("GET", BASE_URL, "todos", "");
		JSONArray todos = obj.getJSONArray("todos");
		if(todos.length() < 1){
			Assert.fail();
		}
		
		// Delete newly added todo
		sendRequest("DELETE", BASE_URL, "todos/"+id, "");
		
		System.out.println("\n");
	}
	
	// =====================/todos/:id====================
	@Test
	void testAmendWithPut() throws JSONException {
		JSONObject obj = sendRequest("POST", BASE_URL, "todos", PARAM1);
		String id = (String) obj.get("id");
		
		// Change title and status and assert info
		obj = sendRequest("PUT", BASE_URL, "todos/"+id, PARAM2);
		assertEquals(TITLE2, obj.get(TITLE_S));
		assertEquals(STATUS2, obj.get(DONESTATUS_S));
		assertEquals(DESCRIPTION, obj.get(DESCRIPTION_S));
		
		
		// Delete newly added todo
		sendRequest("DELETE", BASE_URL, "todos/"+id, "");
		
		System.out.println("\n");
	}
	
	@Test
	void testDoubleDelete() throws JSONException {
		JSONObject obj = sendRequest("POST", BASE_URL, "todos", PARAM1);
		
		String id = (String) obj.get("id");

		// Delete twice should give error
		getResponseCode("DELETE", BASE_URL, "todos/"+id, "");		
		int error2 = getResponseCode("DELETE", BASE_URL, "todos/"+id, "");
		assertEquals(NOT_FOUND, error2);
		
		System.out.println("\n");
	}
	
	// =====================helper methods====================
	private static void shutDown() {
		try {
			URL url = new URL("http://localhost:4567/shutdown");
			
			System.out.println("Sending: GET " + url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            connection.setDoInput(true);
            connection.setDoOutput(true);
            
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.getResponseCode();
			
		} catch (IOException e) {
        }
	}
	
	private static boolean testConnection() {
		try { 
		    URL url = new URL(BASE_URL);
		    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		    connection.connect();
		    connection.disconnect();
		    return true;
		} 
		catch (IOException e) {   
		    return false;
		}
	}
	
	private static int getResponseCode(String requestType, String baseUrl, String path, String body) {
        try {
            URL url = new URL(baseUrl + path);
            System.out.println("Sending: " + requestType + " " + url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            connection.setDoInput(true);
            connection.setDoOutput(true);
            
            connection.setRequestMethod(requestType);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            
            if(body != "") {
            	OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
                writer.write(body);
                writer.close();
            }
            
            connection.disconnect();
            return connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return 0;
    }
	
	private static JSONObject sendRequest(String requestType, String baseUrl, String path, String body) {
        try {
            URL url = new URL(baseUrl + path);
            System.out.println("Sending: " + requestType + " " + url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            connection.setDoInput(true);
            connection.setDoOutput(true);
            
            connection.setRequestMethod(requestType);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            
            if(body != "") {
            	OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
                writer.write(body);
                writer.close();
            }
            
            System.out.println("Response Code: "+ connection.getResponseCode() + " "
            		+ connection.getResponseMessage());
            
            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            String response = br.readLine();
            if (response != null) {
            	JSONObject r = new JSONObject(response);
                connection.disconnect();
                return r;
            }
        } catch (JSONException | IOException e) {
            //e.printStackTrace();
        }
        return null;
    }

}
