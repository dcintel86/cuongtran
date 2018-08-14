package apiTesting;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import apiTesting.Constant;
public class SignIn_Action {
	@Test
	public static void Execute() throws Exception{
		//This is to get the values from Excel sheet, passing parameters (Row num &amp; Col num)to getCellData method
		ExcelUtils.setExcelFile(Constant.Path_TestData + Constant.File_TestData,"Sheet1");
		String sUserName = ExcelUtils.getCellData(1, 1);
		String sPassword = ExcelUtils.getCellData(1, 2);
		System.out.println("User Name: "+sUserName);
		System.out.println("Password: "+sPassword);
		ExcelUtils.setCellData("Pass+Fail", 1, 3);

	}

}
