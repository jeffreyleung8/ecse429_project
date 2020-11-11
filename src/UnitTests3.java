import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class UnitTests3 {
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
    void test_todo_query() throws JSONException {
        String title = "scan%20paperwork";
        int code = Client.getResponseCode("GET", Const.BASE_URL, "todos?title="+title, "");
        assertEquals(200, code);
    }

    // =====================/projects====================
    @Test
    void test_project_query() throws JSONException {
        String title = "Office%20Work";
        int code = Client.getResponseCode("GET", Const.BASE_URL, "projects?title="+title, "");
        assertEquals(200, code);
    }

    // =====================/categories====================
    @Test
    void test_category_query() throws JSONException {
        String title = "Home";
        int code = Client.getResponseCode("GET", Const.BASE_URL, "categories?title=" + title, "");
        assertEquals(200, code);
    }
}
