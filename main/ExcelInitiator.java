package JoS.JAVAProjects;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import java.io.IOException;
import java.util.*;



public class ExcelInitiator {
	
	String sFilePath;
	
	public ExcelInitiator(){
		sFilePath = "C:\\";
	}
	public void ReadExcelValue() throws IOException{
		XSSFWorkbook wb=null;
		XSSFSheet sheet;
		XSSFRow row;
		
		int firstnumofRows;
	    int totnumofRows;
	    String sValue;

		try{
			wb = new XSSFWorkbook(sFilePath);
		    sheet = wb.getSheet("RunManager");
		    firstnumofRows = sheet.getFirstRowNum();
		    totnumofRows = sheet.getLastRowNum();
		    for (int i= firstnumofRows;i<=totnumofRows;i++){
		    	row = sheet.getRow(i);
		    	sValue = row.getCell(5).getStringCellValue();
		    	if(sValue.toUpperCase() =="YES") {
		    		Map<String, String> map = new HashMap<String, String>();
		    		map.put(row.getCell(2).getStringCellValue(),row.getCell(4).getStringCellValue());
		    	}
		    }
		    wb.close();
		}
		catch(Exception Fe){ 
			wb.close();
		}
		
		
		
	}
	public static void startExecution() throws IOException{
		ExcelInitiator ei = new ExcelInitiator();
		ei.ReadExcelValue();
		//Main.InitateExecution(map);
	}
}
