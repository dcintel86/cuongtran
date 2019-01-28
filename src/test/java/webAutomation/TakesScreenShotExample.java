package webAutomation;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.automation.remarks.testng.VideoListener;
import com.automation.remarks.video.annotations.Video;

//user define
import automation.core.DriverFactory;

@Listeners(VideoListener.class)
public class TakesScreenShotExample extends DriverFactory {

	public static final Logger logger = LogManager.getLogger("TakesScreenShotExample");

	@Video
	@Test
	public void ContextClick() throws Exception {

		
		// Print put: "TakesScreenShotExample"
		System.out.println("TakesScreenShotExample");

		// Open browser, chrome/firefox depends on Maven configuration file
		WebDriver driver = getDriver();

		// Maximum browser
		driver.manage().window().maximize();

		// Navigate browser to open website: "https://www.packtpub.com/"
		driver.get("https://www.packtpub.com/");

		// Logs a message with level INFO on this logger
		logger.info("Open Web site demo");
		
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		System.out.println("Image saved at: " + scrFile.getAbsolutePath());
	}

}