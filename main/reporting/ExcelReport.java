package JoS.JAVAProjects.Reporting;


public class ExcelReport implements ReportType {
	private ExcelDataAccess testLogAccess;   
	private ExcelDataAccess resultSummaryAccess;   
	private ReportSettings reportSettings;   
	private ReportTheme reportTheme;
	private ExcelCellFormatting cellFormatting = new ExcelCellFormatting();   
	private int currentSectionRowNum = 0;
	private int currentSubSectionRowNum = 0;

	public ExcelReport(ReportSettings reportSettings, ReportTheme reportTheme){
		this.reportSettings = reportSettings;
		this.reportTheme = reportTheme;     
		testLogAccess =  new ExcelDataAccess(reportSettings.getReportPath(), reportSettings.getReportName());
		resultSummaryAccess = new ExcelDataAccess(reportSettings.getReportPath(), "Summary");
	} 

	public void initializeTestLog(){
		testLogAccess.createWorkbook();
		testLogAccess.addSheet("Cover_Page");
		testLogAccess.addSheet("Test_Log");     
		initializeTestLogColorPalette();     
		testLogAccess.setRowSumsBelow(false);
	}

	private void initializeTestLogColorPalette() {
		testLogAccess.setCustomPaletteColor((short)8, reportTheme.getHeadingBackColor());
		testLogAccess.setCustomPaletteColor((short)9, reportTheme.getHeadingForeColor());
		testLogAccess.setCustomPaletteColor((short)10, reportTheme.getSectionBackColor());
		testLogAccess.setCustomPaletteColor((short)11, reportTheme.getSectionForeColor());
		testLogAccess.setCustomPaletteColor((short)12, reportTheme.getContentBackColor());
		testLogAccess.setCustomPaletteColor((short)13, reportTheme.getContentForeColor());
		testLogAccess.setCustomPaletteColor((short)14, "#008000");
		testLogAccess.setCustomPaletteColor((short)15, "#FF0000");
		testLogAccess.setCustomPaletteColor((short)16, "#FF8000");
		testLogAccess.setCustomPaletteColor((short)17, "#000000");
		testLogAccess.setCustomPaletteColor((short)18, "#00FF80");
	}


	public void addTestLogHeading(String heading) {
		testLogAccess.setDatasheetName("Cover_Page");
		int rowNum = testLogAccess.getLastRowNum();
		if (rowNum != 0) {
			rowNum = testLogAccess.addRow();
		}

		cellFormatting.setFontName("Copperplate Gothic Bold");
		cellFormatting.setFontSize((short)12);
		cellFormatting.bold = true;
		cellFormatting.centred = true;
		cellFormatting.setBackColorIndex((short)8);
		cellFormatting.setForeColorIndex((short)9);     
		testLogAccess.setValue(rowNum, 0, heading, cellFormatting);
		testLogAccess.mergeCells(rowNum, rowNum, 0, 4);
	}



