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
	final static String TITLE = "title";
	final static String ID = "id";

	//Todos
	final static String DONESTATUS_S = "doneStatus";
	final static String DESCRIPTION_S = "description";
	final static String TITLE1 = "TestTODO1";
	final static String TITLE2 = "TestTODO2";
	final static String TRUE = "true";
	final static String FALSE = "false";
	final static String DESCRIPTION = "none";
	final static String TODO_PARAM1 = ("{" + TITLE + ": " + TITLE1 + "," + DONESTATUS_S + ": " + TRUE + "," + DESCRIPTION_S + ": " + DESCRIPTION + "}");
	final static String TODO_PARAM2 = ("{" + TITLE + ": " + TITLE2 + "," + DONESTATUS_S + ": " + FALSE + "," + DESCRIPTION_S + ": " + DESCRIPTION + "}");

	//Projects
	final static String PROJECT_TITLE_0 = "Office Work";
	final static String PROJECT_TITLE_1 = "School Work";
	final static String PROJECT_TITLE_2 = "Laboratory Work";
	final static String PROJECT_PARAM1 =  ("{\""+ TITLE +"\": \""+ PROJECT_TITLE_1 +"\"}");
	final static String PROJECT_PARAM2 =  ("{\""+ TITLE +"\": \""+ PROJECT_TITLE_2 +"\"}");

	// Categories
	final static String CATEGORY_TITLE_0 = "Office";
	final static String CATEGORY_TITLE_1 = "Home";
	final static String CATEGORY_TITLE_2 = "School";
	final static String CATEGORY_TITLE_3 = "Hospital";
	final static String CATEGORY_PARAM2 =  ("{\""+ TITLE +"\": \""+ CATEGORY_TITLE_2 +"\"}");
	final static String CATEGORY_PARAM3 =  ("{\""+ TITLE +"\": \""+ CATEGORY_TITLE_3 +"\"}");
}
