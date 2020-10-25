import static org.junit.jupiter.api.Assertions.*;

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
	private final String BASE_URL = "http://localhost:4567/";
	private final String TITLE_S = "title";
	private final String DONESTATUS_S = "doneStatus";
	private final String DESCRIPTION_S = "description";
	

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Runtime rt = Runtime.getRuntime();
		rt.exec("java -jar ./runTodoManagerRestAPI-1.5.5.jar");
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		shutDown();
		Runtime rt = Runtime.getRuntime();
		rt.exec("java -jar ./runTodoManagerRestAPI-1.5.5.jar");
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGetTodos() throws JSONException {
		JSONObject obj = sendRequest("GET", BASE_URL, "todos", "");

		JSONArray todos = obj.getJSONArray("todos");
		// By default, there are 2 elements in the todo list
		assertEquals(2, todos.length());
		
		System.out.println("\n");
	}
	
	@Test
	void testPostNewTodos() throws JSONException {
		String title = "TestCreateTODO";
		String param = ("{" + TITLE_S + ": " + title + "}");
		JSONObject obj = sendRequest("POST", BASE_URL, "todos", param);
		
		assertEquals("false", obj.get(DONESTATUS_S));
		assertEquals(title, obj.get(TITLE_S));
		String id = (String) obj.get("id");
		
		// There are now 3 elements in the todos
		obj = sendRequest("GET", BASE_URL, "todos", "");
		JSONArray todos = obj.getJSONArray("todos");
		assertEquals(3, todos.length());
		
		// Delete newly added todo
		obj = sendRequest("DELETE", BASE_URL, "todos/"+id, "");
		
		System.out.println("\n");
	}
	
	@Test
	void testAmendWithPut() throws JSONException {
		String title1 = "TestAmendTODO";
		String param1 = ("{" + TITLE_S + ": " + title1 + "}");
		String title2 = "TestAmended";
		String status = "true";
		String des = "none";
		String param2 = ("{" 
				+ TITLE_S + ": " + title2 + "," 
				+ DONESTATUS_S + ": " + status + ","
				+ DESCRIPTION_S + ": " + des
				+ "}");
		
		// Create new obj
		JSONObject obj = sendRequest("POST", BASE_URL, "todos", param1);
		assertEquals(title1, obj.get("title"));
		
		String id = (String) obj.get("id");
		
		// Change title and status
		obj = sendRequest("PUT", BASE_URL, "todos/"+id, param2);
		assertEquals(title2, obj.get(TITLE_S));
		assertEquals(status, obj.get(DONESTATUS_S));
		assertEquals(des, obj.get(DESCRIPTION_S));
		
		
		// Delete newly added todo
		obj = sendRequest("DELETE", BASE_URL, "todos/"+id, "");
		
		System.out.println("\n");
	}
	
	@Test
	void testAmendWithPost() throws JSONException {
		String title1 = "TestAmendTODO";
		String param1 = ("{" + TITLE_S + ": " + title1 + "}");
		String title2 = "TestAmended";
		String status = "true";
		String des = "none";
		String param2 = ("{" 
				+ TITLE_S + ": " + title2 + "," 
				+ DONESTATUS_S + ": " + status + ","
				+ DESCRIPTION_S + ": " + des
				+ "}");
		
		// Create new obj
		JSONObject obj = sendRequest("POST", BASE_URL, "todos", param1);
		assertEquals(title1, obj.get("title"));
		
		String id = (String) obj.get("id");
		
		// Change title and status
		obj = sendRequest("POST", BASE_URL, "todos/"+id, param2);
		assertEquals(title2, obj.get(TITLE_S));
		assertEquals(status, obj.get(DONESTATUS_S));
		assertEquals(des, obj.get(DESCRIPTION_S));
		
		
		// Delete newly added todo
		obj = sendRequest("DELETE", BASE_URL, "todos/"+id, "");
		
		System.out.println("\n");
	}
	
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
