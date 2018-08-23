package JoS.JAVAProjects;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class EmailUtility {
	// final static Logger log =
	// Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	ApplicationParams gOBJ = null;
	ResultSet rQueryLatestRunID = null;
	ResultSet rQueryLastRunIDSet = null;
	boolean HasExecutionFailed = false;
	private String strEmailBody = "";
	private Logger log = null;

	EmailUtility(ApplicationParams gOBJTemp) {
		gOBJ = gOBJTemp;
		// gOBJ.LoadSettings();
		String log4jConfPath = "log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		log = Logger.getLogger(getClass());
	}

	public void EmailLastRunReport(String EmailSettingID, String pLocator,
			String pSubLocator, String pTestSet) throws SQLException,
			IOException {
		String QueryLatestRunID = null;
		String QueryLastRunIDSet = null;
		String QueryListofUniqueTestID = null;
		int rowCount, iFailCounter = 0;
		String strTestID, pTestID, strTaskID, strExecution_Order, strActual_Result, strStatus, strExecDate, strExecTime;
		ResultSet rQueryListofUniqueTestID = null;
		ResultSet rQueryTestSetID = null;
		int rowCount1 = 0;
		String EntireStepsForEmail = "TestID,TestName,TaskID,Execution_Order,Actual_Result,Status,ExecutionDate,ExecutionTime,RunID\n";
		EmailUtilityLogger(EntireStepsForEmail);
		EmailUtilityLogger("gOBJ.getSql_db_name(): " + gOBJ.getSql_db_name());
		DB_Handler con = new DB_Handler(gOBJ);
		rQueryTestSetID = con.ExecuteSelectQuery("*",
				"testsettable where testsetname = '" + pTestSet + "';");

		String pStrTestSetID = "";
		if (rQueryTestSetID.next()) {
			pStrTestSetID = rQueryTestSetID.getString("testsetID");
		}
		if (pStrTestSetID.isEmpty()) {
			EmailUtilityLogger("testsetname is empty");
			con.CloseConnection();
			return;
		} else {
			EmailUtilityLogger("testsetname is NOT empty");
		}

		// get the list of all unique Test IDs within a test set
		QueryListofUniqueTestID = "testcasesfortestsettable where TestSetID = '"
				+ pStrTestSetID + "' and Status = 'Active';";
		EmailUtilityLogger("QueryListofUniqueTestID: "
				+ QueryListofUniqueTestID);
		rQueryListofUniqueTestID = con.ExecuteSelectQuery("*",
				QueryListofUniqueTestID);
		rowCount1 = new ExecuteAutomation(gOBJ)
				.getRowCount(rQueryListofUniqueTestID);
		if (rowCount1 != 0) {
			EmailUtilityLogger("The total record received for unique list of test ids for a testset:::"
					+ rowCount1);
			if (rQueryListofUniqueTestID.next()) {
				for (int j = 0; j < rowCount1; j++) {
					pTestID = rQueryListofUniqueTestID.getString("TestCase_ID");

					// get the latest run ID for the given Test ID/test
					// set/locator/sublocator
					QueryLatestRunID = "run_instance_result where FieldLocator = '"
							+ pLocator
							+ "' and SubLocator = '"
							+ pSubLocator
							+ "' and Test_Set = '"
							+ pTestSet
							+ "' and TestID = '" + pTestID + "';";
					EmailUtilityLogger("QueryLatestRunID: " + QueryLatestRunID);
					rQueryLatestRunID = con.ExecuteSelectQuery(
							"MAX(RunID) AS latestRun", QueryLatestRunID);
					rQueryLatestRunID.first();
					String sQueryLatestRunID = rQueryLatestRunID
							.getString("latestRun");
					EmailUtilityLogger("sQueryLatestRunID: "
							+ sQueryLatestRunID);

					// Get the result set for the last run ID
					QueryLastRunIDSet = "run_instance_result where runID = '"
							+ sQueryLatestRunID
							+ "' order by Execution_Date and Execution_Time;";
					rQueryLastRunIDSet = con.ExecuteSelectQuery("*",
							QueryLastRunIDSet);
					rowCount = new ExecuteAutomation(gOBJ)
							.getRowCount(rQueryLastRunIDSet);
					if (rowCount != 0) {
						EmailUtilityLogger("The total record received for run id --"
								+ sQueryLatestRunID + ":::" + rowCount);
						if (rQueryLastRunIDSet.next()) {
							for (int i = 0; i < rowCount; i++) {
								strTestID = rQueryLastRunIDSet
										.getString("TestID");
								strTaskID = rQueryLastRunIDSet
										.getString("TaskID");
								strExecution_Order = rQueryLastRunIDSet
										.getString("Execution_Order");
								strActual_Result = rQueryLastRunIDSet
										.getString("Actual_Result");
								strStatus = rQueryLastRunIDSet
										.getString("Status");
								strExecDate = rQueryLastRunIDSet
										.getString("Execution_Date");
								strExecTime = rQueryLastRunIDSet
										.getString("Execution_Time");
								EntireStepsForEmail = EntireStepsForEmail
										+ "\n" + strTestID + ","
										+ GetTestName(strTestID) + ","
										+ strTaskID + "," + strExecution_Order
										+ "," + strActual_Result + ","
										+ strStatus + "," + strExecDate + "," + strExecTime + "," + sQueryLatestRunID;
								if (strStatus.equalsIgnoreCase("fail")) {
									EmailUtilityLogger("FailedStep: "
											+ strTestID + ";"
											+ GetTestName(strTestID) + ";"
											+ strTaskID + ";"
											+ strExecution_Order + ";"
											+ strActual_Result + ";"
											+ strStatus);
									strEmailBody = strEmailBody 
											+ strTestID+ ";"
											+ GetTestName(strTestID) + ";"
											+ strTaskID + ";"
											+ strExecution_Order + ";"
											+ strActual_Result + ";"
											+ strStatus + ";"
											+ strExecDate + ";"
											+ strExecTime + ";"
											+ sQueryLatestRunID + "#";
									iFailCounter++;
									HasExecutionFailed = true;
								}
								 rQueryLastRunIDSet.next();
							}
						}
					}
					 rQueryListofUniqueTestID.next();
				}
			}
		}
		EmailUtilityLogger("EntireStepsForEmail:" + EntireStepsForEmail);
		File tempDir = new File(System.getProperty("java.io.tmpdir"));
		EmailUtilityLogger("Temp file : " + tempDir);
		String tempDirPath = tempDir.getAbsolutePath();
		File DestPathout = new File(tempDirPath + "\\EntireStepsForEmail.csv");
		FileOutputStream fos = new FileOutputStream(DestPathout, true);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		try {
			bw.write(EntireStepsForEmail);
			bw.newLine();
			bw.close();
		} catch (IOException e1) {
			con.CloseConnection();
			e1.printStackTrace();
		}
		con.CloseConnection();
		EmailUtilityLogger("strEmailBody: " + strEmailBody);
		SendTestResultsEmail(strEmailBody, EmailSettingID, HasExecutionFailed,
				pLocator, EmailSettingID, iFailCounter);

	}

	public String GetTestName(String pstrTestID) throws SQLException {
		ResultSet rQueryTestName = null;
		DB_Handler con = new DB_Handler(gOBJ);
		String QueryTestName = "business_flow where  Test_ID = '" + pstrTestID
				+ "';";
		EmailUtilityLogger("QueryTestName: " + QueryTestName);
		rQueryTestName = con.ExecuteSelectQuery("*", QueryTestName);
		rQueryTestName.first();
		String sTestName = rQueryTestName.getString("Business_Flow");
		con.CloseConnection();
		return sTestName;
	}

	public void SendTestResultsEmail(String pstrEmailBody,
			String EmailSettingID, boolean TestSetStatus, String pLocator,
			String pEmailSettingID, int iFailCounter) throws IOException,
			SQLException {
		String QueryEmailSetting = "";
		String QueryEnvironmentSetting = "";
		ResultSet rQueryEmailSetting = null;
		ResultSet rQueryEnvironmentSetting = null;
		String strEmailTo;
		String strEmailFrom;
		String strEmailSubject;
		// String strEmailCCList;
		String strOutlookHostName;
		String strEnvironmentToTest;
		String strProjectName;
		String SubjectStatus;
		String strDocumentationPath;
		String strProjectContactEmails;
		DB_Handler con = new DB_Handler(gOBJ);

		QueryEmailSetting = "email_settings where SettingID = '"
				+ EmailSettingID + "';";
		EmailUtilityLogger("QueryEmailSetting: " + QueryEmailSetting);
		rQueryEmailSetting = con.ExecuteSelectQuery("*", QueryEmailSetting);
		rQueryEmailSetting.first();

		strEmailTo = rQueryEmailSetting.getString("EmailToList");
		strEmailFrom = rQueryEmailSetting.getString("EmailFrom");
		strEmailSubject = rQueryEmailSetting.getString("EmailSubject");
		// strEmailCCList = rQueryEmailSetting.getString("EmailCCList");
		strOutlookHostName = rQueryEmailSetting.getString("OutlookHostName");
		strDocumentationPath = rQueryEmailSetting
				.getString("DocumentationPath");
		strProjectContactEmails = rQueryEmailSetting
				.getString("ProjectContactEmails");

		// construct the email subject status as pass or fail
		if (HasExecutionFailed) {
			SubjectStatus = "FAILED";
		} else {
			SubjectStatus = "PASSED";
		}

		QueryEnvironmentSetting = "environmentvariable where LocatorFolder = '"
				+ pLocator + "';";
		EmailUtilityLogger("QueryEnvironmentSetting: "
				+ QueryEnvironmentSetting);
		rQueryEnvironmentSetting = con.ExecuteSelectQuery("*",
				QueryEnvironmentSetting);
		rQueryEnvironmentSetting.first();
		strEnvironmentToTest = rQueryEnvironmentSetting
				.getString("EnvironmentToTest");
		strProjectName = rQueryEnvironmentSetting.getString("ProjectName");

		// Construct the email subject based on environment and project name
		strEmailSubject = SubjectStatus + ": " + strProjectName + " - "
				+ strEmailSubject + " - " + strEnvironmentToTest;

		Properties props = new Properties();
		//props.put("mail.smtp.host", strOutlookHostName);
		//props.put("mail.smtp.auth", "false");
		//Session session = Session.getDefaultInstance(props, null);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", strOutlookHostName);
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("jostestingtool@gmail.com", "Jos@43230");
			}
		  });
		session.setDebug(true);

		try {
			MimeMessage message = new MimeMessage(session);
			Multipart multipart = new MimeMultipart();
			message.saveChanges();
			message.setFrom(new InternetAddress(strEmailFrom));
			message.setSubject(strEmailSubject);
			message.addRecipients(Message.RecipientType.TO,
					InternetAddress.parse(strEmailTo));

			// creates body part for the message
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			StringBuffer sb = new StringBuffer();
			if (HasExecutionFailed) {
				sb.append(FormatMail_Fail(pstrEmailBody, iFailCounter,
						strProjectContactEmails, strDocumentationPath));
			} else {
				sb.append(FormatMail_Pass(strProjectContactEmails,
						strDocumentationPath));
			}
			EmailUtilityLogger("sb:\n" + sb);
			messageBodyPart.setContent(sb.toString(), "text/html");

			// Get the local path
			File tempDir = new File(System.getProperty("java.io.tmpdir"));
			EmailUtilityLogger("Temp file : " + tempDir);
			String tempDirPath = tempDir.getAbsolutePath();

			// Creates body part for the attachment
			MimeBodyPart attachPart = new MimeBodyPart();
			String attachFile = tempDirPath + "\\EntireStepsForEmail.csv";

			DataSource source = new FileDataSource(attachFile);
			attachPart.setDataHandler(new DataHandler(source));
			attachPart.setFileName(new File(attachFile).getName());

			// Adds parts to the multipart
			multipart.addBodyPart(attachPart);
			multipart.addBodyPart(messageBodyPart);

			// Sets the multipart as message's content
			message.setContent(multipart);
			Thread.currentThread().setContextClassLoader(
					getClass().getClassLoader());
			Transport.send(message);
			new File(attachFile).delete();
			con.CloseConnection();
		} catch (MessagingException e) {
			e.printStackTrace();
			con.CloseConnection();
		}
	}

	public StringBuffer FormatMail_Fail(String pstrEmailBody, int iFailCounter,
			String strProjectContactEmail, String DocumentatioPath) {
		StringBuffer sb = new StringBuffer();
		int iWidth = 0;
		String strStep = "";
		sb.append("<HTML>");
		sb.append("<body>");
		sb.append("<p align='Left'> ***********************************************************<br>************** This is an automatically generated email ************<br>***********************************************************</p>");
		sb.append("<b> Smoke Test Failed - # of Failures :</b>");
		sb.append("<b><font color = \"red\">" + iFailCounter + "</font></b>");

		// Setting the mail format for the headers
		sb.append(emailHeader());

		// Format the failed message in table format
		String[] Split_pstrEmailBody = pstrEmailBody.split("#");
		for (int iloop = 0; iloop < Split_pstrEmailBody.length; iloop++) {
			EmailUtilityLogger("Going for loop: " + iloop);
			String[] Split_pstrEmailBody1 = Split_pstrEmailBody[iloop]
					.split(";");
			sb.append("<TR>");
			for (int jloop = 0; jloop < Split_pstrEmailBody1.length; jloop++) {
				switch (jloop) {
				case 0:
					iWidth = 150;
					break;
				case 1:
					iWidth = 300;
					break;
				case 2:
					iWidth = 250;
					break;
				case 3:
					iWidth = 100;
					break;
				case 4:
					iWidth = 2000;
					break;
				case 5:
					iWidth = 150;
					break;
				case 6:
					iWidth = 150;
					break;
				case 7:
					iWidth = 150;
					break;
				case 8:
					iWidth = 150;
					break;
				}
				if (Split_pstrEmailBody1[jloop].contains("#")) {
					strStep = Split_pstrEmailBody1[jloop].replace("#", ",");
				} else {
					strStep = Split_pstrEmailBody1[jloop];
				}
				sb.append("<td width='" + iWidth + "' colspan='3' >" + strStep
						+ "</TD>");
				strStep = "";
			}
			sb.append("</TR>");
		}

		sb.append(emailFooter(strProjectContactEmail, DocumentatioPath));
		return sb;
	}

	public StringBuffer FormatMail_Pass(String strProjectContactEmail,
			String DocumentatioPath) {
		StringBuffer sb = new StringBuffer();
		sb.append("<HTML>");
		sb.append("<body>");
		sb.append("<p align='Left'> ***********************************************************<br>************** This is an automatically generated email ************<br>***********************************************************</p>");
		sb.append("<br align='Top'>");
		sb.append(emailFooter(strProjectContactEmail, DocumentatioPath));
		return sb;
	}

	public StringBuffer emailHeader() {
		// header of the email html body
		StringBuffer sb = new StringBuffer();
		sb.append("<div align=\"Center\">");
		sb.append("<br>");
		sb.append("<table border='1' style='border-collapse: collapse'>");
		sb.append("<tr bgcolor='#DDDDDD' bordercolor='#DDDDDD'>");
		sb.append("<td width='150' colspan='3' ><H3>Test ID</H3></td>");
		sb.append("<td width='300' colspan='3' ><H3>Test Name</H3></td>");
		sb.append("<td width='250' colspan='3' ><H3>Task Name</H3></td>");
		sb.append("<td width='100' colspan='3' ><H3>Step #</H3></td>");
		sb.append("<td width='2000' colspan='3' ><H3>Actual Result</H3></td>");
		sb.append("<td width='150' colspan='3' ><H3>Status</H3></td>");
		sb.append("<td width='150' colspan='3' ><H3>Execution Date</H3></td>");
		sb.append("<td width='150' colspan='3' ><H3>Execution Time</H3></td>");
		sb.append("<td width='150' colspan='3' ><H3>Run ID</H3></td>");
		return sb.append("</tr>");
	}

	public StringBuffer emailFooter(String strProjectContactEmail,
			String DocumentatioPath) {
		// footer of the email html body
		StringBuffer sb = new StringBuffer();
		sb.append("</br>");
		sb.append("</table></div>");
		sb.append("<div align=\"Left\">");
		sb.append("<br>");
		sb.append("<table border='1' style='border-collapse: collapse'>");
		sb.append("<tr bgcolor='#DDDDDD' bordercolor='#DDDDDD'>");
		sb.append("<td width='600' colspan='5' align = 'Left'>In case of questions pertaining to automation, please email to the below contacts:</TD></TR>");
		sb.append("<tr><td width='600' colspan='5' align = 'Left'>"
				+ strProjectContactEmail + "</TD></TR>");
		sb.append("</table>");
		sb.append("</br>");
		sb.append("</div>");
		sb.append("<table border='5' style='border-collapse: collapse'>");
		sb.append("<tr><TD width='200' colspan='5' align = 'center'>Documentation</TD>");
		sb.append("<TD width='700' colspan='5' align = 'Left'>"
				+ DocumentatioPath + "</TD>");
		sb.append("</tr></table><p></p>");
		sb.append("<br align = \"Left\"><font color=\"green\">Thanks</font>");
		sb.append("<br align = \"Left\"><font color=\"green\">Automation Team</font></br>");
		sb.append("</body>");
		sb.append("</HTML>");
		return sb;
	}
	public boolean SendEmailWithAttachment(String EmailSettingID, String PathOfAttchment) throws IOException,SQLException {
		String QueryEmailSetting = "";
		String QueryEnvironmentSetting = "";
		ResultSet rQueryEmailSetting = null;
		ResultSet rQueryEnvironmentSetting = null;
		String strEmailTo;
		String strEmailFrom;
		String strEmailSubject;
		// String strEmailCCList;
		String strOutlookHostName;
		String strEnvironmentToTest;
		String strProjectName;
		DB_Handler con = new DB_Handler(gOBJ);

		QueryEmailSetting = "email_settings where SettingID = '"
				+ EmailSettingID + "';";
		EmailUtilityLogger("QueryEmailSetting: " + QueryEmailSetting);
		rQueryEmailSetting = con.ExecuteSelectQuery("*", QueryEmailSetting);
		rQueryEmailSetting.first();

		strEmailTo = rQueryEmailSetting.getString("EmailToList");
		strEmailFrom = rQueryEmailSetting.getString("EmailFrom");
		strEmailSubject = rQueryEmailSetting.getString("EmailSubject");
		// strEmailCCList = rQueryEmailSetting.getString("EmailCCList");
		strOutlookHostName = rQueryEmailSetting.getString("OutlookHostName");

		QueryEnvironmentSetting = "environmentvariable where LocatorFolder = '"
				+ gOBJ.getgStrReleaseFolder().trim() + "';";
		EmailUtilityLogger("QueryEnvironmentSetting: "
				+ QueryEnvironmentSetting);
		rQueryEnvironmentSetting = con.ExecuteSelectQuery("*",
				QueryEnvironmentSetting);
		rQueryEnvironmentSetting.first();
		strEnvironmentToTest = rQueryEnvironmentSetting
				.getString("EnvironmentToTest");
		strProjectName = gOBJ.getStrProjectName();

		// Construct the email subject based on environment and project name
		strEmailSubject = strProjectName + " - "	+ strEmailSubject + " - " + strEnvironmentToTest;

		Properties props = new Properties();
		props.put("mail.smtp.host", strOutlookHostName);
		props.put("mail.smtp.auth", "false");
		Session session = Session.getDefaultInstance(props, null);
		session.setDebug(true);

		try {
			MimeMessage message = new MimeMessage(session);
			Multipart multipart = new MimeMultipart();
			message.saveChanges();
			message.setFrom(new InternetAddress(strEmailFrom));
			message.setSubject(strEmailSubject);
			message.addRecipients(Message.RecipientType.TO,
					InternetAddress.parse(strEmailTo));

			// creates body part for the message
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			StringBuffer sb = new StringBuffer();
			
			EmailUtilityLogger("sb:\n" + sb);
			messageBodyPart.setContent(sb.toString(), "text/html");

			// Creates body part for the attachment
			MimeBodyPart attachPart = new MimeBodyPart();
			//input = new FileInputStream((new File (".").getCanonicalPath())+"\\settings.properties");
			String attachFile = new File (".").getCanonicalPath() + "\\" + gOBJ.getStrProjectName() + "\\" + PathOfAttchment;
			EmailUtilityLogger("attachFile:\n" + attachFile);
			if (new File(attachFile).exists()){
				DataSource source = new FileDataSource(attachFile);
				attachPart.setDataHandler(new DataHandler(source));
				attachPart.setFileName(new File(attachFile).getName());

				// Adds parts to the multipart
				multipart.addBodyPart(attachPart);
				multipart.addBodyPart(messageBodyPart);

				// Sets the multipart as message's content
				message.setContent(multipart);
				Thread.currentThread().setContextClassLoader(
						getClass().getClassLoader());
				Transport.send(message);	
				con.CloseConnection();
				HasExecutionFailed = true;
			}else{
				con.CloseConnection();
				HasExecutionFailed = false;
			}

		} catch (MessagingException e) {
			EmailUtilityLogger("MessagingException:" + e.getMessage() );
			HasExecutionFailed = false;
			con.CloseConnection();
		}
		return HasExecutionFailed;
	}
	
	public void EmailUtilityLogger(String msg) {
		log.info(msg);

	}
}
