package JoS.JAVAProjects;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.UnreachableBrowserException;

import JoS.JAVAProjects.Reporting.HtmlReport;
import JoS.JAVAProjects.Reporting.ReportSettings;
import JoS.JAVAProjects.Reporting.ReportTheme;
import JoS.JAVAProjects.Reporting.Status;

public class Utils {
	ApplicationParams gOBJ = null;// =

	Utils(ApplicationParams gOBJTemp) {
		gOBJ = gOBJTemp;
	}

	public void OpenBrowser() {
		new DebugInterface(gOBJ).Debug("Inside the function OpenBrowser()...");
		SetWebDriver(gOBJ.getgBrowserType());
		new DebugInterface(gOBJ).Debug(gOBJ.getWbDriver().getCurrentUrl());
		if ((gOBJ.getWbDriver().getCurrentUrl()).equals("")) {
			new DebugInterface(gOBJ).Debug(gOBJ.getgStrTaskID() + " Step - "
					+ gOBJ.getgIntExecOrder() + "-" + gOBJ.getgObjType()
					+ " - Open Browser - Browser has NOT been launched: Fail");
			ReportStepToResultDB(
					"Open Browser - Browser has NOT been launched", "Fail");
			new DebugInterface(gOBJ)
			.Debug("Exiting the function OpenBrowser()...");
			new DoScreenActions(gOBJ).taskfail = true;
		} else {
			new DebugInterface(gOBJ).Debug(gOBJ.getgStrTaskID() + " Step - "
					+ gOBJ.getgIntExecOrder() + "-" + gOBJ.getgObjType()
					+ " - Open Browser - Browser has been launched: Pass");
			ReportStepToResultDB("Open Browser - Browser has been launched",
					"Pass");
			new DebugInterface(gOBJ)
			.Debug("Exiting the function OpenBrowser()...");
		}
	}
	public synchronized void WriteToOuputTXTFile(String Option, String path, String OuputText){
		new DebugInterface(gOBJ).Debug("Inside WriteToOuputTXTFile()...");
		new DebugInterface(gOBJ).Debug("Option: " + Option);
		new DebugInterface(gOBJ).Debug("path: " + path);
		new DebugInterface(gOBJ).Debug("OuputText: "+ OuputText);
		if(Option.equalsIgnoreCase("canonical")){
			//to save it to the canonical path
			FileOutputStream fos = null;
			String canPath = null;
			/*
			 * This is to handle the case where the user forgets to provide the .txt extension
			 */
			if (path.toLowerCase().contains(".txt")){
				//do nothing
				new DebugInterface(gOBJ).Debug("extension .txt exists in the path name");
			}else{
				path = path + ".txt";
				new DebugInterface(gOBJ).Debug("added .txt to the path name");
			}

			try {
				canPath = new File(".").getCanonicalPath();
			} catch (IOException e2) {
				System.out.println("IOException: " + e2.getMessage());
				new DebugInterface(gOBJ).Debug("IOException: " + e2.getMessage());
			}
			File varTmpDir = new File (canPath+"\\CustomInputOutput\\" + gOBJ.getStrProjectName() + "\\" + path);
			System.out.println(canPath+"\\CustomInputOutput\\" + gOBJ.getStrProjectName() + "\\" + path);
			File dir = new File(canPath + "\\CustomInputOutput\\" + gOBJ.getStrProjectName());
			if(dir.exists() && dir.isDirectory()) {
			    /*try {
					FileUtils.cleanDirectory(dir);
					System.out.println("Cleaning Directory");
				} catch (IOException e) {
					e.printStackTrace();
				}*/
			}else{		//create the directory
				dir.mkdirs();
				System.out.println("Directory created");
			}
			
			if (varTmpDir.exists()){
				System.out.println("File already exists!");

				try {
					OuputText = "\r\n"+OuputText;
					Files.write(Paths.get(varTmpDir.getAbsolutePath()), OuputText.getBytes(), StandardOpenOption.APPEND);
				} catch (IOException e) {
					System.out.println("IOException: " + e.getMessage());
					new DebugInterface(gOBJ).Debug("IOException: " + e.getMessage());
				}

				/*ew DebugInterface(gOBJ).Debug("File already exists!");
				try {
					fos = new FileOutputStream(path, true);
				} catch (FileNotFoundException e) {
					System.out.println("FileNotFoundException: " + e.getMessage());
					new DebugInterface(gOBJ).Debug("FileNotFoundException: " + e.getMessage());
				}
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
				try {
					bw.write(OuputText);
					bw.newLine();
					bw.close();
				} catch (IOException e1) {
					System.out.println("IOException: " + e1.getMessage());
					new DebugInterface(gOBJ).Debug("IOException: " + e1.getMessage());
				}*/
			}else{        
				if(dir.exists() && dir.isDirectory()) {
					//do nothing
				}else{		//create the directory
					dir.mkdir();
					System.out.println("Directory created");
				}
				try {
					varTmpDir.createNewFile();
				} catch (IOException e1) {
					System.out.println("IOException: " + e1.getMessage());
					new DebugInterface(gOBJ).Debug("IOException: " + e1.getMessage());
				}
				try {
					fos = new FileOutputStream(canPath +"\\CustomInputOutput\\" + gOBJ.getStrProjectName() + path, true);
				} catch (FileNotFoundException e) {
					System.out.println("FileNotFoundException: " + e.getMessage());
					new DebugInterface(gOBJ).Debug("FileNotFoundException: " + e.getMessage());
				}
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
				try {
					bw.write(OuputText);
					bw.close();
					fos.close();
				} catch (IOException ex) {
					System.out.println("IOException: " + ex.getMessage());
					new DebugInterface(gOBJ).Debug("IOException: " + ex.getMessage());
				}
			}
		}else{
			//to save it to the user given path
			FileOutputStream fos = null;
			/*
			 * This is to handle the case where the user forgets to provide the .txt extension
			 */
			if (path.toLowerCase().contains(".txt")){
				//do nothing
			}else{
				path = path + ".txt";
			}

			File varTmpDir = new File (path);
			System.out.println("Path: " + path);
			if (varTmpDir.exists()){
				System.out.println("File exists!");
				try {
					fos = new FileOutputStream(path, true);
				} catch (FileNotFoundException e) {
					System.out.println("FileNotFoundException: " + e.getMessage());
					new DebugInterface(gOBJ).Debug("FileNotFoundException: " + e.getMessage());
				}
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
				try {
					bw.write(OuputText);
					bw.close();
					fos.close();
				} catch (IOException ex) {
					System.out.println("IOException: " + ex.getMessage());
					new DebugInterface(gOBJ).Debug("IOException: " + ex.getMessage());
				}
			}else{
				ReportStepToResultDB("File path does not exist: " + path, "Fail");
				new DebugInterface(gOBJ).Debug("File path does not exist: " + path);
			}				
		}
		new DebugInterface(gOBJ).Debug("Exiting WriteToOuputTXTFile()...");
	}

