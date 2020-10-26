import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Client {

    private final static String BASE_URL = "http://localhost:4567/";

    public static void shutDown() {
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

    public static boolean testConnection() {
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

    public static int getResponseCode(String requestType, String baseUrl, String path, String body) {
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

    public static JSONObject sendRequest(String requestType, String baseUrl, String path, String body) {
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
