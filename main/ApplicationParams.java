package JoS.JAVAProjects;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

//import javax.swing.JOptionPane;







import org.openqa.selenium.WebDriver;

import JoS.JAVAProjects.Reporting.Report;

public class ApplicationParams {
	private String gDrive; // = "F:\\CorpHQ\\CHQ Performance Engineering\\Internal\\";
	private String ResultPath;
	private String gNumRunID;
	private String strProjectName = null;
	public String getStrProjectName() {
		return strProjectName;
	}
	public void setStrProjectName(String strProjectName) {
		this.strProjectName = strProjectName;
	}
	private String gStrTester; 
	public String getgStrTester() {
		return gStrTester;
	}
	public void setgStrTester(String gStrTester) {
		this.gStrTester = gStrTester;
	}
	private int TestStepFailCount = 0;	
	private int TestSteppassCount = 0;	
	private String gSubLocator;	
	private String gTestSet;
	private String mTestID;	
	public Report rep;
	private String gStrDebugLogPath; // "\Run_mm-dd-yyyy_hh-mm-ss_XX";
	private String gStrDebugFileFullPath;
	private String gDebugFileName;
	private String gStrAutomationPath ; //= gDrive + "JoS\\";
	private String gStrAutomationPathNew;
	private File gDebugFileOut;
	private String gTestName;
	private BufferedWriter bufferWriter;
	private String gStrReportName;	//contains the file name for the report;
	private String gStrXMLPath, gStrXMLPathEnv;
	private String gResultFlag;
	private String gStepCount;
	private String gObjBrowser;
	private String gObjPage;
	private String gObjParentHierarchy;
	private String gStrReleaseFolder;
	private String gStrBrowserHandle;
	private String gStrPageHandle;
	private String gStrTestModule;
	private String gDescProgStr;
	private String gStrDbValueFieldValue; //to store the value of the tag for each execution order;
	private int gCurrentTestIteration = 1;
	private boolean gStrTimeToExit = false;
	private String Folder;
	private String Files;
	private String File;
	private int gTestIterationCount=0;	//store the test script total iteration value
	private String gEnvironmentToTest;	//to read and store environment to point to for testing
	private String gURLInfo;	//to store URL record from DB after the query
	private String gStrBusinessFlow;	//to store business flow record from DB after the query
	private String gStrFlow;	//to store flow record from DB after the query
	private String gStrTaskDef;	//to store task definition record from DB after the query
	private String gStrLogicalID; 	//contains the logical ID
	private String gStrTaskID	= "NULL";	//Contains the task ID
	private String gIntExecOrder = "0";	//store the current value of execution order from DB
	private String gStrTaskIDArray;	//contains the split of task ID
	private String intTaskIDLoop;	//to loop the task ID
	private String gStrTaskIDByExOrd;	//to store the task ID based on execution order
	private String intLoopByExOrd;	//to loop in by execution order
	private String Split_gStrTaskIDByExOrd;	// to store one task id at a time (order by ex order)
	private String gStrObjDef;		//to store a record from obj def table for a particular Logical ID
	private String Split_gStrObjDef;	//to store the column values from obj def table for a particular Logical ID
	private String gStrDbVariableField;	//this is to store the input value of the particular task id (for a particular execution order)
	private String gObjAction;	//this is to store the action of the particular task id (for a particular execution order)
	private String gObjDefinition;	//to store the definition of an object of a logical ID
	private String gObjType;	//to store the value of an object type of a logical ID
	private String gStrMsg;	//to store the message of a logical ID
	private String gStrTaskStepInfo;	//to store the custom steps to be taken in the task ID
	private String gIntCurrentInstanceTaskID;
	private String getCurrentTestDataDir;
	private String gObjBrowserName;
	private String gObjPageName;
	private String gObjFrame;
	private String gObjDesc;
	private String gObjMsg;
	public Hashmap hm;
	private String NumberOfThreads; 

