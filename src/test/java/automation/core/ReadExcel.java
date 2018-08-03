package automation.core;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ReadExcel {
	public static final Logger logger = LogManager.getLogger("Read Data from Excel file");
	
	private String testName;
	private String tcTitle = null;
	private String userInput = null;
	private String rawData = null;
	private String isRun = null;
	private List<String[]> varArray = new ArrayList<String[]>();
	private String[] dataArray;
	private Object[][] excelData = null;
	final String dataFile = System.getProperty("dataFile");
	final String dataSheet = System.getProperty("dataSheet");
	
	public static String[][] getExcelData(String fileName, String sheetName){
		String[][] arrayExcelData = null;
		try {
			FileInputStream fs = new FileInputStream(fileName);
			Workbook wb = Workbook.getWorkbook(fs);
			Sheet sh = wb.getSheet(sheetName);
			
			int totalNoofCols = sh.getColumns();
			int totalNoofRows = sh.getRows();
			
			arrayExcelData = new String[totalNoofRows-1][totalNoofCols];
			for (int i = 1; i < totalNoofRows; i++) {
				for (int j = 0; j < totalNoofCols; j++) {
					arrayExcelData[i-1][j] = sh.getCell(j, i).getContents();
				}
				
			}
			
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}catch (BiffException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return arrayExcelData;
	}
	
	
	private void extractVar() {
		// TODO Auto-generated method stub
		for (int i = 0; i < excelData.length; i++) {
			System.out.println("TEST NAME: "+ excelData[i][0].toString().toLowerCase());
			if (excelData[i][0].toString().toLowerCase().equals(testName.toLowerCase())) {
				rawData = excelData[i][2].toString();
				isRun = excelData[i][3].toString();
				System.out.println("RAW: " + rawData);
				break;
			}
		}
		if (rawData.isEmpty()) {
			logger.error("Unable to find test case");
			}else {
			dataArray = rawData.split("\n");
			for (int i = 0; i < dataArray.length; i++) {
				varArray.add(i, dataArray[i].split("="));
			}
		}
			
	}
		

	public ReadExcel() {
		Object[][] arrayObject = getExcelData(System.getProperty("usr.dir")+File.separator+"data"+File.separator+"Excel"+File.separator+dataFile, dataSheet);
		this.excelData = arrayObject;
		this.testName = testName;
		extractVar();
	}
	
	public String getVar(String varName){
		String result = null;
		for (int i = 0; i < varArray.size(); i++) {
			if (varArray.get(i)[0].equals(varName)) {
				result = varArray.get(i)[1];
				break;
			}
		}
		if(result == null) logger.error("Unable to find variable " + varName);
		return result;
	}
	
	public boolean isSkip() {
		if (isRun.toLowerCase().equals("y")) return false;
		else return true;
	}

	

}
