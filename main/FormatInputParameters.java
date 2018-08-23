package JoS.JAVAProjects;

import java.util.*;
import java.text.*;

public class FormatInputParameters {	

	ApplicationParams gOBJ=null;//=
	FormatInputParameters(ApplicationParams gOBJTemp)
	{
		gOBJ=gOBJTemp;
	}
	public void FormatInputOutputData( String DBValue, int currentInstanceId,DoScreenActions da) throws ParseException{	
		DebugInterface di=new DebugInterface(gOBJ);
		di.Debug ("Inside FormatInputOutputData()...");
		di.Debug ("Variable from DB - ::: " + da.sVariablefromDB);
		di.Debug ("Current Instance for task id:::xxx " + currentInstanceId);

		//provide the key based on the current task iteration within a flow.
		//if a task is called for first time within a flow, no change in the key is made.
		//but if the task is called twice within the same flow, the key is modified by adding "__2" or "__3" 
		//based on the current iteration (to hold the value for that iteration)
		if (!(da.sVariablefromDB.toString().equalsIgnoreCase("NULL"))){
			if (currentInstanceId == 0){
				/* do nothing
				 No change is required to the key since its called for first time
				 */		
				di.Debug ("No change is required to the key since its called for first time");
			}
			else{
				da.sVariablefromDB = da.sVariablefromDB+"__"+(currentInstanceId+1);
				di.Debug ("Current DB variable :::xxx " + da.sVariablefromDB);
			}

			//Read the value of the provided variable from the Test database
			if (gOBJ.hm.DoesHashKeyExist(da.sVariablefromDB)){
				di.Debug ("Following variable Key exists in hashmap ::: xxx " + da.sVariablefromDB);
				di.Debug ("Value from DB for variable ::: " + gOBJ.hm.GetHashValueFromHashKey(da.sVariablefromDB));
				da.sValuefromTestDataTable = gOBJ.hm.GetHashValueFromHashKey(da.sVariablefromDB);

				if (da.sValuefromTestDataTable.toString().equalsIgnoreCase("")){
					di.Debug ("The DB value field is empty for variable : " + da.sValuefromTestDataTable);
					di.Debug ("No Formatting is needed since the value of the variable is empty : " + da.sValuefromTestDataTable);
					return;
				}else{
					di.Debug ("The DB value field is NOT empty for variable : " + da.sValuefromTestDataTable);
					di.Debug ("Formatting is needed for value  : " + da.sValuefromTestDataTable);

					if(da.sValuefromTestDataTable.toUpperCase().contains("RNDNAME")){
						da.sValuefromTestDataTable=da.sValuefromTestDataTable.toLowerCase().replaceAll("rndname","");
						di.Debug(da.sValuefromTestDataTable);
						da.sValuefromTestDataTable = da.sValuefromTestDataTable + gOBJ.getgNumRunID();
						gOBJ.hm.AssignHashValueToHashKey(da.sVariablefromDB, da.sValuefromTestDataTable);
					}else if (da.sValuefromTestDataTable.toUpperCase().contains("RNDDATE")){
						/*da.sValuefromTestDataTable can be as below 
						 * -----------------will return date in mm/dd/yyyy format
						 * randdate+0' for current date
						 * randdate-0' for current date
						 * randdate+5' for for future day (+5 or +7 etc, adds the number of day/s to current date
						 * randdate-7' for for past day (-5 or -7 etc, subtracts the number of day/s from the current date
						 * randdatemonth+5' for for future month (+5 or +7 etc, adds the number of months to current date
						 * randdatemonth-7' for for past month (-5 or -7 etc, subtracts the number of months from the current date
						 * randdateyear+5' for for future year (+5 or +7 etc, adds the number of year/s to current date
						 * randdateyear-7' for for past year (-5 or -7 etc, subtracts the number of year/s from the current date						 
						 */
						da.sValuefromTestDataTable = GetCurrentDate(da.sValuefromTestDataTable);
					}else if(da.sValuefromTestDataTable.toUpperCase().contains("RNDDAY")){
						/*
						 * da.sValuefromTestDataTable can be as below:
						 * rndday+5 - for future day (returns only day part of the date between 01 and 31)
						 * rndday-4 - for past day (returns only day part of the date between 01 and 31)
						 * rndday+0 - for same day (returns only day part of the date between 01 and 31)
						 * rndday-0 - for same day (returns only day part of the date between 01 and 31)
						 */
						da.sValuefromTestDataTable = (GetCurrentDate(da.sValuefromTestDataTable)).split("/")[1];
					}else if (da.sValuefromTestDataTable.toUpperCase().contains("RNDMONTH")){
						/*
						 * da.sValuefromTestDataTable can be as below:
						 * rndmonth+5 - for future month (returns only month part of the date between 01 and 12)
						 * rndmonth-4 - for past month (returns only month part of the date between 01 and 12)
						 * rndmonth+0 - for same month (returns only mont parth of the date between 01 and 12)
						 * rndmonth-0 - for same month (returns only month part of the date between 01 and 12)
						 * rndmonthalpha+5 - for future month (returns only month part of the date between 01 and 12) - in alpha like "January"
						 * rndmonthshortalpha+5 - for future month (returns only month part of the date between 01 and 12) - in short alpha like "Jan" - 3 chars
						 */
						String tempConfigInfo = da.sValuefromTestDataTable;
						da.sValuefromTestDataTable = (GetCurrentDate(da.sValuefromTestDataTable)).split("/")[0];
						di.Debug ("RNDMONTH = "+da.sValuefromTestDataTable);
						if (tempConfigInfo.toUpperCase().contains("ALPHA")){
							if (tempConfigInfo.toUpperCase().contains("SHORT")){
								di.Debug ("Month to be returned in Alpha");
								String[] arrMonth = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
								da.sValuefromTestDataTable = arrMonth[Integer.valueOf(da.sValuefromTestDataTable)-1];
								di.Debug ("Month to be returned in short Alpha - "+da.sValuefromTestDataTable);
							}else{
								di.Debug ("Month to be returned in Alpha");
								String[] arrMonth = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
								da.sValuefromTestDataTable = arrMonth[Integer.valueOf(da.sValuefromTestDataTable)-1];
								di.Debug ("Month to be returned in Alpha - "+da.sValuefromTestDataTable);
							}
						}
					}else{
						/*Do nothing*/	
						di.Debug ("No change to the tag value");	
					}
					di.Debug ("The DB field ::::xxxx"+da.sVariablefromDB);
					di.Debug ("The value field ::::xxxx"+da.sValuefromTestDataTable);
					di.Debug ("Exiting from FormatInputOutputData()...");
				}				
			}else{
				di.Debug ("Following variable Key DOES NOT exist in hashmap ::: xxx " + da.sVariablefromDB);
				new Utils(gOBJ).ReportStepToResultDB("Following variable Key DOES NOT exist in hashmap ::: xxx " + da.sVariablefromDB, "Fail");
				da.taskfail = true;
				da.teststepfail = true;
				//the iteration should exit -include code here.
			}
		}
	}	