	public int getTestStepFailCount() {
		return TestStepFailCount;
	}
	public void setTestStepFailCount(int testStepFailCount) {
		TestStepFailCount = testStepFailCount;
	}
	public int getTestSteppassCount() {
		return TestSteppassCount;
	}
	public void setTestSteppassCount(int testSteppassCount) {
		TestSteppassCount = testSteppassCount;
	}

	public String getNumberOfThreads() {
		return NumberOfThreads;
	}
	public void setNumberOfThreads(String numberOfThreads) {
		NumberOfThreads = numberOfThreads;
	}
	////***********************************************************
	////***********************************************************
	private Connection connextion;
	public Connection getConnextion() {
		return connextion;
	}
	public void setConnextion(Connection connextion) {
		this.connextion = connextion;
	}
	public WebDriver getWbDriver() {
		return wbDriver;
	}
	public void setWbDriver(WebDriver wbDriver) {
		if(wbDriver!=null)
			this.wbDriver = wbDriver;
	}
	private String OBJ_REP_QUERY = "SELECT * FROM obj_repository where Logical_ID = ";
	private String Business_Flow_Query = "SELECT * FROM business_flow where Test_ID like ";	//'42'";
	private String Task_ID_Query = "SELECT * FROM task_definition where Task_ID = ";
	private WebDriver wbDriver;
	private String ieDriverPath ; // =  gStrAutomationPath + "Servers\\IEDriverServer_Win32_2.45.0\\IEDriverServer.exe";
	private String chromeDriverPath; // = gStrAutomationPath + "Servers\\chromedriver_win32\\chromedriver.exe";
	private String gBrowserType; //= "ie";
	private String gTestSetID;
	private String serverpath;
	private String Dbname;
	private String port_number;
	private String sUserName;
	private String sPassword;
	private String connstring;	
	private String sql_db_name;
	private String ObjectLoadTime;
	private ApplicationParams gObj=null;

	public String getObjectLoadTime() {
		return ObjectLoadTime;
	}
	public void setObjectLoadTime(String objectLoadTime) {
		ObjectLoadTime = objectLoadTime;
	}
	public String getPageLoadTime() {
		return PageLoadTime;
	}
	public void setPageLoadTime(String pageLoadTime) {
		PageLoadTime = pageLoadTime;
	}
	private String PageLoadTime;

