import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Scanner {

    final static String todosFile = "src/main/resources/todos.csv";
    final static String projectsFile = "src/main/resources/projects.csv";
    final static String categoriesFile = "src/main/resources/categories.csv";

    public static ArrayList<String []> readCSV(String csvFile) {
        String line = "";
        String cvsSplitBy = ",";

        ArrayList <String[]> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] data = line.split(cvsSplitBy);
                list.add(data);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
