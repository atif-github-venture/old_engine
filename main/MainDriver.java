package JoS.JAVAProjects;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;


public class MainDriver{
	static Calendar cal = Calendar.getInstance();
	public void DriverMain (Object[] strObj) throws ParseException
	{
		DriveMain(strObj[0].toString(),strObj[1].toString(),strObj[2].toString(),strObj[3].toString(),strObj[4].toString(),strObj[5].toString(),strObj[6].toString(),strObj[7].toString());
	}
	/*public void DriveMainFromCmdLine (String strRunID, String pLocator, String pSubLocator, String pTestSet, String pTestID, String pBrowserType) throws ParseException {
		long lStartTime = new Date().getTime();

		ApplicationParams gOBJ=new ApplicationParams();//.GetObject().LoadSettings();
		gOBJ.LoadSettings();
		DebugInterface di=new DebugInterface(gOBJ);
		gOBJ.setTestID(pTestID);
		gOBJ.setgNumRunID(strRunID);
		gOBJ.setgSubLocator(pSubLocator);
		gOBJ.setgTestSet(pTestSet);
		gOBJ.setgBrowserType(pBrowserType);
		gOBJ.setgStrReleaseFolder(pLocator);
		gOBJ.setgStrAutomationPathNew(gOBJ.getgStrAutomationPath() + "\\Results\\" + gOBJ.getgStrReleaseFolder() + "\\");
		//To check if the pLocator path is not existing in file system.
		File file = new File(gOBJ.getgStrAutomationPathNew());

		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println ("Test suite path does not exist, hence created! = " + gOBJ.getgStrAutomationPathNew());
			} else {
				System.out.println ("Test suite path exists! = " + gOBJ.getgStrAutomationPathNew());
			}
		}


		//Any logs to debug file will be done only after calling Load automation interfaces
		new ConfigurationManager(gOBJ).LoadAutomationInterfaces();
		di.Debug ("Going for InitializeTestReport()");
		//MainDriver.InitializeTestReport();		
		di.Debug ("Going for ExecuteAutomation()");
		new ExecuteAutomation(gOBJ).GlobalIterationRun();

		long lEndTime = new Date().getTime();
		long ExecutionTime = lEndTime - lStartTime;
		di.Debug ("Test Execution time in seconds (long) : " + (double)ExecutionTime/1000);
		di.Debug ("Test Execution time in seconds : " + String.valueOf((double)ExecutionTime/1000));
		//new HtmlReportInterface(gOBJ).HtmlFooter(String.valueOf((double)ExecutionTime/1000) + " secs", gOBJ.getTestSteppassCount(), gOBJ.getTestStepFailCount());
		if(gOBJ.rep!=null)
			gOBJ.rep.addTestLogFooter(String.valueOf((double)ExecutionTime/1000));
		DebugInterface ZdF = new DebugInterface(gOBJ);
		try {
			ZdF.ZipDebugFolder();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		new DB_Handler(gOBJ).CloseConnection();
		gOBJ.DestroyObject();
	//	new KillProcess().CleanUpProcess();
	}*/
	public void DriveMain (String strRunID, String pLocator, String pSubLocator, String pTestSet, String pTestID, String pBrowserType,String strDBName,String strUserName) throws ParseException {
		long lStartTime = new Date().getTime();

		ApplicationParams gOBJ=new ApplicationParams();//.GetObject().LoadSettings();
		gOBJ.setSql_db_name(strDBName);
		gOBJ.LoadSettings();
		DebugInterface di=new DebugInterface(gOBJ);
		gOBJ.setTestID(pTestID);
		gOBJ.setgNumRunID(strRunID);
		gOBJ.setgSubLocator(pSubLocator);
		gOBJ.setgTestSet(pTestSet);
		gOBJ.setgBrowserType(pBrowserType);
		gOBJ.setgStrTester(strUserName);
		gOBJ.setgStrReleaseFolder(pLocator);
		DB_Handler con = new DB_Handler("common_config");		
		ResultSet rs = con.ExecuteSelectQuery("db_name_for_Login_Screen", "all_schema_names where db_name = '" + strDBName + "';");
		
		try {
			if (rs.next())
				try {					
					gOBJ.setStrProjectName(rs.getString("db_name_for_Login_Screen"));
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		con.CloseConnection();
		gOBJ.setSql_db_name(strDBName);
		gOBJ.setgStrAutomationPathNew(gOBJ.getgStrAutomationPath() + "\\Results\\" + gOBJ.getStrProjectName() + "\\" + gOBJ.getgStrReleaseFolder() + "\\");
		//To check if the pLocator path is not existing in file system.
		File file = new File(gOBJ.getgStrAutomationPathNew());

		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println ("Test suite path does not exist, hence created! = " + gOBJ.getgStrAutomationPathNew());
			} else {
				System.out.println ("Test suite path exists! = " + gOBJ.getgStrAutomationPathNew());
			}
		}


		//Any logs to debug file will be done only after calling Load automation interfaces
		new ConfigurationManager(gOBJ).LoadAutomationInterfaces();
		di.Debug ("Going for InitializeTestReport()");
		//MainDriver.InitializeTestReport();		
		di.Debug ("Going for ExecuteAutomation()");
		new ExecuteAutomation(gOBJ).GlobalIterationRun();

		long lEndTime = new Date().getTime();
		long ExecutionTime = lEndTime - lStartTime;
		di.Debug ("Test Execution time in seconds (long) : " + (double)ExecutionTime/1000);
		di.Debug ("Test Execution time in seconds : " + String.valueOf((double)ExecutionTime/1000));
		//new HtmlReportInterface(gOBJ).HtmlFooter(String.valueOf((double)ExecutionTime/1000) + " secs", gOBJ.getTestSteppassCount(), gOBJ.getTestStepFailCount());
		if(gOBJ.rep!=null)
			gOBJ.rep.addTestLogFooter(String.valueOf((double)ExecutionTime/1000));
		DebugInterface ZdF = new DebugInterface(gOBJ);
		try {
			ZdF.ZipDebugFolder();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		new DB_Handler(gOBJ).CloseConnection();
		gOBJ.DestroyObject();
		//	new KillProcess().CleanUpProcess();
	}

}