	public String GetCurrentDate(String RandomizationFormat)	{		
		DebugInterface di=new DebugInterface(gOBJ);	
		di.Debug ("Inside function GetCurrentDate()...");
		String DateTobeReturned = null;
		int dateAdjustment = 0, dateFormating = 0;
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar now = Calendar.getInstance(TimeZone.getDefault());
		Date currentDate = now.getTime();

		di.Debug("currentDate:" + dateFormat.format(currentDate));
		di.Debug ("RandomizationFormat:" + RandomizationFormat);

		if(RandomizationFormat.contains("+")){
			di.Debug ("'+' date ahead or today");
			dateAdjustment = Integer.parseInt((RandomizationFormat.split("\\+"))[1]);
			dateFormating = 0;

		}else if (RandomizationFormat.contains("-")){
			di.Debug ("'-' back date");
			dateAdjustment = Integer.parseInt((RandomizationFormat.split("\\-"))[1]);
			dateFormating = 1;
		}

		di.Debug("dateAdjustment:" + dateAdjustment);
		di.Debug("dateFormating:" + dateFormating);

		if (RandomizationFormat.toUpperCase().contains("MONTH")){
			di.Debug("Inside month formatting block");
			if((dateAdjustment==0 && dateFormating==0)||(dateAdjustment==0 && dateFormating==1)){
				DateTobeReturned = dateFormat.format(currentDate);
			}else if(dateAdjustment>0 && dateFormating==0){
				now.add(Calendar.MONTH,Integer.valueOf(dateAdjustment));
				DateTobeReturned = dateFormat.format(now.getTime());				
			}else if(dateAdjustment>0 && dateFormating==1){
				now.add(Calendar.MONTH,-Integer.valueOf(dateAdjustment));
				DateTobeReturned = dateFormat.format(now.getTime());
			}			
			di.Debug("DateTobeReturned: " + DateTobeReturned);
			di.Debug("Exiting month formatting block");
		}else if (RandomizationFormat.toUpperCase().contains("YEAR")){
			di.Debug("Inside year formatting block");
			if((dateAdjustment==0 && dateFormating==0)||(dateAdjustment==0 && dateFormating==1)){
				DateTobeReturned = dateFormat.format(currentDate);
			}else if(dateAdjustment>0 && dateFormating==0){
				now.add(Calendar.YEAR,Integer.valueOf(dateAdjustment));
				DateTobeReturned = dateFormat.format(now.getTime());				
			}else if(dateAdjustment>0 && dateFormating==1){
				now.add(Calendar.YEAR,-Integer.valueOf(dateAdjustment));
				DateTobeReturned = dateFormat.format(now.getTime());
			}			
			di.Debug("DateTobeReturned: " + DateTobeReturned);
			di.Debug("Exiting year formatting block");
		}else{
			if((dateAdjustment==0 && dateFormating==0)||(dateAdjustment==0 && dateFormating==1)){
				DateTobeReturned = dateFormat.format(currentDate);
			}else if(dateAdjustment>0 && dateFormating==0){
				now.add(Calendar.DAY_OF_MONTH,Integer.valueOf(dateAdjustment));
				DateTobeReturned = dateFormat.format(now.getTime());
			}else if(dateAdjustment>0 && dateFormating==1){
				now.add(Calendar.DAY_OF_MONTH,-Integer.valueOf(dateAdjustment));
				DateTobeReturned = dateFormat.format(now.getTime());
			}
			di.Debug("DateTobeReturned: " + DateTobeReturned);
			di.Debug("Exiting month formatting block");
		}

		di.Debug ("Exiting function GetCurrentDate()...");
		return DateTobeReturned;
	}
}
