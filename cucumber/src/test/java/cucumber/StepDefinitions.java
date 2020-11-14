package cucumber;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class StepDefinitions {

    @Before
    public void initialization() throws Exception {
        Client.returnCode = "200";
        if(Client.testConnection(DefinitionsHelper.BASE_URL)) {
            System.out.println("Start\tSystem is ready");
        } else {
            System.out.println("Error\tlocalhost:4567 is not ready");
            System.out.println("Start\tStarting the System Now");
            Runtime rt = Runtime.getRuntime();
            rt.exec(DefinitionsHelper.COMMAND);
        }
    }

    @After
    public void resetServer() throws Exception {
        Client.shutDown(DefinitionsHelper.BASE_URL);
        Runtime rt = Runtime.getRuntime();
        rt.exec(DefinitionsHelper.COMMAND);
    }

    //========================== given ==========================
    @Given("system is ready")
    public void system_is_ready() {
        if(Client.testConnection(DefinitionsHelper.BASE_URL)) {
            System.out.println("Set-up\tSystem is ready");
        } else {
            System.out.println("Set-up\tError connecting to localhost:4567");
            Assert.fail();
        }
    }
    @Given("the following categories are created in the system:")
    public void the_following_categories_are_created_in_the_system(DataTable dataTable) throws JSONException {
        List<Map<String, String>> categories = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> c : categories) {
            String title = c.get("title");
            String description = c.get("description");
            String body = "{ title :" + title + ","
                    + "description :" + description + "}";
            JSONObject obj = Client.sendRequest("POST",
                    DefinitionsHelper.BASE_URL,
                    "categories",
                    body);
        }
    }
    @Given("the following todos are created in the system:")
    public void the_following_todos_are_created_in_the_system(DataTable dataTable) throws JSONException {
        List<Map<String, String>> todos = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> todo : todos) {
            String title = todo.get("title");
            String description = todo.get("description");
            String doneStatus = todo.get("doneStatus");
            String body = "{ title :" + title + ", "
                    + "description :" + description + ", "
                    + "doneStatus : " + doneStatus + "}";
            JSONObject obj = Client.sendRequest("POST",
                    DefinitionsHelper.BASE_URL,
                    "todos",
                    body);
        }
    }
    
    @Given("the following projects are created in the system:")
    public void the_following_projects_are_created_in_the_system(DataTable dataTable) throws JSONException {
        List<Map<String, String>> projects = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> project : projects) {
            String title = project.get("title");
            String completed = project.get("completed");
            String active = project.get("active");
            String description = project.get("description");
            String body = "{ title :" + title + ", "
                    + "completed :" + completed + ", "
                    + "active : " + active + ", "
                    + "description : " + description + "}";
            JSONObject obj = Client.sendRequest("POST",
                    DefinitionsHelper.BASE_URL,
                    "projects",
                    body);
        }
    }
    
    @Given("^the todo (.*) exists in the system$")
    public void the_todo_exists_in_the_system(String t) {
        assertNotNull(DefinitionsHelper.getTodoId(t));
    }
    @Given("^the todo (.*) does not exist in the system$")
    public void the_todo_does_not_exists_in_the_system(String t) {
        assertNull(DefinitionsHelper.getTodoId(t));
    }
    
    @Given("^the category (.*) exists in the system$")
    public void the_category_exists_in_the_system(String c) {
        assertNotNull(DefinitionsHelper.getCategoryId(c));
    }
    @Given("^the category (.*) does not exist in the system$")
    public void the_category_does_not_exist_in_the_system(String c) {
        assertNull(DefinitionsHelper.getCategoryId(c));
    }
    @Given("^the todo (.*) is marked (.*)$")
    public void the_todo_is_marked(String todo, String status) throws JSONException {
        JSONObject obj = Client.sendRequest("GET",
                DefinitionsHelper.BASE_URL,
                "todos?title=" + todo,
                "");
        boolean completed = false;
        if(status.equals("completed")) completed = true;
        JSONArray todos = obj.getJSONArray("todos");
        if(todos.length() > 1){
            assertEquals(Boolean.toString(completed), todos.getJSONObject(0).get("doneStatus"));
        }

    }

    //========================== when ==========================
    @When("^remove category (.*) from todo (.*)$")
    public void remove_category_from_todo(String category, String todo) {
        String todo_id = DefinitionsHelper.getTodoId(todo);
        String category_id = DefinitionsHelper.getCategoryId(category);
        JSONObject obj = Client.sendRequest("DELETE",
                DefinitionsHelper.BASE_URL,
                "todos/" + todo_id + "/categories/" +category_id,
                "");
    }
    @When("^remove the todo (.*) from the system$")
    public void remove_the_todo_from_the_system(String todo) {
        String todo_id = DefinitionsHelper.getTodoId(todo);
        JSONObject obj = Client.sendRequest("DELETE",
                DefinitionsHelper.BASE_URL,
                "todos/" + todo_id,
                "");
    }
    @When("^categorize todo (.*) as category (.*)$")
    public void categorize_as(String todo, String category) {
        String todo_id = DefinitionsHelper.getTodoId(todo);
        String category_id = DefinitionsHelper.getCategoryId(category);
        String body = "{ id :" + "\"" + category_id +"\" }";
        JSONObject obj = Client.sendRequest("POST",
                DefinitionsHelper.BASE_URL,
                "todos/" + todo_id + "/categories",
                body);
    }
    @When("^add the todo (.*) to the category (.*)$")
    public void add_the_todo_to_the_category(String todo, String category) {
        String todo_id = DefinitionsHelper.getTodoId(todo);
        String category_id = DefinitionsHelper.getCategoryId(category);
        String body = "{ id :" + "\"" + todo_id +"\" }";
        JSONObject obj = Client.sendRequest("POST",
                DefinitionsHelper.BASE_URL,
                "categories/" + category_id + "/todos",
                body);
    }
    @When("^marking the todo (.*) as (.*)$")
    public void marking_the_todo_as_(String todo, String status) {
        String todo_id = DefinitionsHelper.getTodoId(todo);
        boolean completed = false;
        if(status.equals("completed")) completed = true;
        String body = "{ doneStatus :" + completed + "}";
        JSONObject obj = Client.sendRequest("POST",
                DefinitionsHelper.BASE_URL,
                "todos/" + todo_id,
                body);
    }

    //========================== then ==========================
    
    @Then("^the todo (.*) should be marked completed in the system$")
    public void the_todo_should_be(String todo) throws JSONException {
    	boolean completed = false;
    	String todo_id = DefinitionsHelper.getTodoId(todo);
    	JSONObject obj = Client.sendRequest("GET", DefinitionsHelper.BASE_URL, "todos/" + todo_id, "");
    	if (obj.get("doneStatus").equals("true")) {
    		completed = true;
    	}
    	assertEquals(true, completed);
    }
    
    @Then("^the category of the todo (.*) should be (.*)$")
    public void the_category_of_the_todo_should_be(String todo, String category) throws JSONException {
        boolean sameId = false;
        String todo_id = DefinitionsHelper.getTodoId(todo);
        JSONObject obj = Client.sendRequest("GET",
                DefinitionsHelper.BASE_URL,
                "todos/" + todo_id + "/categories",
                "");
        JSONArray categories = obj.getJSONArray("categories");
        for(int i = 0; i < categories.length(); i++){
            JSONObject c = categories.getJSONObject(i);
            if(c.get("title").equals(category)) {
                sameId = true;
                break;
            }
        }
        assertEquals(true, sameId);
    }
    @Then("^the todo (.*) with status (.*) should be under the category (.*)$")
    public void the_todo_should_be_under_the_category(String todo, String status, String category) throws JSONException {
        boolean sameId = false;
        String cat_id = DefinitionsHelper.getCategoryId(category);
        JSONObject obj;
        switch(status){
            case "incomplete":
                obj = Client.sendRequest("GET",
                        DefinitionsHelper.BASE_URL,
                        "categories/" + cat_id + "/todos?doneStatus=false",
                        "");
                break;
            case "completed":
                obj = Client.sendRequest("GET",
                        DefinitionsHelper.BASE_URL,
                        "categories/" + cat_id + "/todos?doneStatus=true",
                        "");
                break;
            default:
                obj = Client.sendRequest("GET",
                        DefinitionsHelper.BASE_URL,
                        "categories/" + cat_id + "/todos",
                        "");
                break;
        }
        JSONArray todos = obj.getJSONArray("todos");
        for(int i = 0; i < todos.length(); i++){
            JSONObject c = todos.getJSONObject(i);
            if(c.get("title").equals(todo)) {
                sameId = true;
                break;
            }
        }
        assertEquals(true, sameId);
    }
    @Then("^the list of todos with status (.*) under the category (.*) should be empty$")
    public void the_list_of_todos_under_the_category_should_be_empty(String status, String category) throws JSONException {
        String cat_id = DefinitionsHelper.getCategoryId(category);
        JSONObject obj;
        switch(status){
            case "incomplete":
                obj = Client.sendRequest("GET",
                        DefinitionsHelper.BASE_URL,
                        "categories/" + cat_id + "/todos?doneStatus=false",
                        "");
                break;
            case "completed":
                obj = Client.sendRequest("GET",
                        DefinitionsHelper.BASE_URL,
                        "categories/" + cat_id + "/todos?doneStatus=true",
                        "");
                break;
            default:
                obj = Client.sendRequest("GET",
                        DefinitionsHelper.BASE_URL,
                        "categories/" + cat_id + "/todos",
                        "");
                break;
        }
        JSONArray todos = obj.getJSONArray("todos");
        assertEquals(0, todos.length());
    }
    
    @Then("an error not found message should be displayed")
    public void an_error_not_found_message_should_be_displayed() {
        assertEquals("404", Client.returnCode);
    }

}