	public String getgDrive() {
		return gDrive;
	}
	public void setgDrive(String gDrive) {
		this.gDrive = gDrive;
	}
	public String getResultPath() {
		return ResultPath;
	}
	public void setResultPath(String resultPath) {
		ResultPath = resultPath;
	}
	public String getgNumRunID() {
		return gNumRunID;
	}
	public void setgNumRunID(String gNumRunID) {
		this.gNumRunID = gNumRunID;
	}
	public String getgSubLocator() {
		return gSubLocator;
	}
	public void setgSubLocator(String gSubLocator) {
		this.gSubLocator = gSubLocator;
	}
	public String getgTestSet() {
		return gTestSet;
	}
	public void setgTestSet(String gTestSet) {
		this.gTestSet = gTestSet;
	}
	public String getmTestID() {
		return mTestID;
	}
	public void setmTestID(String mTestID) {
		this.mTestID = mTestID;
	}
	public String getgStrDebugLogPath() {
		return gStrDebugLogPath;
	}
	public void setgStrDebugLogPath(String gStrDebugLogPath) {
		this.gStrDebugLogPath = gStrDebugLogPath;
	}
	public String getgStrDebugFileFullPath() {
		return gStrDebugFileFullPath;
	}
	public void setgStrDebugFileFullPath(String gStrDebugFileFullPath) {
		this.gStrDebugFileFullPath = gStrDebugFileFullPath;
	}
	public String getgDebugFileName() {
		return gDebugFileName;
	}
	public void setgDebugFileName(String gDebugFileName) {
		this.gDebugFileName = gDebugFileName;
	}
	public String getgStrAutomationPath() {
		return gStrAutomationPath;
	}
	public void setgStrAutomationPath(String gStrAutomationPath) {
		this.gStrAutomationPath = gStrAutomationPath;
	}
	public String getgStrAutomationPathNew() {
		return gStrAutomationPathNew;
	}
	public void setgStrAutomationPathNew(String gStrAutomationPathNew) {
		this.gStrAutomationPathNew = gStrAutomationPathNew;
	}
	public File getgDebugFileOut() {
		return gDebugFileOut;
	}
	public void setgDebugFileOut(File gDebugFileOut) {
		this.gDebugFileOut = gDebugFileOut;
	}
	public String getgTestName() {
		return gTestName;
	}
	public void setgTestName(String gTestName) {
		this.gTestName = gTestName;
	}
	public BufferedWriter getBufferWriter() {
		return bufferWriter;
	}
	public void setBufferWriter(BufferedWriter bufferWriter) {
		this.bufferWriter = bufferWriter;
	}
	public String getgStrReportName() {
		return gStrReportName;
	}
	public void setgStrReportName(String gStrReportName) {
		this.gStrReportName = gStrReportName;
	}
	public String getgStrXMLPath() {
		return gStrXMLPath;
	}
	public void setgStrXMLPath(String gStrXMLPath) {
		this.gStrXMLPath = gStrXMLPath;
	}
	public String getgStrXMLPathEnv() {
		return gStrXMLPathEnv;
	}
	public void setgStrXMLPathEnv(String gStrXMLPathEnv) {
		this.gStrXMLPathEnv = gStrXMLPathEnv;
	}
	public String getgResultFlag() {
		return gResultFlag;
	}
	public void setgResultFlag(String gResultFlag) {
		this.gResultFlag = gResultFlag;
	}
	public String getgStepCount() {
		return gStepCount;
	}
	public void setgStepCount(String gStepCount) {
		this.gStepCount = gStepCount;
	}
	public String getgObjBrowser() {
		return gObjBrowser;
	}
	public void setgObjBrowser(String gObjBrowser) {
		this.gObjBrowser = gObjBrowser;
	}
	public String getgObjPage() {
		return gObjPage;
	}
	public void setgObjPage(String gObjPage) {
		this.gObjPage = gObjPage;
	}
	public String getgObjParentHierarchy() {
		return gObjParentHierarchy;
	}
	public void setgObjParentHierarchy(String gObjParentHierarchy) {
		this.gObjParentHierarchy = gObjParentHierarchy;
	}
	public String getgStrReleaseFolder() {
		return gStrReleaseFolder;
	}
	public void setgStrReleaseFolder(String gStrReleaseFolder) {
		this.gStrReleaseFolder = gStrReleaseFolder;
	}
	public String getgStrBrowserHandle() {
		return gStrBrowserHandle;
	}
	public void setgStrBrowserHandle(String gStrBrowserHandle) {
		this.gStrBrowserHandle = gStrBrowserHandle;
	}
	public String getgStrPageHandle() {
		return gStrPageHandle;
	}
	public void setgStrPageHandle(String gStrPageHandle) {
		this.gStrPageHandle = gStrPageHandle;
	}
	public String getgStrTestModule() {
		return gStrTestModule;
	}
	public void setgStrTestModule(String gStrTestModule) {
		this.gStrTestModule = gStrTestModule;
	}
	public String getgDescProgStr() {
		return gDescProgStr;
	}
	public void setgDescProgStr(String gDescProgStr) {
		this.gDescProgStr = gDescProgStr;
	}
	public String getgStrDbValueFieldValue() {
		return gStrDbValueFieldValue;
	}
	public void setgStrDbValueFieldValue(String gStrDbValueFieldValue) {
		this.gStrDbValueFieldValue = gStrDbValueFieldValue;
	}
	public int getgCurrentTestIteration() {
		return gCurrentTestIteration;
	}
	public void setgCurrentTestIteration(int gCurrentTestIteration) {
		this.gCurrentTestIteration = gCurrentTestIteration;
	}
	public boolean isgStrTimeToExit() {
		return gStrTimeToExit;
	}
	public void setgStrTimeToExit(boolean gStrTimeToExit) {
		this.gStrTimeToExit = gStrTimeToExit;
	}
	public String getFolder() {
		return Folder;
	}
	public void setFolder(String folder) {
		Folder = folder;
	}
	public String getFiles() {
		return Files;
	}
	public void setFiles(String files) {
		Files = files;
	}
	public String getFile() {
		return File;
	}
	public void setFile(String file) {
		File = file;
	}
	public int getgTestIterationCount() {
		return gTestIterationCount;
	}
	public void setgTestIterationCount(int gTestIterationCount) {
		this.gTestIterationCount = gTestIterationCount;
	}
	public String getgEnvironmentToTest() {
		return gEnvironmentToTest;
	}
	public void setgEnvironmentToTest(String gEnvironmentToTest) {
		this.gEnvironmentToTest = gEnvironmentToTest;
	}
	public String getgURLInfo() {
		return gURLInfo;
	}
	public void setgURLInfo(String gURLInfo) {
		this.gURLInfo = gURLInfo;
	}
	public String getgStrBusinessFlow() {
		return gStrBusinessFlow;
	}
	public void setgStrBusinessFlow(String gStrBusinessFlow) {
		this.gStrBusinessFlow = gStrBusinessFlow;
	}
	public String getgStrFlow() {
		return gStrFlow;
	}
	public void setgStrFlow(String gStrFlow) {
		this.gStrFlow = gStrFlow;
	}
	public String getgStrTaskDef() {
		return gStrTaskDef;
	}
	public void setgStrTaskDef(String gStrTaskDef) {
		this.gStrTaskDef = gStrTaskDef;
	}
	public String getgStrLogicalID() {
		return gStrLogicalID;
	}
	public void setgStrLogicalID(String gStrLogicalID) {
		this.gStrLogicalID = gStrLogicalID;
	}
	public String getgStrTaskID() {
		return gStrTaskID;
	}
	public void setgStrTaskID(String gStrTaskID) {
		this.gStrTaskID = gStrTaskID;
	}
	public String getgIntExecOrder() {
		return gIntExecOrder;
	}
	public void setgIntExecOrder(String gIntExecOrder) {
		this.gIntExecOrder = gIntExecOrder;
	}
	public String getgStrTaskIDArray() {
		return gStrTaskIDArray;
	}
	public void setgStrTaskIDArray(String gStrTaskIDArray) {
		this.gStrTaskIDArray = gStrTaskIDArray;
	}
	public String getIntTaskIDLoop() {
		return intTaskIDLoop;
	}
	public void setIntTaskIDLoop(String intTaskIDLoop) {
		this.intTaskIDLoop = intTaskIDLoop;
	}
	public String getgStrTaskIDByExOrd() {
		return gStrTaskIDByExOrd;
	}
	public void setgStrTaskIDByExOrd(String gStrTaskIDByExOrd) {
		this.gStrTaskIDByExOrd = gStrTaskIDByExOrd;
	}
	public String getIntLoopByExOrd() {
		return intLoopByExOrd;
	}
	public void setIntLoopByExOrd(String intLoopByExOrd) {
		this.intLoopByExOrd = intLoopByExOrd;
	}
	public String getSplit_gStrTaskIDByExOrd() {
		return Split_gStrTaskIDByExOrd;
	}
	public void setSplit_gStrTaskIDByExOrd(String split_gStrTaskIDByExOrd) {
		Split_gStrTaskIDByExOrd = split_gStrTaskIDByExOrd;
	}
	public String getgStrObjDef() {
		return gStrObjDef;
	}
	public void setgStrObjDef(String gStrObjDef) {
		this.gStrObjDef = gStrObjDef;
	}
	public String getSplit_gStrObjDef() {
		return Split_gStrObjDef;
	}
	public void setSplit_gStrObjDef(String split_gStrObjDef) {
		Split_gStrObjDef = split_gStrObjDef;
	}
	public String getgStrDbVariableField() {
		return gStrDbVariableField;
	}
	public void setgStrDbVariableField(String gStrDbVariableField) {
		this.gStrDbVariableField = gStrDbVariableField;
	}
	public String getgObjAction() {
		return gObjAction;
	}
	public void setgObjAction(String gObjAction) {
		this.gObjAction = gObjAction;
	}
	public String getgObjDefinition() {
		return gObjDefinition;
	}
	public void setgObjDefinition(String gObjDefinition) {
		this.gObjDefinition = gObjDefinition;
	}
	public String getgObjType() {
		return gObjType;
	}
	public void setgObjType(String gObjType) {
		this.gObjType = gObjType;
	}
	public String getgStrMsg() {
		return gStrMsg;
	}
	public void setgStrMsg(String gStrMsg) {
		this.gStrMsg = gStrMsg;
	}
	public String getgStrTaskStepInfo() {
		return gStrTaskStepInfo;
	}
	public void setgStrTaskStepInfo(String gStrTaskStepInfo) {
		this.gStrTaskStepInfo = gStrTaskStepInfo;
	}
	public String getgIntCurrentInstanceTaskID() {
		return gIntCurrentInstanceTaskID;
	}
	public void setgIntCurrentInstanceTaskID(String gIntCurrentInstanceTaskID) {
		this.gIntCurrentInstanceTaskID = gIntCurrentInstanceTaskID;
	}
	public String getGetCurrentTestDataDir() {
		return getCurrentTestDataDir;
	}
	public void setGetCurrentTestDataDir(String getCurrentTestDataDir) {
		this.getCurrentTestDataDir = getCurrentTestDataDir;
	}
	public String getgObjBrowserName() {
		return gObjBrowserName;
	}
	public void setgObjBrowserName(String gObjBrowserName) {
		this.gObjBrowserName = gObjBrowserName;
	}
	public String getgObjPageName() {
		return gObjPageName;
	}
	public void setgObjPageName(String gObjPageName) {
		this.gObjPageName = gObjPageName;
	}
	public String getgObjFrame() {
		return gObjFrame;
	}
	public void setgObjFrame(String gObjFrame) {
		this.gObjFrame = gObjFrame;
	}
	public String getgObjDesc() {
		return gObjDesc;
	}
	public void setgObjDesc(String gObjDesc) {
		this.gObjDesc = gObjDesc;
	}
	public String getgObjMsg() {
		return gObjMsg;
	}
	public void setgObjMsg(String gObjMsg) {
		this.gObjMsg = gObjMsg;
	}
	public String getOBJ_REP_QUERY() {
		return OBJ_REP_QUERY;
	}

