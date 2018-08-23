package JoS.JAVAProjects;

import java.sql.*;
import java.text.ParseException;

import JoS.JAVAProjects.ApplicationParams;

public class ExecuteAutomation {

	private ResultSet rBusinessFlow = null;
	private String sBusinessFlow = null;
	int getCurrentTaskIDInstance = 0;
	private ResultSet rTaskId = null;
	private boolean testcasefail = false;
	ApplicationParams gOBJ=null;//=
	ExecuteAutomation(ApplicationParams gOBJTemp)
	{
		gOBJ=gOBJTemp;
	}
	/**
	 * Function to initiate execution of test id iteration
	 * Parameters : Nil
	 * @return = Nil(void)
	 * Description
	 * 		Sets the web driver based on the global variable webdriver variable
	 * 		Reads the xml data and stores it into map
	 * 		Starts the business flow 
	 * @throws ParseException 
	 */
	public void GlobalIterationRun() throws ParseException{
		DebugInterface di=new DebugInterface(gOBJ);
		TestDataReader ReadData=new TestDataReader(gOBJ);
		for (int i=0;i< gOBJ.getgTestIterationCount();i++){
			ReadData.ReadTestData(gOBJ.getmTestID(), gOBJ.getgCurrentTestIteration());
			start_Business_Flow_Exec();
			if (testcasefail) {
				di.Debug ("###Test Case Level Error occurred for test id - "+gOBJ.getmTestID());
				di.Debug ("Exiting the Test Case Execution");
				//new KillProcess().CleanUpProcess();
				//gOBJ.getWbDriver().quit();
				//break;
				//ExecuteBusinessFlow("PC_Logout");				
				if(gOBJ.getgStrTester().equalsIgnoreCase("Automated Trigger")){
					di.Debug ("Closing browser/webdriver due to test case failure");
					gOBJ.getWbDriver().quit();
				}
				testcasefail = false;
			}
			gOBJ.setgCurrentTestIteration(gOBJ.getgCurrentTestIteration() + 1);
		}
		new Utils(gOBJ).SetWebDriver();

	}
	/**
	 * Gets the business flow details from the Business Flow table
	 * parameter - nil
	 * @return(success scenario) = businessflow(String)
	 * @return(failure scenario) = null (String)
	 */

