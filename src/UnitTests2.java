import static org.junit.jupiter.api.Assertions.*;

import junit.framework.Assert;
import org.junit.jupiter.api.*;
import org.json.JSONException;
import org.json.JSONObject;

@TestMethodOrder(MethodOrderer.Random.class)
class UnitTests2 {
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
    void test_get_todo_valid_id_return_code() throws JSONException {
        String id = "1";
        int code  = Client.getResponseCode("GET", Const.BASE_URL, "todos"+"/"+id, "");
        assertEquals(200, code);
    }

    @Test
    void test_get_todo_invalid_id_return_code() throws JSONException {
        String id = "-1";
        int code  = Client.getResponseCode("GET", Const.BASE_URL, "todos"+"/"+id, "");
        assertEquals(404, code);
    }

    @Test
    void test_post_todo_return_code() throws JSONException {
        int code = Client.getResponseCode("POST", Const.BASE_URL, "todos", Const.TODO_PARAM1);
        assertEquals(201, code);
    }

    // =====================/todos/:id====================
    @Test
    void test_put_todo_return_code() throws JSONException {
        JSONObject obj = Client.sendRequest("POST", Const.BASE_URL, "todos", Const.TODO_PARAM1);
        String id = (String) obj.get(Const.ID);

        // Change title and status and assert info
        int code = Client.getResponseCode("PUT", Const.BASE_URL, "todos/" + id, Const.TODO_PARAM2);
        assertEquals(200, code);
    }

    @Test
    void test_delete_todo_valid_id_return_code() throws JSONException {
        JSONObject obj = Client.sendRequest("POST", Const.BASE_URL, "todos", Const.TODO_PARAM1);
        String id = (String) obj.get(Const.ID);

        int code = Client.getResponseCode("DELETE", Const.BASE_URL, "todos/"+id, "");
        assertEquals(200, code);
    }

    @Test
    void test_delete_todo_invalid_id_return_code() throws JSONException {
        String id = "-1";
        int code = Client.getResponseCode("DELETE", Const.BASE_URL, "todos/"+id, "");
        assertEquals(404, code);
    }

    // =====================/todos/:id/tasksof====================
    @Test
    void test_post_todo_projects_return_code() throws JSONException {
        JSONObject proj_obj = Client.sendRequest("POST", Const.BASE_URL, "projects", Const.PROJECT_PARAM1);
        String idproject = (String) proj_obj.get(Const.ID);
        JSONObject todo_obj = Client.sendRequest("POST", Const.BASE_URL, "todos", Const.TODO_PARAM1);
        String idtodo = (String) todo_obj.get(Const.ID);

        String paramID = ("{\""+ Const.ID +"\": \""+ idproject +"\"}");

        int code = Client.getResponseCode("POST", Const.BASE_URL, "todos/"+idtodo+"/tasksof", paramID);
        assertEquals(201, code);
    }
    // =====================/todos/:id/categories====================
    @Test
    void test_post_todo_categories_return_code() throws JSONException {
        JSONObject cat_obj = Client.sendRequest("POST", Const.BASE_URL, "categories", Const.CATEGORY_PARAM2);
        String idcategory = (String) cat_obj.get(Const.ID);
        JSONObject todo_obj = Client.sendRequest("POST", Const.BASE_URL, "todos", Const.TODO_PARAM1);
        String idtodo = (String) todo_obj.get(Const.ID);

        String paramID = "{" + Const.ID + ":" + "\"" + idcategory +"\"" +"}";

        int code = Client.getResponseCode("POST", Const.BASE_URL, "todos/"+idtodo+"/categories", paramID);
        assertEquals(201, code);
    }

    // =====================/projects====================
    @Test
    void test_get_all_projects_return_code() throws JSONException {
        int code = Client.getResponseCode("GET", Const.BASE_URL, "projects", "");
        assertEquals(200, code);
    }

    // =====================/projects/:id====================
    @Test
    void test_get_project_valid_id_return_code() throws JSONException {
        String id = "1";
        int code  = Client.getResponseCode("GET", Const.BASE_URL, "projects"+"/"+id, "");
        assertEquals(200, code);
    }

    @Test
    void test_get_project_invalid_id_return_code() throws JSONException {
        String id = "-1";
        int code = Client.getResponseCode("GET", Const.BASE_URL, "projects"+"/"+id, "");
        assertEquals(404, code);
    }

    @Test
    void test_post_project_return_code() throws JSONException {
        int code = Client.getResponseCode("POST", Const.BASE_URL, "projects", Const.PROJECT_PARAM1);
        assertEquals(201, code);
    }

    @Test
    void test_delete_project_valid_id_return_code() throws JSONException {
        JSONObject obj_1= Client.sendRequest("POST", Const.BASE_URL, "projects", Const.PROJECT_PARAM2);
        assertEquals(Const.PROJECT_TITLE_2, obj_1.get(Const.TITLE));
        String id = (String) obj_1.get(Const.ID);

        int code = Client.getResponseCode("DELETE", Const.BASE_URL, "projects/"+id, "");
        assertEquals(200, code);
    }

