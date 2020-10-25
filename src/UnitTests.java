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
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class UnitTests {
	private final String BASE_URL = "http://localhost:4567/";
    private JSONObject joResponse;
    private JSONArray jaResponse;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGetTodos() {
		JSONObject s1 = sendRequest("GET", BASE_URL, "todos", "");
		System.out.println(s1);
	}
	
	private static JSONObject sendRequest(String requestType, String baseUrl, String path, String parameters) {
        try {
            URL url = new URL(baseUrl + path + ((parameters == null) ? "" : ("?" + parameters)));
            System.out.println("Sending: " + url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestType);
            connection.setRequestProperty("Accept", "application/json");
            if (connection.getResponseCode() != 200) {
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
