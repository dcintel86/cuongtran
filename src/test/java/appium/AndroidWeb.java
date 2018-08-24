package appium;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.automation.remarks.testng.VideoListener;
import com.automation.remarks.video.annotations.Video;

//user define
import automation.core.DriverFactory;
import io.appium.java_client.MobileElement;

public class AndroidWeb extends DriverFactory {

	public static final Logger logger = LogManager.getLogger("Session1");

	@Test
	public void loginTMA() throws Exception {
		// Print put: "Hello this is the first test case"
		System.out.println("Hello this is the Android Web automation");

		// Open browser, chrome/firefox depends on Maven configuration file
		WebDriver driver = getDriver();

		// Navigate browser to open website: "https://webmail.tma.com.vn"
		driver.get("https://webmail.tma.com.vn");

		// Logs a message with level INFO on this logger
		logger.info("Open Web mail TMA");

		// Find Element by name
		WebElement username = driver.findElement(By.name("username"));
		
		// Send "Yourname" to the element: username
		username.sendKeys("Yourname");

		WebElement password = driver.findElement(By.xpath("//*[@id=\'password\']"));
		password.sendKeys("YourPassword");
		Thread.sleep(200);

		// From element: password, submit Login form
		WebElement signin = driver.findElement(By.xpath("//*[@value='Sign In']"));
		signin.click();

		// Verify the current URL equals to "https://webmail.tma.com.vn/" in case of
		// failure login
		Assert.assertTrue(driver.findElement(By.xpath("//*[@id=\"ZLoginErrorPanel\"]/table/tbody/tr/td[2]")).getText().contains("The username or password is incorrect"));


		Thread.sleep(1000);
		System.out.println("Login TMA");
	}

}