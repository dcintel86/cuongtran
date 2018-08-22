package automation.utils.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class CommonData{

    public static String[][] getExcelData(String fileName, String sheetName) {
        String[][] arrayExcelData = null;
    try {
        FileInputStream fs = new FileInputStream(fileName);
        Workbook wb = Workbook.getWorkbook(fs);
        Sheet sh = wb.getSheet(sheetName);

        int totalNoOfCols = sh.getColumns();
        int totalNoOfRows = sh.getRows();
        
        arrayExcelData = new String[totalNoOfRows-1][totalNoOfCols];
        
        for (int i= 1 ; i < totalNoOfRows; i++) {

            for (int j=0; j < totalNoOfCols; j++) {
                arrayExcelData[i-1][j] = sh.getCell(j, i).getContents();
            }

        }
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
        e.printStackTrace();
    } catch (BiffException e) {
        e.printStackTrace();
    }
        return arrayExcelData;
    }  
    
    public static String[][] getExcelDataList(String fileName, String[] sheetNames) {
        String[][] arrayExcelData = null;
        String[][] arrayExcelDataResult = null;
    try {
        FileInputStream fs = new FileInputStream(fileName);
        Workbook wb = Workbook.getWorkbook(fs);
        
        for(int t=0;t< sheetNames.length;t++)
        {
         Sheet sh = wb.getSheet(sheetNames[t]);
 
         int totalNoOfCols = sh.getColumns();
         int totalNoOfRows = sh.getRows();
         
         arrayExcelData = new String[totalNoOfRows-1][totalNoOfCols];
         
         for (int i= 1 ; i < totalNoOfRows; i++) {
             for (int j=0; j < totalNoOfCols; j++) {
                 arrayExcelData[i-1][j] = sh.getCell(j, i).getContents();
             }
         }
         arrayExcelDataResult = appendArrays(arrayExcelDataResult, arrayExcelData);
        }
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
        e.printStackTrace();
    } catch (BiffException e) {
        e.printStackTrace();
    }
        return arrayExcelDataResult;
    }
    
    private static String[][] appendArrays(String[][] array1, String[][] array2) {
        if(array1 == null)
         return array2;
        if(array2 == null)
         return array1;
           String[][] ret = new String[array1.length + array2.length][];
           int i = 0;
           for (;i<array1.length;i++) {
               ret[i] = array1[i];
           }
           for (int j = 0;j<array2.length;j++) {
               ret[i++] = array2[j];
           }
           return ret;
       }
    
    

}