    @Test
    void test_delete_project_invalid_id_return_code() throws JSONException {
        String id = "-1";
        int code = Client.getResponseCode("DELETE", Const.BASE_URL, "projects/"+id, "");
        assertEquals(404, code);
    }

    // =====================/projects/:id/task====================
    @Test
    void test_get_project_tasks_return_code() throws JSONException {
        String id = "1";
        int code = Client.getResponseCode("GET", Const.BASE_URL, "projects"+"/"+id+"/tasks", "");
        assertEquals(200, code);
    }

    @Test
    void test_post_project_tasks_return_code() throws JSONException {
        JSONObject proj_obj = Client.sendRequest("POST", Const.BASE_URL, "projects", Const.PROJECT_PARAM1);
        String idproject = (String) proj_obj.get(Const.ID);
        JSONObject todo_obj = Client.sendRequest("POST", Const.BASE_URL, "todos", Const.TODO_PARAM1);
        String idtodo = (String) todo_obj.get(Const.ID);

        String paramID =  ("{\""+ Const.ID +"\": \""+ idtodo +"\"}");

        int code = Client.getResponseCode("POST", Const.BASE_URL, "projects/"+idproject+"/tasks", paramID);
        assertEquals(201, code);
    }


    // =====================/categories====================
    @Test
    void test_get_categories_return_code() throws JSONException {
        int code = Client.getResponseCode("GET", Const.BASE_URL, "categories", "");
        assertEquals(200, code);

    }

    // =====================/categories/:id====================

    @Test
    void test_get_categories_valid_id_return_code() throws JSONException {
        String id = "1";
        int code = Client.getResponseCode("GET", Const.BASE_URL, "categories"+"/"+id, "");
        assertEquals(200, code);
    }

    @Test
    void test_get_categories_invalid_id_return_code() throws JSONException {
        String id = "-1";
        int code = Client.getResponseCode("DELETE", Const.BASE_URL, "categories/"+id, "");
        assertEquals(404, code);
    }

    @Test
    void test_post_new_category_return_code() throws JSONException {
        int code = Client.getResponseCode("POST", Const.BASE_URL, "categories", Const.CATEGORY_PARAM3);
        assertEquals(201, code);
    }

    @Test
    void test_delete_category_valid_id_return_code() throws JSONException {
        JSONObject obj_1= Client.sendRequest("POST", Const.BASE_URL, "categories", Const.CATEGORY_PARAM2);
        assertEquals(Const.CATEGORY_TITLE_2, obj_1.get(Const.TITLE));
        String id = (String) obj_1.get(Const.ID);

        int code = Client.getResponseCode("DELETE", Const.BASE_URL, "categories/"+id, "");
        assertEquals(200, code);
    }

    @Test
    void test_delete_category_invalid_id_return_code() throws JSONException {
        String id = "-1";
        int code = Client.getResponseCode("DELETE", Const.BASE_URL, "categories/"+id, "");
        assertEquals(404, code);
    }

    // =====================/categories/:id/projects====================
    @Test
    void test_get_category_projects_return_code() throws JSONException {
        String id = "1";
        int code = Client.getResponseCode("GET", Const.BASE_URL, "categories"+"/"+id+"/projects", "");
        assertEquals(200, code);
    }

    @Test
    void test_post_category_projects_return_code() throws JSONException {
        JSONObject category_obj = Client.sendRequest("POST", Const.BASE_URL, "categories", Const.CATEGORY_PARAM2);
        String idcategory = (String) category_obj.get(Const.ID);
        JSONObject proj_obj = Client.sendRequest("POST", Const.BASE_URL, "projects", Const.PROJECT_PARAM1);
        String idproject = (String) proj_obj.get(Const.ID);

        String paramID =  ("{\""+ Const.ID +"\": \""+ idproject +"\"}");

        int code = Client.getResponseCode("POST", Const.BASE_URL, "categories/"+idcategory+"/projects", paramID);
        assertEquals(201, code);
    }
    
    // =====================line queries /todo====================
    @Test
    void test_todo_query() throws JSONException {
        String title = "scan%20paperwork";
        int code = Client.getResponseCode("GET", Const.BASE_URL, "todos?title="+title, "");
        assertEquals(200, code);
    }

    // =====================line queries /projects====================
    @Test
    void test_project_query() throws JSONException {
        String title = "Office%20Work";
        int code = Client.getResponseCode("GET", Const.BASE_URL, "projects?title="+title, "");
        assertEquals(200, code);
    }

    // =====================line queries /categories====================
    @Test
    void test_category_query() throws JSONException {
        String title = "Home";
        int code = Client.getResponseCode("GET", Const.BASE_URL, "categories?title=" + title, "");
        assertEquals(200, code);
    }
}
