import org.json.JSONObject;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/***
 * Class containing all the methods making requests to the backend server
 * @author Jeffrey
 *
 */
public class Controller {

    private String lastTodoId;
    private String lastProjectId;
    private String lastCategoryId;
    private String lastTodoTitle;
    private String lastProjectTitle;
    private String lastCategoryTitle;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public Controller(){}

    private String generate_random_string(){

        String characters = "abcdefghijklmnopqrstuvwxyz";
        int length = 10;
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int rndCharAt = random.nextInt(characters.length());
            char randomChar = characters.charAt(rndCharAt);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    // CREATE INSTANCE
    private JSONObject create_todo() throws Exception{
        JSONObject todo = new JSONObject();
        todo.put(Const.TITLE, generate_random_string());
        todo.put(Const.DONESTATUS, Boolean.parseBoolean("false"));
        todo.put(Const.DESCRIPTION, generate_random_string());
        return todo;
    }

    private JSONObject create_project() throws Exception{
        JSONObject obj = new JSONObject();
        obj.put(Const.TITLE, generate_random_string());
        obj.put(Const.COMPLETED, Boolean.parseBoolean("false"));
        obj.put(Const.ACTIVE, Boolean.parseBoolean("false"));
        obj.put(Const.DESCRIPTION, generate_random_string());
        return obj;
    }

    private JSONObject create_category() throws Exception{
        JSONObject obj = new JSONObject();
        obj.put(Const.TITLE, generate_random_string());
        obj.put(Const.DESCRIPTION, generate_random_string());
        return obj;
    }

    // ADD
    private void add_random(String class_name) throws Exception{
        JSONObject obj;
        switch(class_name) {
            case Const.TODO:
                JSONObject todo = create_todo();
                obj = Client.sendRequest("POST", Const.BASE_URL, "todos", todo.toString());
                lastTodoId = (String) obj.get(Const.ID);
                break;
            case Const.PROJECT:
                JSONObject project = create_project();
                obj = Client.sendRequest("POST", Const.BASE_URL, "projects", project.toString());
                lastProjectId = (String) obj.get(Const.ID);
                break;
            case Const.CATEGORY:
                JSONObject category = create_category();
                obj = Client.sendRequest("POST", Const.BASE_URL, "categories", category.toString());
                lastCategoryId = (String) obj.get(Const.ID);
                break;
        }
    }
    

    // CHANGE
    private void change_last(String class_name) throws Exception{
        switch(class_name) {
            case Const.TODO:
                JSONObject todo = create_todo();
                lastTodoTitle = (String) todo.get(Const.TITLE);
                Client.sendRequest("PUT", Const.BASE_URL, "todos/"+lastTodoId, todo.toString());
                break;
            case Const.PROJECT:
                JSONObject project = create_project();
                lastProjectTitle = (String) project.get(Const.TITLE);
                Client.sendRequest("PUT", Const.BASE_URL, "projects/"+lastProjectId, project.toString());
                break;
            case Const.CATEGORY:
                JSONObject category = create_category();
                lastCategoryTitle = (String) category.get(Const.TITLE);
                Client.sendRequest("PUT", Const.BASE_URL, "categories/"+lastCategoryId, category.toString());
                break;
        }
    }

    // DELETE
    private void delete_last(String class_name) throws Exception{
        switch(class_name) {
            case Const.TODO:
                Client.sendRequest("DELETE", Const.BASE_URL, "todos/"+lastTodoId, "");
                break;
            case Const.PROJECT:
                Client.sendRequest("DELETE", Const.BASE_URL, "projects/"+lastProjectId, "");
                break;
            case Const.CATEGORY:
                Client.sendRequest("DELETE", Const.BASE_URL, "categories/"+lastCategoryId, "");
                break;
        }
    }
    

    // INITIALIZE DATA
    private void initialize_data(int size, String class_name) throws Exception{
        for(int i = 0 ; i < size; i++){
            add_random(class_name);
        }
    }

    // CHECK REQUEST CORRECTNESS
    private void verify_request(String request_type, String class_name) throws Exception{
        String id = "";
        String title = "";
        JSONObject obj;
        switch(class_name) {
            case Const.TODO:
                id = lastTodoId;
                title = lastTodoTitle;
                break;
            case Const.PROJECT:
                id = lastProjectId;
                title = lastProjectTitle;
                break;
            case Const.CATEGORY:
                id = lastCategoryId;
                title = lastCategoryTitle;
                break;
        }

        switch(request_type){
            case Const.ADD:
                obj = Client.sendRequest("GET", Const.BASE_URL, class_name+"?id="+ id, "");
                String retrieved_id =  (String) obj.getJSONArray(class_name).getJSONObject(0).get(Const.ID);
                assertEquals(id, retrieved_id);
                break;
            case Const.CHANGE:
                obj = Client.sendRequest("GET", Const.BASE_URL, class_name+"?title="+ title, "");
                String retrieved_title =  (String) obj.getJSONArray(class_name).getJSONObject(0).get(Const.TITLE);
                assertEquals(title, retrieved_title);
                break;
            case Const.DELETE:
                int err = Client.getResponseCode("DELETE", Const.BASE_URL, class_name+"/"+ id, "");
                assertEquals(Const.NOT_FOUND, err);
                break;
        }
    }

    // MEASURE PERFORMANCE
    public String[] measure_performance(int size, String request_type, String class_name) throws Exception{

        OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long t1 = 0;
        long t2 = 0;
        double total_cpu = 0.0;
        double total_memory_usage = 0.0;

        long t1_start_time = System.nanoTime();

        //Initialize class_name
        initialize_data(size, class_name);

        // Do operation
        for(int i = 0; i < Const.NUM_SAMPLES; i++){

            long t2_start_time = System.nanoTime();
            switch(request_type){
                case Const.ADD:
                    add_random(class_name);
                    break;
                case Const.CHANGE:
                    change_last(class_name);
                    break;
                case Const.DELETE:
                    delete_last(class_name);
                    break;
            }

            long t2_end_time = System.nanoTime();
            t2 += t2_end_time - t2_start_time;
            total_cpu += operatingSystemMXBean.getProcessCpuLoad();
            total_memory_usage = (double) Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            // Check correctness
            verify_request(request_type, class_name);

            //Undo operation
            switch(request_type){
                case Const.ADD:
                    delete_last(class_name);
                    break;
                case Const.CHANGE:
                    delete_last(class_name);
                    add_random(class_name);
                    break;
                case Const.DELETE:
                    add_random(class_name);
                    break;
            }
        }

        long t1_end_time = System.nanoTime();
        t1 += t1_end_time - t1_start_time;

        //Calculate performance measures
        double avg_t1 = (double) t1;
        double avg_t2 = (double) t2 / Const.NUM_SAMPLES;
        String avg_cpu = df2.format((total_cpu / (double) Const.NUM_SAMPLES) * 100.0);
        String avg_memory = String.valueOf(total_memory_usage / (double) Const.NUM_SAMPLES);

        return new String[]{String.valueOf(avg_t1), String.valueOf(avg_t2), avg_cpu, avg_memory};
    }

}
