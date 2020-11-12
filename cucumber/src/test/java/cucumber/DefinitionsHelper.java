package cucumber;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DefinitionsHelper {
    final static String BASE_URL = "http://localhost:4567/";
    final static String COMMAND = "java -jar ../runTodoManagerRestAPI-1.5.5.jar";

    public static String getTodoId(String t){
        try {
            JSONObject obj = Client.sendRequest("GET", BASE_URL, "todos?title=" + t, "");
            JSONArray todos = obj.getJSONArray("todos");

            if (todos.length() > 0) {
                return (String) todos.getJSONObject(0).get("id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCategoryId(String t){
        try {
            JSONObject obj = Client.sendRequest("GET", BASE_URL, "categories?title=" + t, "");
            JSONArray todos = obj.getJSONArray("categories");

            if (todos.length() > 0) {
                return (String) todos.getJSONObject(0).get("id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
