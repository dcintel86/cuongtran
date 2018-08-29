package appium;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import automation.core.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

public class AndroidApp_Lazada extends DriverFactory{
	
	public static final Logger logger = LogManager.getLogger("Amazon Shopping");
	//WebDriverWait wait = new WebDriverWait(driver_androidDriver, 10);
	//TouchActions touch = new TouchActions(driver_androidDriver);
	
	@SuppressWarnings("deprecation")
	@Test
	public void SignIn() throws Exception {
		logger.info("Lazada Shopping");
		AndroidDriver<?> driver = getDriver_Appium();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		
		//Select target app
		driver.pressKeyCode(AndroidKeyCode.HOME);
		driver.findElement(By.id("com.sec.android.app.launcher:id/iconview_imageView")).click();
		Thread.sleep(500);

		// Click on Tai Khoan button
		driver.findElementByXPath("//android.widget.FrameLayout[@index=4]").click();
		//Click on Dang Nhap/Dang Ky button
		Thread.sleep(1000);
		driver.findElementById("com.lazada.android:id/login_signup").click();;
		
		//Click on Dang Nhap button button
		//driver.findElement(By.id("com.lazada.android:id/btn_login_fresh_welcome_login"));
		driver.findElementById("com.lazada.android:id/btn_login_fresh_welcome_login").click();
		// Fulfill email and password
		driver.findElementById("com.lazada.android:id/et_laz_form_input_field_edit").sendKeys("Username12121341@gmail.com.vn");
		
		driver.findElementById("com.lazada.android:id/et_laz_form_password_field_edit").sendKeys("Password");
		Thread.sleep(1000);
		
		
		//Click on Dang Nhap button
		driver.findElementById("com.lazada.android:id/btn_login_form_account_login").click();
		

		
		System.out.println("TC done");
		//driver.quit();
		
	}

}
