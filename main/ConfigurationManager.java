package JoS.JAVAProjects;

import java.sql.ResultSet;
import java.sql.SQLException;

import JoS.JAVAProjects.Reporting.HtmlReport;
import JoS.JAVAProjects.Reporting.Report;
import JoS.JAVAProjects.Reporting.ReportSettings;
import JoS.JAVAProjects.Reporting.ReportTheme;
import JoS.JAVAProjects.Reporting.ReportThemeFactory;
import JoS.JAVAProjects.Reporting.ReportThemeFactory.Theme;

public class ConfigurationManager {
	ApplicationParams gOBJ=null;
	ConfigurationManager(ApplicationParams gOBJTemp)
	{
		gOBJ=gOBJTemp;
	}
	public void LoadAutomationInterfaces(){
		//DB_Handler con=new DB_Handler();
		new DebugInterface(gOBJ).DebugFileInitiate();
		
		gOBJ.hm.ClearHashMap();
		System.out.println(gOBJ.hm.GetHashValueFromHashKey("LocatorFolder"));
		TestDataReader ReadData=new TestDataReader(gOBJ);		
		ReadData.ReadEnvironmentVariable();
		
		//html and excel reporting ::START
		ReportSettings RepSetting =  new ReportSettings(gOBJ.getgStrDebugLogPath(), "Report");		
		RepSetting.setProjectName(gOBJ.hm.GetHashValueFromHashKey("ProjectName"));
		RepSetting.setLogLevel(Integer.valueOf(gOBJ.hm.GetHashValueFromHashKey("LogLevel")));
		RepSetting.takeScreenshotFailedStep = Boolean.valueOf(gOBJ.hm.GetHashValueFromHashKey("TakeScreenshotFailedStep").toLowerCase());
		RepSetting.takeScreenshotPassedStep = Boolean.valueOf(gOBJ.hm.GetHashValueFromHashKey("TakeScreenshotPassedStep").toLowerCase());
		RepSetting.generateHtmlReports = Boolean.valueOf(gOBJ.hm.GetHashValueFromHashKey("HtmlReport").toLowerCase());
		RepSetting.generateExcelReports = Boolean.valueOf(gOBJ.hm.GetHashValueFromHashKey("ExcelReport").toLowerCase());
		
		ReportTheme RepTheme = null;
		if (gOBJ.hm.GetHashValueFromHashKey("ReportTheme").equals("AUTUMN")){
			RepTheme = new ReportThemeFactory().getReportsTheme(Theme.AUTUMN);
		}else if (gOBJ.hm.GetHashValueFromHashKey("ReportTheme").equals("SERENE")){
			RepTheme = new ReportThemeFactory().getReportsTheme(Theme.SERENE);
		}else if (gOBJ.hm.GetHashValueFromHashKey("ReportTheme").equals("RETRO")){
			RepTheme = new ReportThemeFactory().getReportsTheme(Theme.RETRO);
		}else if (gOBJ.hm.GetHashValueFromHashKey("ReportTheme").equals("REBEL")){
			RepTheme = new ReportThemeFactory().getReportsTheme(Theme.REBEL);
		}else if (gOBJ.hm.GetHashValueFromHashKey("ReportTheme").equals("OLIVE")){
			RepTheme = new ReportThemeFactory().getReportsTheme(Theme.OLIVE);
		}else if (gOBJ.hm.GetHashValueFromHashKey("ReportTheme").equals("MYSTIC")){
			RepTheme = new ReportThemeFactory().getReportsTheme(Theme.MYSTIC);
		}else if (gOBJ.hm.GetHashValueFromHashKey("ReportTheme").equals("CLASSIC")){
			RepTheme = new ReportThemeFactory().getReportsTheme(Theme.CLASSIC);
		}
		gOBJ.rep = new Report(RepSetting, RepTheme);	
		gOBJ.rep.initialize();
		gOBJ.rep.initializeTestLog();
		gOBJ.rep.addTestLogHeading("TestID_" + gOBJ.getmTestID() + "_runID_" + gOBJ.getgNumRunID());
		//rep.addTestLogSubHeading("a", "b", "c", "d");		   
		gOBJ.rep.addTestLogSection("Test ID:" + gOBJ.getmTestID());
		gOBJ.rep.addTestLogSubSection("Test Case: " + GetTestName());
		gOBJ.rep.addTestLogTableHeadings();
		//
		
		//html and excel reporting:: END
		
		gOBJ.setgTestIterationCount(ReadData.GetNumberOfTestIterations());	//Read the script specific input XML and get the count of iteration, to loop the entire run accordingly
		new DebugInterface(gOBJ).Debug("Exiting LoadAutomationInterfaces()...");
		//con.CloseConnection();//();
	}
	
	private String GetTestName (){
		new DebugInterface(gOBJ).Debug("Inside GetTestName()...");
		ResultSet rBusinessFlow = null;
		String sTestName = null;

		DB_Handler dbConnection=new DB_Handler(gOBJ);
		rBusinessFlow = dbConnection.ExecuteQuery(gOBJ.getBusiness_Flow_Query() +"'"+gOBJ.getmTestID()+"'");
		try {
			rBusinessFlow.first();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new DebugInterface(gOBJ).Debug ("Retrieved the value from the database");

		try {
			sTestName = rBusinessFlow.getString("Business_Flow");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new DebugInterface(gOBJ).Debug ("Test name retrieved from the database - "+sTestName);
		dbConnection.CloseConnection();
		new DebugInterface(gOBJ).Debug("Exiting GetTestName()...");
		return sTestName;
	}
}
