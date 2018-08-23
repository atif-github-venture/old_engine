package JoS.JAVAProjects.Reporting;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Report
{
	private int nStepsPassed = 0; private int nStepsFailed = 0;
	private int nTestsPassed = 0; private int nTestsFailed = 0;

	private List<ReportType> reportTypes = new ArrayList();

	public String testStatus = "Passed";
	private ReportSettings reportSettings;
	private ReportTheme reportTheme;
	private int stepNumber;
	private String failureDescription;

	public String getTestStatus() {
		return testStatus;
	}

	public String getFailureDescription()
	{
		return failureDescription;
	}

	public Report(ReportSettings reportSettings, ReportTheme reportTheme)
	{
		this.reportSettings = reportSettings;
		this.reportTheme = reportTheme;
	}

	public void initialize()
	{
		if (reportSettings.generateExcelReports)
		{
			new File(reportSettings.getReportPath());// + Util.getFileSeparator() + "Excel Results").mkdir();

			ExcelReport excelReport = new ExcelReport(reportSettings, reportTheme);
			reportTypes.add(excelReport);
		}

		if (reportSettings.generateHtmlReports)
		{
			new File(reportSettings.getReportPath());// + Util.getFileSeparator() + "HTML Results").mkdir();

			HtmlReport htmlReport = new HtmlReport(reportSettings, reportTheme);
			reportTypes.add(htmlReport);
		}


		new File(reportSettings.getReportPath());// + Util.getFileSeparator() + "Screenshots").mkdir();
	}

	public void initializeTestLog()
	{
		if (reportSettings.getReportName().isEmpty()) {
			throw new FrameworkException("The report name cannot be empty!");
		}

		for (int i = 0; i < reportTypes.size(); i++) {
			((ReportType)reportTypes.get(i)).initializeTestLog();
		}
	}


	public void addTestLogHeading(String heading)
	{
		for (int i = 0; i < reportTypes.size(); i++) {
			((ReportType)reportTypes.get(i)).addTestLogHeading(heading);
		}
	}


	public void addTestLogSubHeading(String subHeading1, String subHeading2, String subHeading3, String subHeading4)
	{
		for (int i = 0; i < reportTypes.size(); i++) {
			((ReportType)reportTypes.get(i)).addTestLogSubHeading(subHeading1, subHeading2, 
					subHeading3, subHeading4);
		}
	}


	public void addTestLogTableHeadings()
	{
		for (int i = 0; i < reportTypes.size(); i++) {
			((ReportType)reportTypes.get(i)).addTestLogTableHeadings();
		}
	}

	public void addTestLogSection(String section)
	{
		for (int i = 0; i < reportTypes.size(); i++) {
			((ReportType)reportTypes.get(i)).addTestLogSection(section);
		}

		stepNumber = 1;
	}


	public void addTestLogSubSection(String subSection)
	{
		for (int i = 0; i < reportTypes.size(); i++) {
			((ReportType)reportTypes.get(i)).addTestLogSubSection(subSection);
		}
	}

	public void updateTestLog(String stepName, String stepDescription, Status stepStatus)
	{
		if (stepStatus.equals(Status.FAIL)) {
			testStatus = "Failed";

			if (failureDescription == null) {
				failureDescription = stepDescription;
			} else {
				failureDescription = (failureDescription + "; " + stepDescription);
			}

			nStepsFailed += 1;
		}

		if (stepStatus.equals(Status.PASS)) {
			nStepsPassed += 1;
		}

		if (stepStatus.ordinal() <= reportSettings.getLogLevel())
		{
			String screenshotName = null;

			if ((stepStatus.equals(Status.FAIL)) && 
					(reportSettings.takeScreenshotFailedStep)) {
				screenshotName = 
						reportSettings.getReportName() + "_" + Util.getCurrentFormattedTime(reportSettings.getDateFormatString()).replace(" ", "_").replace(":", "-") + ".png";
				takeScreenshot(reportSettings.getReportPath() + 
						Util.getFileSeparator() + 
						Util.getFileSeparator() + screenshotName);
			}


			if ((stepStatus.equals(Status.PASS)) && 
					(reportSettings.takeScreenshotPassedStep)) {
				screenshotName = 
						reportSettings.getReportName() + "_" + Util.getCurrentFormattedTime(reportSettings.getDateFormatString()).replace(" ", "_").replace(":", "-") + ".png";
				takeScreenshot(reportSettings.getReportPath() + 
						Util.getFileSeparator() + "Screenshots" + 
						Util.getFileSeparator() + screenshotName);
			}


			if (stepStatus.equals(Status.SCREENSHOT))
			{
				screenshotName = 
						reportSettings.getReportName() + "_" + Util.getCurrentFormattedTime(reportSettings.getDateFormatString()).replace(" ", "_").replace(":", "-") + ".png";
				takeScreenshot(reportSettings.getReportPath() + 
						Util.getFileSeparator() + "Screenshots" + 
						Util.getFileSeparator() + screenshotName);
			}

			for (int i = 0; i < reportTypes.size(); i++) {
				((ReportType)reportTypes.get(i)).updateTestLog(Integer.toString(stepNumber), stepName, stepDescription, stepStatus, screenshotName);
			}

			stepNumber += 1;
		}
	}

	protected void takeScreenshot(String screenshotPath)
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		Rectangle rectangle = new Rectangle(0, 0, screenSize.width, screenSize.height);
		Robot robot;	
		try
		{
			robot = new Robot();
		} catch (AWTException e) { //Robot robot;
			e.printStackTrace();
			throw new FrameworkException("Error while creating Robot object (for taking screenshot)");
		}
		//Robot robot;
		BufferedImage screenshotImage = robot.createScreenCapture(rectangle);
		File screenshotFile = new File(screenshotPath);
		try
		{
			ImageIO.write(screenshotImage, "jpg", screenshotFile);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while writing screenshot to .jpg file");
		}
	}

	public void addTestLogFooter(String executionTime)
	{
		for (int i = 0; i < reportTypes.size(); i++) {
			((ReportType)reportTypes.get(i)).addTestLogFooter(executionTime, nStepsPassed, nStepsFailed);
		}
	}

	public void consolidateScreenshotsInWordDoc()
	{
		String screenshotsConsolidatedFolderPath = reportSettings.getReportPath() + 
				Util.getFileSeparator() + 
				"Screenshots (Consolidated)";
		new File(screenshotsConsolidatedFolderPath).mkdir();

		WordDocumentManager documentManager = 
				new WordDocumentManager(screenshotsConsolidatedFolderPath, 
						reportSettings.getReportName());

		String screenshotsFolderPath = reportSettings.getReportPath() + 
				Util.getFileSeparator() + 
				"Screenshots";
		File screenshotsFolder = new File(screenshotsFolderPath);

		FilenameFilter filenameFilter = new FilenameFilter()
		{
			public boolean accept(File dir, String fileName) {
				if (fileName.contains(reportSettings.getReportName())) {
					return true;
				}
				return false;
			}


		};
		File[] screenshots = screenshotsFolder.listFiles(filenameFilter);
		if ((screenshots != null) && (screenshots.length > 0)) {
			documentManager.createDocument();
			File[] arrayOfFile1;
			int j = (arrayOfFile1 = screenshots).length; for (int i = 0; i < j; i++) { File screenshot = arrayOfFile1[i];
			documentManager.addPicture(screenshot);
			}
		}
	}

	public void initializeResultSummary()
	{
		for (int i = 0; i < reportTypes.size(); i++) {
			((ReportType)reportTypes.get(i)).initializeResultSummary();
		}
	}

	public void addResultSummaryHeading(String heading)
	{
		for (int i = 0; i < reportTypes.size(); i++) {
			((ReportType)reportTypes.get(i)).addResultSummaryHeading(heading);
		}
	}

	public void addResultSummarySubHeading(String subHeading1, String subHeading2, String subHeading3, String subHeading4)
	{
		for (int i = 0; i < reportTypes.size(); i++) {
			((ReportType)reportTypes.get(i)).addResultSummarySubHeading(subHeading1, subHeading2, 
					subHeading3, subHeading4);
		}
	}

	public void addResultSummaryTableHeadings()
	{
		for (int i = 0; i < reportTypes.size(); i++) {
			((ReportType)reportTypes.get(i)).addResultSummaryTableHeadings();
		}
	}

	public synchronized void updateResultSummary(String scenarioName, String testcaseName, String testcaseDescription, String executionTime, String testStatus)
	{
		if (testStatus.equalsIgnoreCase("failed"))
		{
			nTestsFailed += 1;
		}
		else if (testStatus.equalsIgnoreCase("passed"))
		{
			nTestsPassed += 1;
		}

		for (int i = 0; i < reportTypes.size(); i++) {
			((ReportType)reportTypes.get(i)).updateResultSummary(scenarioName, testcaseName, 
					testcaseDescription, 
					executionTime, testStatus);
		}
	}

	public void addResultSummaryFooter(String totalExecutionTime)
	{
		for (int i = 0; i < reportTypes.size(); i++) {
			((ReportType)reportTypes.get(i)).addResultSummaryFooter(totalExecutionTime, nTestsPassed, nTestsFailed);
		}
	}
}