	public String getBusiness_Flow_Query() {
		return Business_Flow_Query;
	}

	public String getTask_ID_Query() {
		return Task_ID_Query;
	}
	public String getIeDriverPath() {
		return ieDriverPath;
	}
	public void setIeDriverPath(String ieDriverPath) {
		this.ieDriverPath = ieDriverPath;
	}
	public String getChromeDriverPath() {
		return chromeDriverPath;
	}
	public void setChromeDriverPath(String chromeDriverPath) {
		this.chromeDriverPath = chromeDriverPath;
	}
	public String getgBrowserType() {
		return gBrowserType;
	}
	public void setgBrowserType(String gBrowserType) {
		this.gBrowserType = gBrowserType;
	}
	public String getgTestSetID() {
		return gTestSetID;
	}
	public void setgTestSetID(String gTestSetID) {
		this.gTestSetID = gTestSetID;
	}
	public String getServerpath() {
		return serverpath;
	}
	public void setServerpath(String serverpath) {
		this.serverpath = serverpath;
	}
	public String getDbname() {
		return Dbname;
	}
	public void setDbname(String dbname) {
		Dbname = dbname;
	}
	public String getPort_number() {
		return port_number;
	}
	public void setPort_number(String port_number) {
		this.port_number = port_number;
	}
	public String getsUserName() {
		return sUserName;
	}
	public void setsUserName(String sUserName) {
		this.sUserName = sUserName;
	}
	public String getsPassword() {
		return sPassword;
	}
	public void setsPassword(String sPassword) {
		this.sPassword = sPassword;
	}
	public String getConnstring() {
		return connstring;
	}
	public void setConnstring(String connstring) {
		this.connstring = connstring;
	}
	public String getSql_db_name() {
		return sql_db_name;
	}
	public void setSql_db_name(String sql_db_name) {
		this.sql_db_name = sql_db_name;
	}
	public String getTestID() {
		return mTestID;
	}
	public void setTestID(String strTestID) {
		this.mTestID = strTestID;
	}
	public ApplicationParams()
	{
		hm=new Hashmap();
		rep=null;
	}
	/*public ApplicationParams GetObject()
	{
		if(gObj==null)
			gObj=new ApplicationParams();
		return gObj;
	}*/
	public void DestroyObject()
	{
		gObj=null;
	}
	/*public void LoadSettingsAndDB(){
		Properties prop= new Properties();
		FileInputStream input = null;
		try {
			input = new FileInputStream((new File (".").getCanonicalPath())+"\\settings.properties");
			chromeDriverPath=new File (".").getCanonicalPath()+"\\BrowserServer\\chromedriver.exe";
			ieDriverPath =new File (".").getCanonicalPath()+"\\BrowserServer\\IEDriverServer.exe";
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			prop.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		gStrAutomationPath = prop.getProperty("PARENT_DIRECTORY"); //		"JoS\\"
		sql_db_name = prop.getProperty("SQL_DB_NAME");
		serverpath = prop.getProperty("DB_SERVER_IP_ADDRESS");
		port_number = prop.getProperty("DB_SERVER_PORT");
		sUserName = prop.getProperty("DB_SERVER_USERNAME");
		sPassword = prop.getProperty("DB_SERVER_PASSWORD");
		NumberOfThreads = prop.getProperty("NUMBER_OF_THREADS");
		ObjectLoadTime = prop.getProperty("OBJECT_LOAD_TIME");
		PageLoadTime = prop.getProperty("PAGE_LOAD_TIME");
		if(NumberOfThreads==null || NumberOfThreads.isEmpty())
			NumberOfThreads="1";
	}*/

