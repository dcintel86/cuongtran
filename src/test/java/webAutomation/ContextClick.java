package webAutomation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
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
public class ContextClick extends DriverFactory {

	public static final Logger logger = LogManager.getLogger("ContextClick");

	@Video
	@Test
	public void ContextClick() throws Exception {

		
		// Print put: "Right-Click"
		System.out.println("Right-Click");

		// Open browser, chrome/firefox depends on Maven configuration file
		WebDriver driver = getDriver();

		// Maximum browser
		driver.manage().window().maximize();

		// Navigate browser to open website: "http://swisnl.github.io/jQuery-contextMenu/demo.html"
		driver.get("http://swisnl.github.io/jQuery-contextMenu/demo.html");

		// Logs a message with level INFO on this logger
		logger.info("Open Web site demo");
		
		WebDriverWait wait = new WebDriverWait(driver, 2);
		Actions contextclick = new Actions(driver);
		
		WebElement rightclickme = driver.findElement(By.xpath("//span[text()='right click me']"));
		WebElement copy_btn = driver.findElement(By.xpath("//span[text()='Copy']"));
		contextclick.contextClick(rightclickme).click(copy_btn).build().perform();
		String text = wait.until(ExpectedConditions.alertIsPresent()).getText();
		System.out.println("Popup text: " + text);
	}

}