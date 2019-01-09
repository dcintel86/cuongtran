package webAutomation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.automation.remarks.testng.VideoListener;
import com.automation.remarks.video.annotations.Video;

//user define
import automation.core.DriverFactory;

@Listeners(VideoListener.class)
public class DragAndDrop extends DriverFactory {

	public static final Logger logger = LogManager.getLogger("Drag and Drop");

	@Video
	@Test
	public void loginTMA() throws Exception {
		// Print put: "Drag and Drop"
		System.out.println("Drag and Drop using clickAndHold of Action");

		// Open browser, chrome/firefox depends on Maven configuration file
		WebDriver driver = getDriver();

		// Maximum browser
		driver.manage().window().maximize();

		// Navigate browser to open website: "https://webmail.tma.com.vn"
		driver.get("http://demo.guru99.com/test/drag_drop.html");

		// Logs a message with level INFO on this logger
		logger.info("Open Web site demo");

		Actions draganddrop = new Actions(driver);
		//draganddrop.clickAndHold(driver.findElement(By.xpath("//a[normalize-space()='BANK']"))).moveToElement(driver.findElement(By.xpath("//*[normalize-space()='DEBIT SIDE']/following::li[1]"))).release().build().perform();
		draganddrop.clickAndHold(driver.findElement(By.xpath("//a[normalize-space()='BANK']"))).release(driver.findElement(By.xpath("//*[normalize-space()='DEBIT SIDE']/following::li[1]"))).build().perform();
		Thread.sleep(2000);
	}

}