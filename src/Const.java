/***
 * Class containing all the constant variables for the testing
 * @author Sophie and Jeffrey
 *
 */
public class Const {
	final static String BASE_URL = "http://localhost:4567/";
	final static String COMMAND = "java -jar ./runTodoManagerRestAPI-1.5.5.jar";
	final static int OK_CODE = 200;
	final static int CREATED_CODE = 201;
	final static int NOT_FOUND = 404;
	
	// parameters to use
	final static String TITLE_S = "title";
	final static String ID_S = "id";
	final static String DONESTATUS_S = "doneStatus";
	final static String DESCRIPTION_S = "description";
	final static String TITLE1 = "TestTODO1";
	final static String TITLE2 = "TestTODO2";
	final static String STATUS1 = "true";
	final static String STATUS2 = "false";
	final static String DESCRIPTION = "none";
	
	final static String TODO_PARAM1 = ("{" 
			+ TITLE_S + ": " + TITLE1 + "," 
			+ DONESTATUS_S + ": " + STATUS1 + ","
			+ DESCRIPTION_S + ": " + DESCRIPTION
			+ "}");
	
	final static String TODO_PARAM2 = ("{" 
			+ TITLE_S + ": " + TITLE2 + "," 
			+ DONESTATUS_S + ": " + STATUS2 + ","
			+ DESCRIPTION_S + ": " + DESCRIPTION
			+ "}");
	
	final static String PROJECT_PARAM1 = ("{" 
			+ TITLE_S + ": " + "Project1"
			+ "}");
}
