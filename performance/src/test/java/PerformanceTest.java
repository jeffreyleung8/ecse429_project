import org.junit.Test;
import java.io.File;
/***
 * Class testing the performance of the rest service
 * @author Jeffrey
 *
 */
public class PerformanceTest {

    int[] size_dt = new int[]{10, 100, 1000};
    String[] classes = new String[]{Const.TODO, Const.PROJECT, Const.CATEGORY};
    String[] request_type = new String[]{Const.ADD, Const.CHANGE, Const.DELETE};
    Controller controller = new Controller();

    private void start_rest_service() throws Exception{
        if(Client.testConnection(Const.BASE_URL)) {
            System.out.println("Start\tSystem is ready");
        }
        else {
            System.out.println("Error\tlocalhost:4567 is not ready");
            System.out.println("Start\tStarting the System Now");
            while(!Client.testConnection(Const.BASE_URL)) {
                Runtime rt = Runtime.getRuntime();
                String path = new File("runTodoManagerRestAPI-1.5.5.jar").getAbsolutePath();
                rt.exec("java -jar " + path);
            }
        }
    }

    private void end_rest_service() throws Exception{
        Client.shutDown(Const.BASE_URL);
        Runtime rt = Runtime.getRuntime();
        String path = new File("runTodoManagerRestAPI-1.5.5.jar").getAbsolutePath();
        rt.exec("java -jar "+ path);
    }

    @Test
    public void test_performance() throws Exception{

        Output.create_csv();
        for(int size : size_dt){

            //Restart service
            start_rest_service();

            for(String c : classes){
                for(String type : request_type){
                    String[] results = controller.measure_performance(size, type, c);
                    String[] data = new String[]{c, type, String.valueOf(size), results[0], results[1], results[2], results[3]};
                    Output.write_to_csv(data, true);
                }
            }
            //End service
            end_rest_service();
        }
    }
}
