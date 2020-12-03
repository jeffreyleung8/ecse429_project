import org.json.JSONObject;
import java.security.SecureRandom;
/***
 * Class containing all the methods making requests to the backend server
 * @author Jeffrey
 *
 */
public class Controller {

    public String lastTodoId;
    public String lastProjectId;
    public String lastCategoryId;

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
    public void add_random(String class_name) throws Exception{
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
    public void change_last(String class_name) throws Exception{
        switch(class_name) {
            case Const.TODO:
                JSONObject todo = create_todo();
                Client.sendRequest("PUT", Const.BASE_URL, "todos/"+lastTodoId, todo.toString());
                break;
            case Const.PROJECT:
                JSONObject project = create_project();
                Client.sendRequest("PUT", Const.BASE_URL, "projects/"+lastProjectId, project.toString());
                break;
            case Const.CATEGORY:
                JSONObject category = create_category();
                Client.sendRequest("PUT", Const.BASE_URL, "categories/"+lastCategoryId, category.toString());
                break;
        }
    }

    // DELETE
    public void delete_last(String class_name) throws Exception{
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
    public void initialize_data(int size, String class_name) throws Exception{
        for(int i = 0 ; i < size; i++){
            add_random(class_name);
        }
    }

    // MEASURE PERFORMANCE
    public long measure_performance(int size, String request_type, String class_name) throws Exception{

        long total_time = 0;

        //Initialize class_name
        initialize_data(size, class_name);

        // Do operation
        for(int i = 0; i < Const.NUM_SAMPLES; i++){

            long start_time = System.nanoTime();
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

            long end_time = System.nanoTime();
            total_time += end_time - start_time;

            //Undo operation
            switch(request_type){
                case Const.ADD:
                    delete_last(class_name);
                    break;
                case Const.CHANGE:
                    break;
                case Const.DELETE:
                    add_random(class_name);
                    break;
            }
        }
        
        //Calculate average time of operation
        long avg_time = total_time / Const.NUM_SAMPLES;
        return avg_time;
    }

}
