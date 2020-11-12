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
    //========================== when ==========================
    @When("remove category {string} from todo {string}")
    public void remove_category_from_todo(String category, String todo) {
        String todo_id = DefinitionsHelper.getTodoId(todo);
        String category_id = DefinitionsHelper.getCategoryId(category);
        JSONObject obj = Client.sendRequest("DELETE",
                DefinitionsHelper.BASE_URL,
                "todos/" + todo_id + "/categories/" +category_id,
                "");
    }

    @When("categorize todo {string} as category {string}")
    public void categorize_as(String todo, String category) {
        String todo_id = DefinitionsHelper.getTodoId(todo);
        String category_id = DefinitionsHelper.getCategoryId(category);
        String body = "{ id :" + "\"" + category_id +"\" }";
        JSONObject obj = Client.sendRequest("POST",
                DefinitionsHelper.BASE_URL,
                "todos/" + todo_id + "/categories",
                body);
    }
    //========================== then ==========================
    @Then("the category of the todo {string} should be {string}")
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

    @Then("the return code should be {string}")
    public void the_return_code_should_be(String code) {
        assertEquals(code, Client.returnCode);
    }

}
