package appium;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import automation.core.DriverFactory;

public class AndroidApp_Calculator extends DriverFactory{
	
	@Test
	private void Calculator() throws Exception {
		System.out.println("Hello this is Androi App TC");
		WebDriver driver = getDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		Thread.sleep(500);
		
		WebElement numberA = driver.findElement(By.id("com.sec.android.app.popupcalculator:id/bt_07"));
		numberA.click();
		numberA.click();
		
		WebElement minus = driver.findElement(By.id("com.sec.android.app.popupcalculator:id/bt_sub"));
		minus.click();
		
		WebElement numberB = driver.findElement(By.id("com.sec.android.app.popupcalculator:id/bt_09"));
		numberB.click();
		
		WebElement result = driver.findElement(By.id("com.sec.android.app.popupcalculator:id/bt_equal"));
		result.click();
		
		Assert.assertEquals(Integer.parseInt(driver.findElement(By.id("com.sec.android.app.popupcalculator:id/txtCalc")).getText()),68 );
		System.out.println("Verified successfully");

	
	}

}