	public void getscreenshot(String StepName) throws Exception {
		new DebugInterface(gOBJ).Debug("Inside getscreenshot()...");
		File scrFile = null;
		try{
			scrFile = ((TakesScreenshot) gOBJ.getWbDriver()).getScreenshotAs(OutputType.FILE);
			// The below method will save the screen shot in mentioned path with
			// name "screenshot.png"
			FileUtils.copyFile(scrFile, new File(gOBJ.getgStrDebugLogPath() + "\\" + StepName + " .jpeg"));
		}catch(UnreachableBrowserException e){
			//new Utils(gOBJ).ReportStepToResultDB("Unreachable Browser Exception", "Fail");
			new DebugInterface(gOBJ).Debug("Unreachable Browser Exception - Fail - " + e.getMessage());
		}	

		

		new DebugInterface(gOBJ).Debug("Exiting getscreenshot()...");
	}

	/*
	 * public void ReportStepToResultDB(String strMsg, String stepStatus) { new
	 * DebugInterface(gOBJ).Debug
	 * ("Inside the function ReportStepToResultDB()..."); Date today = new
	 * Date(); DateFormat date = new SimpleDateFormat("MM/dd/yyyy"); DateFormat
	 * time = new SimpleDateFormat("hh:mm:ss a"); System.out.println("Date: " +
	 * date.format(today)); System.out.println("Time: " + time.format(today));
	 * String ExecutionDate = date.format(today); String ExecutionTime =
	 * time.format(today); String sUpdateQuery = "INSERT INTO " +
	 * gOBJ.getSql_db_name() +
	 * ".run_instance_result (TestID,RunID,FieldLocator,SubLocator,Test_Set,TaskID,Execution_Order,Actual_Result,Status,Execution_Date,Execution_Time, BrowserType) VALUES ('"
	 * +gOBJ.getmTestID() + "','" + gOBJ.getgNumRunID() + "','" +
	 * gOBJ.getgStrReleaseFolder() + "','" + gOBJ.getgSubLocator() + "','" +
	 * gOBJ.getgTestSet() + "','" + gOBJ.getgStrTaskID() + "','" +
	 * gOBJ.getgIntExecOrder() + "','" + strMsg + "','" + stepStatus + "','" +
	 * ExecutionDate + "','" + ExecutionTime + "','" +
	 * gOBJ.getgBrowserType().toUpperCase() + "');"; new
	 * DebugInterface(gOBJ).Debug (sUpdateQuery); DB_Handler dbConnection=new
	 * DB_Handler(); dbConnection.ExecuteUpdate(sUpdateQuery); new
	 * DebugInterface(gOBJ).Debug("Screenshot for failed step set to " +
	 * gOBJ.hm.GetHashValueFromHashKey("TakeScreenshotFailedStep")); new
	 * DebugInterface(gOBJ).Debug("Screenshot for failed step set to " +
	 * gOBJ.hm.GetHashValueFromHashKey("TakeScreenshotPassedStep")); //take
	 * screenshots (for failed steps) if the setup is asked to do so thru
	 * environment variable set up if
	 * ((gOBJ.hm.GetHashValueFromHashKey("TakeScreenshotFailedStep"
	 * ).equalsIgnoreCase("true")) && stepStatus.equalsIgnoreCase("fail")){ try
	 * { new
	 * DebugInterface(gOBJ).Debug("Inside for Take Screen shot for Failed Step"
	 * ); getscreenshot(gOBJ.getgStrTaskID() + "_" + gOBJ.getgIntExecOrder() +
	 * "_" + stepStatus); } catch (Exception e) {
	 * dbConnection.CloseConnection(); System.out.println("Exception caught:" +
	 * e); new DebugInterface(gOBJ).Debug ("Exception caught:" + e);
	 * e.printStackTrace(); } } //take screenshots (for passed steps) if the
	 * setup is asked to do so thru environment variable set up if
	 * ((gOBJ.hm.GetHashValueFromHashKey
	 * ("TakeScreenshotPassedStep").equalsIgnoreCase("true")) &&
	 * stepStatus.equalsIgnoreCase("pass")){ try { new
	 * DebugInterface(gOBJ).Debug
	 * ("Inside for Take Screen shot for Passed Step");
	 * getscreenshot(gOBJ.getgStrTaskID() + "_" + gOBJ.getgIntExecOrder() + "_"
	 * + stepStatus); } catch (Exception e) { dbConnection.CloseConnection();
	 * System.out.println("Exception caught:" + e); new
	 * DebugInterface(gOBJ).Debug ("Exception caught:" + e);
	 * e.printStackTrace(); } } dbConnection.CloseConnection(); new
	 * DebugInterface(gOBJ).Debug
	 * ("Exiting the function ReportStepToResultDB()..."); }
	 */

