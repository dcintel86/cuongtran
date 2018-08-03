/*
Step 1: Open https://www.google.com/
Step 2: Input "Selenium"
Step 3: Click on first result
Step 4: Verify current url = "https://www.seleniumhq.org/"
*/







































package webAutomation;

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

@Listeners(VideoListener.class)
public class Session1Fail extends DriverFactory {
	public static final Logger logger = LogManager.getLogger("Session1Review");
	
	@Video
	@Test
	public void loginTMAFail() throws Exception {
		
		//Print put: "Hello this is the first test case"
		System.out.println("Hello this is the session review test case");
		
		//Open browser, chrome/firefox depends on Maven configuration file
		WebDriver driver = getDriver();
		
		//Maximum browser
		driver.manage().window().maximize();
		
		//Navigate browser to open website: "https://webmail.tma.com.vn"
		driver.get("https://www.google.com/");
		
		//Logs a message with level INFO on this logger
		logger.info("Open Google website");
		
		//Find Element by name
		WebElement input = driver.findElement(By.name("q"));
		//Send "Selenium" to the element
		input.sendKeys("Selenium");
		input.submit();
		
		Thread.sleep(2000);
		
		//Click on first item
		WebElement firstitem = driver.findElement(By.xpath("//*[@id=\"rso\"]/div[1]/div/div/div/div/h3/a"));
		firstitem.click();
		
		//Verify the current URL equals to "https://www.seleniumhq.org/" in case of failure login
		Assert.assertTrue(driver.getCurrentUrl().equals("https://www.seleniumhq.org/1"));
		
		Thread.sleep(5000);
		System.out.println("Login SeleniumHQ website successfully");
	}
	
}