	public void LoadSettings(){
		Properties prop= new Properties();
		FileInputStream input = null;
		try {
			input = new FileInputStream((new File (".").getCanonicalPath())+"\\settings.properties");
			chromeDriverPath=new File (".").getCanonicalPath()+"\\BrowserServer\\chromedriver.exe";
			ieDriverPath =new File (".").getCanonicalPath()+"\\BrowserServer\\IEDriverServer.exe";
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			prop.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			gStrAutomationPath = new File (".").getCanonicalPath();
			System.out.println("gStrAutomationPath: " + gStrAutomationPath);
		} catch (IOException e) {
			System.out.println("IOException:  " + e);
		}
		serverpath = prop.getProperty("DB_SERVER_IP_ADDRESS");
		port_number = prop.getProperty("DB_SERVER_PORT");
		sUserName = prop.getProperty("DB_SERVER_USERNAME");
		sPassword = prop.getProperty("DB_SERVER_PASSWORD");

		DB_Handler conn= new DB_Handler(sql_db_name);
		ResultSet rs= conn.ExecuteSelectQuery("*", "application_settings");
		try {
			if(rs.next()){
				NumberOfThreads = rs.getString("num_of_threads");
				ObjectLoadTime = rs.getString("obj_load_time");
				PageLoadTime = rs.getString("page_load_time");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn.CloseConnection();


		//NumberOfThreads = prop.getProperty("NUMBER_OF_THREADS");
		//ObjectLoadTime = prop.getProperty("OBJECT_LOAD_TIME");
		//PageLoadTime = prop.getProperty("PAGE_LOAD_TIME");

		if(NumberOfThreads==null || NumberOfThreads.isEmpty())
			NumberOfThreads="1";
	}
	public String  parentPath(){
		return gDrive;
	}
}