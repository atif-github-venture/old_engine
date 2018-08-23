package JoS.JAVAProjects;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestDataReader {
	ApplicationParams gOBJ=null;//=
	TestDataReader(ApplicationParams gOBJTemp)
	{
		gOBJ=gOBJTemp;
	}

	 public void ReadTestData(String pTestID, int pCurrentIteraiton) {
		 DebugInterface di=new DebugInterface(gOBJ);
		 di.Debug ("Inside function ReadTestData()..");
		 ResultSet rTestDataQuery = null;
		 DB_Handler dbConnection=new DB_Handler(gOBJ);
		 di.Debug ("QUERY ::" + "SELECT * FROM test_data where LocatorFolder = '" + gOBJ.getgStrReleaseFolder() + "' AND TestID = '" + gOBJ.getTestID() + "'" + " AND Iteration = '" + pCurrentIteraiton+ "'");
		 rTestDataQuery = dbConnection.ExecuteQuery("SELECT * FROM test_data where LocatorFolder = '" + gOBJ.getgStrReleaseFolder() + "' AND TestID = '" + gOBJ.getTestID() + "'" + " AND Iteration = '" + pCurrentIteraiton+ "'");

			int rowCount = new ExecuteAutomation(gOBJ).getRowCount(rTestDataQuery);
			di.Debug ("rowCount: " + rowCount);
			if (rowCount!=0) {
				try {
					if(rTestDataQuery.next()) {
						for (int i=0;i<rowCount;i++){
							try {
								gOBJ.hm.AssignHashValueToHashKey (rTestDataQuery.getString("VariableName"), rTestDataQuery.getString("Value"));					

							} catch (SQLException e) {
								di.Debug ("SQLException e:: " + e); 
								e.printStackTrace();
							}
							rTestDataQuery.next();
						}
						
					}
					dbConnection.CloseConnection();
				} catch (SQLException e) {
					dbConnection.CloseConnection();
					e.printStackTrace();
				}

			}else{
				di.Debug ("The test data is not customized for Test ID :: " + gOBJ.getTestID() + " and iteration :: " + pCurrentIteraiton); 
			}
			dbConnection.CloseConnection();
			di.Debug ("Exiting function ReadTestData().."); 
		 }
	 public void ReadEnvironmentVariable () {
		 DebugInterface di=new DebugInterface(gOBJ);
		 di.Debug ("Inside function ReadEnvironmentVariable()..");
		 DB_Handler dbConnection=new DB_Handler(gOBJ);
		 ResultSet rEnvTableQuery = null;
		 di.Debug ("Select * From EnvironmentVariable where LocatorFolder = '" + gOBJ.getgStrReleaseFolder() + "'");
		 rEnvTableQuery = dbConnection.ExecuteQuery("Select * From EnvironmentVariable where LocatorFolder = '" + gOBJ.getgStrReleaseFolder() + "'");
		 
			int rowCount = new ExecuteAutomation(gOBJ).getRowCount(rEnvTableQuery);
			if (rowCount!=0) {
				try {
					if(rEnvTableQuery.next()) {
						di.Debug ("The Envoironment setting is available for : " + gOBJ.getgStrReleaseFolder());
						try {
							gOBJ.hm.AssignHashValueToHashKey ("LocatorFolder", rEnvTableQuery.getString("LocatorFolder"));
							gOBJ.hm.AssignHashValueToHashKey ("EnvironmentToTest", rEnvTableQuery.getString("EnvironmentToTest"));
							gOBJ.hm.AssignHashValueToHashKey ("TakeScreenshotFailedStep", rEnvTableQuery.getString("TakeScreenshotFailedStep"));
							gOBJ.hm.AssignHashValueToHashKey ("TakeScreenshotPassedStep", rEnvTableQuery.getString("TakeScreenshotPassedStep"));
							gOBJ.hm.AssignHashValueToHashKey ("HtmlReport", rEnvTableQuery.getString("HtmlReport"));
							gOBJ.hm.AssignHashValueToHashKey ("ExcelReport", rEnvTableQuery.getString("ExcelReport"));
							gOBJ.hm.AssignHashValueToHashKey ("ProjectName", rEnvTableQuery.getString("ProjectName"));
							gOBJ.hm.AssignHashValueToHashKey ("LogLevel", rEnvTableQuery.getString("LogLevel"));
							gOBJ.hm.AssignHashValueToHashKey ("ReportTheme", rEnvTableQuery.getString("ReportTheme"));
							di.Debug ("Environment Variable 'LocatorFolder': " + gOBJ.hm.GetHashValueFromHashKey ("LocatorFolder"));
							di.Debug ("Environment Variable 'EnvironmentToTest': " + gOBJ.hm.GetHashValueFromHashKey ("EnvironmentToTest"));
							di.Debug ("Environment Variable 'TakeScreenshotFailedStep': " + gOBJ.hm.GetHashValueFromHashKey ("TakeScreenshotFailedStep"));
							di.Debug ("Environment Variable 'TakeScreenshotPassedStep': " + gOBJ.hm.GetHashValueFromHashKey ("TakeScreenshotPassedStep"));
							di.Debug ("Environment Variable 'HtmlReport': " + gOBJ.hm.GetHashValueFromHashKey ("HtmlReport"));
							di.Debug ("Environment Variable 'ExcelReport': " + gOBJ.hm.GetHashValueFromHashKey ("ExcelReport"));
							di.Debug ("Environment Variable 'ProjectName': " + gOBJ.hm.GetHashValueFromHashKey ("ProjectName"));
							di.Debug ("Environment Variable 'LogLevel': " + gOBJ.hm.GetHashValueFromHashKey ("LogLevel"));
							di.Debug ("Environment Variable 'ReportTheme': " + gOBJ.hm.GetHashValueFromHashKey ("ReportTheme"));
							di.Debug ("Exiting function ReadEnvironmentVariable()..");
						} catch (SQLException e) {
							dbConnection.CloseConnection();
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch (SQLException e) {
					dbConnection.CloseConnection();
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			dbConnection.CloseConnection();
	 }
	 public int GetNumberOfTestIterations() {
		 DebugInterface di=new DebugInterface(gOBJ);
		 di.Debug ("Inside function GetNumberOfTestIterations()..");
		 //SELECT MAX(Iteration) FROM mydb.test_data where LocatorFolder = 'Smoke_Suite' AND TestID = '49'
		 ResultSet rTestDataQuery = null;
		 DB_Handler dbConnection=new DB_Handler(gOBJ);
		 int intIterations = 0;
		 di.Debug ("QUERY ::" + "SELECT MAX(Iteration) FROM test_data where LocatorFolder = '" + gOBJ.getgStrReleaseFolder() + "' AND TestID = '" + gOBJ.getTestID() + "'");
		 rTestDataQuery = dbConnection.ExecuteQuery("SELECT MAX(Iteration) FROM test_data where LocatorFolder = '" + gOBJ.getgStrReleaseFolder() + "' AND TestID = '" + gOBJ.getTestID() + "'");

		int rowCount = new ExecuteAutomation(gOBJ).getRowCount(rTestDataQuery);
		if (rowCount!=0) {
			try {
				if(rTestDataQuery.next()) {
					di.Debug ("rTestDataQuery.getString(MAX(Iteration)::" + rTestDataQuery.getString("MAX(Iteration)"));
					if ((rTestDataQuery.getString("MAX(Iteration)") != null)){
						intIterations = Integer.parseInt((rTestDataQuery.getString("MAX(Iteration)")).trim());
					}else{
						di.Debug ("The test data is not customized."); 
					}
				}
			} catch (SQLException e) {
				dbConnection.CloseConnection();
				di.Debug ("SQLException e:: " + e); 
				e.printStackTrace();
				}
			}
		dbConnection.CloseConnection();
			di.Debug ("Exiting function GetNumberOfTestIterations().."); 
			return intIterations;
		 }
}
