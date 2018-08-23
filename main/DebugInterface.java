package JoS.JAVAProjects;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.commons.io.FileUtils;

import JoS.JAVAProjects.Reporting.*;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.Calendar;

public class DebugInterface {
	/**public static void main (String argv[]) {
		DebugFileInitiate();
		MoveToZip();
	}
	*/
	ApplicationParams gOBJ=null;//=
	DebugInterface(ApplicationParams gOBJTemp){
		gOBJ=gOBJTemp;
		//DebugFileInitiate ();
	}
	//Function to create the Debug file for logging the execution/reporting steps
	public void DebugFileInitiate () {	    
		System.out.println(gOBJ.getgStrAutomationPathNew());
		File dir = new File(gOBJ.getgStrAutomationPathNew() + "TestID_" + gOBJ.getmTestID() +"_runID_" + gOBJ.getgNumRunID());
		
		//Cleanup the directory if directory path exists
		if(dir.exists() && dir.isDirectory()) {
		    try {
				FileUtils.cleanDirectory(dir);
				System.out.println("Cleaning Directory");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{		//create the directory
			dir.mkdirs();
			System.out.println("Directory created");
		}
		gOBJ.setgStrDebugLogPath(gOBJ.getgStrAutomationPathNew() + "TestID_" + gOBJ.getmTestID() + "_runID_" + gOBJ.getgNumRunID());
		//gOBJ.setgDebugFileName("Debug-" + gOBJ.getmTestID() + "-" + gOBJ.getgNumRunID()  + "-" + gOBJ.getgTestSet());
		gOBJ.setgDebugFileName("TestGenerated-Log");
		gOBJ.setgStrDebugFileFullPath(gOBJ.getgStrDebugLogPath() + "\\" + gOBJ.getgDebugFileName() +  ".txt");

	    System.out.println("gStrDebugFileFullPath: = " + gOBJ.getgStrDebugFileFullPath());
	    gOBJ.setgDebugFileOut(new File(gOBJ.getgStrDebugFileFullPath()));
	    DefineDebugLogFile();
	}
	
	
	//Function to handle the reporting part of the framework
	//Reports the steps to a local log file till the run lasts
	public void Debug (String pStrComment) {

		System.out.println(pStrComment);
		FileOutputStream fos = null;		
    	Calendar cal = Calendar.getInstance();    	
		try {
			fos = new FileOutputStream(gOBJ.getgDebugFileOut(), true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	 
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		try {
			bw.write(cal.getTime() + "--" + pStrComment);
			bw.newLine();
			bw.close();
			fos.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public  void DefineDebugLogFile() {
		Debug ("________________*******Debug Log File Initiated*******__________________________");
		Debug ("________________________________________________________________________________");
		Debug ("File/Logs/Reports Location: " + gOBJ.getgStrDebugLogPath());
		Debug ("****************************************Test Begins**************************************");
		Debug ("Test ID::" + gOBJ.getmTestID());
		Debug ("Test Name::" + gOBJ.getgTestName());
		Debug ("Run ID::" + gOBJ.getgNumRunID());
		Debug ("Test Set::" + gOBJ.getgTestSet());
		Debug ("*****************************************************************************************");
		Debug ("*****************************************************************************************");
	}
	
	public void ZipDebugFolder () throws IOException {
		Debug ("Inside function ZipDebugFolder()...");
		Debug ("**********************************************************************************************");
		Debug ("**********************************************************************************************");
		Debug ("__________________________________ Zipping the Debug File______________________________________");
		Debug ("________________________________________ EOF___________________________________________________");
		Debug ("_______________________________________________________________________________________________");
		Debug ("_______________________________________________________________________________________________");
		

		//FileOutputStream DebugTxtFile_fos = new FileOutputStream(GlobalVariables.gStrDebugLogPath + "\\" + GlobalVariables.gDebugFileName + ".zip");
		FileOutputStream DebugTxtFile_fos = null;
		DebugTxtFile_fos = new FileOutputStream(gOBJ.getgStrDebugLogPath() + ".zip");
		ZipOutputStream zos = new ZipOutputStream(DebugTxtFile_fos);

		String fileName = gOBJ.getgDebugFileOut().getAbsolutePath();
		System.out.println("gOBJ.getgDebugFileOut - " + gOBJ.getgDebugFileOut());
		System.out.println("gOBJ.getgDebugFileOut.getAbsolutePath() - " + gOBJ.getgDebugFileOut().getAbsolutePath());
		System.out.println("Writing '" + fileName + "' to zip file");
		File Originalfolder = new File(gOBJ.getgStrDebugLogPath());
		
		//int TotalFileCount = new File(Originalfolder.getPath()).listFiles().length;
		//<List> FileListOfOrgFol = new File(Originalfolder.getPath());
		File[] filesInOriginalFolder = Originalfolder.listFiles();
		
		for (File abc: filesInOriginalFolder){
			File file = new File(abc.getAbsolutePath());
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			ZipEntry zipEntry = new ZipEntry(abc.getName());
			zos.putNextEntry(zipEntry);
			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
					zos.write(bytes, 0, length);
				}
			fis.close();
		}
		zos.closeEntry();		
		zos.close();
		DebugTxtFile_fos.close();
		
		File DoesZipExist = new File(gOBJ.getgStrDebugLogPath() + ".zip");
		
		//Cleanup the directory if directory path exists
		if(DoesZipExist.exists()){			
			
		    if(filesInOriginalFolder!=null) { //some JVMs return null for empty dirs
		        for(File eachf: filesInOriginalFolder) {
		            if(eachf.isDirectory()) {
		            	//DeleteFolder(eachf);
		            	eachf.delete();
		            } else {
		                eachf.delete();
		            }
		        }
		    }
		    Originalfolder.delete();
			}
		}	
}
