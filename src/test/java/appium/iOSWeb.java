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
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public class iOSWeb extends DriverFactory {

	public static final Logger logger = LogManager.getLogger("Session1");

	@Test
	public void loginTMA() throws Exception {
		// Print put: "Hello this is the first test case"
		System.out.println("Hello this is the iOS Web automation");

		// Open browser, chrome/firefox   on Maven configuration file
		AppiumDriver driver = getDriver_iOS();

		driver.get("http://saucelabs.com/test/guinea-pig");
		WebElement div = driver.findElement(By.id("i_am_an_id"));
		Assert.assertEquals("I am a div", div.getText()); //check the text retrieved matches expected value
		driver.findElement(By.id("comments")).sendKeys("My comment"); //populate the comments field by id.
		Thread.sleep(5000);
	}

}