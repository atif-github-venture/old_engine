package JoS.JAVAProjects;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;
/**
 * 
 * DoScreenAction class
 *
 */
public class DoScreenActions  {

	public String sLogicId;
	public String sFrame;
	public String sDef;
	public String sAction;
	public String sVariablefromDB;
	public String sValuefromTestDataTable;
	public String sMsg ;
	public WebElement wbElement;

	@SuppressWarnings("rawtypes")
	public List RadButtonList;
	public Select dropdown;
	public ResultSet robjDetails;
	public boolean teststepfail = false;
	public boolean taskfail = false;
	boolean bFrame;


	ApplicationParams gOBJ=null;
	DoScreenActions(ApplicationParams gOBJTemp)
	{
		gOBJ=gOBJTemp;
	}
	public void Do_Action() throws SQLException {
		DebugInterface di=new DebugInterface(gOBJ);
		di.Debug (" In the function Do_Action inside DoScreenAction.java file");
		di.Debug ("sValuefromTestDataTable: " + sValuefromTestDataTable);
		di.Debug ("sVariablefromDB: " + sVariablefromDB);
		di.Debug ("sFrame: " + sFrame);
		/**
		 * Loop for setting the frame
		 */
		if (!(sFrame.toString().equalsIgnoreCase("NULL"))){
			di.Debug (" Selecting the frame in the function Do_Action inside DoScreenAction.java file"+sFrame);
			gOBJ.setWbDriver(gOBJ.getWbDriver().switchTo().frame(gOBJ.getWbDriver().findElement(By.id(sFrame))));
			bFrame = true;
		}
		/**
		 * Logic for handling different actions for adding the different type of entry
		 */
		//
		if(sAction==null || sAction.isEmpty())
			return;
		switch (sAction.toUpperCase()){

		//*********************************************************************************************************	

		//*********************************************************************************************************		
		/* 
		 * Every action related utility functions is covered
		 */

		/* 
		 * To open any browser
		 */
		case "OPEN_BROWSER" : {
			di.Debug ("Inside case 'OPEN_BROWSER'");
			new Utils(gOBJ).OpenBrowser();	
			di.Debug ("Exiting case 'OPEN_BROWSER'");
		}break;

		/* 
		 * To close any browser
		 */ 
		case "CLOSE_BROWSER" :{
			di.Debug ("Inside keyword CLOSE_BROWSER()...");	
			try{
				gOBJ.getWbDriver().close();
				new Utils(gOBJ).ReportStepToResultDB("Browser has been closed", "Pass");
				di.Debug ("Exiting keyword CLOSE_BROWSER()...");
				bFrame = false;
			}catch(UnreachableBrowserException e){
				new Utils(gOBJ).ReportStepToResultDB("Unreachable Browser Exception", "Fail");
				di.Debug("Unreachable Browser Exception - Fail - " + e.getMessage());
				teststepfail = true;
			}			
		}break;

		/* 
		 * To navigate to given URL any browser
		 */

		case "NAVIGATE_URL" : {
			di.Debug ("Inside case 'NAVIGATE_URL'");
			new Utils(gOBJ).NavigateURL(sValuefromTestDataTable);		 
			di.Debug ("Exiting case 'NAVIGATE_URL'");
		}break;

		case "SEND_EMAIL_WITH_ATTACHMENT" : {
			di.Debug ("Inside case 'SEND_EMAIL_WITH_ATTACHMENT'");
			if (sValuefromTestDataTable.equalsIgnoreCase("null")){
				//do nothing
				//if its null no email is sent the step is not executed.
				di.Debug ("The step is not executed for case EXECUTE_VBS_FILE since the variable value is null : Pass");
				new Utils(gOBJ).ReportStepToResultDB("The step is not executed for case EXECUTE_VBS_FILE since the variable value is null", "Pass");
			}else{
				try {
					//send  the email setting ID and the path of the email				
					boolean EmailSendStatus = new EmailUtility(gOBJ).SendEmailWithAttachment(sValuefromTestDataTable.split(";")[0].trim(), sValuefromTestDataTable.split(";")[1].trim());
					if (EmailSendStatus){
						new Utils(gOBJ).ReportStepToResultDB("Email has been sent to ID - " + sValuefromTestDataTable.split(";")[0].trim(), "Pass");
						di.Debug ("Email has been sent to ID - " + sValuefromTestDataTable.split(";")[0].trim() + ": Pass");
					}else{
						teststepfail = true;
						new Utils(gOBJ).ReportStepToResultDB("The path of the file - " + sValuefromTestDataTable.split(";")[1].trim() + " not found in the project: " + gOBJ.getStrProjectName(), "Fail");
						di.Debug ("The path of the file - " + sValuefromTestDataTable.split(";")[1].trim() + " not found in the project: " + gOBJ.getStrProjectName() +  ": Fail");
					}
				} catch (IOException e) {				
					di.Debug ("IOException" + e.getMessage());
					new Utils(gOBJ).ReportStepToResultDB ("IOException" + e.getMessage(), "Fail");
					teststepfail = true;
				} 
			}

			di.Debug ("Exiting case 'SEND_EMAIL_WITH_ATTACHMENT'");
		}break;

		case "EXECUTE_VBS_FILE" : {
			di.Debug ("Inside case 'EXECUTE_VBS_FILE'");
			if (sValuefromTestDataTable.equalsIgnoreCase("null")){
				//do nothing
				//if its null no email is sent the step is not executed.
				di.Debug ("The step is not executed for case EXECUTE_VBS_FILE since the variable value is null : Pass");
				new Utils(gOBJ).ReportStepToResultDB("The step is not executed for case EXECUTE_VBS_FILE since the variable value is null", "Pass");
			}else{
				try {
					String file = new File (".").getCanonicalPath() + "\\" + gOBJ.getStrProjectName() + "\\" + sValuefromTestDataTable.trim();
					di.Debug("file: " + file);
					if (new File(file).exists()){
						try {
							Process process = Runtime.getRuntime().exec( "cscript " + file );
							di.Debug ("VBS file called to be executed for : " + file);
							new Utils(gOBJ).ReportStepToResultDB ("VBS file called to be executed for : " + file, "Pass");
						} catch (IOException e) {
							teststepfail = true;
							di.Debug ("IOException" + e.getMessage());
							new Utils(gOBJ).ReportStepToResultDB ("IOException" + e.getMessage(), "Fail");
						}	 
					}
				} catch (IOException e) {
					teststepfail = true;
					di.Debug ("IOException" + e.getMessage());
					new Utils(gOBJ).ReportStepToResultDB ("IOException" + e.getMessage(), "Fail");
				}
			}
			di.Debug ("Exiting case 'EXECUTE_VBS_FILE'");
		}break;

		//*********************************************************************************************************			   

		//*********************************************************************************************************		
		/* 
		 * Every action related with Text Box is covered here
		 */

		case "TEXTBOX_ENTER_DATA" : {
			di.Debug ("Inside case 'TEXTBOX_ENTER_DATA'");
			if (setExecutionObject()) {
				di.Debug (" Entering value for text box entry in the function Do_Action inside DoScreenAction.java file");
				/*
				 * if the selection is desired to be done through a reference value from previous step
				 * the value should be like ref=AccountNumber
				 * and 'AccountNumber' should be a variable defined for previous step having some value.
				 */	
				if(sValuefromTestDataTable.toLowerCase().contains("ref=")){
					sValuefromTestDataTable = sValuefromTestDataTable.split("=")[1].trim();
					if (gOBJ.hm.DoesHashKeyExist(sValuefromTestDataTable)){
						di.Debug ("Key exists in hashmap: " + sValuefromTestDataTable);
						sValuefromTestDataTable = gOBJ.hm.GetHashValueFromHashKey(sValuefromTestDataTable);
						di.Debug ("Key Value now is: " + sValuefromTestDataTable);
						try{
							wbElement.sendKeys(sValuefromTestDataTable);
						}catch(UnreachableBrowserException e){
							new Utils(gOBJ).ReportStepToResultDB("Unreachable Browser Exception", "Fail");
							di.Debug("Unreachable Browser Exception - Fail - " + e.getMessage());
							teststepfail = true;

						}
						new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on textbox " + sMsg + " with value = " + sValuefromTestDataTable, "Pass");
						di.Debug ("Exiting case 'TEXTBOX_ENTER_DATA'");
					}else{
						new Utils(gOBJ).ReportStepToResultDB ("Expected:: Key DOES NOT exist in hashmap: " + sValuefromTestDataTable, "Fail");
						di.Debug ("Key DOES NOT exist in hashmap: " + sValuefromTestDataTable);
						teststepfail = true;
					}
				}else{
					try{
						wbElement.sendKeys(sValuefromTestDataTable);
						new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on textbox " + sMsg + " with value = " + sValuefromTestDataTable, "Pass");
						di.Debug ("Exiting case 'TEXTBOX_ENTER_DATA'");
					}catch(StaleElementReferenceException e){
						new Utils(gOBJ).ReportStepToResultDB("Stale Element Reference Exception - Element not found in the cache - perhaps the page has changed since it was looked up", "Fail");
						di.Debug("Stale Element Reference Exception - Element not found in the cache - perhaps the page has changed since it was looked up - Fail - " + e.getMessage());
						teststepfail = true;
					}catch(UnreachableBrowserException e){
						new Utils(gOBJ).ReportStepToResultDB("Unreachable Browser Exception", "Fail");
						di.Debug("Unreachable Browser Exception - Fail - " + e.getMessage());
						teststepfail = true;

					}
				}
			}
		}break;

		case "TEXTBOX_CHECK_EXISTS" :{
			di.Debug ("Inside case 'TEXTBOX_CHECK_EXISTS'");
			if (setExecutionObject()) {
				di.Debug ("Action:" + sAction + " performed on textbox " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on textbox " + sMsg, "Pass");
			}
			di.Debug ("Exiting case 'TEXTBOX_CHECK_EXISTS'");
		}break;
		case "TEXTBOX_CHECK_NOTEXISTS" :{
			di.Debug ("Inside case 'TEXTBOX_CHECK_NOTEXISTS'");
			if (setExecutionObject()) {
				di.Debug ("Action:" + sAction + " performed on textbox " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on textbox " + sMsg, "Pass");
			}
			di.Debug ("Exiting case 'TEXTBOX_CHECK_NOTEXISTS'");
		}break;

		case "TEXTBOX_CHECK_EDITABLE" :{
			di.Debug ("Inside keyword TEXTBOX_CHECK_EDITABLE()...");
			if (setExecutionObject()){				
				//String svaluetoValidate = wbElement.getAttribute("readonly");
				//Assert.assertNotNull("readonly");
				//String svaluetoValidate1 = wbElement.getAttribute("contentEditable");
				try{
					if (wbElement.isEnabled()){
						di.Debug ("Textbox " + sMsg + " is editable");
						new Utils(gOBJ).ReportStepToResultDB("Textbox " + sMsg + " is editable", "Pass");
						teststepfail = false;
					}
					else{
						di.Debug ("Textbox " + sMsg + " is not editable");
						new Utils(gOBJ).ReportStepToResultDB("Textbox " + sMsg + " is not editable", "Fail");
						teststepfail = true;
					}
				}catch(UnreachableBrowserException e){
					new Utils(gOBJ).ReportStepToResultDB("Unreachable Browser Exception", "Fail");
					di.Debug("Unreachable Browser Exception - Fail - " + e.getMessage());
					teststepfail = true;

				}
			}
			di.Debug ("Exiting keyword TEXTBOX_CHECK_EDITABLE()...");
		}break;

		case "TEXTBOX_CHECK_NOTEDITABLE" :{
			di.Debug ("Inside keyword TEXTBOX_CHECK_NOTEDITABLE()...");
			if (setExecutionObject()){
				//String svaluetoValidate = wbElement.getAttribute("contentEditable");
				try{
					if (!(wbElement.isEnabled())){
						di.Debug ("Textbox " + sMsg + " is not editable");
						new Utils(gOBJ).ReportStepToResultDB("Textbox " + sMsg + " is not editable", "Pass");
						teststepfail = false;
					}
					else{
						di.Debug ("Textbox " + sMsg + " is editable");
						new Utils(gOBJ).ReportStepToResultDB("Textbox " + sMsg + " is editable", "Fail");
						teststepfail = true;
					}
				}catch(UnreachableBrowserException e){
					new Utils(gOBJ).ReportStepToResultDB("Unreachable Browser Exception", "Fail");
					di.Debug("Unreachable Browser Exception - Fail - " + e.getMessage());
					teststepfail = true;

				}				
			}
			di.Debug ("Exiting keyword TEXTBOX_CHECK_NOTEDITABLE()...");
		}break;

		case "TEXTBOX_CLICK" :{
			di.Debug ("Inside keyword TEXTBOX_CLICK()...");
			if (setExecutionObject()) {
				di.Debug (" Validating click action for text box in the function Do_Action inside DoScreenAction.java file");
				try{
					wbElement.click();
					di.Debug ("Action:" + sAction + " performed on textbox " + sMsg + ": Pass");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on textbox " + sMsg, "Pass");
				}catch(UnreachableBrowserException e){
					new Utils(gOBJ).ReportStepToResultDB("Unreachable Browser Exception", "Fail");
					di.Debug("Unreachable Browser Exception - Fail - " + e.getMessage());
					teststepfail = true;
				}catch(StaleElementReferenceException e){
					new Utils(gOBJ).ReportStepToResultDB("Stale Element Reference Exception - Element not found in the cache - perhaps the page has changed since it was looked up", "Fail");
					di.Debug("Stale Element Reference Exception - Element not found in the cache - perhaps the page has changed since it was looked up - Fail - " + e.getMessage());
					teststepfail = true;
				}
			}			
			di.Debug ("Exiting keyword TEXTBOX_CLICK()...");
		}break;


		case "TEXTBOX_VERIFY_ENTERED_VALUE":
			di.Debug ("Inside keyword TEXTBOX_VERIFY_ENTERED_VALUE()...");
			if (setExecutionObject()) {
				di.Debug ("grabbing the attribute value for text box entry in the function Do_Action inside DoScreenAction.java file");
				try{
					String svaluetoValidate = wbElement.getAttribute("value");

					/*
					 * if the varification is desired to be done through a reference value from any previous step
					 * the value should be like ref=AccountNumber
					 * and 'AccountNumber' should be a variable defined for previous step having some value.
					 */	
					if(sValuefromTestDataTable.toLowerCase().contains("ref=")){
						sValuefromTestDataTable = sValuefromTestDataTable.split("=")[1].trim();
						if (gOBJ.hm.DoesHashKeyExist(sValuefromTestDataTable)){
							di.Debug ("Key exists in hashmap: " + sValuefromTestDataTable);
							sValuefromTestDataTable = gOBJ.hm.GetHashValueFromHashKey(sValuefromTestDataTable);
							di.Debug ("Key Value now is: " + sValuefromTestDataTable);
							if (svaluetoValidate.equalsIgnoreCase(sValuefromTestDataTable)){
								di.Debug ("Pass :::: Expected Value -"+sValuefromTestDataTable+" ::::: Actual Value - "+svaluetoValidate);
								new Utils(gOBJ).ReportStepToResultDB("Expected Value -"+sValuefromTestDataTable+" ::::: Actual Value - "+svaluetoValidate, "Pass");
							}
							else{
								di.Debug ("Fail :::: Expected Value -"+sValuefromTestDataTable+" ::::: Actual Value - "+svaluetoValidate);
								new Utils(gOBJ).ReportStepToResultDB("Expected Value -"+sValuefromTestDataTable+" ::::: Actual Value - "+svaluetoValidate, "Fail");
								teststepfail = true;
							} 
							di.Debug ("Exiting case 'TEXTBOX_ENTER_DATA'");
						}else{
							new Utils(gOBJ).ReportStepToResultDB ("Expected:: Key DOES NOT exist in hashmap: " + sValuefromTestDataTable, "Fail");
							di.Debug ("Key DOES NOT exist in hashmap: " + sValuefromTestDataTable);
							teststepfail = true;
						}
					}else{
						wbElement.sendKeys(sValuefromTestDataTable);
						new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on textbox " + sMsg + " with value = " + sValuefromTestDataTable, "Pass");
						di.Debug ("Exiting case 'TEXTBOX_ENTER_DATA'");
					}
				}catch(UnreachableBrowserException e){
					new Utils(gOBJ).ReportStepToResultDB("Unreachable Browser Exception", "Fail");
					di.Debug("Unreachable Browser Exception - Fail - " + e.getMessage());
					teststepfail = true;
				}
				di.Debug ("Exiting keyword TEXTBOX_VERIFY_ENTERED_VALUE()...");
				break;
			}break;


		case "TEXTBOX_CLEAR" :
			di.Debug ("Inside keyword TEXTBOX_CLEAR()...");
			if (setExecutionObject()) {
				di.Debug (" Validating click action for text box in the function Do_Action inside DoScreenAction.java file");
				try{
					wbElement.clear(); //wbElement.click();
				}catch (WebDriverException e){
					di.Debug ("WebDriverException: " + e.getMessage());
					new Utils(gOBJ).ReportStepToResultDB("WebDriverException", "Fail");
					di.Debug("WebDriver Exception - Fail - " + e.getMessage());
					teststepfail = true;
					break;
				}		
				di.Debug ("Action:" + sAction + " performed on textbox " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on textbox " + sMsg, "Pass");
				di.Debug ("Exiting keyword TEXTBOX_CLEAR()...");
				break;
			}break;	


		case "TEXTBOX_ENTER_DATA_OPTIONAL" : {
			di.Debug ("Inside case 'TEXTBOX_ENTER_DATA_OPTIONAL'");
			if (setExecutionObject()) {
				di.Debug (" Entering value for text box entry in the function Do_Action inside DoScreenAction.java file");
				/*
				 * if the selection is desired to be done through a reference value from previous step
				 * the value should be like ref=AccountNumber
				 * and 'AccountNumber' should be a variable defined for previous step having some value.
				 */	
				try{
					if(sValuefromTestDataTable.toLowerCase().contains("ref=")){
						sValuefromTestDataTable = sValuefromTestDataTable.split("=")[1].trim();
						if (gOBJ.hm.DoesHashKeyExist(sValuefromTestDataTable)){
							di.Debug ("Key exists in hashmap: " + sValuefromTestDataTable);
							sValuefromTestDataTable = gOBJ.hm.GetHashValueFromHashKey(sValuefromTestDataTable);
							di.Debug ("Key Value now is: " + sValuefromTestDataTable);
							wbElement.sendKeys(sValuefromTestDataTable);
							new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on textbox " + sMsg + " with value = " + sValuefromTestDataTable, "Pass");
							di.Debug ("Exiting case 'TEXTBOX_ENTER_DATA'");
						}else{
							new Utils(gOBJ).ReportStepToResultDB ("Expected:: Key DOES NOT exist in hashmap: " + sValuefromTestDataTable, "Fail");
							di.Debug ("Expected:: Key DOES NOT exist in hashmap: " + sValuefromTestDataTable);
							teststepfail = true;
						}
					}else{
						wbElement.sendKeys(sValuefromTestDataTable);
						new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on textbox " + sMsg + " with value = " + sValuefromTestDataTable, "Pass");
						di.Debug ("Exiting case 'TEXTBOX_ENTER_DATA'");
					}
				}catch(UnreachableBrowserException e){
					new Utils(gOBJ).ReportStepToResultDB("Unreachable Browser Exception", "Fail");
					di.Debug("Unreachable Browser Exception - Fail - " + e.getMessage());
					teststepfail = true;
				}
			}
		}break;

		case "TEXTBOX_VERIFY_EMPTY" : {
			di.Debug ("Inside case 'TEXTBOX_VERIFY_EMPTY'");
			if (setExecutionObject()) {
				di.Debug ("Verifying value for within the text box in the function Do_Action inside DoScreenAction.java file");
				if (wbElement.getAttribute("value").toString().isEmpty()){
					di.Debug ("Action:" + sAction + " performed on textbox " + sMsg + "- Its empty - :Pass");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on textbox " + sMsg + "- Its empty -", "Pass");
				}else{
					di.Debug ("Action:" + sAction + " performed on textbox " + sMsg + "- Its NOT empty - :Fail");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on textbox " + sMsg + "- Its NOT empty -", "Fail");
					teststepfail = true;
				}				
				di.Debug ("Exiting case 'TEXTBOX_VERIFY_EMPTY'");
			}
		}break;

		case "TEXTBOX_VERIFY_NOT_EMPTY" : {
			di.Debug ("Inside case 'TEXTBOX_VERIFY_NOT_EMPTY'");
			if (setExecutionObject()) {
				di.Debug ("Verifying value for within the text box in the function Do_Action inside DoScreenAction.java file");
				wbElement.sendKeys(sValuefromTestDataTable);	
				if (wbElement.getAttribute("value").toString().isEmpty()){
					di.Debug ("Action:" + sAction + " performed on textbox " + sMsg + "- Its empty - :Fail");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on textbox " + sMsg + "- Its empty -", "Fail");
					teststepfail = true;
				}else{
					di.Debug ("Action:" + sAction + " performed on textbox " + sMsg + "- Its NOT empty - :Pass");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on textbox " + sMsg + "- Its NOT empty -", "Pass");					
				}				
				di.Debug ("Exiting case 'TEXTBOX_VERIFY_NOT_EMPTY'");
			}
		}break;

		// Case statement for Text Box entry completed
		//*********************************************************************************************************

		//*********************************************************************************************************
		/*
		 * Every action related to TextArea is covered below	 
		 */
		case "TEXTAREA_VERIFY_EMPTY" : {
			di.Debug ("Inside case 'TEXTAREA_VERIFY_EMPTY'");
			if (setExecutionObject()) {
				di.Debug ("Verifying value for within the text box in the function Do_Action inside DoScreenAction.java file");
				di.Debug("wbElement.getText(): " + wbElement.getText());
				if (wbElement.getText().toString().isEmpty()){
					di.Debug ("Action:" + sAction + " performed on textbox " + sMsg + "- Its empty - :Pass");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on textbox " + sMsg + "- Its empty -", "Pass");
				}else{
					di.Debug("wbElement.getText(): " + wbElement.getText());
					di.Debug ("Action:" + sAction + " performed on textbox " + sMsg + "- Its NOT empty - :Fail");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on textbox " + sMsg + "- Its NOT empty -", "Fail");
					teststepfail = true;
				}				
				di.Debug ("Exiting case 'TEXTAREA_VERIFY_EMPTY'");
			}
		}break;

		case "TEXTAREA_VERIFY_NOT_EMPTY" : {
			di.Debug ("Inside case 'TEXTAREA_VERIFY_NOT_EMPTY'");

			if (setExecutionObject()) {
				di.Debug("wbElement.getText(): " + wbElement.getText());
				di.Debug ("Verifying value for within the text box in the function Do_Action inside DoScreenAction.java file");
				if (wbElement.getText().toString().isEmpty()){
					di.Debug ("Action:" + sAction + " performed on textbox " + sMsg + "- Its empty - :Fail");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on textbox " + sMsg + "- Its empty -", "Fail");
					teststepfail = true;
				}else{
					di.Debug("wbElement.getText(): " + wbElement.getText());
					di.Debug ("Action:" + sAction + " performed on textbox " + sMsg + "- Its NOT empty - :Pass");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on textbox " + sMsg + "- Its NOT empty -", "Pass");					
				}				
				di.Debug ("Exiting case 'TEXTAREA_VERIFY_NOT_EMPTY'");
			}
		}break;
		// Case statement for TextArea entry completed
		//*********************************************************************************************************

		//*********************************************************************************************************
		/*
		 * Every action related to Button is covered below	 
		 */
		case "BUTTON_CLICK" :{
			di.Debug ("Inside Button_click case");
			if (setExecutionObject()) {
				try{
					wbElement.click();
				}catch(UnreachableBrowserException e){
					new Utils(gOBJ).ReportStepToResultDB("Unreachable Browser Exception", "Fail");
					di.Debug("Unreachable Browser Exception - Fail - " + e.getMessage());
					teststepfail = true;
				}
				di.Debug ("Action:" + sAction + " performed on button - " + sMsg + " :Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on button - " + sMsg , "Pass");
				//			 gOBJ.gtsflag = true;
				di.Debug ("Exiting Button_click case");
				break;
			}
		}break;

		case "BUTTON_CHECK_NOTEXISTS" :{
			di.Debug ("Inside BUTTON_CHECK_NOTEXISTS case");
			if (setExecutionObject()) {
				di.Debug ("Action:" + sAction + " performed on button " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on button " + sMsg, "Pass");
				di.Debug ("Exiting BUTTON_CHECK_NOTEXISTS case");
				break;
			}
			//			gOBJ.gtsflag = true;
		}break;

		case "BUTTON_CHECK_EXISTS" :{
			di.Debug ("Inside BUTTON_CHECK_EXISTS case");
			if (setExecutionObject()) {
				di.Debug ("Action:" + sAction + " performed on button " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on button " + sMsg, "Pass");
				di.Debug ("Exiting BUTTON_CHECK_EXISTS case");
				break;
			}
			//			gOBJ.gtsflag = true;
		}break;

		case "BUTTON_CLICK_IFEXISTS" :{
			di.Debug ("Inside BUTTON_CLICK_IFEXISTS case");
			if (setExecutionObject()) {
				wbElement.click();
				di.Debug ("Action:" + sAction + " performed on button - " + sMsg + " :Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on button - " + sMsg , "Pass");
				//			 gOBJ.gtsflag = true;
				di.Debug ("Exiting BUTTON_CLICK_IFEXISTS case");
				break;
			}
		}break;

		case "BUTTON_CLICK_RIGHT_ARROW" :{
			di.Debug ("Inside BUTTON_CLICK_RIGHT_ARROW case");
			if (setExecutionObject()) {
				di.Debug ("Click right arrorw action in the function Do_Action inside DoScreenAction.java file");
				wbElement.sendKeys(Keys.ARROW_RIGHT);
				di.Debug ("Action:" + sAction + " performed on button - " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on button - " + sMsg , "Pass");
				di.Debug ("Exiting BUTTON_CLICK_RIGHT_ARROW case");
				break;
			}	
		}break;		 

		case "BUTTON_CLICK_ENTER" :{
			di.Debug ("Inside BUTTON_CLICK_ENTER case");
			if (setExecutionObject()) {
				di.Debug ("Click enter action in the function Do_Action inside DoScreenAction.java file");
				wbElement.sendKeys(Keys.ENTER);			
				di.Debug ("Action:" + sAction + " performed on button - " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on button - " + sMsg , "Pass");
				di.Debug ("Exiting BUTTON_CLICK_ENTER case");
				break;
			}	
		}break;		 

		case "BUTTON_CLICK_DOWN_ARROW" :{
			di.Debug ("Inside BUTTON_CLICK_DOWN_ARROW case");
			if (setExecutionObject()) {
				di.Debug ("Click down arrorw action in the function Do_Action inside DoScreenAction.java file");
				wbElement.sendKeys(Keys.ARROW_DOWN);	
				di.Debug ("Action:" + sAction + " performed on button - " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on button - " + sMsg , "Pass");
				di.Debug ("Exiting BUTTON_CLICK_DOWN_ARROW case");
				break;
			}			 
		}break;

		case "BUTTON_CLICK_WITH_LINKTEXT" :{
			di.Debug ("Inside BUTTON_CLICK_WITH_LINKTEXT case");
			if (setExecutionObject()) {
				wbElement.click();
				di.Debug ("Action:" + sAction + " performed on button - " + sMsg + " :Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on button - " + sMsg , "Pass");
				di.Debug ("Exiting BUTTON_CLICK_WITH_LINKTEXT case");
				break;
			}			
		}break;

		case "BUTTON_CLICK_WITH_LINKTEXT_IFEXISTS" :{
			di.Debug ("Inside BUTTON_CLICK_WITH_LINKTEXT_IFEXISTS case");
			if (setExecutionObject()) {
				wbElement.click();
				di.Debug ("Action:" + sAction + " performed on button - " + sMsg + " :Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on button - " + sMsg , "Pass");
				di.Debug ("Exiting BUTTON_CLICK_WITH_LINKTEXT_IFEXISTS case");
				break;
			}			
		}break;

		case "BUTTON_CLICK_WITH_PARTIALLINKTEXT" :{
			di.Debug ("Inside BUTTON_CLICK_WITH_PARTIALLINKTEXT case");
			if (setExecutionObject()) {
				wbElement.click();
				di.Debug ("Action:" + sAction + " performed on button - " + sMsg + " :Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on button - " + sMsg , "Pass");
				di.Debug ("Exiting BUTTON_CLICK_WITH_PARTIALLINKTEXT case");
				break;
			}			
		}break;

		case "BUTTON_CLICK_WITH_PARTIALLINKTEXT_IFEXISTS" :{
			di.Debug ("Inside BUTTON_CLICK_WITH_PARTIALLINKTEXT_IFEXISTS case");
			if (setExecutionObject()) {
				wbElement.click();
				di.Debug ("Action:" + sAction + " performed on button - " + sMsg + " :Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on button - " + sMsg , "Pass");
				di.Debug ("Exiting BUTTON_CLICK_WITH_PARTIALLINKTEXT_IFEXISTS case");
				break;
			}			
		}break;

		case "BUTTON_CLICK_WITH_TAGANDTEXT" :{
			boolean TextFoundFlg = false;
			di.Debug ("Inside BUTTON_CLICK_WITH_TAGANDTEXT case");
			di.Debug ("sValuefromTestDataTable: " + sValuefromTestDataTable);
			di.Debug ("sDef: " + sDef);
			//String[] Split_sValuefromTestDataTable = sValuefromTestDataTable.split("#");
			WebElement Bunch = gOBJ.getWbDriver().findElement(By.xpath("//" + sDef));
			List<WebElement> allOptions = Bunch.findElements(By.xpath("//" + sDef));
			for (WebElement option : allOptions) {
				if ((option.getText().equals(sValuefromTestDataTable))){
					di.Debug ("found!!!!!!!!!!#############%%%%%%%");
					option.click();
					di.Debug ("Action:" + sAction + " performed on button - " + sMsg + "with text: " + sValuefromTestDataTable +  ": Pass");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on button - " + sMsg + "with text: " + sValuefromTestDataTable, "Pass");
					TextFoundFlg = true;
					break;
				}			    
			}
			if (!(TextFoundFlg)){
				di.Debug ("Action:" + sAction + " NOT performed on button - " + sMsg + "with text: " + sValuefromTestDataTable +  " - Not found in the application: Fail");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " NOT performed on button - " + sMsg + "with text: " + sValuefromTestDataTable + " - Not found in the application", "Fail");
				di.Debug ("Exiting BUTTON_CLICK_WITH_TAGANDTEXT case");
				teststepfail = true;
				taskfail = true;
				break;
			}
		}break;

		case "BUTTON_CLICK_WITH_TAGANDTEXT_IFEXISTS" :{
			boolean TextFoundFlg = false;
			di.Debug ("Inside BUTTON_CLICK_WITH_TAGANDTEXT_IFEXISTS case");
			di.Debug ("sValuefromTestDataTable: " + sValuefromTestDataTable);
			if (gOBJ.hm.DoesHashKeyExist(sValuefromTestDataTable)){
				di.Debug ("Key exists in hashmap: " + sValuefromTestDataTable);
				sValuefromTestDataTable = gOBJ.hm.GetHashValueFromHashKey(sValuefromTestDataTable);
				di.Debug ("Key Value now is: " + sValuefromTestDataTable);
			}
			di.Debug ("sDef: " + sDef);
			//String[] Split_sValuefromTestDataTable = sValuefromTestDataTable.split("#");
			WebElement Bunch = gOBJ.getWbDriver().findElement(By.xpath("//" + sDef));
			List<WebElement> allOptions = Bunch.findElements(By.xpath("//" + sDef));
			for (WebElement option : allOptions) {
				di.Debug ("option.getText():----->> " + option.getText());
				if ((option.getText().equals(sValuefromTestDataTable))){
					di.Debug ("found!!!!!!!!!!#############%%%%%%%");
					option.click();
					di.Debug ("Action:" + sAction + " performed on button - " + sMsg + "with text: " + sValuefromTestDataTable +  ": Pass");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on button - " + sMsg + "with text: " + sValuefromTestDataTable, "Pass");
					TextFoundFlg = true;
					break;
				}			    
			}
			if (!(TextFoundFlg)){
				di.Debug ("Action:" + sAction + " NOT performed on button - " + sMsg + "with text: " + sValuefromTestDataTable +  " - Not found in the application: Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " NOT performed on button - " + sMsg + "with text: " + sValuefromTestDataTable + " - Not found in the application", "Pass");
				di.Debug ("Exiting BUTTON_CLICK_WITH_TAGANDTEXT_IFEXISTS case");
				break;
			}
		}break;

		case "BUTTON_EXTRACT_TEXT" :{
			di.Debug ("Inside BUTTON_EXTRACT_TEXT case");
			if (setExecutionObject()) {
				di.Debug (wbElement.getText());
				if(sValuefromTestDataTable.toLowerCase().contains("path")){
					String [] pathToSaveTo = sValuefromTestDataTable.split("=");
					new Utils(gOBJ).WriteToOuputTXTFile("canonical", pathToSaveTo[1], sVariablefromDB + "=" + wbElement.getText());
				}
				gOBJ.hm.AssignHashValueToHashKey(sVariablefromDB, wbElement.getText());	
				sValuefromTestDataTable = wbElement.getText();
				di.Debug ("Action:" + sAction + " performed on Object - " + sMsg + " and retrieved information is :: " + gOBJ.hm.GetHashValueFromHashKey(sVariablefromDB) + " : Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on button - " + sMsg + " and retrieved information is :: " + gOBJ.hm.GetHashValueFromHashKey(sVariablefromDB) , "Pass");
				di.Debug ("The DB field ::::xxxx"+sVariablefromDB);
				di.Debug ("The value field ::::xxxx"+sValuefromTestDataTable);
				di.Debug ("Exiting BUTTON_EXTRACT_TEXT case");
			}	
		}
		break;

		case "BUTTON_CLICK_AND_REFRESH" :{
			di.Debug ("Inside BUTTON_CLICK_AND_REFRESH case");
			if (setExecutionObject()) {
				di.Debug (" click and refresh action in the function Do_Action inside DoScreenAction.java file");
				wbElement.click();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				gOBJ.getWbDriver().navigate().refresh();		
				di.Debug ("Action:" + sAction + " performed on button - " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on button - " + sMsg , "Pass");
				di.Debug ("Exiting BUTTON_CLICK_AND_REFRESH case");
				//			 gOBJ.gtsflag = true;
			}	
		}
		break;

		case "BUTTON_MOUSEOVER" :{
			di.Debug ("Inside BUTTON_MOUSEOVER case");
			if (setExecutionObject()) {
				Actions Act = new Actions(gOBJ.getWbDriver());
				Act.moveToElement(wbElement).build().perform();
				di.Debug ("Action:" + sAction + " performed on button - " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on button - " + sMsg , "Pass");
				di.Debug ("Exiting BUTTON_MOUSEOVER case");
				//			 gOBJ.gtsflag = true;
			}	
		}
		break;

		case "BUTTON_CHECK_IF_ENABLED" :{
			di.Debug ("Inside keyword BUTTON_CHECK_IF_ENABLED()...");
			if (setExecutionObject()){				
				//String svaluetoValidate = wbElement.getAttribute("readonly");
				//Assert.assertNotNull("readonly");
				//String svaluetoValidate1 = wbElement.getAttribute("contentEditable");

				if (wbElement.isEnabled()){
					di.Debug ("Button " + sMsg + " is enabled");
					new Utils(gOBJ).ReportStepToResultDB("Button " + sMsg + " is enabled", "Pass");
					teststepfail = false;
				}
				else{
					di.Debug ("Button " + sMsg + " is not enabled");
					new Utils(gOBJ).ReportStepToResultDB("Button " + sMsg + " is not enabled", "Fail");
					teststepfail = true;
				}
			}
			di.Debug ("Exiting keyword BUTTON_CHECK_IF_ENABLED()...");
		}break;

		case "BUTTON_CHECK_IF_NOTENABLED" :{
			di.Debug ("Inside keyword BUTTON_CHECK_IF_NOTENABLED()...");
			if (setExecutionObject()){
				//String svaluetoValidate = wbElement.getAttribute("contentEditable");
				if (!(wbElement.isEnabled())){
					di.Debug ("Button " + sMsg + " is not editable");
					new Utils(gOBJ).ReportStepToResultDB("Button " + sMsg + " is not enabled", "Pass");
					teststepfail = false;
				}
				else{
					di.Debug ("Button " + sMsg + " is editable");
					new Utils(gOBJ).ReportStepToResultDB("Button " + sMsg + " is enabled", "Fail");
					teststepfail = true;
				}
			}
			di.Debug ("Exiting keyword BUTTON_CHECK_IF_NOTENABLED()...");
		}break;

		// Case statement for Button entry completed
		//*********************************************************************************************************		

		//*********************************************************************************************************	
		/*
		 * Every action related to Link is covered below	 
		 */

		case "LINK_CLICK" :{
			di.Debug ("Inside Link_click case");
			if (setExecutionObject()) {
				wbElement.click();
				di.Debug ("Action:" + sAction + " performed on Link - " + sMsg + " :Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Link - " + sMsg , "Pass");
				//			 gOBJ.gtsflag = true;
				di.Debug ("Exiting Link_click case");
				break;
			}
		}break;

		case "LINK_CHECK_NOTEXISTS" :{
			di.Debug ("Inside LINK_CHECK_NOTEXISTS case");
			if (setExecutionObject()) {
				di.Debug ("Action:" + sAction + " performed on Link " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Link " + sMsg, "Pass");
				di.Debug ("Exiting LINK_CHECK_NOTEXISTS case");
				break;
			}
			//			gOBJ.gtsflag = true;
		}break;

		case "LINK_CHECK_EXISTS" :{
			di.Debug ("Inside LINK_CHECK_EXISTS case");
			if (setExecutionObject()) {
				di.Debug ("Action:" + sAction + " performed on Link " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Link " + sMsg, "Pass");
				di.Debug ("Exiting LINK_CHECK_EXISTS case");
				break;
			}
			//			gOBJ.gtsflag = true;
		}break;

		case "LINK_CLICK_IFEXISTS" :{
			di.Debug ("Inside LINK_CLICK_IFEXISTS case");
			if (setExecutionObject()) {
				wbElement.click();
				di.Debug ("Action:" + sAction + " performed on Link - " + sMsg + " :Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Link - " + sMsg , "Pass");
				//			 gOBJ.gtsflag = true;
				di.Debug ("Exiting LINK_CLICK_IFEXISTS case");
				break;
			}
		}break;

		case "LINK_CLICK_RIGHT_ARROW" :{
			di.Debug ("Inside LINK_CLICK_RIGHT_ARROW case");
			if (setExecutionObject()) {
				di.Debug ("Click right arrorw action in the function Do_Action inside DoScreenAction.java file");
				wbElement.sendKeys(Keys.ARROW_RIGHT);
				di.Debug ("Action:" + sAction + " performed on Link - " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Link - " + sMsg , "Pass");
				di.Debug ("Exiting LINK_CLICK_RIGHT_ARROW case");
				break;
			}	
		}break;		 

		case "LINK_CLICK_ENTER" :{
			di.Debug ("Inside LINK_CLICK_ENTER case");
			if (setExecutionObject()) {
				di.Debug ("Click enter action in the function Do_Action inside DoScreenAction.java file");
				wbElement.sendKeys(Keys.ENTER);			
				di.Debug ("Action:" + sAction + " performed on Link - " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Link - " + sMsg , "Pass");
				di.Debug ("Exiting LINK_CLICK_ENTER case");
				break;
			}	
		}break;		 

		case "LINK_CLICK_DOWN_ARROW" :{
			di.Debug ("Inside LINK_CLICK_DOWN_ARROW case");
			if (setExecutionObject()) {
				di.Debug ("Click down arrorw action in the function Do_Action inside DoScreenAction.java file");
				wbElement.sendKeys(Keys.ARROW_DOWN);	
				di.Debug ("Action:" + sAction + " performed on Link - " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Link - " + sMsg , "Pass");
				di.Debug ("Exiting LINK_CLICK_DOWN_ARROW case");
				break;
			}			 
		}break;

		case "LINK_CLICK_WITH_PARTIALLINKTEXT" :{
			di.Debug ("Inside LINK_CLICK_WITH_PARTIALLINKTEXT case");
			if (setExecutionObject()) {
				wbElement.click();
				di.Debug ("Action:" + sAction + " performed on Link - " + sMsg + " :Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Link - " + sMsg , "Pass");
				di.Debug ("Exiting LINK_CLICK_WITH_PARTIALLINKTEXT case");
				break;
			}			
		}break;

		case "LINK_CLICK_WITH_PARTIALLINKTEXT_IFEXISTS" :{
			di.Debug ("Inside LINK_CLICK_WITH_PARTIALLINKTEXT_IFEXISTS case");
			if (setExecutionObject()) {
				wbElement.click();
				di.Debug ("Action:" + sAction + " performed on Link - " + sMsg + " :Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Link - " + sMsg , "Pass");
				di.Debug ("Exiting LINK_CLICK_WITH_PARTIALLINKTEXT_IFEXISTS case");
				break;
			}			
		}break;

		case "LINK_CLICK_WITH_LINKTEXT" :{
			di.Debug ("Inside LINK_CLICK_WITH_LINKTEXT case");
			if (setExecutionObject()) {
				wbElement.click();
				di.Debug ("Action:" + sAction + " performed on Link - " + sMsg + " :Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Link - " + sMsg , "Pass");
				di.Debug ("Exiting LINK_CLICK_WITH_LINKTEXT case");
				break;
			}			
		}break;

		case "LINK_CLICK_WITH_LINKTEXT_IFEXISTS" :{
			di.Debug ("Inside LINK_CLICK_WITH_LINKTEXT_IFEXISTS case");
			if (setExecutionObject()) {
				wbElement.click();
				di.Debug ("Action:" + sAction + " performed on Link - " + sMsg + " :Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Link - " + sMsg , "Pass");
				di.Debug ("Exiting LINK_CLICK_WITH_LINKTEXT_IFEXISTS case");
				break;
			}			
		}break;

		case "LINK_CLICK_WITH_TAGANDTEXT" :{
			boolean TextFoundFlg = false;
			di.Debug ("Inside LINK_CLICK_WITH_TAGANDTEXT case");
			di.Debug ("sValuefromTestDataTable: " + sValuefromTestDataTable);
			if (gOBJ.hm.DoesHashKeyExist(sValuefromTestDataTable)){
				di.Debug ("Key exists in hashmap: " + sValuefromTestDataTable);
				sValuefromTestDataTable = gOBJ.hm.GetHashValueFromHashKey(sValuefromTestDataTable);
				di.Debug ("Key Value now is: " + sValuefromTestDataTable);
			}
			di.Debug ("sDef: " + sDef);
			//String[] Split_sValuefromTestDataTable = sValuefromTestDataTable.split("#");
			WebElement Bunch = gOBJ.getWbDriver().findElement(By.xpath("//" + sDef));
			List<WebElement> allOptions = Bunch.findElements(By.xpath("//" + sDef));
			for (WebElement option : allOptions) {
				di.Debug ("option.getText():----->> " + option.getText());
				if ((option.getText().equals(sValuefromTestDataTable))){
					di.Debug ("found!!!!!!!!!!#############%%%%%%%");
					option.click();
					di.Debug ("Action:" + sAction + " performed on Link - " + sMsg + "with text: " + sValuefromTestDataTable +  ": Pass");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Link - " + sMsg + "with text: " + sValuefromTestDataTable, "Pass");
					TextFoundFlg = true;
					break;
				}			    
			}
			if (!(TextFoundFlg)){
				di.Debug ("Action:" + sAction + " NOT performed on Link - " + sMsg + "with text: " + sValuefromTestDataTable +  " - Not found in the application: Fail");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " NOT performed on Link - " + sMsg + "with text: " + sValuefromTestDataTable + " - Not found in the application", "Fail");
				di.Debug ("Exiting LINK_CLICK_WITH_TAGANDTEXT case");
				teststepfail = true;
				taskfail = true;
				break;
			}

		}break;

		case "LINK_CLICK_WITH_TAGANDTEXT_IFEXISTS" :{
			boolean TextFoundFlg = false;
			di.Debug ("Inside LINK_CLICK_WITH_TAGANDTEXT_IFEXISTS case");
			di.Debug ("sValuefromTestDataTable: " + sValuefromTestDataTable);
			if (gOBJ.hm.DoesHashKeyExist(sValuefromTestDataTable)){
				di.Debug ("Key exists in hashmap: " + sValuefromTestDataTable);
				sValuefromTestDataTable = gOBJ.hm.GetHashValueFromHashKey(sValuefromTestDataTable);
				di.Debug ("Key Value now is: " + sValuefromTestDataTable);
			}
			di.Debug ("sDef: " + sDef);
			//String[] Split_sValuefromTestDataTable = sValuefromTestDataTable.split("#");
			WebElement Bunch = gOBJ.getWbDriver().findElement(By.xpath("//" + sDef));
			List<WebElement> allOptions = Bunch.findElements(By.xpath("//" + sDef));
			for (WebElement option : allOptions) {
				di.Debug ("option.getText():----->> " + option.getText());
				if ((option.getText().equals(sValuefromTestDataTable))){
					di.Debug ("found!!!!!!!!!!#############%%%%%%%");
					option.click();
					di.Debug ("Action:" + sAction + " performed on Link - " + sMsg + "with text: " + sValuefromTestDataTable +  ": Pass");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Link - " + sMsg + "with text: " + sValuefromTestDataTable, "Pass");
					TextFoundFlg = true;
					break;
				}			    
			}
			if (!(TextFoundFlg)){
				di.Debug ("Action:" + sAction + " NOT performed on Link - " + sMsg + "with text: " + sValuefromTestDataTable +  " - Not found in the application: Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " NOT performed on Link - " + sMsg + "with text: " + sValuefromTestDataTable + " - Not found in the application", "Pass");
				di.Debug ("Exiting LINK_CLICK_WITH_TAGANDTEXT_IFEXISTS case");
				break;
			}
		}break;

		case "LINK_EXTRACT_TEXT" :{
			di.Debug ("Inside LINK_EXTRACT_TEXT case");
			if (setExecutionObject()) {
				di.Debug (wbElement.getText());
				if(sValuefromTestDataTable.toLowerCase().contains("path")){
					String [] pathToSaveTo = sValuefromTestDataTable.split("=");
					new Utils(gOBJ).WriteToOuputTXTFile("canonical", pathToSaveTo[1], sVariablefromDB + "=" + wbElement.getText());
				}
				gOBJ.hm.AssignHashValueToHashKey(sVariablefromDB, wbElement.getText());	
				sValuefromTestDataTable = wbElement.getText();
				di.Debug ("Action:" + sAction + " performed on Object - " + sMsg + " and retrieved information is :: " + gOBJ.hm.GetHashValueFromHashKey(sVariablefromDB) + " : Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Link - " + sMsg + " and retrieved information is :: " + gOBJ.hm.GetHashValueFromHashKey(sVariablefromDB) , "Pass");
				di.Debug ("The DB field ::::xxxx"+sVariablefromDB);
				di.Debug ("The value field ::::xxxx"+sValuefromTestDataTable);
				di.Debug ("Exiting LINK_EXTRACT_TEXT case");
			}	
		}
		break;

		case "LINK_CLICK_AND_REFRESH" :{
			di.Debug ("Inside LINK_CLICK_AND_REFRESH case");
			if (setExecutionObject()) {
				di.Debug (" click and refresh action in the function Do_Action inside DoScreenAction.java file");
				wbElement.click();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {					
					e.printStackTrace();
				}
				gOBJ.getWbDriver().navigate().refresh();		
				di.Debug ("Action:" + sAction + " performed on Link - " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Link - " + sMsg , "Pass");
				di.Debug ("Exiting LINK_CLICK_AND_REFRESH case");
				//			 gOBJ.gtsflag = true;
			}	
		}
		break;

		case "LINK_MOUSEOVER" :{
			di.Debug ("Inside LINK_MOUSEOVER case");
			if (setExecutionObject()) {
				Actions Act = new Actions(gOBJ.getWbDriver());
				Act.moveToElement(wbElement).build().perform();
				di.Debug ("Action:" + sAction + " performed on Link - " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Link - " + sMsg , "Pass");
				di.Debug ("Exiting LINK_MOUSEOVER case");
				//			 gOBJ.gtsflag = true;
			}	
		}
		break;
		// Case statement for Link entry completed
		//*********************************************************************************************************		

		//*********************************************************************************************************		

		/*
		 * Every action related to Label is covered below	 
		 */
		case "LABEL_CLICK" :{
			di.Debug ("Inside LABEL_CLICK case");
			if (setExecutionObject()) {
				wbElement.click();
				di.Debug ("Action:" + sAction + " performed on Label - " + sMsg + " :Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Label - " + sMsg , "Pass");
				//			 gOBJ.gtsflag = true;
				di.Debug ("Exiting LABEL_CLICK case");
				break;
			}
		}break;

		case "LABEL_CHECK_NOTEXISTS" :{
			di.Debug ("Inside LABEL_CHECK_NOTEXISTS case");
			if (setExecutionObject()) {
				di.Debug ("Action:" + sAction + " performed on Label " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Label " + sMsg, "Pass");
				di.Debug ("Exiting LABEL_CHECK_NOTEXISTS case");
				break;
			}
			//			gOBJ.gtsflag = true;
		}break;

		case "LABEL_CHECK_EXISTS" :{
			di.Debug ("Inside LABEL_CHECK_EXISTS case");
			if (setExecutionObject()) {
				di.Debug ("Action:" + sAction + " performed on Label " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Label " + sMsg, "Pass");
				di.Debug ("Exiting LABEL_CHECK_EXISTS case");
				break;
			}
			//			gOBJ.gtsflag = true;
		}break;

		case "LABEL_CLICK_IFEXISTS" :{
			di.Debug ("Inside LABEL_CLICK_IFEXISTS case");
			if (setExecutionObject()) {
				wbElement.click();
				di.Debug ("Action:" + sAction + " performed on Label - " + sMsg + " :Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Label - " + sMsg , "Pass");
				//			 gOBJ.gtsflag = true;
				di.Debug ("Exiting LABEL_CLICK_IFEXISTS case");
				break;
			}
		}break;

		case "LABEL_CLICK_RIGHT_ARROW" :{
			di.Debug ("Inside LABEL_CLICK_RIGHT_ARROW case");
			if (setExecutionObject()) {
				di.Debug ("Click right arrorw action in the function Do_Action inside DoScreenAction.java file");
				wbElement.sendKeys(Keys.ARROW_RIGHT);
				di.Debug ("Action:" + sAction + " performed on Label - " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Label - " + sMsg , "Pass");
				di.Debug ("Exiting LABEL_CLICK_RIGHT_ARROW case");
				break;
			}	
		}break;		 

		case "LABEL_CLICK_ENTER" :{
			di.Debug ("Inside LABEL_CLICK_ENTER case");
			if (setExecutionObject()) {
				di.Debug ("Click enter action in the function Do_Action inside DoScreenAction.java file");
				wbElement.sendKeys(Keys.ENTER);			
				di.Debug ("Action:" + sAction + " performed on Label - " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Label - " + sMsg , "Pass");
				di.Debug ("Exiting LABEL_CLICK_ENTER case");
				break;
			}	
		}break;		 

		case "LABEL_CLICK_DOWN_ARROW" :{
			di.Debug ("Inside LABEL_CLICK_DOWN_ARROW case");
			if (setExecutionObject()) {
				di.Debug ("Click down arrorw action in the function Do_Action inside DoScreenAction.java file");
				wbElement.sendKeys(Keys.ARROW_DOWN);	
				di.Debug ("Action:" + sAction + " performed on Label - " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Label - " + sMsg , "Pass");
				di.Debug ("Exiting LABEL_CLICK_DOWN_ARROW case");
				break;
			}			 
		}break;

		case "LABEL_CLICK_WITH_LINKTEXT" :{
			di.Debug ("Inside LABEL_CLICK_WITH_LINKTEXT case");
			if (setExecutionObject()) {
				wbElement.click();
				di.Debug ("Action:" + sAction + " performed on Label - " + sMsg + " :Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Label - " + sMsg , "Pass");
				di.Debug ("Exiting LABEL_CLICK_WITH_LINKTEXT case");
				break;
			}			
		}break;

		case "LABEL_CLICK_WITH_LINKTEXT_IFEXISTS" :{
			di.Debug ("Inside LABEL_CLICK_WITH_LINKTEXT_IFEXISTS case");
			if (setExecutionObject()) {
				wbElement.click();
				di.Debug ("Action:" + sAction + " performed on Label - " + sMsg + " :Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Label - " + sMsg , "Pass");
				di.Debug ("Exiting LABEL_CLICK_WITH_LINKTEXT_IFEXISTS case");
				break;
			}			
		}break;

		case "LABEL_CLICK_WITH_PARTIALLINKTEXT" :{
			di.Debug ("Inside LABEL_CLICK_WITH_PARTIALLINKTEXT case");
			if (setExecutionObject()) {
				wbElement.click();
				di.Debug ("Action:" + sAction + " performed on Label - " + sMsg + " :Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Label - " + sMsg , "Pass");
				di.Debug ("Exiting LABEL_CLICK_WITH_PARTIALLINKTEXT case");
				break;
			}			
		}break;

		case "LABEL_CLICK_WITH_PARTIALLINKTEXT_IFEXISTS" :{
			di.Debug ("Inside LABEL_CLICK_WITH_PARTIALLINKTEXT_IFEXISTS case");
			if (setExecutionObject()) {
				wbElement.click();
				di.Debug ("Action:" + sAction + " performed on Label - " + sMsg + " :Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Label - " + sMsg , "Pass");
				di.Debug ("Exiting LABEL_CLICK_WITH_PARTIALLINKTEXT_IFEXISTS case");
				break;
			}			
		}break;

		case "LABEL_CLICK_WITH_TAGANDTEXT" :{
			boolean TextFoundFlg = false;
			di.Debug ("Inside LABEL_CLICK_WITH_TAGANDTEXT case");
			di.Debug ("sValuefromTestDataTable: " + sValuefromTestDataTable);
			di.Debug ("sDef: " + sDef);
			//String[] Split_sValuefromTestDataTable = sValuefromTestDataTable.split("#");
			WebElement Bunch = gOBJ.getWbDriver().findElement(By.xpath("//" + sDef));
			List<WebElement> allOptions = Bunch.findElements(By.xpath("//" + sDef));
			for (WebElement option : allOptions) {
				if ((option.getText().equalsIgnoreCase(sValuefromTestDataTable))){
					di.Debug ("found!!!!!!!!!!#############%%%%%%%");
					option.click();
					di.Debug ("Action:" + sAction + " performed on Label - " + sMsg + "with text: " + sValuefromTestDataTable +  ": Pass");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Label - " + sMsg + "with text: " + sValuefromTestDataTable, "Pass");
					TextFoundFlg = true;
					break;
				}			    
			}
			if (!(TextFoundFlg)){
				di.Debug ("Action:" + sAction + " NOT performed on Label - " + sMsg + "with text: " + sValuefromTestDataTable +  " - Not found in the application: Fail");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " NOT performed on Label - " + sMsg + "with text: " + sValuefromTestDataTable + " - Not found in the application", "Fail");
				di.Debug ("Exiting LABEL_CLICK_WITH_TAGANDTEXT case");
				teststepfail = true;
				taskfail = true;
				break;
			}
		}break;

		case "LABEL_CLICK_WITH_TAGANDTEXT_IFEXISTS" :{
			boolean TextFoundFlg = false;
			di.Debug ("Inside LABEL_CLICK_WITH_TAGANDTEXT_IFEXISTS case");
			di.Debug ("sValuefromTestDataTable: " + sValuefromTestDataTable);
			if (gOBJ.hm.DoesHashKeyExist(sValuefromTestDataTable)){
				di.Debug ("Key exists in hashmap: " + sValuefromTestDataTable);
				sValuefromTestDataTable = gOBJ.hm.GetHashValueFromHashKey(sValuefromTestDataTable);
				di.Debug ("Key Value now is: " + sValuefromTestDataTable);
			}
			di.Debug ("sDef: " + sDef);
			//String[] Split_sValuefromTestDataTable = sValuefromTestDataTable.split("#");
			WebElement Bunch = gOBJ.getWbDriver().findElement(By.xpath("//" + sDef));
			List<WebElement> allOptions = Bunch.findElements(By.xpath("//" + sDef));
			for (WebElement option : allOptions) {
				di.Debug ("option.getText():----->> " + option.getText());
				if ((option.getText().equals(sValuefromTestDataTable))){
					di.Debug ("found!!!!!!!!!!#############%%%%%%%");
					option.click();
					di.Debug ("Action:" + sAction + " performed on Label - " + sMsg + "with text: " + sValuefromTestDataTable +  ": Pass");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Label - " + sMsg + "with text: " + sValuefromTestDataTable, "Pass");
					TextFoundFlg = true;
					break;
				}			    
			}
			if (!(TextFoundFlg)){
				di.Debug ("Action:" + sAction + " NOT performed on Label - " + sMsg + "with text: " + sValuefromTestDataTable +  " - Not found in the application: Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " NOT performed on Label - " + sMsg + "with text: " + sValuefromTestDataTable + " - Not found in the application", "Pass");
				di.Debug ("Exiting LABEL_CLICK_WITH_TAGANDTEXT_IFEXISTS case");
				break;
			}
		}break;

		case "LABEL_EXTRACT_TEXT" :
			di.Debug ("Inside LABEL_EXTRACT_TEXT case");
			if (setExecutionObject()) {
				di.Debug (wbElement.getText());
				if(sValuefromTestDataTable.toLowerCase().contains("path")){
					String [] pathToSaveTo = sValuefromTestDataTable.split("=");
					new Utils(gOBJ).WriteToOuputTXTFile("canonical", pathToSaveTo[1], sVariablefromDB + "=" + wbElement.getText());
				}
				gOBJ.hm.AssignHashValueToHashKey(sVariablefromDB, wbElement.getText());	
				sValuefromTestDataTable = wbElement.getText();
				di.Debug ("Action:" + sAction + " performed on Object - " + sMsg + " and retrieved information is :: " + gOBJ.hm.GetHashValueFromHashKey(sVariablefromDB) + " : Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Label - " + sMsg + " and retrieved information is :: " + gOBJ.hm.GetHashValueFromHashKey(sVariablefromDB) , "Pass");
				di.Debug ("The DB field ::::xxxx"+sVariablefromDB);
				di.Debug ("The value field ::::xxxx"+sValuefromTestDataTable);
				di.Debug ("Exiting LABEL_EXTRACT_TEXT case");
			}	
			break;

		case "LABEL_CLICK_AND_REFRESH" :
			di.Debug ("Inside LABEL_CLICK_AND_REFRESH case");
			if (setExecutionObject()) {
				di.Debug (" click and refresh action in the function Do_Action inside DoScreenAction.java file");
				wbElement.click();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				gOBJ.getWbDriver().navigate().refresh();		
				di.Debug ("Action:" + sAction + " performed on Label - " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Label - " + sMsg , "Pass");
				di.Debug ("Exiting LABEL_CLICK_AND_REFRESH case");
				//			 gOBJ.gtsflag = true;
			}	
			break;

		case "LABEL_MOUSEOVER" :{
			di.Debug ("Inside LABEL_MOUSEOVER case");
			if (setExecutionObject()) {
				Actions Act = new Actions(gOBJ.getWbDriver());
				Act.moveToElement(wbElement).build().perform();
				di.Debug ("Action:" + sAction + " performed on Lable - " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Lable - " + sMsg , "Pass");
				di.Debug ("Exiting LABEL_MOUSEOVER case");
				//			 gOBJ.gtsflag = true;
			}	
		}
		break;
		// Case statement for Label entry completed
		//*********************************************************************************************************		

		//*********************************************************************************************************		

		/*
		 * Every action related to RadioButton is covered below	 
		 */

		//NEEDS TO BE TESTED -------------------WARNING!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

		case "RADIOBUTTON_CHECK_EXISTS" :{
			di.Debug ("Inside RADIOBUTTON_CHECK_EXISTS case");
			if (setExecutionObject()) {
				di.Debug ("Action:" + sAction + " performed on RadioButton " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on RadioButton " + sMsg, "Pass");
				di.Debug ("Exiting RADIOBUTTON_CHECK_EXISTS case");
				break;
			}
			//			gOBJ.gtsflag = true;
		}break;		 

		case "RADIOBUTTON_CHECK_NOTEXISTS" :{
			di.Debug ("Inside RADIOBUTTON_CHECK_NOTEXISTS case");
			if (setExecutionObject()) {
				di.Debug ("Action:" + sAction + " performed on RadioButton " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on RadioButton " + sMsg, "Pass");
				di.Debug ("Exiting RADIOBUTTON_CHECK_NOTEXISTS case");
				break;
			}
			//			gOBJ.gtsflag = true;
		}break;

		case "RADIOBUTTON_SELECT" :{
			di.Debug ("Inside RADIOBUTTON_SELECT case");
			if (setExecutionObject()) {
				wbElement.click();
				di.Debug ("Action:" + sAction + " performed on RadioButton - " + sMsg + " :Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on RadioButton - " + sMsg , "Pass");
				//			 gOBJ.gtsflag = true;
				di.Debug ("Exiting RADIOBUTTON_SELECT case");
				break;
			}
		}break;

		case "RADIOBUTTON_SELECT_IFEXISTS" :{
			di.Debug ("Inside RADIOBUTTON_SELECT_IFEXISTS case");
			if (setExecutionObject()) {
				wbElement.click();
				di.Debug ("Action:" + sAction + " performed on RadioButton - " + sMsg + " :Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on RadioButton - " + sMsg , "Pass");
				//			 gOBJ.gtsflag = true;
				di.Debug ("Exiting RADIOBUTTON_SELECT_IFEXISTS case");
				break;
			}
		}break;

		case "RADIOBUTTON_CHECK_IF_SELECTED" :{
			di.Debug ("Inside RADIOBUTTON_CHECK_IF_SELECTED case");
			if (setExecutionObject()) {
				if (wbElement.isSelected()){
					di.Debug ("Action:" + sAction + " performed on RadioButton " + sMsg + ": Pass");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on RadioButton " + sMsg, "Pass");
					di.Debug ("Exiting RADIOBUTTON_CHECK_IF_SELECTED case");
					break;
				}
			}else{
				di.Debug ("Action:" + sAction + " performed on RadioButton " + sMsg + "- Its should be selected but its not selected -  : Fail");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on RadioButton " + sMsg + "- Its should be selected but its not selected - ", "Fail");
				di.Debug ("Exiting RADIOBUTTON_CHECK_IF_SELECTED case");
				teststepfail = true;
				taskfail = true;
			}
			//			gOBJ.gtsflag = true;
		}break;		 

		case "RADIOBUTTON_CHECK_IF_NOTSELECTED" :{
			di.Debug ("Inside RADIOBUTTON_CHECK_IF_NOTSELECTED case");
			if (setExecutionObject()) {
				if (wbElement.isSelected()){
					di.Debug ("Action:" + sAction + " performed on RadioButton " + sMsg + "- Its should not be selected but its selected -  : Fail");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on RadioButton " + sMsg + "- Its should not be selected but its selected -  ", "Fail");
					di.Debug ("Exiting RADIOBUTTON_CHECK_IF_NOTSELECTED case");
					teststepfail = true;
					taskfail = true;
				}
			}else{
				di.Debug ("Action:" + sAction + " performed on RadioButton " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on RadioButton " + sMsg, "Pass");
				di.Debug ("Exiting RADIOBUTTON_CHECK_IF_SELECTED case");
				break;
			}
			//			gOBJ.gtsflag = true;
		}break;

		// Case statement for RadioButton entry completed
		//*********************************************************************************************************		

		//*********************************************************************************************************	

		/*
		 * Every action related to CheckBox is covered below	 
		 */

		//NEEDS TO BE TESTED -------------------WARNING!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

		case "CHECKBOX_CHECK_EXISTS" :{
			di.Debug ("Inside CHECKBOX_CHECK_EXISTS case");
			if (setExecutionObject()) {
				di.Debug ("Action:" + sAction + " performed on CheckBox " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on CheckBox " + sMsg, "Pass");
				di.Debug ("Exiting CHECKBOX_CHECK_EXISTS case");
				break;
			}
			//		gOBJ.gtsflag = true;
		}break;		 

		case "CHECKBOX_CHECK_NOTEXISTS" :{
			di.Debug ("Inside CHECKBOX_CHECK_NOTEXISTS case");
			if (setExecutionObject()) {
				di.Debug ("Action:" + sAction + " performed on CheckBox " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on CheckBox " + sMsg, "Pass");
				di.Debug ("Exiting CHECKBOX_CHECK_NOTEXISTS case");
				break;
			}
			//		gOBJ.gtsflag = true;
		}break;

		case "CHECKBOX_SELECT" :{
			di.Debug ("Inside CHECKBOX_SELECT case");
			if (setExecutionObject()) {
				wbElement.click();
				di.Debug ("Action:" + sAction + " performed on CheckBox - " + sMsg + " :Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on CheckBox - " + sMsg , "Pass");
				//			 gOBJ.gtsflag = true;
				di.Debug ("Exiting CHECKBOX_SELECT case");
				break;
			}
		}break;

		case "CHECKBOX_SELECT_IFEXISTS" :{
			di.Debug ("Inside CHECKBOX_SELECT_IFEXISTS case");
			if (setExecutionObject()) {
				wbElement.click();
				di.Debug ("Action:" + sAction + " performed on CheckBox - " + sMsg + " :Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on CheckBox - " + sMsg , "Pass");
				//			 gOBJ.gtsflag = true;
				di.Debug ("Exiting CHECKBOX_SELECT_IFEXISTS case");
				break;
			}
		}break;

		case "CHECKBOX_CHECK_IF_SELECTED" :{
			di.Debug ("Inside CHECKBOX_CHECK_IF_SELECTED case");
			if (setExecutionObject()) {
				if (wbElement.isSelected()){
					di.Debug ("Action:" + sAction + " performed on CheckBox " + sMsg + ": Pass");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on CheckBox " + sMsg, "Pass");
					di.Debug ("Exiting CHECKBOX_CHECK_IF_SELECTED case");
					break;
				}
			}else{
				di.Debug ("Action:" + sAction + " performed on CheckBox " + sMsg + "- Its should be selected but its not selected -  : Fail");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on CheckBox " + sMsg + "- Its should be selected but its not selected - ", "Fail");
				di.Debug ("Exiting CHECKBOX_CHECK_IF_SELECTED case");
				teststepfail = true;
				taskfail = true;
			}
			//		gOBJ.gtsflag = true;
		}break;		 

		case "CHECKBOX_CHECK_IF_NOTSELECTED" :{
			di.Debug ("Inside CHECKBOX_CHECK_IF_NOTSELECTED case");
			if (setExecutionObject()) {
				if (wbElement.isSelected()){
					di.Debug ("Action:" + sAction + " performed on CheckBox " + sMsg + "- Its should not be selected but its selected -  : Fail");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on CheckBox " + sMsg + "- Its should not be selected but its selected -  ", "Fail");
					di.Debug ("Exiting CHECKBOX_CHECK_IF_NOTSELECTED case");
					teststepfail = true;
					taskfail = true;
				}
			}else{
				di.Debug ("Action:" + sAction + " performed on CheckBox " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on CheckBox " + sMsg, "Pass");
				di.Debug ("Exiting CHECKBOX_CHECK_IF_NOTSELECTED case");
				break;
			}
			//		gOBJ.gtsflag = true;
		}break;

		case "CHECKBOX_UNSELECT" :{
			di.Debug ("Inside CHECKBOX_UNSELECT case");
			if (setExecutionObject()) {
				if (wbElement.isSelected()){ 
					wbElement.click();
					di.Debug ("Action:" + sAction + " performed on CheckBox - " + sMsg + " :Pass");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on CheckBox - " + sMsg , "Pass");
					//			 gOBJ.gtsflag = true;
					di.Debug ("Exiting CHECKBOX_UNSELECT case");
					break;
				}else{
					di.Debug ("Action:" + sAction + " NOT performed on CheckBox - " + sMsg + "- the CheckBox is NOT selected - :Fail");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " NOT performed on CheckBox - " + sMsg + "- the CheckBox is NOT selected -", "Fail");
					//			 gOBJ.gtsflag = true;
					di.Debug ("Exiting CHECKBOX_UNSELECT case");
					teststepfail = true;
					taskfail = true;
					break;
				}
			}
		}break;

		case "CHECKBOX_UNSELECT_IFEXISTS" :{
			di.Debug ("Inside CHECKBOX_UNSELECT_IFEXISTS case");
			if (setExecutionObject()) {
				if (wbElement.isSelected()){ 
					wbElement.click();
					di.Debug ("Action:" + sAction + " performed on CheckBox - " + sMsg + " :Pass");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on CheckBox - " + sMsg , "Pass");
					//			 gOBJ.gtsflag = true;
					di.Debug ("Exiting CHECKBOX_UNSELECT_IFEXISTS case");
					break;
				}else{
					di.Debug ("Action:" + sAction + " NOT performed on CheckBox - " + sMsg + "- the CheckBox exists but is NOT selected - :Fail");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " NOT performed on CheckBox - " + sMsg + "- the CheckBox exists but is NOT selected -", "Fail");
					//			 gOBJ.gtsflag = true;
					di.Debug ("Exiting CHECKBOX_UNSELECT_IFEXISTS case");
					teststepfail = true;
					taskfail = true;
					break;
				}
			}
		}break;

		// Case statement for CheckBox entry completed
		//*********************************************************************************************************		

		//*********************************************************************************************************		

		/*
		 * Every action related to List box is covered below	 
		 */
		case "LIST_CHECK_EXISTS" :{
			di.Debug ("Inside LIST_CHECK_EXISTS case");
			if (setExecutionObject()) {
				di.Debug ("Action:" + sAction + " performed on List " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on List " + sMsg, "Pass");
				di.Debug ("Exiting LIST_CHECK_EXISTS case");
				break;
			}
			//				gOBJ.gtsflag = true;
		}break;

		case "LIST_CHECK_NOTEXISTS" :{
			di.Debug ("Inside LIST_CHECK_NOTEXISTS case");
			if (setExecutionObject()) {
				di.Debug ("Action:" + sAction + " performed on List " + sMsg + ": Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on List " + sMsg, "Pass");
				di.Debug ("Exiting LIST_CHECK_NOTEXISTS case");
				break;
			}
			//				gOBJ.gtsflag = true;
		}break;

		case "LIST_CLICK" :{
			di.Debug ("Inside List_click case");
			if (setExecutionObject()) {
				wbElement.click();
				di.Debug ("Action:" + sAction + " performed on List - " + sMsg + " :Pass");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on List - " + sMsg , "Pass");
				//			 gOBJ.gtsflag = true;
				di.Debug ("Exiting List_click case");
				break;
			}
		}break;

		case "LIST_SELECT" :
			di.Debug ("Inside LIST_SELECT case");
			if (setDropdownObject()) {
				di.Debug ("sValuefromTestDataTable: " + sValuefromTestDataTable);
				if (selectValueinDropdown()){
					di.Debug ("Exiting LIST_SELECT case");
					//					gOBJ.gtsflag = true;
				}					
			}	
			break;

		case "LIST_SELECT_OPTIONAL" :
			di.Debug ("Inside LIST_SELECT_OPTIONAL case");
			if (setDropdownObject()) {
				di.Debug ("sValuefromTestDataTable: " + sValuefromTestDataTable);
				if (selectValueinDropdown()){
					di.Debug ("Exiting LIST_SELECT_OPTIONAL case");
					//					gOBJ.gtsflag = true;
				}					
			}	
			break;

		case "LIST_SELECT_IFEXISTS" :
			di.Debug ("Inside LIST_SELECT_IFEXISTS case");
			if (setDropdownObject()) {
				di.Debug ("sValuefromTestDataTable: " + sValuefromTestDataTable);
				if (selectValueinDropdown()){
					di.Debug ("Exiting LIST_SELECT_IFEXISTS case");
					//					gOBJ.gtsflag = true;
				}					
			}	
			break;

		case "LIST_VERIFY_SELECTED_VALUE" :
			di.Debug ("Inside LIST_VERIFY_SELECTED_VALUE case");
			if (setExecutionObject()) {
				Select mySelect= new Select(wbElement);
				WebElement option = mySelect.getFirstSelectedOption();
				di.Debug (option.getText()); //prints "Option"
				if (sValuefromTestDataTable==option.getText()){
					di.Debug ("Action:" + sAction + " performed on list " + sMsg + " - Exists in list: " + sValuefromTestDataTable + " : Pass");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on list " + sMsg + " - Exists in list: " + sValuefromTestDataTable, "Pass");
				}else{
					di.Debug ("Action:" + sAction + " NOT performed on list " + sMsg + " with value = " + sValuefromTestDataTable + " does not exist in the drop down list - "+sMsg + " :Fail");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " NOT performed on list " + sMsg + " with value = " + sValuefromTestDataTable + " does not exist in the drop down list - "+sMsg, "Fail");
					teststepfail = true;
				}
			}	
			di.Debug ("Exiting LIST_VERIFY_SELECTED_VALUE case");	
			break;

		case "LIST_VERIFY_ALL_ITEM" :
			di.Debug ("Inside LIST_VERIFY_ALL_ITEM case");
			if (setExecutionObject()) {
				di.Debug ("sValuefromTestDataTable: " + sValuefromTestDataTable);
				boolean bFlag = false;
				int matchCounter = 0;
				List <WebElement> options = dropdown.getOptions();
				String[] ItemToBeVerified = sValuefromTestDataTable.split(";");
				for (int iloop=0; iloop<ItemToBeVerified.length; iloop++){
					for(WebElement we:options){
						if(we.getText().equals(ItemToBeVerified[iloop])){
							di.Debug (sValuefromTestDataTable+" exists in the drop down list - "+sMsg);								
							bFlag = true;
							matchCounter++;
							break;
						}
						if (!bFlag){
							di.Debug ("Action:" + sAction + " NOT performed on list " + sMsg + " with value = " + ItemToBeVerified[iloop] + " does not exist in the drop down list - "+sMsg + " :Fail");
							new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " NOT performed on list " + sMsg + " with value = " + ItemToBeVerified[iloop] + " does not exist in the drop down list - "+sMsg, "Fail");
							teststepfail = true;
						}
					}	
				}
				if (ItemToBeVerified.length == matchCounter){
					di.Debug ("Action:" + sAction + " performed on list " + sMsg + " - all values verified - : Pass");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on list " + sMsg + " - all values verified - ", "Pass");
				}

				di.Debug ("Exiting LIST_VERIFY_ALL_ITEM case");			
			}	
			break;

			// Case Statement for Drop down box completed	
			//*********************************************************************************************************				 

		case "CHECKTEXT_EXIST" :{
			di.Debug ("Inside keyword CHECKTEXT_EXIST()...");
			di.Debug ("gOBJ.gObjDesc::: " + sDef);
			String[] TextToBeVerified = sDef.split("=");
			/*
				 	if the portion after the '=' is passed as a key/variable then check for the same in 
					hashmap else it would be a text which user wants to check on page 
			 */
			//user can also give multiple checktext in single go.. like text=abc&&def&7qaz
			String[] chekpointTexts = TextToBeVerified[1].split("&&");
			for (int i = 0; i<chekpointTexts.length; i++){
				if (gOBJ.hm.DoesHashKeyExist(chekpointTexts[i].trim())){
					di.Debug ("Value exists in hashmap for :::xxx " + chekpointTexts[i].trim());
					chekpointTexts[i] = gOBJ.hm.GetHashValueFromHashKey(chekpointTexts[i].trim());
					di.Debug ("Value retrieved from hashmap :::xxx " + chekpointTexts[i]);
				}	
				try{
					if (gOBJ.getWbDriver().getPageSource().contains(chekpointTexts[i])) {
						di.Debug ("The following text/message is available/displayed on the page ::" + chekpointTexts[i]);
						new Utils(gOBJ).ReportStepToResultDB("The following text/message is available/displayed on the page ::" + chekpointTexts[i], "Pass");
					}else{
						di.Debug ("The following text/message is NOT available/displayed on the page ::" + chekpointTexts[i]);
						new Utils(gOBJ).ReportStepToResultDB("The following text/message is NOT available/displayed on the page ::" + chekpointTexts[i], "Fail");
						teststepfail = true;
					}
				}catch(UnreachableBrowserException e){
					new Utils(gOBJ).ReportStepToResultDB("Unreachable Browser Exception", "Fail");
					di.Debug("Unreachable Browser Exception - Fail - " + e.getMessage());
					teststepfail = true;
					break;
				}

			}


			//String bodyText = gOBJ.getWbDriver().findElement(By.tagName("body")).getText();
			//di.Debug ("bodyText:::\n" + bodyText);
			//Assert.assertTrue("The search returned zero results.", bodyText.contains("The search returned zero results."));
			di.Debug ("Exiting keyword CHECKTEXT_EXIST()...");
		}break;

		case "CHECKTEXT_NOTEXIST" :{
			di.Debug ("Inside keyword CHECKTEXT_NOTEXIST()...");
			di.Debug ("gOBJ.gObjDesc::: " + sDef);
			String[] TextToBeVerified = sDef.split("=");
			/*
				 	if the portion after the '=' is passed as a key/variable then check for the same in 
					hashmap else it would be a text which user wants to check on page 
			 */
			//user can also give multiple checktext in single go.. like text=abc&&def&7qaz
			String[] chekpointTexts = TextToBeVerified[1].split("&&");
			for (int i = 0; i<chekpointTexts.length; i++){
				if (gOBJ.hm.DoesHashKeyExist(chekpointTexts[i].trim())){
					di.Debug ("Value exists in hashmap for :::xxx " + chekpointTexts[i].trim());
					chekpointTexts[i] = gOBJ.hm.GetHashValueFromHashKey(chekpointTexts[i].trim());
					di.Debug ("Value retrieved from hashmap :::xxx " + chekpointTexts[i]);
				}	
				if (gOBJ.getWbDriver().getPageSource().contains(chekpointTexts[i])) {
					di.Debug ("The following text/message is available/displayed on the page ::" + chekpointTexts[i]);
					new Utils(gOBJ).ReportStepToResultDB("The following text/message is available/displayed on the page which is not expected::" + chekpointTexts[i], "Fail");
					teststepfail = true;
				}else{
					di.Debug ("The following text/message is NOT available/displayed on the page ::" + chekpointTexts[i]);
					new Utils(gOBJ).ReportStepToResultDB("The following text/message is NOT available/displayed on the page ::" + chekpointTexts[i], "Pass");
				}
			}
			//String bodyText = gOBJ.getWbDriver().findElement(By.tagName("body")).getText();
			//di.Debug ("bodyText:::\n" + bodyText);
			//Assert.assertTrue("The search returned zero results.", bodyText.contains("The search returned zero results."));
			di.Debug ("Exiting keyword CHECKTEXT_NOTEXIST()...");
		}break;

		case "EXTRACT_TEXT_FROM_GIVEN_ROW_COLUMN_IN_TABLE" :{
			di.Debug ("Inside keyword EXTRACT_TEXT_FROM_GIVEN_ROW_COLUMN_IN_TABLE()...");				
			if (setExecutionObject()) {
				di.Debug ("sValuefromTestDataTable: " + sValuefromTestDataTable);
				di.Debug ("sDef: " + sDef);
				boolean foundTextFlag=false;
				String foundText = "";
				int rwNumDesired = Integer.parseInt(sValuefromTestDataTable.split(";")[0].trim());
				int ColNumDesired = Integer.parseInt(sValuefromTestDataTable.split(";")[1].trim());
				di.Debug ("rwNumDesired: " + rwNumDesired);
				di.Debug ("ColNumDesired: " + ColNumDesired);		

				//WebElement table_element = gOBJ.getWbDriver().findElement(By.id("MessagingDestinationControlList:MessagingDestinationControlListScreen:MessagingDestinationsControlLV"));
				//List<WebElement> tr_collection=table_element.findElements(By.xpath("id('MessagingDestinationControlList:MessagingDestinationControlListScreen:MessagingDestinationsControlLV')//tbody/tr"));
				WebElement table_element = gOBJ.getWbDriver().findElement(By.id(sDef.split("=")[1].trim()));
				List<WebElement> tr_collection=table_element.findElements(By.xpath("id('" + sDef.split("=")[1].trim() + "')//tbody/tr"));

				di.Debug("NUMBER OF ROWS IN THIS TABLE = "+tr_collection.size());
				int row_num,col_num = 0;
				row_num=1;
				for(WebElement trElement : tr_collection){
					List<WebElement> td_collection=trElement.findElements(By.xpath("td"));
					di.Debug("NUMBER OF COLUMNS="+td_collection.size());
					col_num=1;
					for(WebElement tdElement : td_collection){
						di.Debug("row # "+row_num+", col # "+col_num+ "text="+tdElement.getText());
						if((row_num==rwNumDesired) && (col_num==ColNumDesired)){							
							foundTextFlag = true;
							foundText = tdElement.getText();
							break;
						}
						if(foundTextFlag){
							break;
						}
						col_num++;
					}
					if(foundTextFlag){
						break;
					}
					row_num++;
				} 
				if(foundTextFlag){
					gOBJ.hm.AssignHashValueToHashKey(sVariablefromDB, foundText);
					di.Debug("Assigned the extracted value - " + foundText + " to variable: " + sVariablefromDB);
					di.Debug ("Action:" + sAction + " performed on Table " + sMsg + " row # "+row_num+", col # "+col_num+ " - text="+foundText +  "- : Pass");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Table " + sMsg + " row # "+row_num+", col # "+col_num+ " - text="+ foundText, "Pass");
				}else{
					di.Debug ("Text was not found for row # "+row_num+", col # "+col_num+ "- : Fail");
					new Utils(gOBJ).ReportStepToResultDB("Text was not found for row # "+row_num+", col # "+col_num, "Fail");
					teststepfail = true;
				}
			}

			di.Debug ("Exiting keyword EXTRACT_TEXT_FROM_GIVEN_ROW_COLUMN_IN_TABLE()...");
			break;
		}

		case "CLICK_BY_TEXT_IN_GIVEN_ROW_COLUMN_IN_TABLE" :{
			di.Debug ("Inside keyword CLICK_BY_TEXT_IN_GIVEN_ROW_COLUMN_IN_TABLE()...");		
			di.Debug ("sValuefromTestDataTable: " + sValuefromTestDataTable);
			di.Debug ("sDef: " + sDef);
			boolean foundTextFlag=false;
			String foundText = "";
			int rwNumDesired = Integer.parseInt(sValuefromTestDataTable.split(";")[0].trim());
			int ColNumDesired = Integer.parseInt(sValuefromTestDataTable.split(";")[1].trim());
			String strToVerify = sValuefromTestDataTable.split(";")[2].trim();
			di.Debug ("rwNumDesired: " + rwNumDesired);
			di.Debug ("ColNumDesired: " + ColNumDesired);	
			di.Debug ("strToVerify: " + strToVerify);
			if (strToVerify.toLowerCase().contains("ref=")){
				di.Debug ("strToVerify: " + strToVerify + " has a reference variable to be pointed to");
				String strKey = strToVerify.split("=")[1].trim();
				di.Debug ("Variable: " + strKey);
				if(gOBJ.hm.DoesHashKeyExist(strKey)){
					strToVerify = gOBJ.hm.GetHashValueFromHashKey(strKey);
					di.Debug ("Value for variable: " + strKey + " = " + strToVerify);
					if (setExecutionObject()) {				
						WebElement table_element = gOBJ.getWbDriver().findElement(By.id(sDef.split("=")[1].trim()));
						List<WebElement> tr_collection=table_element.findElements(By.xpath("id('" + sDef.split("=")[1].trim() + "')//tbody/tr"));

						di.Debug("NUMBER OF ROWS IN THIS TABLE = "+tr_collection.size());
						int row_num,col_num = 0;
						row_num=1;
						for(WebElement trElement : tr_collection){
							List<WebElement> td_collection=trElement.findElements(By.xpath("td"));
							di.Debug("NUMBER OF COLUMNS="+td_collection.size());
							col_num=1;
							for(WebElement tdElement : td_collection){
								di.Debug("row # "+row_num+", col # "+col_num+ "text="+tdElement.getText());
								if((row_num==rwNumDesired) && (col_num==ColNumDesired)){						
									if(tdElement.getText().equals(strToVerify)){
										foundTextFlag = true;
										try{
											tdElement.click();
											foundText = tdElement.getText();
										}catch(UnreachableBrowserException e){
											new Utils(gOBJ).ReportStepToResultDB("Unreachable Browser Exception", "Fail");
											di.Debug("Unreachable Browser Exception - Fail - " + e.getMessage());
											teststepfail = true;
											foundTextFlag = false;
										}						
										break;
									}
									break;
								}
								if(foundTextFlag){
									break;
								}
								col_num++;
							}
							if(foundTextFlag){
								break;
							}
							row_num++;
						} 
						if(foundTextFlag){
							di.Debug ("Action:" + sAction + " performed on Table " + sMsg + " for row # "+rwNumDesired+", col # "+ColNumDesired+ " - with text = "+foundText +  "- : Pass");
							new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Table " + sMsg + " row # "+rwNumDesired+", col # "+ColNumDesired+ " - with text ="+ foundText, "Pass");					
						}else{
							teststepfail = true;
							di.Debug ("Text -" + strToVerify + "- was not found for row # "+rwNumDesired+", col # "+ColNumDesired+ "- : Fail");
							new Utils(gOBJ).ReportStepToResultDB("Text -" + strToVerify + "- was not found for row # "+rwNumDesired+", col # "+ColNumDesired, "Fail");
						}
					}
				}else{
					teststepfail = true;
					di.Debug ("Referenced variable not found " + strKey + ": Fail");
					new Utils(gOBJ).ReportStepToResultDB("Referenced variable not found " + strKey,"Fail");
				}

			}else{
				if (setExecutionObject()) {			
					WebElement table_element = gOBJ.getWbDriver().findElement(By.id(sDef.split("=")[1].trim()));
					List<WebElement> tr_collection=table_element.findElements(By.xpath("id('" + sDef.split("=")[1].trim() + "')//tbody/tr"));

					di.Debug("NUMBER OF ROWS IN THIS TABLE = "+tr_collection.size());
					int row_num,col_num = 0;
					row_num=1;
					for(WebElement trElement : tr_collection){
						List<WebElement> td_collection=trElement.findElements(By.xpath("td"));
						di.Debug("NUMBER OF COLUMNS="+td_collection.size());
						col_num=1;
						for(WebElement tdElement : td_collection){
							di.Debug("row # "+row_num+", col # "+col_num+ "text="+tdElement.getText());
							if((row_num==rwNumDesired) && (col_num==ColNumDesired)){							
								if(tdElement.getText().equals(strToVerify)){
									foundTextFlag = true;
									try{
										tdElement.click();
										foundText = tdElement.getText();
									}catch(UnreachableBrowserException e){
										new Utils(gOBJ).ReportStepToResultDB("Unreachable Browser Exception", "Fail");
										di.Debug("Unreachable Browser Exception - Fail - " + e.getMessage());
										teststepfail = true;
										foundTextFlag = false;
									}	
									break;
								}
								break;
							}
							if(foundTextFlag){
								break;
							}
							col_num++;
						}
						if(foundTextFlag){
							break;
						}
						row_num++;
					} 
					if(foundTextFlag){
						di.Debug ("Action:" + sAction + " performed on Table " + sMsg + " for row # "+rwNumDesired+", col # "+ColNumDesired+ " - with text = "+foundText +  "- : Pass");
						new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Table " + sMsg + " row # "+rwNumDesired+", col # "+ColNumDesired+ " - with text ="+ foundText, "Pass");					
					}else{
						teststepfail = true;
						di.Debug ("Text -" + strToVerify + "- was not found for row # "+rwNumDesired+", col # "+ColNumDesired+ "- : Fail");
						new Utils(gOBJ).ReportStepToResultDB("Text -" + strToVerify + "- was not found for row # "+rwNumDesired+", col # "+ColNumDesired, "Fail");
					}
				}
			}

			di.Debug ("Exiting keyword CLICK_BY_TEXT_IN_GIVEN_ROW_COLUMN_IN_TABLE()...");
			break;
		}

		case "CLICK_IN_GIVEN_ROW_COLUMN_IN_TABLE" :{
			di.Debug ("Inside keyword CLICK_IN_GIVEN_ROW_COLUMN_IN_TABLE()...");		
			di.Debug ("sValuefromTestDataTable: " + sValuefromTestDataTable);
			di.Debug ("sDef: " + sDef);
			boolean foundTextFlag=false;
			String foundText = "";
			int rwNumDesired = Integer.parseInt(sValuefromTestDataTable.split(";")[0].trim());
			int ColNumDesired = Integer.parseInt(sValuefromTestDataTable.split(";")[1].trim());
			di.Debug ("rwNumDesired: " + rwNumDesired);
			di.Debug ("ColNumDesired: " + ColNumDesired);

			if (setExecutionObject()) {	
				WebElement table_element = gOBJ.getWbDriver().findElement(By.id(sDef.split("=")[1].trim()));
				List<WebElement> tr_collection=table_element.findElements(By.xpath("id('" + sDef.split("=")[1].trim() + "')//tbody/tr"));

				di.Debug("NUMBER OF ROWS IN THIS TABLE = "+tr_collection.size());
				int row_num,col_num = 0;
				row_num=1;
				for(WebElement trElement : tr_collection){
					List<WebElement> td_collection=trElement.findElements(By.xpath("td"));
					di.Debug("NUMBER OF COLUMNS="+td_collection.size());
					col_num=1;
					for(WebElement tdElement : td_collection){
						di.Debug("row # "+row_num+", col # "+col_num+ "text="+tdElement.getText());
						if((row_num==rwNumDesired) && (col_num==ColNumDesired)){							
							foundTextFlag = true;
							try{
								tdElement.click();
							}catch(UnreachableBrowserException e){
								new Utils(gOBJ).ReportStepToResultDB("Unreachable Browser Exception", "Fail");
								di.Debug("Unreachable Browser Exception - Fail - " + e.getMessage());
								teststepfail = true;
								foundTextFlag = false;
							}	
							break;
						}
						if(foundTextFlag){
							break;
						}
						col_num++;
					}
					if(foundTextFlag){
						break;
					}
					row_num++;
				} 
				if(foundTextFlag){
					di.Debug ("Action:" + sAction + " performed on Table " + sMsg + " for row # "+rwNumDesired+", col # "+ColNumDesired+  "- : Pass");
					new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Table " + sMsg + " row # "+rwNumDesired+", col # "+ColNumDesired, "Pass");					
				}else{
					teststepfail = true;
					di.Debug ("NOT found:: row # "+rwNumDesired+", col # "+ColNumDesired+ "- : Fail");
					new Utils(gOBJ).ReportStepToResultDB("NOT found:: row # "+rwNumDesired+", col # "+ColNumDesired, "Fail");
				}
			}

			di.Debug ("Exiting keyword CLICK_IN_GIVEN_ROW_COLUMN_IN_TABLE()...");
			break;
		}

		case "VERIFY_TEXT_IN_TABLE_IN_GIVEN_ROW_COLUMN" :{
			di.Debug ("Inside keyword VERIFY_TEXT_IN_TABLE_IN_GIVEN_ROW_COLUMN()...");		
			di.Debug ("sValuefromTestDataTable: " + sValuefromTestDataTable);
			di.Debug ("sDef: " + sDef);
			boolean foundTextFlag=false;
			String foundText = "";
			int rwNumDesired = Integer.parseInt(sValuefromTestDataTable.split(";")[0].trim());
			int ColNumDesired = Integer.parseInt(sValuefromTestDataTable.split(";")[1].trim());
			String strToVerify = sValuefromTestDataTable.split(";")[2].trim();
			di.Debug ("rwNumDesired: " + rwNumDesired);
			di.Debug ("ColNumDesired: " + ColNumDesired);	
			di.Debug ("strToVerify: " + strToVerify);
			if (strToVerify.toLowerCase().contains("ref=")){
				di.Debug ("strToVerify: " + strToVerify + " has a reference variable to be pointed to");
				String strKey = strToVerify.split("=")[1].trim();
				di.Debug ("Variable: " + strKey);
				if(gOBJ.hm.DoesHashKeyExist(strKey)){
					strToVerify = gOBJ.hm.GetHashValueFromHashKey(strKey);
					di.Debug ("Value for variable: " + strKey + " = " + strToVerify);
					if (setExecutionObject()) {				
						WebElement table_element = gOBJ.getWbDriver().findElement(By.id(sDef.split("=")[1].trim()));
						List<WebElement> tr_collection=table_element.findElements(By.xpath("id('" + sDef.split("=")[1].trim() + "')//tbody/tr"));

						di.Debug("NUMBER OF ROWS IN THIS TABLE = "+tr_collection.size());
						int row_num,col_num = 0;
						row_num=1;
						for(WebElement trElement : tr_collection){
							List<WebElement> td_collection=trElement.findElements(By.xpath("td"));
							di.Debug("NUMBER OF COLUMNS="+td_collection.size());
							col_num=1;
							for(WebElement tdElement : td_collection){
								di.Debug("row # "+row_num+", col # "+col_num+ "text="+tdElement.getText());
								if((row_num==rwNumDesired) && (col_num==ColNumDesired)){							
									foundTextFlag = true;
									foundText = tdElement.getText();
									break;
								}
								if(foundTextFlag){
									break;
								}
								col_num++;
							}
							if(foundTextFlag){
								break;
							}
							row_num++;
						} 
						if(foundTextFlag){
							if(strToVerify.equalsIgnoreCase(foundText)){
								di.Debug ("Action:" + sAction + " performed on Table " + sMsg + " for row # "+rwNumDesired+", col # "+ColNumDesired+ " - text match = "+foundText +  "- : Pass");
								new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Table " + sMsg + " row # "+rwNumDesired+", col # "+ColNumDesired+ " - text match ="+ foundText, "Pass");
							}else{
								teststepfail = true;
								di.Debug ("For row # "+rwNumDesired+", col # "+ColNumDesired+ " - text MISMATCH="+foundText +  " - Expected - " + strToVerify + ": Fail");
								new Utils(gOBJ).ReportStepToResultDB("For row # "+rwNumDesired+", col # "+ColNumDesired+ " - text MISMATCH="+foundText +  " - Expected - " + strToVerify, "Fail");
							}						
						}else{
							teststepfail = true;
							di.Debug ("Text -" + strToVerify + "- was not found for row # "+rwNumDesired+", col # "+ColNumDesired+ "- : Fail");
							new Utils(gOBJ).ReportStepToResultDB("Text -" + strToVerify + "- was not found for row # "+rwNumDesired+", col # "+ColNumDesired, "Fail");
						}
					}
				}else{
					di.Debug ("Referenced variable not found " + strKey + ": Fail");
					new Utils(gOBJ).ReportStepToResultDB("Referenced variable not found " + strKey,"Fail");
				}

			}else{
				if (setExecutionObject()) {		
					WebElement table_element = gOBJ.getWbDriver().findElement(By.id(sDef.split("=")[1].trim()));
					List<WebElement> tr_collection=table_element.findElements(By.xpath("id('" + sDef.split("=")[1].trim() + "')//tbody/tr"));

					di.Debug("NUMBER OF ROWS IN THIS TABLE = "+tr_collection.size());
					int row_num,col_num = 0;
					row_num=1;
					for(WebElement trElement : tr_collection){
						List<WebElement> td_collection=trElement.findElements(By.xpath("td"));
						di.Debug("NUMBER OF COLUMNS="+td_collection.size());
						col_num=1;
						for(WebElement tdElement : td_collection){
							di.Debug("row # "+row_num+", col # "+col_num+ "text="+tdElement.getText());
							if((row_num==rwNumDesired) && (col_num==ColNumDesired)){							
								foundTextFlag = true;
								foundText = tdElement.getText();
								break;
							}
							if(foundTextFlag){
								break;
							}
							col_num++;
						}
						if(foundTextFlag){
							break;
						}
						row_num++;
					} 
					if(foundTextFlag){
						if(strToVerify.equalsIgnoreCase(foundText)){
							di.Debug ("Action:" + sAction + " performed on Table " + sMsg + " for row # "+rwNumDesired+", col # "+ColNumDesired+ " - text match = "+foundText +  "- : Pass");
							new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Table " + sMsg + " row # "+rwNumDesired+", col # "+ColNumDesired+ " - text match ="+ foundText, "Pass");
						}else{
							teststepfail = true;
							di.Debug ("For row # "+rwNumDesired+", col # "+ColNumDesired+ " - text MISMATCH="+foundText +  " - Expected - " + strToVerify + ": Fail");
							new Utils(gOBJ).ReportStepToResultDB("For row # "+rwNumDesired+", col # "+ColNumDesired+ " - text MISMATCH="+foundText +  " - Expected - " + strToVerify, "Fail");
						}						
					}else{
						teststepfail = true;
						di.Debug ("Text -" + strToVerify + "- was not found for row # "+rwNumDesired+", col # "+ColNumDesired+ "- : Fail");
						new Utils(gOBJ).ReportStepToResultDB("Text -" + strToVerify + "- was not found for row # "+rwNumDesired+", col # "+ColNumDesired, "Fail");
					}
				}
			}
			di.Debug ("Exiting keyword VERIFY_TEXT_IN_TABLE_IN_GIVEN_ROW_COLUMN()...");
			break;
		}

		case "VERIFY_TEXT_IN_TABLE_IN_GIVEN_ROW" :{
			di.Debug ("Inside keyword VERIFY_TEXT_IN_TABLE_IN_GIVEN_ROW()...");		
			di.Debug ("sValuefromTestDataTable: " + sValuefromTestDataTable);
			di.Debug ("sDef: " + sDef);
			boolean foundTextFlag=false;
			int rwNumDesired = Integer.parseInt(sValuefromTestDataTable.split(";")[0].trim());
			String strToVerify = sValuefromTestDataTable.split(";")[1].trim();
			di.Debug ("rwNumDesired: " + rwNumDesired);
			di.Debug ("strToVerify: " + strToVerify);
			if (strToVerify.toLowerCase().contains("ref=")){
				di.Debug ("strToVerify: " + strToVerify + " has a reference variable to be pointed to");
				String strKey = strToVerify.split("=")[1].trim();
				di.Debug ("Variable: " + strKey);
				if(gOBJ.hm.DoesHashKeyExist(strKey)){
					strToVerify = gOBJ.hm.GetHashValueFromHashKey(strKey);
					di.Debug ("Value for variable: " + strKey + " = " + strToVerify);
					if (setExecutionObject()) {			
						WebElement table_element = gOBJ.getWbDriver().findElement(By.id(sDef.split("=")[1].trim()));
						List<WebElement> tr_collection=table_element.findElements(By.xpath("id('" + sDef.split("=")[1].trim() + "')//tbody/tr"));

						di.Debug("NUMBER OF ROWS IN THIS TABLE = "+tr_collection.size());
						int row_num,col_num = 0;
						row_num=1;
						for(WebElement trElement : tr_collection){
							List<WebElement> td_collection=trElement.findElements(By.xpath("td"));
							di.Debug("NUMBER OF COLUMNS="+td_collection.size());
							col_num=1;
							for(WebElement tdElement : td_collection){
								di.Debug("row # "+row_num+", col # "+col_num+ "text="+tdElement.getText());
								if((row_num==rwNumDesired) && tdElement.getText().equals(strToVerify)){							
									foundTextFlag = true;
									break;
								}
								if(foundTextFlag){
									break;
								}
								col_num++;
							}
							if(foundTextFlag){
								break;
							}
							row_num++;
						} 
						if(foundTextFlag){
							di.Debug ("Action:" + sAction + " performed on Table " + sMsg + " on row # "+rwNumDesired + " - text match = "+strToVerify +  "- : Pass");
							new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Table " + sMsg + " on row # "+rwNumDesired+" - text match ="+ strToVerify, "Pass");

						}else{
							teststepfail = true;
							di.Debug ("Text -" + strToVerify + "- was not found in row # "+rwNumDesired+ "- : Fail");
							new Utils(gOBJ).ReportStepToResultDB("Text -" + strToVerify + "- was not found in row # "+rwNumDesired, "Fail");
						}
					}
				}else{
					teststepfail = true;
					di.Debug ("Referenced variable not found " + strKey + ": Fail");
					new Utils(gOBJ).ReportStepToResultDB("Referenced variable not found " + strKey,"Fail");
				}

			}else{
				if (setExecutionObject()) {			
					WebElement table_element = gOBJ.getWbDriver().findElement(By.id(sDef.split("=")[1].trim()));
					List<WebElement> tr_collection=table_element.findElements(By.xpath("id('" + sDef.split("=")[1].trim() + "')//tbody/tr"));

					di.Debug("NUMBER OF ROWS IN THIS TABLE = "+tr_collection.size());
					int row_num,col_num = 0;
					row_num=1;
					for(WebElement trElement : tr_collection){
						List<WebElement> td_collection=trElement.findElements(By.xpath("td"));
						di.Debug("NUMBER OF COLUMNS="+td_collection.size());
						col_num=1;
						for(WebElement tdElement : td_collection){
							di.Debug("row # "+row_num+", col # "+col_num+ "text="+tdElement.getText());
							if((row_num==rwNumDesired) && tdElement.getText().equals(strToVerify)){							
								foundTextFlag = true;
								break;
							}
							if(foundTextFlag){
								break;
							}
							col_num++;
						}
						if(foundTextFlag){
							break;
						}
						row_num++;
					} 
					if(foundTextFlag){
						di.Debug ("Action:" + sAction + " performed on Table " + sMsg + " on row # "+rwNumDesired + " - text match = "+strToVerify +  "- : Pass");
						new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Table " + sMsg + " on row # "+rwNumDesired+" - text match ="+ strToVerify, "Pass");

					}else{
						teststepfail = true;
						di.Debug ("Text -" + strToVerify + "- was not found in row # "+rwNumDesired+ "- : Fail");
						new Utils(gOBJ).ReportStepToResultDB("Text -" + strToVerify + "- was not found in row # "+rwNumDesired, "Fail");
					}
				}
			}
			di.Debug ("Exiting keyword VERIFY_TEXT_IN_TABLE_IN_GIVEN_ROW()...");
			break;
		}

		case "VERIFY_TEXT_IN_TABLE_IN_GIVEN_COLUMN" :{
			di.Debug ("Inside keyword VERIFY_TEXT_IN_TABLE_IN_GIVEN_COLUMN()...");		
			di.Debug ("sValuefromTestDataTable: " + sValuefromTestDataTable);
			di.Debug ("sDef: " + sDef);
			boolean foundTextFlag=false;
			int ColNumDesired = Integer.parseInt(sValuefromTestDataTable.split(";")[0].trim());
			String strToVerify = sValuefromTestDataTable.split(";")[1].trim();
			di.Debug ("ColNumDesired: " + ColNumDesired);
			di.Debug ("strToVerify: " + strToVerify);
			if (strToVerify.toLowerCase().contains("ref=")){
				di.Debug ("strToVerify: " + strToVerify + " has a reference variable to be pointed to");
				String strKey = strToVerify.split("=")[1].trim();
				di.Debug ("Variable: " + strKey);
				if(gOBJ.hm.DoesHashKeyExist(strKey)){
					strToVerify = gOBJ.hm.GetHashValueFromHashKey(strKey);
					di.Debug ("Value for variable: " + strKey + " = " + strToVerify);
					if (setExecutionObject()) {				
						WebElement table_element = gOBJ.getWbDriver().findElement(By.id(sDef.split("=")[1].trim()));
						List<WebElement> tr_collection=table_element.findElements(By.xpath("id('" + sDef.split("=")[1].trim() + "')//tbody/tr"));

						di.Debug("NUMBER OF ROWS IN THIS TABLE = "+tr_collection.size());
						int row_num,col_num = 0;
						row_num=1;
						for(WebElement trElement : tr_collection){
							List<WebElement> td_collection=trElement.findElements(By.xpath("td"));
							di.Debug("NUMBER OF COLUMNS="+td_collection.size());
							col_num=1;
							for(WebElement tdElement : td_collection){
								di.Debug("row # "+row_num+", col # "+col_num+ "text="+tdElement.getText());
								if((col_num==ColNumDesired) && tdElement.getText().equals(strToVerify)){							
									foundTextFlag = true;
									break;
								}
								if(foundTextFlag){
									break;
								}
								col_num++;
							}
							if(foundTextFlag){
								break;
							}
							row_num++;
						} 
						if(foundTextFlag){
							di.Debug ("Action:" + sAction + " performed on Table " + sMsg + " on Column # "+ColNumDesired + " - text match = "+strToVerify +  "- : Pass");
							new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Table " + sMsg + " on Column # "+ColNumDesired+" - text match ="+ strToVerify, "Pass");

						}else{
							teststepfail = true;
							di.Debug ("Text -" + strToVerify + "- was not found in Column # "+ColNumDesired+ "- : Fail");
							new Utils(gOBJ).ReportStepToResultDB("Text -" + strToVerify + "- was not found in Column # "+ ColNumDesired, "Fail");
						}
					}
				}else{
					di.Debug ("Referenced variable not found " + strKey + ": Fail");
					new Utils(gOBJ).ReportStepToResultDB("Referenced variable not found " + strKey,"Fail");
					teststepfail = true;
				}

			}else{
				if (setExecutionObject()) {				
					WebElement table_element = gOBJ.getWbDriver().findElement(By.id(sDef.split("=")[1].trim()));
					List<WebElement> tr_collection=table_element.findElements(By.xpath("id('" + sDef.split("=")[1].trim() + "')//tbody/tr"));

					di.Debug("NUMBER OF ROWS IN THIS TABLE = "+tr_collection.size());
					int row_num,col_num = 0;
					row_num=1;
					for(WebElement trElement : tr_collection){
						List<WebElement> td_collection=trElement.findElements(By.xpath("td"));
						di.Debug("NUMBER OF COLUMNS="+td_collection.size());
						col_num=1;
						for(WebElement tdElement : td_collection){
							di.Debug("row # "+row_num+", col # "+col_num+ "text="+tdElement.getText());
							if((col_num==ColNumDesired) && tdElement.getText().equals(strToVerify)){							
								foundTextFlag = true;
								break;
							}
							if(foundTextFlag){
								break;
							}
							col_num++;
						}
						if(foundTextFlag){
							break;
						}
						row_num++;
					} 
					if(foundTextFlag){
						di.Debug ("Action:" + sAction + " performed on Table " + sMsg + " on Column # "+ColNumDesired + " - text match = "+strToVerify +  "- : Pass");
						new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on Table " + sMsg + " on Column # "+ColNumDesired+" - text match ="+ strToVerify, "Pass");

					}else{
						teststepfail = true;
						di.Debug ("Text -" + strToVerify + "- was not found in Column # "+ColNumDesired+ "- : Fail");
						new Utils(gOBJ).ReportStepToResultDB("Text -" + strToVerify + "- was not found in Column # "+ ColNumDesired, "Fail");
					}
				}
			}
			di.Debug ("Exiting keyword VERIFY_TEXT_IN_TABLE_IN_GIVEN_COLUMN()...");
			break;
		}

		case "VERIFY_TABLE_COLUMN_HEAD" :
			di.Debug ("Inside keyword VERIFY_TABLE_COLUMN_HEAD()...");				
			String ColumnNames  = ""; 
			String[] Split_sValuefromTestDataTable;
			boolean MatchFlag = true;

			String[] PropertyDef = sDef.split("=");

			WebElement tableSet = gOBJ.getWbDriver().findElement(By.id(PropertyDef[1]));
			List<WebElement> Div_Collection = tableSet.findElements(By.xpath("id('" + PropertyDef[1] + "')/div"));

			System.out.println("NUMBER OF DIV IN THIS TABLE (1st Level) = "+ Div_Collection.size());
			int row_num1 = 0,col_num1 = 0;
			int row_num=1, col_num = 0;
			for(WebElement divElement : Div_Collection){
				List<WebElement> Column_collection = divElement.findElements(By.xpath("div/div/div"));
				System.out.println("NUMBER OF COLUMNS = " + Column_collection.size());
				if (row_num == 2 ){		// in this guidewire it always displays column name in the 2nd DIV pane
					col_num=1;
					for(WebElement ColumnElement : Column_collection)
					{
						System.out.println("row # "+row_num1+", col # "+col_num1+ "text=" + ColumnElement.getText());
						ColumnNames = ColumnNames + ColumnElement.getText() + ";";
						col_num++;
					}			            
				}
				row_num++;
				if (row_num==3){
					break;
				}

			} 
			di.Debug("ColumnName	    :" + ColumnNames);
			Split_sValuefromTestDataTable = sValuefromTestDataTable.split(";");		        
			for (int i=0; i<Split_sValuefromTestDataTable.length; i++){
				if (ColumnNames.toLowerCase().indexOf(Split_sValuefromTestDataTable[i].toLowerCase()) != -1){
					di.Debug ("Following column name exists: " + Split_sValuefromTestDataTable[i]);
				}else{
					di.Debug ("Following column name DOES NOT exist: " + Split_sValuefromTestDataTable[i]);
					new Utils(gOBJ).ReportStepToResultDB("Following column name DOES NOT exist: " + Split_sValuefromTestDataTable[i], "Fail");
					MatchFlag = false;
					break;		        		
				}
			}
			if (MatchFlag){
				new Utils(gOBJ).ReportStepToResultDB("Following Table Column Head Verified" + ColumnNames, "Pass");
			}else{
				new Utils(gOBJ).ReportStepToResultDB("Table Column Head Verification failed", "Fail");
				teststepfail = true;
			}

			di.Debug ("Exiting keyword VERIFY_TABLE_COLUMN_HEAD()...");
			break;

		case "REQUESTMETHODPOST" : 		//RequestMethodPost
			di.Debug ("Inside type :: RequestMethodPost...");
			WebServices RequestMethodPost = new WebServices(gOBJ);
			di.Debug ("sVariablefromDB:  " + sVariablefromDB);
			di.Debug ("sValuefromTestDataTable:  " + sValuefromTestDataTable);
			String[] PostToURLParam = sValuefromTestDataTable.split("#");
			RequestMethodPost.PostParamatersToURLs(gOBJ.hm.GetHashValueFromHashKey(PostToURLParam[0]), gOBJ.hm.GetHashValueFromHashKey(PostToURLParam[1]), gOBJ.hm.GetHashValueFromHashKey(PostToURLParam[2]));
			break;

		case "WAIT" :{
			if (sValuefromTestDataTable != null && !(sValuefromTestDataTable.isEmpty())){
				if(new Utils(gOBJ).isInteger(sValuefromTestDataTable)){
					di.Debug ("Inside case 'Wait'");
					int waitTime = 0;
					try{
						waitTime =  Integer.parseInt(sValuefromTestDataTable);
					} catch(NumberFormatException e) { 
						di.Debug ("NumberFormatException: - " + e); 
					} catch(NullPointerException e) {
						di.Debug ("NullPointerException: - " + e);
					}

					try {
						for(int i = 0; i<waitTime; i++){
							di.Debug ("Clock ticking - " + i + " sec.");
							Thread.sleep(1000);
						}

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					new Utils(gOBJ).ReportStepToResultDB("Action: WAIT performed with value = " + waitTime, "Pass");
					di.Debug ("Exiting case 'Wait'");
				}else{
					new Utils(gOBJ).ReportStepToResultDB("Action: WAIT NOT performed :: Value = " + sValuefromTestDataTable + " is not of integer type", "Fail");
					di.Debug ("Action: WAIT NOT performed :: Value = " + sValuefromTestDataTable + " is not of integer type: Fail");
					di.Debug ("Exiting case 'Wait'");
					teststepfail = true;
				}
			}else{
				new Utils(gOBJ).ReportStepToResultDB("Action: WAIT NOT performed :: Value = " + sValuefromTestDataTable + " is not of integer type", "Fail");
				di.Debug ("Action: WAIT NOT performed :: Value = " + sValuefromTestDataTable + " is not of integer type: Fail");
				di.Debug ("Exiting case 'Wait'");
				teststepfail = true;
			}
		}break;

		/*case "VALIDATE":				
			String svaluetoValidate = null;
			String [] arrSetofValues = null;
			String[] arrvaluetoValidate = null;
			if (setExecutionObject()) {
				if (sVariablefromDB.contains("$$")){
					arrSetofValues = sVariablefromDB.split("$$");	
					for(int j=0;j<arrSetofValues.length;j++){
						arrvaluetoValidate = arrSetofValues[j].split("=");
						//System.out.println(gOBJ.getWbDriver().findElement(By.xpath("//*[contains(.,'" + arrvaluetoValidate[1] + "')]")));
						svaluetoValidate = wbElement.getAttribute(arrvaluetoValidate[0]);
						if (svaluetoValidate == arrvaluetoValidate[1]){
							di.Debug (" Validation ::::xxxxxxx Pass :::: Expected Value -"+arrvaluetoValidate[1]+" ::::: Actual Value - "+svaluetoValidate);
							new Utils(gOBJ).ReportStepToResultDB(" Validation ::::xxxxxxx Pass :::: Expected Value -"+arrvaluetoValidate[1]+" ::::: Actual Value - "+svaluetoValidate, "Pass");
						}
						else{
							di.Debug (" Validation ::::xxxxxxx Fail :::: Expected Value -"+arrvaluetoValidate[1]+" ::::: Actual Value - "+svaluetoValidate);
							new Utils(gOBJ).ReportStepToResultDB(" Validation ::::xxxxxxx Fail :::: Expected Value -"+arrvaluetoValidate[1]+" ::::: Actual Value - "+svaluetoValidate, "Fail");
							teststepfail = true;
						}
					}
				}
				else{
					arrvaluetoValidate = sVariablefromDB.split("=");
					//WebElement abc = gOBJ.getWbDriver().findElement(By.xpath("//*[contains(.,'returned zero')]"));
					//svaluetoValidate = wbElement.getAttribute("text");
					svaluetoValidate = wbElement.getAttribute(arrvaluetoValidate[0]);
					if (svaluetoValidate == arrvaluetoValidate[1]){
						di.Debug (" Validation ::::xxxxxxx Pass :::: Expected Value -"+arrvaluetoValidate[1]+" ::::: Actual Value - "+svaluetoValidate);	
					}
					else{
						di.Debug (" Validation ::::xxxxxxx Fail :::: Expected Value -"+arrvaluetoValidate[1]+" ::::: Actual Value - "+svaluetoValidate);

					}
				}	
			}
			break;*/

		default: {
			di.Debug ("The operation provided - "+ sAction+ " is not a standard one.");
			new Utils(gOBJ).ReportStepToResultDB("The operation provided - "+ sAction+ " is not a standard/defined one.", "Fail");
			teststepfail = true;
			taskfail = true;
		}
		}
		if(bFrame){
			gOBJ.setWbDriver(gOBJ.getWbDriver().switchTo().defaultContent());
		}
	}

	public boolean setDropdownObject(){
		DebugInterface di=new DebugInterface(gOBJ);
		di.Debug (" In the function setDropdownObject inside DoScreenAction.java file");
		String[] name = sDef.split("=");
		try {
			switch (name[0]){
			case "id"	:
				dropdown = new Select (gOBJ.getWbDriver().findElement(By.id(name[1])));
				di.Debug (sLogicId +" - " + sMsg + " successfully found in the application");
				di.Debug (" Exit the function setDropdownObject inside DoScreenAction.java file");
				if (sAction.toUpperCase().indexOf("NOTEXISTS") > 0){
					di.Debug (sLogicId +" - " + sMsg + " exists in the application");
					di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
					new Utils(gOBJ).ReportStepToResultDB(sLogicId +" - " + sMsg + " with property - " + sDef +" - exists in the application", "Fail");
					teststepfail = true;
					return false;	
				}else
					return true;
			case "name"	:	
				dropdown = new Select (gOBJ.getWbDriver().findElement(By.name(name[1])));
				di.Debug (sLogicId +" - " + sMsg + " successfully found in the application");
				di.Debug (" Exit the function setDropdownObject inside DoScreenAction.java file");
				if (sAction.toUpperCase().indexOf("NOTEXISTS") > 0){
					di.Debug (sLogicId +" - " + sMsg + " exists in the application");
					di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
					new Utils(gOBJ).ReportStepToResultDB(sLogicId +" - " + sMsg + " with property - " + sDef +" - exists in the application", "Fail");
					teststepfail = true;
					return false;	
				}else
					return true;
			case "href" :
				dropdown = new Select (gOBJ.getWbDriver().findElement(By.cssSelector("a[href*='" + name[1] + "']")));
				di.Debug (sLogicId +" - " + sMsg + " successfully found in the application");
				di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
				if (sAction.toUpperCase().indexOf("NOTEXISTS") > 0){
					di.Debug (sLogicId +" - " + sMsg + " exists in the application");
					di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
					new Utils(gOBJ).ReportStepToResultDB(sLogicId +" - " + sMsg + " with property - " + sDef +" - exists in the application", "Fail");
					teststepfail = true;
					return false;	
				}else
					return true;

			case "cssselector" :
				dropdown = new Select (gOBJ.getWbDriver().findElement(By.cssSelector(name[1])));
				di.Debug (sLogicId +" - " + sMsg + " successfully found in the application");
				di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
				if (sAction.toUpperCase().indexOf("NOTEXISTS") > 0){
					di.Debug (sLogicId +" - " + sMsg + " exists in the application");
					di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
					new Utils(gOBJ).ReportStepToResultDB(sLogicId +" - " + sMsg + " with property - " + sDef +" - exists in the application", "Fail");
					teststepfail = true;
					return false;	
				}else
					return true;

			case "xpath" :
				if (name.length>2){
					di.Debug ("The length of the property contains second '='");
					di.Debug ("name[1] :" + name[1]);
					di.Debug ("name[2] :" + name[2]);
					name[1]= name[1] + name[2];
					di.Debug ("name[1] :" + name[1]);
				}
				dropdown = new Select (gOBJ.getWbDriver().findElement(By.xpath(name[1])));
				di.Debug (sLogicId +" - " + sMsg + " successfully found in the application");
				di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
				if (sAction.toUpperCase().indexOf("NOTEXISTS") > 0){
					di.Debug (sLogicId +" - " + sMsg + " exists in the application");
					di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
					new Utils(gOBJ).ReportStepToResultDB(sLogicId +" - " + sMsg + " with property - " + sDef +" - exists in the application", "Fail");
					teststepfail = true;
					return false;	
				}else
					return true;	

			default		:	di.Debug ("Property set is not id/name/href/xpath/cssSelector for the object");
			new Utils(gOBJ).ReportStepToResultDB("Property set is not id/name/href/xpath/cssSelector for the object", "Fail");
			teststepfail = true;
			taskfail = true;
			return false;
			}
		}
		catch (NoSuchElementException ie ){

			if (sAction.toUpperCase().indexOf("NOTEXISTS") > 0){
				di.Debug (sLogicId +" - " + sMsg + " deos not exist in the application");
				di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
				//	System.exit(0);
				teststepfail = false;
				return true;	
			}else if (sAction.toUpperCase().indexOf("IFEXISTS") > 0){
				di.Debug (sLogicId +" - " + sMsg + " deos not exist in the application");
				di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
				//	System.exit(0);
				teststepfail = false;
				return false;	
			}
			else{
				di.Debug (sLogicId +" - " + sMsg + " with property - " + sDef +" - deos not exist in the application" +  " :Fail");
				di.Debug (" Exit the function setDropdownObject inside DoScreenAction.java file");
				new Utils(gOBJ).ReportStepToResultDB(sLogicId +" - " + sMsg + " with property - " + sDef +" - deos not exist in the application", "Fail");
				taskfail = true;
				teststepfail = true;
				return false;
			}
		}

		catch (UnexpectedTagNameException e){
			di.Debug ("UnexpectedTagNameException: " + e);
			new Utils(gOBJ).ReportStepToResultDB("UnexpectedTagNameException: " + e, "Fail");
			di.Debug ("Exit the function setExecutionObject inside DoScreenAction.java file");
			teststepfail = true;
			return false;
		}
		catch (ClassCastException e) {
			di.Debug ("ClassCastException: " + e);
			new Utils(gOBJ).ReportStepToResultDB("ClassCastException: " + e, "Fail");
			di.Debug ("Exit the function setExecutionObject inside DoScreenAction.java file");
			teststepfail = true;
			return false;
		}
		catch(UnreachableBrowserException e){
			new Utils(gOBJ).ReportStepToResultDB("Unreachable Browser Exception", "Fail");
			di.Debug("Unreachable Browser Exception - Fail - " + e.getMessage());
			teststepfail = true;
			return false;
		}
	}

	public boolean setExecutionObject(){
		DebugInterface di=new DebugInterface(gOBJ);
		di.Debug ("Inside function setExecutionObject()...");
		String[] name = sDef.split("=");
		try {
			switch (name[0].toLowerCase()){
			case "id"	:
				wbElement = gOBJ.getWbDriver().findElement(By.id(name[1]));
				di.Debug (sLogicId +" - " + sMsg + " successfully found in the application");
				di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
				if (sAction.toUpperCase().indexOf("NOTEXISTS") > 0){
					di.Debug (sLogicId +" - " + sMsg + " exists in the application");
					di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
					new Utils(gOBJ).ReportStepToResultDB(sLogicId +" - " + sMsg + " with property - " + sDef +" - exists in the application", "Fail");
					teststepfail = true;
					return false;	
				}else
					return true;
			case "name"	:
				wbElement = gOBJ.getWbDriver().findElement(By.name(name[1]));
				di.Debug (sLogicId +" - " + sMsg + " successfully found in the application");
				di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
				if (sAction.toUpperCase().indexOf("NOTEXISTS") > 0){
					di.Debug (sLogicId +" - " + sMsg + " exists in the application");
					di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
					new Utils(gOBJ).ReportStepToResultDB(sLogicId +" - " + sMsg + " with property - " + sDef +" - exists in the application", "Fail");
					teststepfail = true;
					return false;	
				}else
					return true;
			case "href" :
				wbElement = gOBJ.getWbDriver().findElement(By.cssSelector("a[href*='" + name[1] + "']"));
				di.Debug (sLogicId +" - " + sMsg + " successfully found in the application");
				di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
				if (sAction.toUpperCase().indexOf("NOTEXISTS") > 0){
					di.Debug (sLogicId +" - " + sMsg + " exists in the application");
					di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
					new Utils(gOBJ).ReportStepToResultDB(sLogicId +" - " + sMsg + " with property - " + sDef +" - exists in the application", "Fail");
					teststepfail = true;
					return false;	
				}else
					return true;
			case "class" :
				List<WebElement> elements = gOBJ.getWbDriver().findElements(By.className(name[1]));
				WebElement element = elements.get(0);
				di.Debug (element.getAttribute("id"));
				WebElement element1 = elements.get(1);
				di.Debug (element1.getAttribute("id"));
				di.Debug (sLogicId +" - " + sMsg + " successfully found in the application");
				di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
				if (sAction.toUpperCase().indexOf("NOTEXISTS") > 0){
					di.Debug (sLogicId +" - " + sMsg + " exists in the application");
					di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
					new Utils(gOBJ).ReportStepToResultDB(sLogicId +" - " + sMsg + " with property - " + sDef +" - exists in the application", "Fail");
					teststepfail = true;
					return false;	
				}else
					return true;
			case "cssselector" :
				wbElement = gOBJ.getWbDriver().findElement(By.cssSelector(name[1]));
				di.Debug (sLogicId +" - " + sMsg + " successfully found in the application");
				di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
				if (sAction.toUpperCase().indexOf("NOTEXISTS") > 0){
					di.Debug (sLogicId +" - " + sMsg + " exists in the application");
					di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
					new Utils(gOBJ).ReportStepToResultDB(sLogicId +" - " + sMsg + " with property - " + sDef +" - exists in the application", "Fail");
					teststepfail = true;
					return false;	
				}else
					return true;
			case "xpath" :
				if (name.length>2){
					di.Debug ("The length of the property contains second '='");
					di.Debug ("name[1] :" + name[1]);
					di.Debug ("name[2] :" + name[2]);
					name[1]= name[1] + name[2];
					di.Debug ("name[1] :" + name[1]);
				}
				wbElement = gOBJ.getWbDriver().findElement(By.xpath(name[1]));
				di.Debug (sLogicId +" - " + sMsg + " successfully found in the application");
				di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
				if (sAction.toUpperCase().indexOf("NOTEXISTS") > 0){
					di.Debug (sLogicId +" - " + sMsg + " exists in the application");
					di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
					new Utils(gOBJ).ReportStepToResultDB(sLogicId +" - " + sMsg + " with property - " + sDef +" - exists in the application", "Fail");
					teststepfail = true;
					return false;	
				}else
					return true;	
			case "linktext"	:
				wbElement = gOBJ.getWbDriver().findElement(By.linkText(name[1]));
				di.Debug (sLogicId +" - " + sMsg + " successfully found in the application");
				di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
				if (sAction.toUpperCase().indexOf("NOTEXISTS") > 0){
					di.Debug (sLogicId +" - " + sMsg + " exists in the application");
					di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
					new Utils(gOBJ).ReportStepToResultDB(sLogicId +" - " + sMsg + " with property - " + sDef +" - exists in the application", "Fail");
					teststepfail = true;
					return false;	
				}else
					return true;
			case "partialLinkText"	:
				wbElement = gOBJ.getWbDriver().findElement(By.partialLinkText(name[1]));
				di.Debug (sLogicId +" - " + sMsg + " successfully found in the application");
				di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
				if (sAction.toUpperCase().indexOf("NOTEXISTS") > 0){
					di.Debug (sLogicId +" - " + sMsg + " exists in the application");
					di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
					new Utils(gOBJ).ReportStepToResultDB(sLogicId +" - " + sMsg + " with property - " + sDef +" - exists in the application", "Fail");
					teststepfail = true;
					return false;	
				}else
					return true;
			default		:	di.Debug ("Property set is not id/name/href/class/xpath/cssSelector for the object");
			new Utils(gOBJ).ReportStepToResultDB("Property set is not id/name/href/class/xpath/cssSelector/linktext/partialLinkText for the object", "Fail");
			teststepfail = true;
			taskfail = true;
			return false;
			}
		}
		catch (NoSuchElementException ie ){			
			if (sAction.toUpperCase().indexOf("NOTEXISTS") > 0){
				di.Debug (sLogicId +" - " + sMsg + " deos not exist in the application");
				di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
				//	System.exit(0);
				teststepfail = false;
				return true;	
			}else if (sAction.toUpperCase().indexOf("IFEXISTS") > 0 ){
				di.Debug (sLogicId +" - " + sMsg + " deos not exist in the application");
				di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
				//	System.exit(0);
				teststepfail = false;
				return false;
			}else{
				di.Debug (sLogicId +" - " + sMsg + " with property - " + sDef +" - deos not exist in the application" +  " :Fail");
				di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
				new Utils(gOBJ).ReportStepToResultDB(sLogicId +" - " + sMsg + " with property - " + sDef +" - deos not exist in the application", "Fail");
				taskfail = true;
				teststepfail = true;
				return false;
			}		
		}		
		catch (UnexpectedTagNameException e){
			di.Debug ("UnexpectedTagNameException: " + e);
			new Utils(gOBJ).ReportStepToResultDB("UnexpectedTagNameException: " + e, "Fail");
			di.Debug ("Exit the function setExecutionObject inside DoScreenAction.java file");
			teststepfail = true;
			return false;
		}
		catch (ClassCastException e) {
			di.Debug ("ClassCastException: " + e);
			new Utils(gOBJ).ReportStepToResultDB("ClassCastException: " + e, "Fail");
			di.Debug ("Exit the function setExecutionObject inside DoScreenAction.java file");
			teststepfail = true;
			return false;
		}
		catch(UnreachableBrowserException e){
			new Utils(gOBJ).ReportStepToResultDB("Unreachable Browser Exception", "Fail");
			di.Debug("Unreachable Browser Exception - Fail - " + e.getMessage());
			teststepfail = true;
			return false;
		}
	}
	public boolean selectValueinDropdown(){
		DebugInterface di=new DebugInterface(gOBJ);
		boolean bFlag = false;
		di.Debug ("sValuefromTestDataTable: " + sValuefromTestDataTable); 

		/*
		 * if the selection is desired to be done through a reference value from previous step
		 * the value should be like ref=AccountNumber
		 * and 'AccountNumber' should be a variable defined for previous step having some value.
		 */				
		if(sValuefromTestDataTable.toLowerCase().contains("ref=")){
			sValuefromTestDataTable = sValuefromTestDataTable.split("=")[1].trim();
			if (gOBJ.hm.DoesHashKeyExist(sValuefromTestDataTable)){
				di.Debug ("Key exists in hashmap: " + sValuefromTestDataTable);
				sValuefromTestDataTable = gOBJ.hm.GetHashValueFromHashKey(sValuefromTestDataTable);
				di.Debug ("Key Value now is: " + sValuefromTestDataTable);
			}else{
				new Utils(gOBJ).ReportStepToResultDB ("Expected:: Key DOES NOT exist in hashmap: " + sValuefromTestDataTable, "Fail");
				di.Debug ("Key DOES NOT exist in hashmap: " + sValuefromTestDataTable);
			}
		}
		List <WebElement> options = dropdown.getOptions();
		for(WebElement we:options){
			if(we.getText().equals(sValuefromTestDataTable)){
				di.Debug (sValuefromTestDataTable+" exists in the drop down list - "+sMsg);
				//dropdown.selectByValue(sValuefromTestDataTable);
				dropdown.selectByVisibleText(sValuefromTestDataTable);
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " performed on list " + sMsg + " with value = " + sValuefromTestDataTable, "Pass");
				di.Debug (sValuefromTestDataTable+" selected in the drop down list - "+sMsg);
				di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
				bFlag = true;
				break;
			}
			if (!bFlag){
				di.Debug ("Action:" + sAction + " NOT performed on list " + sMsg + " with value = " + sValuefromTestDataTable + " does not exist in the drop down list - "+sMsg + " :Fail");
				new Utils(gOBJ).ReportStepToResultDB("Action:" + sAction + " NOT performed on list " + sMsg + " with value = " + sValuefromTestDataTable + " does not exist in the drop down list - "+sMsg, "Fail");
				di.Debug (" Exit the function setExecutionObject inside DoScreenAction.java file");
				teststepfail = true;
				//	System.exit(0);
			}
		}
		return bFlag;
	}
}
