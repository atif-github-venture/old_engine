package JoS.JAVAProjects;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

class WorkerThread implements Runnable {

	String strRunID, strLocator, strSubLocator, strTestSet, strBrowserType,
	strTestID, strEmailSettingID,strDBName,strUserName;

	public WorkerThread(String strRunID, String strLocator,
			String strSubLocator, String strTestSet, String strTestID,
			String strBrowserType, String strEmailSettingID,String strDBNameTemp,String strUserNameTemp) {
		this.strRunID = strRunID;
		this.strLocator = strLocator;
		this.strSubLocator = strSubLocator;
		this.strTestSet = strTestSet;
		this.strBrowserType = strBrowserType;
		this.strTestID = strTestID;
		this.strEmailSettingID = strEmailSettingID;
		this.strDBName=strDBNameTemp;
		this.strUserName=strUserNameTemp;
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + " Started ");
		try {
			MainDriver md = new MainDriver();
			md.DriveMain(strRunID, strLocator, strSubLocator, strTestSet,
					strTestID, strBrowserType,strDBName,strUserName);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " End.");
	}

	private void processCommand() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "";
	}
}

public class CommandLineExecution {
	private static final String KILL = "taskkill /F /IM ";
	private Logger log = null;

	public static void CleanUpProcess() {
		try {
			killProcess("IEDriverServer.exe");
			killProcess("conhost.exe");
			killProcess("chromedriver.exe");
			killProcess("iexplore.exe");
			killProcess("firefox.exe");
			killProcess("chrome.exe");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void killProcess(String serviceName) throws Exception {
		Runtime.getRuntime().exec(KILL + serviceName);
	}

	public static String GetUniqueIDs(ApplicationParams gOBJ) throws UnknownHostException {
		System.out.println("GetUniqueIDs() Entering");
		try {
			Thread.sleep(1);// to give some duration so that unique ID is
			// generated
		} catch (InterruptedException ex) {
			// Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE,
			// null, ex);
		}
		Date dt = new Date();
		InetAddress IP = InetAddress.getLocalHost();
		String str = IP.getHostAddress().replace(".", "");
		DB_Handler con = new DB_Handler(gOBJ);
		String strVal = "";
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(1000);
		String strGeneratedID = Long.toString(dt.getTime()) + randomInt + str;
		// System.out.println("Insert into run_instance_id_table (`run_instance_UI_Text`) VALUES ('"
		// + Long.toString(dt.getTime())+str+ "');");
		System.out.println("DB name: " + gOBJ.getSql_db_name());
		//DB_Handler con= new DB_Handler(gOBJ.getSql_db_name());
		boolean bInserted = con
				.ExecuteInsertQuery("run_instance_id_table (`run_instance_UI_Text`,`Last_Result`, `Tester`, `ExecutionTime`, `ExecutionDate`) VALUES ('"
						+ strGeneratedID + "','No_Run', 'NULL', 'NULL', 'NULL')");// ) VALUES ('" +
		// Long.toString(dt.getTime())+str+
		// "');");
		try {
			if (bInserted) {
				System.out
				.println("Select run_InstanceID from run_instance_id_table where run_instance_UI_Text='"
						+ strGeneratedID + "'");
				ResultSet rs = con.ExecuteSelectQuery("run_InstanceID",
						"run_instance_id_table where run_instance_UI_Text='"
								+ strGeneratedID + "'");
				if (rs.next())
					strVal = rs.getString("run_InstanceID");
				else {
					con.CloseConnection();
					System.out.println("GetUniqueIDs() Exiting");
					return "";
				}
			} else {
				con.CloseConnection();
				System.out.println("GetUniqueIDs() Exiting");
				return "";
			}
		} catch (SQLException ex) {
			con.CloseConnection();
		}
		con.CloseConnection();
		System.out.println("GetUniqueIDs() Exiting");
		return strVal;

	}

	public static void main(String[] args) throws SQLException, IOException {

		// CleanUpProcess();
		if (args.length == 0) {			
			String strLocator = "Policy_Center_AWS_L2-1";
			String strSubLocator = "PC_TestdatGeneration";
			String strTestSet = "Create_Home_Owners_Policy";
			String strTestID = "50";
			String strBrowserType = "firefox";
			String strDBName="slgw_pc_db";
			ApplicationParams gOBJ = new ApplicationParams();// .GetObject().LoadSettings();
			gOBJ.setSql_db_name(strDBName);
			gOBJ.LoadSettings();
			//gOBJ.setgStrTester("Tool Tester");
			String strRunID = GetUniqueIDs(gOBJ);
			System.out.println("runID generated: " + strRunID);

			try {
				MainDriver md = new MainDriver();
				md.DriveMain(strRunID, strLocator, strSubLocator,
						strTestSet, strTestID, strBrowserType,strDBName,"Tool Tester");
			} catch (ParseException e) {
				System.out
				.println("Error encountered @ command line Execution main() : "
						+ e);
				e.printStackTrace();
			}
		} else if (args.length > 0) {
			//Policy_Center_L2-0 SLGWPC_Forms PC_Login firefox QueueTest slgw_pc_db
			String strTestID;
			String strLocator = args[0];
			String strSubLocator = args[1];
			String strTestSet = args[2];
			String strBrowserType = args[3];
			String strEmailSettingID = args[4];
			String strDBName = args[5];
			String strTestSetID = "";
			ApplicationParams gOBJ = new ApplicationParams();// .GetObject().LoadSettings();
			gOBJ.setSql_db_name(strDBName);
			gOBJ.LoadSettings();
			//gOBJ.setgStrTester("Automated Trigger");
			DB_Handler con = new DB_Handler(gOBJ);
			ExecutorService executor = Executors.newFixedThreadPool(Integer
					.parseInt(gOBJ.getNumberOfThreads()));

			try {
				ResultSet rQueryGetTestSetID = con.ExecuteSelectQuery("*",
						"testsettable where locator = '" + strLocator
						+ "' and sublocator = '" + strSubLocator
						+ "' and testsetname = '" + strTestSet + "';");
				if (rQueryGetTestSetID.next()) {
					strTestSetID = rQueryGetTestSetID.getString("testsetID");
					System.out.println("strTestSetID: " + strTestSetID);
				}

				if (!strTestSetID.isEmpty()) {
					ResultSet rQueryListofUniqueTestID = con
							.ExecuteSelectQuery("distinct TestCase_ID",
									"testcasesfortestsettable where TestSetID = '"
											+ strTestSetID
											+ "' and Status = 'Active';");
					while (rQueryListofUniqueTestID.next()) {
						String strRunID = GetUniqueIDs(gOBJ);
						System.out.println("runID generated: " + strRunID);
						strTestID = rQueryListofUniqueTestID
								.getString("TestCase_ID");
						System.out
						.println("strTestID>>>>>>>>>>>>>>>>>>>>>> :::::: "
								+ strTestID);
						Runnable worker = new WorkerThread(strRunID,
								strLocator, strSubLocator, strTestSet,
								strTestID, strBrowserType, strEmailSettingID,strDBName,"Automated Trigger");
						executor.execute(worker);
					}

				}
				con.CloseConnection();
			} catch (SQLException e) {
				executor.shutdown();
				con.CloseConnection();
				e.printStackTrace();
			}
			executor.shutdown();
			try {
				executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
			} catch (InterruptedException e) {
				// ...
			}
			new EmailUtility(gOBJ).EmailLastRunReport(strEmailSettingID,
					strLocator, strSubLocator, strTestSet);

		}
	}

	public synchronized void ExecuteForQueueSystem(Object[] strObj) {
		String log4jConfPath = "log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		log = Logger.getLogger(getClass());
		// JOptionPane.showMessageDialog(null,"Welcome");

		log.info("Inside ExecuteForQueueSystem()...");
		String strTestID;
		String strLocator = strObj[0].toString();
		String strSubLocator = strObj[1].toString();
		String strTestSet = strObj[2].toString();
		String strBrowserType = strObj[3].toString();
		String strEmailSettingID = strObj[4].toString();
		String strDBName = strObj[5].toString();

		log.info("strLocator: " + strLocator);
		log.info("strSubLocator: " + strSubLocator);
		log.info("strTestSet: " + strTestSet);
		log.info("strBrowserType: " + strBrowserType);
		log.info("strEmailSettingID: " + strEmailSettingID);
		log.info("strDBName: " + strDBName);
		
		ApplicationParams gOBJ = new ApplicationParams();// .GetObject().LoadSettings();
		gOBJ.setSql_db_name(strDBName);
		gOBJ.LoadSettings();
		DB_Handler con = new DB_Handler(gOBJ);
		ExecutorService executor = Executors.newFixedThreadPool(Integer
				.parseInt(gOBJ.getNumberOfThreads()));
		

		String strTestSetID = "";
		try {
			ResultSet rQueryGetTestSetID = con.ExecuteSelectQuery("*",
					"testsettable where locator = '" + strLocator
					+ "' and sublocator = '" + strSubLocator
					+ "' and testsetname = '" + strTestSet + "';");
			if (rQueryGetTestSetID.next()) {
				strTestSetID = rQueryGetTestSetID.getString("testsetID");
			}

			if (!strTestSetID.isEmpty()) {
				ResultSet rQueryListofUniqueTestID = con.ExecuteSelectQuery(
						"distinct TestCase_ID",
						"testcasesfortestsettable where TestSetID = '"
								+ strTestSetID + "' and Status = 'Active';");
				while (rQueryListofUniqueTestID.next()) {
					String strRunID = null;
					try {
						strRunID = GetUniqueIDs(gOBJ);
					} catch (UnknownHostException e) {
						log.info("UnknownHostException: " + e.getMessage());
					}
					System.out.println("runID generated: " + strRunID);
					strTestID = rQueryListofUniqueTestID
							.getString("TestCase_ID");
					System.out
					.println("strTestID>>>>>>>>>>>>>>>>>>>>>> :::::: "
							+ strTestID);
					Runnable worker = new WorkerThread(strRunID, strLocator,
							strSubLocator, strTestSet, strTestID,
							strBrowserType, strEmailSettingID,strDBName,"Automated Trigger");
					executor.execute(worker);
				}

			}
			con.CloseConnection();
		} catch (SQLException e) {
			executor.shutdown();
			con.CloseConnection();
			e.printStackTrace();
		}
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			// ...
		}
		try {
			new EmailUtility(gOBJ).EmailLastRunReport(strEmailSettingID,
					strLocator, strSubLocator, strTestSet);
		} catch (SQLException e) {
			log.info("SQLException: " + e.getMessage());
		} catch (IOException e) {
			log.info("IOException: " + e.getMessage());
		}
		log.info("Back from  Email utility");
	}

	public CommandLineExecution() {
		System.out.println("default constructor");
		// String log4jConfPath = "log4j.properties";
		// PropertyConfigurator.configure(log4jConfPath);
		// log=Logger.getLogger(getClass());
	}

	public CommandLineExecution(int n) {
		System.out.println("constructor");
	}

	public void CallMe(Object[] objParams) {
		for (Object obj : objParams)
			System.out.println("Hello World!" + obj.toString());

	}

}
