package JoS.JAVAProjects.Reporting;

import java.io.File;

public class ReportSettings{
	private final String reportPath;
	private final String reportName;
	private String projectName;
	private int logLevel;
	public boolean generateExcelReports;
	public boolean generateHtmlReports;
	public boolean takeScreenshotFailedStep;
	public boolean takeScreenshotPassedStep;
	public boolean linkScreenshotsToTestLog = false;
	public boolean linkTestLogsToSummary = false;
	public boolean consolidateScreenshotsInWordDoc = false;
	private String dateFormatString = "dd-MMM-yyyy hh:mm:ss a";

	public String getReportPath(){
		return reportPath;
	}
	public String getReportName(){
		return reportName;
	}

	public String getProjectName(){
		return projectName;
	}

	public void setProjectName(String projectName){
		this.projectName = projectName;
	}

	public int getLogLevel(){
		return logLevel;
	}

	public void setLogLevel(int logLevel){
		if (logLevel < 0) {
			logLevel = 0;
		}
		if (logLevel > 5) {
			logLevel = 5;
		}
		this.logLevel = logLevel;
	}

	public String getDateFormatString(){
		return dateFormatString;
	}

	public void setDateFormatString(String dateFormatString){
		this.dateFormatString = dateFormatString;
	}

	public ReportSettings(String reportPath, String reportName){
		boolean reportPathExists = new File(reportPath).isDirectory();
		if (!reportPathExists){
			throw new FrameworkException("The given report path does not exist!");
		}
		this.reportPath = reportPath;
		this.reportName = reportName;
	}
}