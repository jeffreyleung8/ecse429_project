import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
/***
 * Class containing methods to write in csv file
 * @author Jeffrey
 *
 */
public class Output {

    private static final String filename = "src/test/java/performance.csv";
    private static final String[] labels = new String[]{"class", "size", "request type", "single request time (ns)"};

    public static void create_csv(){
        write_to_csv(labels, false);
    }

    public static void write_to_csv(String[] data, Boolean append){
        if (data.length == 0) return;
        try (FileWriter f = new FileWriter(filename, append);
             PrintWriter pw = new PrintWriter(f)) {
                StringBuilder line = new StringBuilder(data[0]);
                for (int i = 1; i < data.length; i++) {
                    line.append(",").append(data[i]);
                }
                pw.println(line);
        } catch (IOException ignored) {}
    }
}
