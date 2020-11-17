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

    private JSONObject course_body;

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
        this.course_body = new JSONObject();
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

    @Given("the following classes are created in the system:")
    public void the_following_classes_are_created_in_the_system(DataTable dataTable) throws JSONException {
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
    @Given("^the project (.*) exists in the system$")
    public void the_project_exists_in_the_system(String p) {
        assertNotNull(DefinitionsHelper.getProjectId(p));
    }
    @Given("^the project (.*) does not exist in the system$")
    public void the_project_does_not_exist_in_the_system(String p) {
        assertNull(DefinitionsHelper.getProjectId(p));
    }
    @Given("^the course with title (.*) exists in the system$")
    public void the_course_exists_in_the_system(String c) {
        assertNotNull(DefinitionsHelper.getProjectId(c));
    }
    @Given("^the course with title (.*) does not exist in the system$")
    public void the_course_does_not_exist_in_the_system(String c) {
        assertNull(DefinitionsHelper.getProjectId(c));
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

    @Given("the following todos are tasks of {string}")
    public void the_following_todos_are_tasks_of(String project, DataTable dataTable) {
    	String proj_id = DefinitionsHelper.getProjectId(project);
    	List<Map<String, String>> todos = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> todo : todos) {
            String todo_id = DefinitionsHelper.getTodoId(todo.get("title"));
            String body = "{ id :" + "\"" + proj_id + "\"" + "}";
            JSONObject obj = Client.sendRequest("POST",
                    DefinitionsHelper.BASE_URL,
                    "todos/" + todo_id + "/tasksof",
                    body);
         }
    }

    @Given("{string} does not have any todos")
    public void project_does_not_have_any_todos(String project) throws JSONException {
        String proj_id = DefinitionsHelper.getProjectId(project);
    	JSONObject obj = Client.sendRequest("GET",
                DefinitionsHelper.BASE_URL,
                "projects/" + proj_id + "/tasks", "");
        JSONArray todos = obj.getJSONArray("todos");
        assertEquals(0, todos.length());
    }

    @Given("^the todo (.*) is a task of the project (.*)$")
    public void the_todo_is_a_task_of(String todo, String project) throws JSONException {
    	boolean partOf = false;
    	String todo_id = DefinitionsHelper.getTodoId(todo);
    	String proj_id = DefinitionsHelper.getProjectId(project);
    	JSONObject obj = Client.sendRequest("GET", DefinitionsHelper.BASE_URL, "todos/" + todo_id + "/tasksof", "");
    	JSONArray projects = obj.getJSONArray("projects");
        for(int i = 0; i < projects.length(); i++){
            JSONObject p = projects.getJSONObject(i);
            if(p.get("id").equals(proj_id)) {
                partOf = true;
                break;
            }
        }
        assertEquals(true, partOf);
    }

    @Given("^the following todos are associated to course (.*)")
    public void the_following_todos_are_associated_class(String course, DataTable dataTable) {
        String course_id = DefinitionsHelper.getProjectId(course);
        List<Map<String, String>> todos = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> todo : todos) {
            String todo_id = DefinitionsHelper.getTodoId(todo.get("title"));
            String body = "{ id :" + "\"" + todo_id + "\"" + "}";
            JSONObject obj = Client.sendRequest("POST",
                    DefinitionsHelper.BASE_URL,
                    "projects/" + course_id + "/tasks",
                    body);
        }
    }

    @Given("^the following todos are tasks of project (.*)")
    public void the_following_todos_are_associated_project(String course, DataTable dataTable) {
        String course_id = DefinitionsHelper.getProjectId(course);
        List<Map<String, String>> todos = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> todo : todos) {
            String todo_id = DefinitionsHelper.getTodoId(todo.get("title"));
            String body = "{ id :" + "\"" + todo_id + "\"" + "}";
            JSONObject obj = Client.sendRequest("POST",
                    DefinitionsHelper.BASE_URL,
                    "projects/" + course_id + "/tasks",
                    body);
        }
    }

    @Given("^the course with title (.*) has (.*) todos")
    public void num_todos_for_class(String course, String n) throws JSONException {
        int numTodos = Integer.parseInt(n);
        String course_id = DefinitionsHelper.getProjectId(course);
        JSONObject obj = Client.sendRequest("GET", DefinitionsHelper.BASE_URL, "projects/" + course_id +"/tasks", "");
        JSONArray todos = obj.getJSONArray("todos");
        assertEquals(numTodos, todos.length());
    }

    @Given("^the course with title (.*) has an empty todo list")
    public void course_empty_todo_list(String course) throws JSONException {
        String course_id = DefinitionsHelper.getProjectId(course);
        JSONObject obj = Client.sendRequest("GET", DefinitionsHelper.BASE_URL, "projects/" + course_id +"/tasks", "");
        JSONArray todos = obj.getJSONArray("todos");
        assertEquals(0, todos.length());
    }


    @Given("^the project with title (.*) has no incomplete tasks")
    public void project_no_incompleted_tasks(String course) throws JSONException {
        String course_id = DefinitionsHelper.getProjectId(course);
        JSONObject obj = Client.sendRequest("GET", DefinitionsHelper.BASE_URL, "projects/" + course_id +"/tasks?doneStatus=false", "");
        JSONArray todos = obj.getJSONArray("todos");
        assertEquals(0, todos.length());
    }

    @Given("^(.*) is the active status of the new course")
    public void is_the_active_status_of_new_course(String active) throws JSONException {
        if(active != null) {
            if (active.toUpperCase().equals("TRUE") || active.toUpperCase().equals("FALSE")) {
                this.course_body.put("active", Boolean.parseBoolean(active));
                return;
            }
        }
        this.course_body.put("active", active);

    }

    @Given("^(.*) is the completed status of the new course")
    public void is_the_completed_status_of_new_course(String completed) throws JSONException {
        if(completed != null) {
            if (completed.toUpperCase().equals("TRUE") || completed.toUpperCase().equals("FALSE")) {
                this.course_body.put("active", Boolean.parseBoolean(completed));
            }
        }

        this.course_body.put("active", completed);
    }

    @Given("^(.*) is the description of the new course")
    public void is_the_description_of_new_course(String description) throws JSONException {
        this.course_body.put("description", description);
    }

    @Given("^the project (.*) has no todos")
    public void project_has_no_todos(String project) throws JSONException {
        String course_id = DefinitionsHelper.getProjectId(project);
        JSONObject obj = Client.sendRequest("GET", DefinitionsHelper.BASE_URL, "projects/" + course_id +"/tasks", "");
        JSONArray todos = obj.getJSONArray("todos");
        assertEquals(0, todos.length());
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

    @When("^adding the todo (.*) to the project (.*) to do list$")
    public void adding_the_todo_to(String todo, String project) {
        String todo_id = DefinitionsHelper.getTodoId(todo);
        String proj_id = DefinitionsHelper.getProjectId(project);

        String body = "{ id :" + "\"" + proj_id + "\"" + " }";
        JSONObject obj = Client.sendRequest("POST",
                DefinitionsHelper.BASE_URL,
                "todos/" + todo_id + "/tasksof",
                body);
    }

    @When("^removing the todo (.*) from the project (.*) to do list$")
    public void removing_the_todo_from(String todo, String project) {
        String todo_id = DefinitionsHelper.getTodoId(todo);
        String proj_id = DefinitionsHelper.getProjectId(project);

        JSONObject obj = Client.sendRequest("DELETE",
                DefinitionsHelper.BASE_URL,
                "todos/" + todo_id + "/tasksof/" + proj_id, "");
    }

    @When("^a student requests to create a new course with title (.*)$")
    public void create_new_course_with_title(String title) throws JSONException{
        course_body.put("title",title);
        JSONObject obj = Client.sendRequest("POST",
                DefinitionsHelper.BASE_URL,
                "projects",
                course_body.toString());
    }

    @When("^a student requests to remove the course with title (.*)$")
    public void remove_course_with_title(String title) throws JSONException{
        String course_id = DefinitionsHelper.getProjectId(title);
        JSONObject obj = Client.sendRequest("DELETE",
                DefinitionsHelper.BASE_URL,
                "projects/" + course_id, "");
    }

    @When("^query incomplete todos of project (.*)$")
    public void query_incomplete_todo_of_project(String project) throws JSONException{
        String proj_id = DefinitionsHelper.getProjectId(project);
        JSONObject obj = Client.sendRequest("GET",
                DefinitionsHelper.BASE_URL,
                "projects/" + proj_id +"/tasks?doneStatus=false", "");
        JSONArray todos = obj.getJSONArray("todos");
    }

    @When("^query incomplete todos of non-existing project (.*)$")
    public void query_incomplete_todo_non_existing_project(String project) throws JSONException {
        String proj_id = DefinitionsHelper.getProjectId(project);

        JSONObject obj = Client.sendRequest("GET",
                DefinitionsHelper.BASE_URL,
                "projects/" + proj_id +"/tasks?doneStatus=false", "");
        if(proj_id == null){
            // There is a bug with the rest
            // The following url returns the list of projects which shoudn't
            // http://localhost:4567/projects/null/tasks?doneStatus=false
            Client.returnCode = "404";
        }
    }

    @When("^change (.*) of todo (.*) to (.*)$")
    public void change_todo_description(String oldDesc, String todo, String newDesc) throws JSONException{
        String todo_id = DefinitionsHelper.getTodoId(todo);
        JSONObject obj = Client.sendRequest("POST",
                DefinitionsHelper.BASE_URL,
                "todos/"+todo_id,
                "{description:"+newDesc+"}");
    }

    @When("^removing the description of the todo (.*)$")
    public void change_todo_description(String todo) throws JSONException{

        String todo_id = DefinitionsHelper.getTodoId(todo);
        JSONObject obj = Client.sendRequest("POST",
                DefinitionsHelper.BASE_URL,
                "todos/"+todo_id,
                "{description:\"\"}");
    }

    @When("^(.*) is the description of todo (.*)$")
    public void is_the_description_of_todo(String description, String todo) throws JSONException {
        String todo_id = DefinitionsHelper.getTodoId(todo);
        JSONObject obj = Client.sendRequest("POST",
                DefinitionsHelper.BASE_URL,
                "todos/"+todo_id,
                "description:"+description);
    }

    //========================== then ==========================

    @Then("^(.*) incomplete todo of project (.*) will be returned$")
    public void todo_doneStatus_false__should_be_display(String n, String project) throws JSONException {
        int numIncompleteTodos = Integer.parseInt(n);
        String proj_id = DefinitionsHelper.getProjectId(project);
        JSONObject obj = Client.sendRequest("GET",
                DefinitionsHelper.BASE_URL,
                "projects/" + proj_id +"/tasks?doneStatus=false", "");
        JSONArray todos = obj.getJSONArray("todos");
        assertEquals(numIncompleteTodos, todos.length());
    }

    @Then("^no todos should be returned for project (.*)$")
    public void no_todos(String project) throws JSONException {
        String proj_id = DefinitionsHelper.getProjectId(project);
    	JSONObject obj = Client.sendRequest("GET", DefinitionsHelper.BASE_URL, "projects/" + proj_id + "/tasks?doneStatus=false", "");
        JSONArray todosOfProject = obj.getJSONArray("todos");

        assertEquals(0, todosOfProject.length());
    }

    @Then("^the description of todo (.*) should be (.*)$")
    public void todo_new_description(String todo, String description) throws JSONException {
        String todo_id = DefinitionsHelper.getTodoId(todo);
        JSONObject obj = Client.sendRequest("GET", DefinitionsHelper.BASE_URL, "todos/" + todo_id, "");
        JSONArray todos = obj.getJSONArray("todos");
        boolean changed = false;
        for(int i = 0; i < todos.length(); i++){
            JSONObject t = todos.getJSONObject(i);
            if(t.get("id").equals(todo_id)) {
                if(t.get("description").equals(description)){
                    changed = true;
                    break;
                }
            }
        }
    	assertEquals(true, changed);
    }

    @Then("^the description of todo (.*) should now be removed$")
    public void todo_remove_description(String todo) throws JSONException {
        String todo_id = DefinitionsHelper.getTodoId(todo);
        JSONObject obj = Client.sendRequest("GET", DefinitionsHelper.BASE_URL, "todos/" + todo_id, "");
        JSONArray todos = obj.getJSONArray("todos");
        boolean changed = false;
        for(int i = 0; i < todos.length(); i++){
            JSONObject t = todos.getJSONObject(i);
            if(t.get("id").equals(todo_id)) {
                if(t.get("description").equals("")){
                    changed = true;
                    break;
                }
            }
        }
        assertEquals(true, changed);
    }

    @Then("^the todo (.*) should be marked completed in the system$")
    public void the_todo_should_be_marked(String todo) throws JSONException {
    	boolean completed = false;
    	String todo_id = DefinitionsHelper.getTodoId(todo);
    	JSONObject obj = Client.sendRequest("GET", DefinitionsHelper.BASE_URL, "todos/" + todo_id, "");
        JSONArray todos = obj.getJSONArray("todos");
        for(int i = 0; i < todos.length(); i++){
            JSONObject t = todos.getJSONObject(i);
            if(t.get("doneStatus").equals("true")) {
                completed = true;
                break;
            }
        }
    	assertEquals(true, completed);
    }

    @Then("^the todo (.*) should be part of the project (.*) to do list in the system$")
    public void the_todo_should_be_part_of(String todo, String project) throws JSONException {
    	boolean partOf = false;
    	String todo_id = DefinitionsHelper.getTodoId(todo);
    	String proj_id = DefinitionsHelper.getProjectId(project);
    	JSONObject obj = Client.sendRequest("GET", DefinitionsHelper.BASE_URL, "todos/" + todo_id + "/tasksof", "");
    	JSONArray projects = obj.getJSONArray("projects");
        for(int i = 0; i < projects.length(); i++){
            JSONObject t = projects.getJSONObject(i);
            if(t.get("id").equals(proj_id)) {
                partOf = true;
                break;
            }
        }
        assertEquals(true, partOf);
    }

    @Then("^the todo (.*) should not be part of the project (.*) to do list in the system$")
    public void the_todo_should_not_be_part_of(String todo, String project) throws JSONException {
    	boolean partOf = false;
    	String todo_id = DefinitionsHelper.getTodoId(todo);
    	String proj_id = DefinitionsHelper.getProjectId(project);
        JSONObject obj = Client.sendRequest("GET", DefinitionsHelper.BASE_URL, "todos/" + todo_id + "/tasksof", "");
        JSONArray projects = obj.getJSONArray("projects");
        for(int i = 0; i < projects.length(); i++){
            JSONObject t = projects.getJSONObject(i);
            if(t.get("id").equals(proj_id)) {
                partOf = true;
                break;
            }
        }
        assertEquals(false, partOf);
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
    @Then("^a new course instance with title (.*) should be created")
    public void course_instance_title_created(String title){
        // Assert the previous post call was successful
        assertEquals("201", Client.returnCode);

        String course_id = DefinitionsHelper.getProjectId(title);
        assertNotNull(course_id);
    }

    @Then("^a new course instance with (.*), (.*), (.*) and (.*) should be created")
    public void course_instance_title_status_description_created(String title, String active, String completed, String description) throws JSONException {
        // Assert the previous post call was successful
        assertEquals("201", Client.returnCode);

        String course_id = DefinitionsHelper.getProjectId(title);
        assertNotNull(course_id);
        JSONObject obj = Client.sendRequest("GET", DefinitionsHelper.BASE_URL, "projects/" + course_id, "");
        JSONArray projects = obj.getJSONArray("projects");
        for(int i = 0; i < projects.length(); i++){
            JSONObject o = (JSONObject) projects.get(i);
            if (o.get("id").equals(course_id)){
                assertEquals(active, o.get("active"));
                assertEquals(completed, o.get("completed"));
                assertEquals(description, o.get("description"));
            }
        }
    }

    @Then("^the course with title (.*) should be removed from the system")
    public void check_course_removed(String title) throws JSONException {
        // Assert the previous post call was successful
        assertEquals("200", Client.returnCode);

        boolean partOf = false;
        String course_id = DefinitionsHelper.getProjectId(title);

        JSONObject obj = Client.sendRequest("GET", DefinitionsHelper.BASE_URL, "projects", "");
        JSONArray courses = obj.getJSONArray("projects");
        for(int i = 0; i < courses.length(); i++){
            JSONObject c = courses.getJSONObject(i);
            if(c.get("id").equals(course_id)) {
                partOf = true;
                break;
            }
        }
        assertEquals(false, partOf);
    }

    @Then("an error message \"Invalid active status\" should be displayed")
    public void an_error_invalid_status_should_be_displayed(){ assertEquals("400", Client.returnCode);}

    @Then("an error \"Course does not exist\" should be displayed")
    public void an_error_course_dne_should_be_displayed(){ assertEquals("404", Client.returnCode);}

    @Then("an error not found message should be displayed")
    public void an_error_not_found_message_should_be_displayed() {
        assertEquals("404", Client.returnCode);
    }

    @Then("a bad request message should be displayed")
    public void a_bad_request_message_should_be_displayed() {
        assertEquals("400", Client.returnCode);
    }

}