	private String getBusinessFlow(){
		DebugInterface di=new DebugInterface(gOBJ);
		try{

			di.Debug ("Getting business flow details for the application");
			DB_Handler dbConnection=new DB_Handler(gOBJ);
			rBusinessFlow = dbConnection.ExecuteQuery(gOBJ.getBusiness_Flow_Query() +"'"+gOBJ.getmTestID()+"'");
			rBusinessFlow.first();
			di.Debug ("Retrieved the value from the database");
			sBusinessFlow = rBusinessFlow.getString("FLOW");
			di.Debug ("Business Flow retrieved from the database - "+sBusinessFlow);
			dbConnection.CloseConnection();
			return sBusinessFlow;
		}
		catch(SQLException se){
			di.Debug ("No values retrieved for the query - "+gOBJ.getBusiness_Flow_Query()+"'"+gOBJ.getmTestID()+"'" + "message:: - " + se);
			//new Utils().ReportStepToResultDB("No values retrieved for the query - "+gOBJ.Business_Flow_Query+"'"+gOBJ.gTestID+"'", "Fail");
			testcasefail = false;
			return null;
		}

	}
	/**
	 * Initiates the execution of the test case by individually passing business flow components 
	 * Obtains the number of instances for a particular task id and retrieves the values from xml
	 * Initiates the business flow  by providing individual business task ids
	 * parameter - nil
	 * @return - nil
	 * @throws ParseException 
	 * 
	 */
	private void start_Business_Flow_Exec() throws ParseException{

		String sBusFlow = null;
		DebugInterface di=new DebugInterface(gOBJ);
		di.Debug ("--------------------Execution of Business Flow starts ------------------");

		sBusFlow = getBusinessFlow();
		String[] arrBusFlows = sBusFlow.split("#");

		di.Debug ("------------------Iteration for business flow execution starts ------------");
		if(!testcasefail){
			for (int i= 0; i<arrBusFlows.length; i++){
				di.Debug ("Calling function start_Business_Flow_Exec() for task ID:: - "+arrBusFlows[i]);
				getCurrentTaskIDInstance = GetCurrentTaskIDInstance(i,arrBusFlows[i]);
				/* 
				 * Code need to be inserted for the formatting string
				 */
				ExecuteBusinessFlow(arrBusFlows[i]);
				if (testcasefail) {
					di.Debug ("Execute Automation:: Failure encountered for task ID:: - "+arrBusFlows[i]);
					di.Debug ("*****Exiting the iteration*****");
					//new KillProcess().CleanUpProcess();
					//gOBJ.getWbDriver().quit();
					break;
				}
			}
		}
	}
	/**
	 * Getting the instance of current task id, to retrieve the value from the xml
	 * @param sStart
	 * @param sValue
	 * @return = number of instances in the businessflow list
	 */
	private int GetCurrentTaskIDInstance(int sStart, String sValue){
		DebugInterface di=new DebugInterface(gOBJ);
		di.Debug ("In the Function GetCurrentTaskID for the task id - "+sValue);
		int iIndex =0;
		String[] arrBusFlows = sBusinessFlow.split("#");	
		for(int iCounter =0;iCounter<sStart;iCounter++){
			if (arrBusFlows[iCounter].equalsIgnoreCase(sValue)){
				iIndex++;
			}
		}
		di.Debug ("In the Function GetCurrentTaskID with return value "+iIndex+" for the task id- "+sValue);
		return iIndex;

	}
	/**
	 * This function retrieves the details from the Task_Definition table and initiates the page wise 
	 * execution.
	 * 
	 * @param sTaskID
	 * @throws ParseException 
	 */
	private void ExecuteBusinessFlow(String sTaskID) throws ParseException{
		DebugInterface di=new DebugInterface(gOBJ);
		di.Debug ("In the Function ExecuteBusinessFlow in the file ExecuteAutomation.java ");
		ResultSet robjDetails = null;
		DB_Handler dbConnection=new DB_Handler(gOBJ);
		try{
			/*
			 * Task_ID_Query 	= query to pull the values from the Task Definition Table
			 * Task_ID_Query	= SELECT * FROM Task_Definition where Task_ID = '" & pStrTaskID & "' order by Execution_Order
			 */
			// getting the details of the values from the 	
			DoScreenActions screenActions=	new DoScreenActions(gOBJ);

			rTaskId =  dbConnection.ExecuteQuery(gOBJ.getTask_ID_Query() + "'" + sTaskID + "' order by Execution_Order");
			int rowCount = getRowCount(rTaskId);
			if (rowCount!=0) {
				di.Debug ("The total steps received for Task id --" + sTaskID + ":::" + rowCount);
				//rTaskId.beforeFirst();
				if(rTaskId.next())
				{
					for (int i=0;i<rowCount;i++){
						di.Debug ("****************************************************");
						di.Debug ("****************************************************");
						di.Debug ("Reading Task ID and its steps details - Loop - " + i);
						di.Debug ("****************************************************");
						di.Debug ("****************************************************");
						di.Debug ("The total steps received for Task id --" + sTaskID + ":::" + rowCount);
						gOBJ.setgIntExecOrder(rTaskId.getString("Execution_Order"));
						gOBJ.setgStrLogicalID(rTaskId.getString("Logical_ID"));
						gOBJ.setgObjAction(rTaskId.getString("Obj_Action"));
						gOBJ.setgStrDbVariableField(rTaskId.getString("Variable_Field"));
						gOBJ.setgStrTaskID(sTaskID);


						screenActions.sAction = gOBJ.getgObjAction();
						screenActions.sVariablefromDB = gOBJ.getgStrDbVariableField();


						di.Debug ("gStrTaskID:" + sTaskID);
						di.Debug ("gIntExecOrder:" + gOBJ.getgIntExecOrder());
						di.Debug ("gStrLogicalID:" + gOBJ.getgStrLogicalID());
						di.Debug ("gObjAction:" + gOBJ.getgObjAction());
						di.Debug ("gStrDbVariableField:" + gOBJ.getgStrDbVariableField());
						//di.Debug ("VarType(gStrDbVariableField): " + gOBJ.getgStrDbVariableField());

						/*
						 * call format parameter function or java class file to retrieve the value from DB and format as per need
						 */
						//gOBJ.gStrDbVariableFieldValue = FormatInputParameters.FormatInputOutputData(gOBJ.gStrDbVariableField, getCurrentTaskIDInstance);
						new FormatInputParameters(gOBJ).FormatInputOutputData(gOBJ.getgStrDbVariableField(), getCurrentTaskIDInstance,screenActions);
						/*
						 * This function retrieves the value from the objRepositoryDb
						 * OBJ_REP_QUERY = SELECT * FROM Obj_Repository where Logical_ID = '" & gStrLogicalID & "'
						 */
						di.Debug ("screenActions.teststepfail: " + screenActions.teststepfail);
						//if (!(screenActions.teststepfail)){
						if (!(testcasefail)){
							if ((gOBJ.getgObjAction().toUpperCase() == "TEXTBOX_ENTER_DATA" ||
									gOBJ.getgObjAction().toUpperCase() == "LIST_SELECT" ||
									gOBJ.getgObjAction().toUpperCase() == "TEXTBOX_ENTER_DATA_OPTIONAL" ||
									gOBJ.getgObjAction().toUpperCase() == "LIST_SELECT_OPTIONAL")
									&&	(screenActions.sValuefromTestDataTable == "")) {
								//do nothing		'this has been designed to skip the ex_order if the value has not been mentioned or reference in the DB under val field
								di.Debug ("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@SKIPPING THE TASK FOR THIS EXECUTION ORDER :: " + gOBJ.getgIntExecOrder());
							} else{
								di.Debug ("Query for Locical ID: " + gOBJ.getgStrLogicalID());
								di.Debug ("Query in table >>>>>>>>>>>>>>>>>>>>>>>>>>>>>'Obj_Repository'");
								//'Read the record of the object repository based on the logical ID obtained from task ID for a particular execution order
								try{
									di.Debug ("Throwing query to obj rep table with :\n" + gOBJ.getOBJ_REP_QUERY() + "'" + gOBJ.getgStrLogicalID() + "' and page_name ='" + rTaskId.getString("Page_Name") + "';");
									robjDetails = dbConnection.ExecuteQuery(gOBJ.getOBJ_REP_QUERY() + "'" + gOBJ.getgStrLogicalID() + "' and page_name = '" + rTaskId.getString("Page_Name") + "';");
									if (robjDetails!= null){
										robjDetails.next();					
										gOBJ.setgObjBrowserName(robjDetails.getString("Application_Name"));
										gOBJ.setgObjPageName(robjDetails.getString("Page_Name"));
										gOBJ.setgObjFrame(robjDetails.getString("Frame"));
										gOBJ.setgObjDesc(robjDetails.getString("obj_desc"));
										gOBJ.setgObjMsg(robjDetails.getString("Msg"));

										screenActions.sLogicId = gOBJ.getgStrLogicalID();
										screenActions.sFrame = gOBJ.getgObjFrame();
										screenActions.sDef = gOBJ.getgObjDesc();
										//screenActions.sValuefromXML = gOBJ.gStrDbVariableField;
										screenActions.sMsg = gOBJ.getgObjMsg()	;

										di.Debug ("gObjBrowserName:" + gOBJ.getgObjBrowserName());
										di.Debug ("gObjPageName:" + gOBJ.getgObjPageName());
										di.Debug ("gObjFrame:" + gOBJ.getgObjFrame());
										di.Debug ("gObjDesc:" + gOBJ.getgObjDesc());
										di.Debug ("gObjMsg:" + gOBJ.getgObjMsg());
										di.Debug ("Calling DoScreenActivities()...");
										di.Debug ("Back to ExecuteAutomation.vbs from DoScreenActivities()...");
										di.Debug ("_________________________________________________________________________________________");
										di.Debug ("_________________________________________________________________________________________");

										screenActions.Do_Action();

										if (screenActions.teststepfail){
											gOBJ.setTestStepFailCount(gOBJ.getTestStepFailCount() + 1);
											if ((gOBJ.getgObjAction().toString().toLowerCase().contains("checktext")) || (gOBJ.getgObjAction().toString().toLowerCase().contains("wait"))){
												testcasefail = false;
											}else{
												testcasefail = true;														
											}
										}else{
											gOBJ.setTestSteppassCount(gOBJ.getTestSteppassCount() + 1);
										}

										if (screenActions.taskfail){
											di.Debug ("Task has failed");
											testcasefail = true;
											dbConnection.CloseConnection();
											return;
										}	
									} 
								}catch (SQLException ex) {
									dbConnection.CloseConnection();
									di.Debug ("Execute Automaiton:: Logical ID does not exist in object repository::" + gOBJ.getgStrLogicalID());
									new Utils(gOBJ).ReportStepToResultDB("Execute Automaiton:: Logical ID does not exist in object repository:::: EXITING TEST ITERATION:: - " + gOBJ.getgStrLogicalID(), "Fail");
									testcasefail = true;
									break;
								}
							}
						}
						di.Debug ("****************************************************");
						di.Debug (">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
						di.Debug ("****************************************************");
						gOBJ.setgStrDbVariableField(null);
						screenActions.sValuefromTestDataTable = null;
						screenActions.sVariablefromDB = null;
						rTaskId.next();
					}
				}
			}else{
				di.Debug ("Execute Automaiton:: Task ID does not exist::" + sTaskID);
				new Utils(gOBJ).ReportStepToResultDB("Task ID does not exist:: EXITING TEST ITERATION:: - " + sTaskID, "Fail");
			}
			dbConnection.CloseConnection();
		}
		catch (SQLException se){
			dbConnection.CloseConnection();
			di.Debug ("Task ID is not available in the Task Definition table::::xxxxxxxxxx " + sTaskID + "- message:: - " + se);
		}
		dbConnection.CloseConnection();
	}
	/**
	 * Get the number of rows for the result set
	 * @param resultSet
	 * @return
	 */
	int getRowCount(ResultSet resultSet) {

		if (resultSet == null) {
			return 0;
		}
		try {
			resultSet.last();
			return resultSet.getRow();
		} catch (SQLException exp) {
			exp.printStackTrace();
		} finally {
			try {
				resultSet.beforeFirst();
			} catch (SQLException exp) {
				exp.printStackTrace();
			}
		}
		return 0;
	}	
}
