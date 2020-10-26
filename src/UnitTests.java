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

class UnitTests {
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Runtime rt = Runtime.getRuntime();
		rt.exec(Const.COMMAND);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		Client.shutDown(Const.BASE_URL);
		Runtime rt = Runtime.getRuntime();
		rt.exec(Const.COMMAND);
	}

	@BeforeEach
	void setUp() throws Exception {
		if(Client.testConnection(Const.BASE_URL)) {
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
	void testTodoGetInvalid() throws JSONException {
		JSONObject obj = Client.sendRequest("POST", Const.BASE_URL, "todos", Const.TODO_PARAM1);
		String id = (String) obj.get("id");
		
		// Delete
		Client.sendRequest("DELETE", Const.BASE_URL, "todos/"+id, "");
		
		//Get with invalid filter
		String filer = "id=" + id;
		obj = Client.sendRequest("GET", Const.BASE_URL, "todos?" + filer, "");
		JSONArray todos = obj.getJSONArray("todos");
		
		assertEquals(0, todos.length());
		
		System.out.println("\n");
	}
	
	@Test
	void testTodoPostNew() throws JSONException {
		JSONObject obj = Client.sendRequest("POST", Const.BASE_URL, "todos", Const.TODO_PARAM1);
		
		assertEquals(Const.STATUS1, obj.get(Const.DONESTATUS_S));
		assertEquals(Const.TITLE1, obj.get(Const.TITLE_S));
		String id = (String) obj.get("id");
		
		// Check there is at least an element in todos
		obj = Client.sendRequest("GET", Const.BASE_URL, "todos", "");
		JSONArray todos = obj.getJSONArray("todos");
		if(todos.length() < 1){
			Assert.fail();
		}
		
		// Delete newly added todo
		Client.sendRequest("DELETE", Const.BASE_URL, "todos/"+id, "");
		
		System.out.println("\n");
	}
	
	// =====================/todos/:id====================
	@Test
	void testTodoAmendWithPut() throws JSONException {
		JSONObject obj = Client.sendRequest("POST", Const.BASE_URL, "todos", Const.TODO_PARAM1);
		String id = (String) obj.get("id");
		
		// Change title and status and assert info
		obj = Client.sendRequest("PUT", Const.BASE_URL, "todos/"+id, Const.TODO_PARAM2);
		assertEquals(Const.TITLE2, obj.get(Const.TITLE_S));
		assertEquals(Const.STATUS2, obj.get(Const.DONESTATUS_S));
		assertEquals(Const.DESCRIPTION, obj.get(Const.DESCRIPTION_S));
		
		
		// Delete newly added todo
		Client.sendRequest("DELETE", Const.BASE_URL, "todos/"+id, "");
		
		System.out.println("\n");
	}
	
	@Test
	void testTodoDoubleDelete() throws JSONException {
		JSONObject obj = Client.sendRequest("POST", Const.BASE_URL, "todos", Const.TODO_PARAM1);
		
		String id = (String) obj.get("id");

		// Delete twice should give error
		Client.sendRequest("DELETE", Const.BASE_URL, "todos/"+id, "");		
		int error2 = Client.getResponseCode("DELETE", Const.BASE_URL, "todos/"+id, "");
		assertEquals(Const.NOT_FOUND, error2);
		
		System.out.println("\n");
	}
	
	// =====================/todos/:id/tasksof====================
	@Test
	void testTodoPostTasksof() throws JSONException {
		JSONObject proj_obj = Client.sendRequest("POST", Const.BASE_URL, "projects", Const.PROJECT_PARAM1);
		String idproject = (String) proj_obj.get("id");
		JSONObject todo_obj = Client.sendRequest("POST", Const.BASE_URL, "todos", Const.TODO_PARAM1);
		String idtodo = (String) todo_obj.get("id");
		
		String paramID = "{" + Const.ID_S + ":" + "\"" + idproject +"\"" +"}";
		
		JSONObject obj = Client.sendRequest("POST", Const.BASE_URL, "todos/"+idtodo+"/tasksof", paramID);
		obj = Client.sendRequest("GET", Const.BASE_URL, "todos/"+idtodo+"/tasksof", "");
		JSONArray projects = obj.getJSONArray("projects");
		assertEquals(1, projects.length());
		
		Client.sendRequest("DELETE", Const.BASE_URL, "todos/"+idtodo+"/tasksof/"+idproject, "");
		
		System.out.println("\n");
	}
	// =====================/todos/:id/categories====================
	@Test
	void testTodoPostCategories() throws JSONException {
		JSONObject cat_obj = Client.sendRequest("POST", Const.BASE_URL, "categories", Const.PROJECT_PARAM1);
		String idcategory = (String) cat_obj.get("id");
		JSONObject todo_obj = Client.sendRequest("POST", Const.BASE_URL, "todos", Const.TODO_PARAM1);
		String idtodo = (String) todo_obj.get("id");
		
		String paramID = "{" + Const.ID_S + ":" + "\"" + idcategory +"\"" +"}";
		
		JSONObject obj = Client.sendRequest("POST", Const.BASE_URL, "todos/"+idtodo+"/categories", paramID);
		obj = Client.sendRequest("GET", Const.BASE_URL, "todos/"+idtodo+"/categories", "");
		JSONArray projects = obj.getJSONArray("categories");
		assertEquals(1, projects.length());
		
		Client.sendRequest("DELETE", Const.BASE_URL, "todos/"+idtodo+"/categories/"+idcategory, "");
		
		System.out.println("\n");
	}
}