	public void ReportStepToResultDB(String strMsg, String stepStatus) {		
		//new DebugInterface(gOBJ).Debug("Inside the function ReportStepToResultDB()...");
		//new HtmlReportInterface(gOBJ).HtmlStepReporting(strMsg, stepStatus);


		if (stepStatus.toUpperCase().equals("FAIL") && gOBJ.rep!=null ){
			gOBJ.rep.updateTestLog( gOBJ.getgStrTaskID(), strMsg,  Status.FAIL);
		}else if (stepStatus.toUpperCase().equals("PASS") &&  gOBJ.rep!=null){
			gOBJ.rep.updateTestLog( gOBJ.getgStrTaskID(), strMsg,  Status.PASS);
		}			

		Date today = new Date();
		DateFormat date = new SimpleDateFormat("MM/dd/yyyy");
		DateFormat time = new SimpleDateFormat("hh:mm:ss a");
		System.out.println("Date: " + date.format(today));
		System.out.println("Time: " + time.format(today));
		String ExecutionDate = date.format(today);
		String ExecutionTime = time.format(today);
		String sUpdateQuery = "INSERT INTO "
				+ gOBJ.getSql_db_name()
				+ ".run_instance_result (TestID,RunID,FieldLocator,SubLocator,Test_Set,TaskID,Execution_Order,Actual_Result,Status,Execution_Date,Execution_Time, BrowserType, ResultPath, Iteration, Expected_Result, Tester) VALUES ('"
				+ gOBJ.getmTestID() + "','" + gOBJ.getgNumRunID() + "','"
				+ gOBJ.getgStrReleaseFolder() + "','" + gOBJ.getgSubLocator()
				+ "','" + gOBJ.getgTestSet() + "','" + gOBJ.getgStrTaskID()
				+ "','" + gOBJ.getgIntExecOrder() + "','" + strMsg + "','"
				+ stepStatus + "','" + ExecutionDate + "','" + ExecutionTime
				+ "','" + gOBJ.getgBrowserType().toUpperCase()
				+ "','" + gOBJ.getgStrDebugLogPath().replace("\\", "/")
				+ "','" + gOBJ.getgCurrentTestIteration()
				+ "','" + "NULL"	//expected result
				+ "','" + gOBJ.getgStrTester()
				+ "');";
		new DebugInterface(gOBJ).Debug(sUpdateQuery);
		DB_Handler dbConnection = new DB_Handler(gOBJ);
		dbConnection.ExecuteUpdate(sUpdateQuery);
		ResultSet rs = dbConnection.ExecuteSelectQuery(
				"Last_Result",
				"run_instance_id_table where run_InstanceID='"
						+ gOBJ.getgNumRunID() + "'");
		try {
			if (rs.next()) {
				String strLastResult = rs.getString("Last_Result");
				if (strLastResult != null && !strLastResult.equals("Fail")) {
					dbConnection
					.ExecuteUpdateQuery("`run_instance_id_table` SET `Last_Result`='" + stepStatus+ "', `ExecutionDate` = '" + ExecutionDate + "', `ExecutionTime` = '" + ExecutionTime +"', `Tester` = '" + gOBJ.getgStrTester()+"' where run_InstanceID='"
							+ gOBJ.getgNumRunID() + "'");
				}
			}
		} catch (SQLException e1) {
			dbConnection.CloseConnection();
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		new DebugInterface(gOBJ).Debug("Screenshot for failed step set to "
				+ gOBJ.hm.GetHashValueFromHashKey("TakeScreenshotFailedStep"));
		new DebugInterface(gOBJ).Debug("Screenshot for failed step set to "
				+ gOBJ.hm.GetHashValueFromHashKey("TakeScreenshotPassedStep"));
		// take screenshots (for failed steps) if the setup is asked to do so
		// thru environment variable set up
		if ((gOBJ.hm.GetHashValueFromHashKey("TakeScreenshotFailedStep")
				.equalsIgnoreCase("true"))
				&& stepStatus.equalsIgnoreCase("fail")) {
			try {
				new DebugInterface(gOBJ)
				.Debug("Inside for Take Screen shot for Failed Step");
				getscreenshot(gOBJ.getgStrTaskID() + "_"
						+ gOBJ.getgIntExecOrder() + "_" + stepStatus);
			} catch (Exception e) {
				dbConnection.CloseConnection();
				System.out.println("Exception caught:" + e);
				new DebugInterface(gOBJ).Debug("Exception caught:" + e);
				e.printStackTrace();
			}
		}
		// take screenshots (for passed steps) if the setup is asked to do so
		// thru environment variable set up
		if ((gOBJ.hm.GetHashValueFromHashKey("TakeScreenshotPassedStep")
				.equalsIgnoreCase("true"))
				&& stepStatus.equalsIgnoreCase("pass")) {
			try {
				new DebugInterface(gOBJ)
				.Debug("Inside for Take Screen shot for Passed Step");
				getscreenshot(gOBJ.getgStrTaskID() + "_"
						+ gOBJ.getgIntExecOrder() + "_" + stepStatus);
			} catch (Exception e) {
				dbConnection.CloseConnection();
				System.out.println("Exception caught:" + e);
				new DebugInterface(gOBJ).Debug("Exception caught:" + e);
				e.printStackTrace();
			}
		}
		dbConnection.CloseConnection();
		new DebugInterface(gOBJ).Debug("Exiting the function ReportStepToResultDB()...");
	}

	private Object HtmlReportInterface(ApplicationParams gOBJ2) {
		// TODO Auto-generated method stub
		return null;
	}

	public void NavigateURL(String pStrDbValueField) throws SQLException {
		ResultSet pStrURLQuery = null;
		String pStrURLFromURLTable, pStrURL, pStrURLDescription;
		new DebugInterface(gOBJ).Debug("Inside the function NavigateURL()...");
		pStrURLFromURLTable = pStrDbValueField + "_"
				+ gOBJ.hm.GetHashValueFromHashKey("EnvironmentToTest");
		new DebugInterface(gOBJ).Debug("Throwing URL quesry for : "
				+ pStrURLFromURLTable);
		DB_Handler dbConnection = new DB_Handler(gOBJ);
		pStrURLQuery = dbConnection
				.ExecuteQuery("SELECT * FROM URL where URL_ID = '"
						+ pStrURLFromURLTable + "'");

		if (pStrURLQuery.next()) {
			pStrURL = pStrURLQuery.getString("URL");
			pStrURLDescription = pStrURLQuery.getString("Description");
			new DebugInterface(gOBJ).Debug("pStrURL:" + pStrURL);
			gOBJ.setgURLInfo(pStrURL);
			gOBJ.getWbDriver().get(pStrURL);
			if ((gOBJ.getWbDriver().getCurrentUrl()).toLowerCase().contains(pStrURL.toLowerCase())) {
				ReportStepToResultDB("Navigated to URL :"+ pStrURLDescription +": >>> " + pStrURL, "Pass");
				new DebugInterface(gOBJ)
				.Debug("Exiting the function NavigateURL()...");
			} else {
				dbConnection.CloseConnection();
				ReportStepToResultDB("Didnot navigate to URL >>> " + pStrURL
						+ " - instead displays :: "
						+ gOBJ.getWbDriver().getCurrentUrl(), "Fail");
				new DebugInterface(gOBJ).Debug("Did not navigate to URL >>> "
						+ pStrURL + " - instead displays :: "
						+ gOBJ.getWbDriver().getCurrentUrl() + " :::: Fail");
				new DebugInterface(gOBJ)
				.Debug("Exiting the function NavigateURL()...");
				new DoScreenActions(gOBJ).taskfail = true;
			}
		} else {
			dbConnection.CloseConnection();
			new DebugInterface(gOBJ)
			.Debug("URL setting is not defined in the database: "
					+ pStrDbValueField + " for environment: "
					+ pStrURLFromURLTable + ": Fail");
			ReportStepToResultDB("URL setting is not defined in the database: "
					+ pStrDbValueField + " for environment: "
					+ pStrURLFromURLTable, "Fail");
			new DoScreenActions(gOBJ).taskfail = true;
		}
		dbConnection.CloseConnection();
	}

	private void SetWebDriver(String sBro) {

		try {
			switch (sBro.toLowerCase()) {
			case "chrome":				
				new DebugInterface(gOBJ).Debug(":::::xxxxxxxxxxx Setting up the chrome driver for the execution :::::xxxxxxxxxxx");
				System.setProperty("webdriver.chrome.driver", gOBJ.getChromeDriverPath());

				try {
					gOBJ.setWbDriver(new ChromeDriver());

				} catch (Exception e) {
					new DebugInterface(gOBJ).Debug("Exception Msg: " + e.getMessage());
					ReportStepToResultDB("Chrome Browser is either corrupt or not available on this system","Fail");
					//ReportStepToResultDB(e.getMessage(),"Fail");
					new DebugInterface(gOBJ).Debug("Chrome Browser is either corrupt or not available on this system: Fail");
					new DoScreenActions(gOBJ).taskfail = true;
				}
				gOBJ.getWbDriver().manage().timeouts().pageLoadTimeout(Integer.parseInt(gOBJ.getPageLoadTime()), TimeUnit.SECONDS);
				gOBJ.getWbDriver().manage().deleteAllCookies();
				gOBJ.getWbDriver().manage().timeouts().implicitlyWait(Integer.parseInt(gOBJ.getObjectLoadTime()), TimeUnit.SECONDS);
				gOBJ.getWbDriver().manage().window().maximize();
				new DebugInterface(gOBJ).Debug(":::::xxxxxxxxxxx Browser Type Launched: Chrome");
				break;
			case "ie":
				/*
				 * For ERROR:- "org.openqa.selenium.WebDriverException: Unexpected error launching Internet Explorer. Protected Mode must be set to the same value (enabled or disabled) for all zones."
				 */ 
				//DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
				//ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				//return new InternetExplorerDriver(ieCapabilities);

				new DebugInterface(gOBJ).Debug(":::::xxxxxxxxxxx Setting up the ie driver for the execution :::::xxxxxxxxxxx");
				new DebugInterface(gOBJ).Debug("gOBJ.getIeDriverPath(): " + gOBJ.getIeDriverPath());
				System.setProperty("webdriver.ie.driver",gOBJ.getIeDriverPath());
				try {
					gOBJ.setWbDriver(new InternetExplorerDriver());
					//gOBJ.setWbDriver(new InternetExplorerDriver(ieCapabilities));
				} catch (Exception e) {
					new DebugInterface(gOBJ).Debug("Exception Msg: " + e.getMessage());
					ReportStepToResultDB("IE Browser is either corrupt or not available on this system","Fail");
					//ReportStepToResultDB(e.getMessage(),"Fail");
					new DebugInterface(gOBJ).Debug("IE Browser is either corrupt or not available on this system: Fail");
					new DoScreenActions(gOBJ).taskfail = true;
				}
				gOBJ.getWbDriver().manage().timeouts().pageLoadTimeout(Integer.parseInt(gOBJ.getPageLoadTime()), TimeUnit.SECONDS);
				gOBJ.getWbDriver().manage().deleteAllCookies();
				gOBJ.getWbDriver().manage().timeouts().implicitlyWait(Integer.parseInt(gOBJ.getObjectLoadTime()), TimeUnit.SECONDS);
				gOBJ.getWbDriver().manage().window().maximize();
				new DebugInterface(gOBJ).Debug(":::::xxxxxxxxxxx Browser Type Launched: IE");
				break;
			case "firefox":			
				new DebugInterface(gOBJ).Debug(":::::xxxxxxxxxxx Setting up the firefox driver for the execution :::::xxxxxxxxxxx");

				try {
					Random randomGenerator = new Random();					
					//int randomInt = 7053 + (randomGenerator.nextInt(Integer.parseInt(gOBJ.getNumberOfThreads())));
					int randomInt = 7071 + (randomGenerator.nextInt(Integer.parseInt(gOBJ.getNumberOfThreads())));
					new DebugInterface(gOBJ).Debug("randomInt: " + randomInt);

					//ProfilesIni allProfiles = new ProfilesIni();
					FirefoxProfile profile = new FirefoxProfile();;//allProfiles.getProfile("WebDriver");
					//profile.setPreference(/*FirefoxProfile.PORT_PREFERENCE*/"webdriver.firefox.port", randomInt);
					//profile.setAcceptUntrustedCertificates(true);
					//WebDriver wbDriver=new FirefoxDriver(profile);
					//gOBJ.setWbDriver(wbDriver);					
					gOBJ.setWbDriver(new FirefoxDriver(profile));


				} catch (Exception e) {
					new DebugInterface(gOBJ).Debug("Exception Msg: " + e.getMessage());
					ReportStepToResultDB("Firefox Browser is either corrupt or not available on this system","Fail");
					//ReportStepToResultDB(e.getMessage(),"Fail");
					new DebugInterface(gOBJ).Debug("Firefox Browser is either corrupt or not available on this system: Fail");
					new DoScreenActions(gOBJ).taskfail = true;
				}
				gOBJ.getWbDriver().manage().timeouts().pageLoadTimeout(Integer.parseInt(gOBJ.getPageLoadTime()), TimeUnit.SECONDS);
				gOBJ.getWbDriver().manage().deleteAllCookies();
				gOBJ.getWbDriver().manage().timeouts().implicitlyWait(Integer.parseInt(gOBJ.getObjectLoadTime()), TimeUnit.SECONDS);
				gOBJ.getWbDriver().manage().window().maximize();
				new DebugInterface(gOBJ).Debug(":::::xxxxxxxxxxx Browser Type Launched: FireFox");
				break;
			default:
				new DebugInterface(gOBJ).Debug(":::::xxxxxxxxxxx Web Driver set up for " + sBro + " is not supported.:::::xxxxxxxxxxx");
			}
		} catch (NullPointerException ee) {
			new DebugInterface(gOBJ).Debug("Browser value is not set... hence this exception");
		}

	}

	public void SetWebDriver() {
		gOBJ.setWbDriver(null);
	}

	/*
	 * public void StoreResultsForInstance(){
	 * gOBJ.setResultPath(gOBJ.getgStrAutomationPathNew() + "Result\\runID_" +
	 * gOBJ.getgNumRunID()); File file = new File(gOBJ.getResultPath()); if
	 * (!file.exists()) { if (file.mkdir()) { new DebugInterface(gOBJ).Debug
	 * ("Directory is created for results instance! = " + gOBJ.getResultPath());
	 * } else { new DebugInterface(gOBJ).Debug
	 * ("Failed to create directory for results instance! = " +
	 * gOBJ.getResultPath()); } } //copy debug from %temp% to above path File
	 * tempDir = new File(System.getProperty("java.io.tmpdir"));
	 * System.out.println("Temp file : " + tempDir); String tempDirPath =
	 * tempDir.getAbsolutePath() + "\\Run_mm-dd-yyyy_hh-mm-ss_XX"; File srcDir =
	 * new File(tempDirPath); File destDir = new File(gOBJ.getResultPath()); try
	 * { FileUtils.copyDirectory(srcDir, destDir); } catch (IOException e) { //
	 * TODO Auto-generated catch block e.printStackTrace();
	 * System.out.println("Copy directory error = " + e); } }
	 */


	public boolean isInteger(String s) {
		return isInteger(s,10);
	}

	public boolean isInteger(String s, int radix) {
		if(s.isEmpty()) return false;
		for(int i = 0; i < s.length(); i++) {
			if(i == 0 && s.charAt(i) == '-') {
				if(s.length() == 1) return false;
				else continue;
			}
			if(Character.digit(s.charAt(i),radix) < 0) return false;
		}
		return true;
	}
}
