package JoS.JAVAProjects;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author kum8449
 */
public class DB_Handler {
	private String serverpath = "";
	String strDBName = "";
	private String Dbname = "";
	private String port_number = "";
	private String sUserName = "";
	private String sPassword = "";
	private String connstring;
	public Connection connextion = null;
	private Logger log = null;
	static int counter = 0;
	ApplicationParams gOBJ=null;
	
	DB_Handler(ApplicationParams gOBJTemp) {
		String log4jConfPath = "log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		log = Logger.getLogger(getClass());
		gOBJ=gOBJTemp;
		strDBName = gOBJ.getSql_db_name();
	}
	DB_Handler(String  strDBNAmeTemp) {
		String log4jConfPath = "log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		log = Logger.getLogger(getClass());
		strDBName=strDBNAmeTemp;
	}
	public synchronized int ExecuteUpdate(String sUpdateQuery) {
		// new DebugInterface().Debug ("Inside ResultSet ExecuteQuery()");
		Statement stmt = null;
		int result;
		try {
			if (strDBName.isEmpty() || connextion== null)
				GetConnection();
			stmt = connextion.createStatement();
			result = stmt.executeUpdate(sUpdateQuery);
			// new DebugInterface().Debug
			// ("Updated the result DB for this step");
			// new DebugInterface().Debug
			// ("Exiting ResultSet ExecuteQuery()...");
			return result;
		} catch (SQLException exp) {
			System.out.println("SQL exception occurred");
			// new DebugInterface().Debug
			// ("Exiting ResultSet ExecuteQuery():: SQL exception occurred >> "
			// + exp);
			return 0;
		}
		// finally{
		// }
	}

	public synchronized ResultSet ExecuteQuery(String sQuery) {
		Statement stmt = null;
		ResultSet result = null;
		try {
			if (connextion == null)
				GetConnection();
			stmt = connextion.createStatement();
			result = stmt.executeQuery(sQuery);
			// log.debug(sQuery);
			return result;
		} catch (SQLException exp) {
			// log.debug("Caught exception for ExecuteQuery with query: "+sQuery+" Exception :"+exp.getMessage());
			System.out.println("SQL exception occurred:" + exp.getMessage());
			return null;
		} finally {
		}
	}

	public synchronized boolean Execute(String sQuery) {
		Statement stmt = null;
		boolean result = false;
		System.out.println("sQuery:" + sQuery);
		try {
			if (strDBName.isEmpty() || connextion== null)
				GetConnection();
			stmt = connextion.createStatement();
			System.out.println(sQuery);
			result = stmt.execute(sQuery);
			// log.debug(sQuery);
			return true;
		} catch (SQLException exp) {
			// log.debug("Caught exception for Execute with query: "+sQuery+" Exception :"+exp.getMessage());
			System.out.println("SQL exception occurred:" + exp.getMessage());
			return false;
		} finally {
		}
	}

	public boolean ExecuteInsertQuery(String strQuery) {
		if (strDBName.isEmpty() || connextion== null)
			GetConnection();
		return Execute("INSERT INTO `" + strDBName + "`." + strQuery);
	}

	public boolean ExecuteUpdateQuery(String strQuery) {
		if (strDBName.isEmpty() || connextion== null)
			GetConnection();
		return Execute("UPDATE `" + strDBName + "`." + strQuery);
	}

	public boolean ExecuteDeleteQuery(String strQuery) {
		if (strDBName.isEmpty() || connextion== null)
			GetConnection();
		return Execute("DELETE FROM `" + strDBName + "`." + strQuery);
	}

	public ResultSet ExecuteSelectQuery(String strFromStmt, String strQuery) {
		// log.info("Inside ExecuteSelectQuery()...");
		System.out.println("strQuery: " + strQuery);
		if (strDBName.isEmpty() || connextion== null)
			GetConnection();
		// log.info("Inside ExecuteSelectQuery()...");
		return ExecuteQuery("SELECT " + strFromStmt + " FROM `" + strDBName
				+ "`." + strQuery);
	}

	public synchronized void GetConnection() {
		// log.info("Inside GetConnection()...");
		FileInputStream input;
		counter++;
		// log.info("No. of DB_Objects opened:"+counter);
		try {
			Properties prop = new Properties();

			input = new FileInputStream((new File(".").getCanonicalPath())
					+ "\\settings.properties");
			prop.load(input);
			serverpath = prop.getProperty("DB_SERVER_IP_ADDRESS");
			// log.info(serverpath);
			//strDBName = gOBJ.getSql_db_name();//Sql_db_name;//prop.getProperty("SQL_DB_NAME");
			// log.info(strDBName);
			port_number = prop.getProperty("DB_SERVER_PORT");
			// log.info(port_number);
			sUserName = prop.getProperty("DB_SERVER_USERNAME");
			// log.info(sUserName);
			sPassword = prop.getProperty("DB_SERVER_PASSWORD");
			// log.info(sPassword);
			// log.info("For connection: DB name- " + strDBName);
			Properties connectProp = new Properties();
			connectProp.put("user", sUserName);
			connectProp.put("password", sPassword);
			connstring = "jdbc:mysql://" + serverpath + ":" + port_number + "/"
					+ strDBName;
			connextion = DriverManager.getConnection(connstring, connectProp);
			// log.info("Exiting GetConnection()...");
		} catch (Exception se) {
			System.out.println("SQL exception occurred:" + se.getMessage());
			// log.info("SQL exception occurred:"+se.getMessage());
			// log.info("Exiting GetConnection()...");
		}
	}

	public synchronized void CloseConnection() {
		try {
			if (connextion != null) {
				connextion.close();
				connextion = null;
				// log.info("No. of DB_Objects closed:"+counter);
				counter--;

			}
		} catch (SQLException se) {
			System.out.println("SQL exception occurred:" + se.getMessage());
		}
	}

}
