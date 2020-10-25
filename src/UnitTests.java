import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Runtime rt = Runtime.getRuntime();
		rt.exec("java -jar runTodoManagerRestAPI-1.5.5.jar");
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		shutDown();
		Runtime rt = Runtime.getRuntime();
		rt.exec("java -jar runTodoManagerRestAPI-1.5.5.jar");
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGetTodos() throws JSONException {
		JSONObject s1 = sendRequest("GET", BASE_URL, "todos", "");
		System.out.println(s1);
	}
	
	@Test
	void testPostNewTodos() throws JSONException {
		String param = ("{\"title\": \"woo\"}");
		JSONObject s1 = sendRequest("POST", BASE_URL, "todos", param);
		System.out.println(s1);
	}
	
	private static void shutDown() {
		try {
			URL url = new URL("http://localhost:4567/shutdown");
			
			System.out.println("Sending: " + url.toString());
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
	
	private static JSONObject sendRequest(String requestType, String baseUrl, String path, String body) {
        try {
            URL url = new URL(baseUrl + path);
            System.out.println("Sending: " + url.toString());
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
            
            if (connection.getResponseCode() != 200 && connection.getResponseCode() != 201) {
                throw new RuntimeException(
                        url.toString() + " failed : HTTP error code : " + connection.getResponseCode());
            }
            
            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            String response = br.readLine();
            if (response != null) {
            	JSONObject r = new JSONObject(response);
                connection.disconnect();
                return r;
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