	public void addTestLogSubHeading(String subHeading1, String subHeading2, String subHeading3, String subHeading4){
		testLogAccess.setDatasheetName("Cover_Page");
		int rowNum = testLogAccess.addRow();     
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short)10);
		cellFormatting.bold = true;
		cellFormatting.centred = false;
		cellFormatting.setBackColorIndex((short)9);
		cellFormatting.setForeColorIndex((short)8);     
		testLogAccess.setValue(rowNum, 0, subHeading1, cellFormatting);
		testLogAccess.setValue(rowNum, 1, subHeading2, cellFormatting);
		testLogAccess.setValue(rowNum, 2, "", cellFormatting);
		testLogAccess.setValue(rowNum, 3, subHeading3, cellFormatting);
		testLogAccess.setValue(rowNum, 4, subHeading4, cellFormatting);
	}


	public void addTestLogTableHeadings(){
		testLogAccess.setDatasheetName("Test_Log");     
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short)10);
		cellFormatting.bold = true;
		cellFormatting.centred = true;
		cellFormatting.setBackColorIndex((short)8);
		cellFormatting.setForeColorIndex((short)9);     
		testLogAccess.addColumn("Step_No", cellFormatting);
		testLogAccess.addColumn("Step_Name", cellFormatting);
		testLogAccess.addColumn("Description", cellFormatting);
		testLogAccess.addColumn("Status", cellFormatting);
		testLogAccess.addColumn("Step_Time", cellFormatting);
	}


	public void addTestLogSection(String section){
		testLogAccess.setDatasheetName("Test_Log");
		int rowNum = testLogAccess.addRow();     
		if (currentSubSectionRowNum != 0)     {
			testLogAccess.groupRows(currentSubSectionRowNum, rowNum - 1);    
		}

		if (currentSectionRowNum != 0){
			testLogAccess.groupRows(currentSectionRowNum, rowNum - 1);
		}

		currentSectionRowNum = (rowNum + 1);
		currentSubSectionRowNum = 0;     
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short)10);
		cellFormatting.bold = true;
		cellFormatting.centred = false;
		cellFormatting.setBackColorIndex((short)10);
		cellFormatting.setForeColorIndex((short)11);     
		testLogAccess.setValue(rowNum, 0, section, cellFormatting);
		testLogAccess.mergeCells(rowNum, rowNum, 0, 4);
	}


	public void addTestLogSubSection(String subSection){
		testLogAccess.setDatasheetName("Test_Log");
		int rowNum = testLogAccess.addRow();

		if (currentSubSectionRowNum != 0){
			testLogAccess.groupRows(currentSubSectionRowNum, rowNum - 1);
		}

		currentSubSectionRowNum = (rowNum + 1);     
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short)10);
		cellFormatting.bold = true;
		cellFormatting.centred = false;
		cellFormatting.setBackColorIndex((short)9);
		cellFormatting.setForeColorIndex((short)8);     
		testLogAccess.setValue(rowNum, 0, " " + subSection, cellFormatting);
		testLogAccess.mergeCells(rowNum, rowNum, 0, 4);
	}




	public void updateTestLog(String stepNumber, String stepName, String stepDescription, Status stepStatus, String screenShotName){
		testLogAccess.setDatasheetName("Test_Log");
		int rowNum = testLogAccess.addRow();     
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short)10);
		cellFormatting.setBackColorIndex((short)12);     
		boolean stepContainsScreenshot = processStatusColumn(stepStatus);
		cellFormatting.centred = true;
		cellFormatting.bold = true;
		int columnNum = testLogAccess.getColumnNum("Status", 0);
		testLogAccess.setValue(rowNum, columnNum, stepStatus.toString(), cellFormatting);

		cellFormatting.setForeColorIndex((short)13);
		cellFormatting.bold = false;
		testLogAccess.setValue(rowNum, "Step_No", stepNumber, cellFormatting);
		testLogAccess.setValue(rowNum, "Step_Time", Util.getCurrentFormattedTime(reportSettings.getDateFormatString()), cellFormatting);

		cellFormatting.centred = false;
		testLogAccess.setValue(rowNum, "Step_Name", stepName, cellFormatting);

		if (stepContainsScreenshot) {
			if (reportSettings.linkScreenshotsToTestLog) {
				testLogAccess.setHyperlink(rowNum, columnNum, "..\\Screenshots\\" + screenShotName);

				testLogAccess.setValue(rowNum, "Description", stepDescription, cellFormatting);
			} else {
				testLogAccess.setValue(rowNum, "Description", 
						stepDescription + " (Refer screenshot @ " + screenShotName + ")", 
						cellFormatting);
			}
		} else {
			testLogAccess.setValue(rowNum, "Description", stepDescription, cellFormatting);
		}
	}

	private boolean processStatusColumn(Status stepStatus)
	{
		boolean stepContainsScreenshot = false;

		switch (stepStatus) {
		case FAIL: 
			cellFormatting.setForeColorIndex((short)14);
			stepContainsScreenshot = reportSettings.takeScreenshotPassedStep;
			break;

		case DEBUG: 
			cellFormatting.setForeColorIndex((short)15);
			stepContainsScreenshot = reportSettings.takeScreenshotFailedStep;
			break;

		case DONE: 
			cellFormatting.setForeColorIndex((short)16);
			stepContainsScreenshot = reportSettings.takeScreenshotFailedStep;
			break;

		case SCREENSHOT: 
			cellFormatting.setForeColorIndex((short)17);
			stepContainsScreenshot = false;
			break;

		case PASS: 
			cellFormatting.setForeColorIndex((short)17);
			stepContainsScreenshot = true;
			break;

		case WARNING: 
			cellFormatting.setForeColorIndex((short)18);
			stepContainsScreenshot = false;
		}
		return stepContainsScreenshot;
	}


	public void addTestLogFooter(String executionTime, int nStepsPassed, int nStepsFailed){
		testLogAccess.setDatasheetName("Test_Log");
		int rowNum = testLogAccess.addRow();

		if (currentSubSectionRowNum != 0){
			testLogAccess.groupRows(currentSubSectionRowNum, rowNum - 1);
		}

		if (currentSectionRowNum != 0){
			testLogAccess.groupRows(currentSectionRowNum, rowNum - 1);
		}

		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short)10);
		cellFormatting.bold = true;
		cellFormatting.centred = true;
		cellFormatting.setBackColorIndex((short)8);
		cellFormatting.setForeColorIndex((short)9);
		testLogAccess.setValue(rowNum, 0, "Execution Duration: " + executionTime, cellFormatting);
		testLogAccess.mergeCells(rowNum, rowNum, 0, 4);
		rowNum = testLogAccess.addRow();
		cellFormatting.centred = false;
		cellFormatting.setBackColorIndex((short)9);
		cellFormatting.setForeColorIndex((short)14);
		testLogAccess.setValue(rowNum, "Step_No", "Steps passed", cellFormatting);
		testLogAccess.setValue(rowNum, "Step_Name", ": " + nStepsPassed, cellFormatting);
		cellFormatting.setForeColorIndex((short)8);
		testLogAccess.setValue(rowNum, "Description", "", cellFormatting);
		cellFormatting.setForeColorIndex((short)15);
		testLogAccess.setValue(rowNum, "Status", "Steps failed", cellFormatting);
		testLogAccess.setValue(rowNum, "Step_Time", ": " + nStepsFailed, cellFormatting);
		wrapUpTestLog();
	}

	private void wrapUpTestLog(){
		testLogAccess.autoFitContents(0, 4);
		testLogAccess.addOuterBorder(0, 4);
		testLogAccess.setDatasheetName("Cover_Page");
		testLogAccess.autoFitContents(0, 4);
		testLogAccess.addOuterBorder(0, 4);
	}





	public void initializeResultSummary(){
		resultSummaryAccess.createWorkbook();
		resultSummaryAccess.addSheet("Cover_Page");
		resultSummaryAccess.addSheet("Result_Summary");
		initializeResultSummaryColorPalette();
	}

	private void initializeResultSummaryColorPalette(){
		resultSummaryAccess.setCustomPaletteColor((short)8, reportTheme.getHeadingBackColor());
		resultSummaryAccess.setCustomPaletteColor((short)9, reportTheme.getHeadingForeColor());
		resultSummaryAccess.setCustomPaletteColor((short)10, reportTheme.getSectionBackColor());
		resultSummaryAccess.setCustomPaletteColor((short)11, reportTheme.getSectionForeColor());
		resultSummaryAccess.setCustomPaletteColor((short)12, reportTheme.getContentBackColor());
		resultSummaryAccess.setCustomPaletteColor((short)13, reportTheme.getContentForeColor());
		resultSummaryAccess.setCustomPaletteColor((short)14, "#008000");
		resultSummaryAccess.setCustomPaletteColor((short)15, "#FF0000");
	}


	public void addResultSummaryHeading(String heading){
		resultSummaryAccess.setDatasheetName("Cover_Page");
		int rowNum = resultSummaryAccess.getLastRowNum();
		if (rowNum != 0) {
			rowNum = resultSummaryAccess.addRow();
		}

		cellFormatting.setFontName("Copperplate Gothic Bold");
		cellFormatting.setFontSize((short)12);
		cellFormatting.bold = true;
		cellFormatting.centred = true;
		cellFormatting.setBackColorIndex((short)8);
		cellFormatting.setForeColorIndex((short)9);
		resultSummaryAccess.setValue(rowNum, 0, heading, cellFormatting);
		resultSummaryAccess.mergeCells(rowNum, rowNum, 0, 4);
	}



	public void addResultSummarySubHeading(String subHeading1, String subHeading2, String subHeading3, String subHeading4){
		resultSummaryAccess.setDatasheetName("Cover_Page");
		int rowNum = resultSummaryAccess.addRow();
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short)10);
		cellFormatting.bold = true;
		cellFormatting.centred = false;
		cellFormatting.setBackColorIndex((short)9);
		cellFormatting.setForeColorIndex((short)8);
		resultSummaryAccess.setValue(rowNum, 0, subHeading1, cellFormatting);
		resultSummaryAccess.setValue(rowNum, 1, subHeading2, cellFormatting);
		resultSummaryAccess.setValue(rowNum, 2, "", cellFormatting);
		resultSummaryAccess.setValue(rowNum, 3, subHeading3, cellFormatting);
		resultSummaryAccess.setValue(rowNum, 4, subHeading4, cellFormatting);
	}


	public void addResultSummaryTableHeadings()	{
		resultSummaryAccess.setDatasheetName("Result_Summary");
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short)10);
		cellFormatting.bold = true;
		cellFormatting.centred = true;
		cellFormatting.setBackColorIndex((short)8);
		cellFormatting.setForeColorIndex((short)9);
		resultSummaryAccess.addColumn("Test_Scenario", cellFormatting);
		resultSummaryAccess.addColumn("Test_Case", cellFormatting);
		resultSummaryAccess.addColumn("Test_Description", cellFormatting);
		resultSummaryAccess.addColumn("Execution_Time", cellFormatting);
		resultSummaryAccess.addColumn("Test_Status", cellFormatting);
	}




	public void updateResultSummary(String scenarioName, String testcaseName, String testcaseDescription, String executionTime, String testStatus){
		resultSummaryAccess.setDatasheetName("Result_Summary");
		int rowNum = resultSummaryAccess.addRow();
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short)10);
		cellFormatting.setBackColorIndex((short)12);
		cellFormatting.setForeColorIndex((short)13);
		cellFormatting.centred = false;
		cellFormatting.bold = false;
		resultSummaryAccess.setValue(rowNum, "Test_Scenario", scenarioName, cellFormatting);

		int columnNum = resultSummaryAccess.getColumnNum("Test_Case", 0);
		resultSummaryAccess.setValue(rowNum, columnNum, testcaseName, cellFormatting);
		if (reportSettings.linkTestLogsToSummary) {
			resultSummaryAccess.setHyperlink(rowNum, columnNum, scenarioName + "_" + testcaseName + ".xls");
		}

		resultSummaryAccess.setValue(rowNum, "Test_Description", testcaseDescription, cellFormatting);

		cellFormatting.centred = true;
		resultSummaryAccess.setValue(rowNum, "Execution_Time", executionTime, cellFormatting);

		cellFormatting.bold = true;
		if (testStatus.equalsIgnoreCase("Passed")) {
			cellFormatting.setForeColorIndex((short)14);
		}
		if (testStatus.equalsIgnoreCase("Failed")) {
			cellFormatting.setForeColorIndex((short)15);
		}
		resultSummaryAccess.setValue(rowNum, "Test_Status", testStatus, cellFormatting);
	}


	public void addResultSummaryFooter(String totalExecutionTime, int nTestsPassed, int nTestsFailed){
		resultSummaryAccess.setDatasheetName("Result_Summary");
		int rowNum = resultSummaryAccess.addRow();
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short)10);
		cellFormatting.bold = true;
		cellFormatting.centred = true;
		cellFormatting.setBackColorIndex((short)8);
		cellFormatting.setForeColorIndex((short)9);
		resultSummaryAccess.setValue(rowNum, 0, "Total Duration: " + totalExecutionTime, cellFormatting);
		resultSummaryAccess.mergeCells(rowNum, rowNum, 0, 4);
		rowNum = resultSummaryAccess.addRow();
		cellFormatting.centred = false;
		cellFormatting.setBackColorIndex((short)9);
		cellFormatting.setForeColorIndex((short)14);
		resultSummaryAccess.setValue(rowNum, "Test_Scenario", "Tests passed", cellFormatting);
		resultSummaryAccess.setValue(rowNum, "Test_Case", ": " + nTestsPassed, cellFormatting);
		cellFormatting.setForeColorIndex((short)8);
		resultSummaryAccess.setValue(rowNum, "Test_Description", "", cellFormatting);
		cellFormatting.setForeColorIndex((short)15);
		resultSummaryAccess.setValue(rowNum, "Execution_Time", "Tests failed", cellFormatting);
		resultSummaryAccess.setValue(rowNum, "Test_Status", ": " + nTestsFailed, cellFormatting);
		wrapUpResultSummary();
	}

	private void wrapUpResultSummary(){
		resultSummaryAccess.autoFitContents(0, 4);
		resultSummaryAccess.addOuterBorder(0, 4);
		resultSummaryAccess.setDatasheetName("Cover_Page");
		resultSummaryAccess.autoFitContents(0, 4);
		resultSummaryAccess.addOuterBorder(0, 4);
	}
}
