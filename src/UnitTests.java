import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class UnitTests {
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		if(Client.testConnection(Const.BASE_URL)) {
			System.out.println("Start\tSystem is ready");
		} else {
			System.out.println("Error\tlocalhost:4567 is not ready");
			System.out.println("Start\tStarting the System Now");
			Runtime rt = Runtime.getRuntime();
			rt.exec(Const.COMMAND);
		}
		
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
	void testGetInvalidTodo() throws JSONException {
		JSONObject obj = Client.sendRequest("POST", Const.BASE_URL, "todos", Const.TODO_PARAM1);
		String id = (String) obj.get(Const.ID);

		Client.sendRequest("DELETE", Const.BASE_URL, "todos/"+id, "");

		//Get with invalid filter
		String filer = "id=" + id;
		obj = Client.sendRequest("GET", Const.BASE_URL, "todos?" + filer, "");
		JSONArray todos = obj.getJSONArray("todos");

		assertEquals(0, todos.length());
	}

	@Test
	void testPostNewTodo() throws JSONException {
		JSONObject obj = Client.sendRequest("POST", Const.BASE_URL, "todos", Const.TODO_PARAM1);

		assertEquals(Const.TRUE, obj.get(Const.DONESTATUS_S));
		assertEquals(Const.TITLE1, obj.get(Const.TITLE));
		String id = (String) obj.get(Const.ID);

		// Check there is at least an element in todos
		obj = Client.sendRequest("GET", Const.BASE_URL, "todos", "");
		JSONArray todos = obj.getJSONArray("todos");
		if(todos.length() < 1){
			Assert.fail();
		}

		// Delete newly added todo
		Client.sendRequest("DELETE", Const.BASE_URL, "todos/"+id, "");
	}

	// =====================/todos/:id====================
	@Test
	void testPutTodoAmend() throws JSONException {
		JSONObject obj = Client.sendRequest("POST", Const.BASE_URL, "todos", Const.TODO_PARAM1);
		String id = (String) obj.get(Const.ID);

		// Change title and status and assert info
		obj = Client.sendRequest("PUT", Const.BASE_URL, "todos/"+id, Const.TODO_PARAM2);
		assertEquals(Const.TITLE2, obj.get(Const.TITLE));
		assertEquals(Const.FALSE, obj.get(Const.DONESTATUS_S));
		assertEquals(Const.DESCRIPTION, obj.get(Const.DESCRIPTION_S));


		// Delete newly added todo
		Client.sendRequest("DELETE", Const.BASE_URL, "todos/"+id, "");
	}

	@Test
	void testDeleteTodoInvalid() throws JSONException {
		JSONObject obj = Client.sendRequest("POST", Const.BASE_URL, "todos", Const.TODO_PARAM1);

		String id = (String) obj.get(Const.ID);

		// Delete twice should give error
		Client.sendRequest("DELETE", Const.BASE_URL, "todos/"+id, "");
		int err = Client.getResponseCode("DELETE", Const.BASE_URL, "todos/"+id, "");
		assertEquals(Const.NOT_FOUND, err);
	}

	// =====================/todos/:id/tasksof====================
	@Test
	void testPostTodoTasksof() throws JSONException {
		JSONObject proj_obj = Client.sendRequest("POST", Const.BASE_URL, "projects", Const.PROJECT_PARAM1);
		String idproject = (String) proj_obj.get(Const.ID);
		JSONObject todo_obj = Client.sendRequest("POST", Const.BASE_URL, "todos", Const.TODO_PARAM1);
		String idtodo = (String) todo_obj.get(Const.ID);

		String paramID = ("{\""+ Const.ID +"\": \""+ idproject +"\"}");

		Client.sendRequest("POST", Const.BASE_URL, "todos/"+idtodo+"/tasksof", paramID);
		JSONObject obj = Client.sendRequest("GET", Const.BASE_URL, "todos/"+idtodo+"/tasksof", "");
		JSONArray projects = obj.getJSONArray("projects");
		assertEquals(1, projects.length());

		Client.sendRequest("DELETE", Const.BASE_URL, "todos/"+idtodo+"/tasksof/"+idproject, "");
		Client.sendRequest("DELETE", Const.BASE_URL, "projects/"+idproject, "");
	}
	// =====================/todos/:id/categories====================
	@Test
	void testPostTodoCategories() throws JSONException {
		JSONObject cat_obj = Client.sendRequest("POST", Const.BASE_URL, "categories", Const.CATEGORY_PARAM2);
		String idcategory = (String) cat_obj.get(Const.ID);
		JSONObject todo_obj = Client.sendRequest("POST", Const.BASE_URL, "todos", Const.TODO_PARAM1);
		String idtodo = (String) todo_obj.get(Const.ID);

		String paramID = "{" + Const.ID + ":" + "\"" + idcategory +"\"" +"}";

		Client.sendRequest("POST", Const.BASE_URL, "todos/"+idtodo+"/categories", paramID);
		JSONObject obj = Client.sendRequest("GET", Const.BASE_URL, "todos/"+idtodo+"/categories", "");
		JSONArray projects = obj.getJSONArray("categories");
		assertEquals(1, projects.length());

		Client.sendRequest("DELETE", Const.BASE_URL, "todos/"+idtodo+"/categories/"+idcategory, "");
		Client.sendRequest("DELETE", Const.BASE_URL, "categories/"+idcategory, "");
	}

	// =====================/projects====================
	@Test
	void testGetAllProjects() throws JSONException {
		JSONObject obj = Client.sendRequest("GET", Const.BASE_URL, "projects", "");
		assertEquals(1, obj.getJSONArray("projects").length());
	}

	// =====================/projects/:id====================
	@Test
	void testGetProjectWithValidID() throws JSONException {
		String id = "1";
		JSONObject obj_1 = Client.sendRequest("GET", Const.BASE_URL, "projects"+"/"+id, "");
		JSONObject obj_2 = (JSONObject)obj_1.getJSONArray("projects").get(0);
		assertEquals(Const.PROJECT_TITLE_0, obj_2.getString(Const.TITLE));
	}

	@Test
	void testGetProjectWithInvalidID() throws JSONException {
		String id = "-1";
		JSONObject obj = Client.sendRequest("GET", Const.BASE_URL, "projects"+"/"+id, "");
		assertEquals(null, obj);
	}

	@Test
	void testPostNewProject() throws JSONException {
		JSONObject obj_1 = Client.sendRequest("POST", Const.BASE_URL, "projects", Const.PROJECT_PARAM1);
		assertEquals(Const.PROJECT_TITLE_1, obj_1.getString("title"));
		String target_id = (String) obj_1.get(Const.ID);

		JSONObject obj_2 = Client.sendRequest("GET", Const.BASE_URL, "projects", "");
		JSONArray list = obj_2.getJSONArray("projects");
		for(int i = 0; i < list.length(); i++){
			 String id =  ((JSONObject)list.get(i)).getString(Const.ID).toString();
			 if (id.equals(target_id)){
			 	break;
			 }
			 if(i == list.length()-1){
				 Assert.fail();
			 }
		}
		Client.sendRequest("DELETE", Const.BASE_URL, "projects/"+target_id, "");
	}

	@Test
	void testDeleteProjectWithValidID() throws JSONException {
		JSONObject obj_1= Client.sendRequest("POST", Const.BASE_URL, "projects", Const.PROJECT_PARAM2);
		assertEquals(Const.PROJECT_TITLE_2, obj_1.get(Const.TITLE));
		String id = (String) obj_1.get(Const.ID);

		Client.sendRequest("DELETE", Const.BASE_URL, "projects/"+id, "");

		JSONObject obj_2 = Client.sendRequest("GET", Const.BASE_URL, "projects", "");
		JSONArray list = obj_2.getJSONArray("projects");
		for(int i = 0; i < list.length(); i++){
			assertTrue( ((JSONObject)list.get(i)).getString(Const.ID) != id);
		}
	}

	@Test
	void testDeleteProjectWithInvalidID() throws JSONException {
		String id = "-1";
		JSONObject obj = Client.sendRequest("DELETE", Const.BASE_URL, "projects/"+id, "");
		assertEquals(null, obj);
	}

	// =====================/projects/:id/task====================
	@Test
	void testGetProjectTasks() throws JSONException {
		String id = "1";
		JSONObject obj_1 = Client.sendRequest("GET", Const.BASE_URL, "projects"+"/"+id+"/tasks", "");
		JSONArray obj_2 = obj_1.getJSONArray("todos");
		assertEquals(2, obj_2.length());
	}

	@Test
	void testPostProjectTasks() throws JSONException {
		JSONObject proj_obj = Client.sendRequest("POST", Const.BASE_URL, "projects", Const.PROJECT_PARAM1);
		String idproject = (String) proj_obj.get(Const.ID);
		JSONObject todo_obj = Client.sendRequest("POST", Const.BASE_URL, "todos", Const.TODO_PARAM1);
		String idtodo = (String) todo_obj.get(Const.ID);

		String paramID =  ("{\""+ Const.ID +"\": \""+ idtodo +"\"}");

		Client.sendRequest("POST", Const.BASE_URL, "projects/"+idproject+"/tasks", paramID);
		JSONObject obj_2 = Client.sendRequest("GET", Const.BASE_URL, "projects/"+idproject+"/tasks", "");
		JSONArray todos = obj_2.getJSONArray("todos");
		assertEquals(1, todos.length());

		Client.sendRequest("DELETE", Const.BASE_URL, "projects/"+idproject+"/tasks/"+idtodo, "");
		Client.sendRequest("DELETE", Const.BASE_URL, "todos/"+idtodo, "");
		Client.sendRequest("DELETE", Const.BASE_URL, "projects/"+idproject, "");
	}


	// =====================/categories====================
	@Test
	void testGetCategories() throws JSONException {
		JSONObject obj = Client.sendRequest("GET", Const.BASE_URL, "categories", "");
		assertEquals(2, obj.getJSONArray("categories").length());
	}

	// =====================/categories/:id====================

	@Test
	void testGetCategoryWithValidID() throws JSONException {
		String id = "1";
		JSONObject obj = Client.sendRequest("GET", Const.BASE_URL, "categories"+"/"+id, "");
		JSONObject o = (JSONObject)obj.getJSONArray("categories").get(0);
		assertEquals(Const.CATEGORY_TITLE_0, o.getString(Const.TITLE));
	}

	@Test
	void testGetCategoryWithInvalidID() throws JSONException {
		String id = "-1";
		JSONObject obj = Client.sendRequest("DELETE", Const.BASE_URL, "categories/"+id, "");
		assertEquals(null, obj);
	}

	@Test
	void testPostNewCategory() throws JSONException {
		JSONObject obj_1 = Client.sendRequest("POST", Const.BASE_URL, "categories", Const.CATEGORY_PARAM3);
		assertEquals(Const.CATEGORY_TITLE_3, obj_1.getString("title"));
		String target_id = (String) obj_1.get(Const.ID);

		JSONObject obj_2 = Client.sendRequest("GET", Const.BASE_URL, "categories", "");
		JSONArray list = obj_2.getJSONArray("categories");
		for(int i = 0; i < list.length(); i++){
			String id =  ((JSONObject)list.get(i)).getString(Const.ID).toString();
			if (id.equals(target_id)){
				break;
			}
			if(i == list.length()-1){
				Assert.fail();
			}
		}
		Client.sendRequest("DELETE", Const.BASE_URL, "categories/"+target_id, "");
	}

	@Test
	void testDeleteCategoryWithValidID() throws JSONException {
		JSONObject obj_1= Client.sendRequest("POST", Const.BASE_URL, "categories", Const.CATEGORY_PARAM2);
		assertEquals(Const.CATEGORY_TITLE_2, obj_1.get(Const.TITLE));
		String id = (String) obj_1.get(Const.ID);

		Client.sendRequest("DELETE", Const.BASE_URL, "categories/"+id, "");

		JSONObject obj_2 = Client.sendRequest("GET", Const.BASE_URL, "categories", "");
		JSONArray list = obj_2.getJSONArray("categories");
		for(int i = 0; i < list.length(); i++){
			assertTrue( ((JSONObject)list.get(i)).getString(Const.ID) != id);
		}
	}

	@Test
	void testDeleteCategoryWithInvalidID() throws JSONException {
		String id = "-1";
		JSONObject obj = Client.sendRequest("DELETE", Const.BASE_URL, "categories/"+id, "");
		assertEquals(null, obj);
	}

	// =====================/categories/:id/projects====================
	@Test
	void testGetCategoryProjects() throws JSONException {
		String id = "1";
		JSONObject obj_1 = Client.sendRequest("GET", Const.BASE_URL, "categories"+"/"+id+"/projects", "");
		JSONArray obj_2 = obj_1.getJSONArray("projects");
		assertEquals(0, obj_2.length());
	}

	@Test
	void testPostCategoryProjects() throws JSONException {
		JSONObject category_obj = Client.sendRequest("POST", Const.BASE_URL, "categories", Const.CATEGORY_PARAM2);
		String idcategory = (String) category_obj.get(Const.ID);
		JSONObject proj_obj = Client.sendRequest("POST", Const.BASE_URL, "projects", Const.PROJECT_PARAM1);
		String idproject = (String) proj_obj.get(Const.ID);

		String paramID =  ("{\""+ Const.ID +"\": \""+ idproject +"\"}");

		Client.sendRequest("POST", Const.BASE_URL, "categories/"+idcategory+"/projects", paramID);
		JSONObject obj_2 = Client.sendRequest("GET", Const.BASE_URL, "categories/"+idcategory+"/projects", "");
		JSONArray projects = obj_2.getJSONArray("projects");
		assertEquals(1, projects.length());

		Client.sendRequest("DELETE", Const.BASE_URL, "categories/"+idcategory+"/projects/"+idproject, "");
		Client.sendRequest("DELETE", Const.BASE_URL, "projects/"+idproject, "");
	}
}
