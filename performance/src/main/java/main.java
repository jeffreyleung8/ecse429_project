import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class main {

    public static void main(String[] args) throws Exception{

        ArrayList<String[]> todosData = Scanner.readCSV(Scanner.todosFile);
        ArrayList<String[]> projectsData = Scanner.readCSV(Scanner.projectsFile);
        ArrayList<String[]> categoriesData = Scanner.readCSV(Scanner.categoriesFile);

        Client.returnCode = Const.OK_CODE;
        if(Client.testConnection(Const.BASE_URL)) {
            System.out.println("Start\tSystem is ready");
        } else {
            System.out.println("Error\tlocalhost:4567 is not ready");
            System.out.println("Start\tStarting the System Now");
            Runtime rt = Runtime.getRuntime();
            rt.exec(Const.COMMAND);
        }

        // Post todos data
        for(String[] todo : todosData){
            JSONObject obj = new JSONObject();
            obj.put(Const.TITLE, todo[0]);
            obj.put(Const.DONESTATUS, Boolean.parseBoolean(todo[1]));
            obj.put(Const.DESCRIPTION, todo[2]);
            Client.sendRequest("POST", Const.BASE_URL, "todos", obj.toString());
        }

        // Post projects data
        for(String[] project : projectsData){
            JSONObject obj = new JSONObject();
            obj.put(Const.TITLE, project[0]);
            obj.put(Const.COMPLETED, Boolean.parseBoolean(project[1]));
            obj.put(Const.ACTIVE, Boolean.parseBoolean(project[2]));
            obj.put(Const.DESCRIPTION, project[3]);
            Client.sendRequest("POST", Const.BASE_URL, "projects", obj.toString());
        }

        // Post categories data
        for(String[] category : categoriesData){
            JSONObject obj = new JSONObject();
            obj.put(Const.TITLE, category[0]);
            obj.put(Const.DESCRIPTION, category[1]);
            Client.sendRequest("POST", Const.BASE_URL, "categories", obj.toString());
        }

    }

}
