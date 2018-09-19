package webAutomation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

//user define
import automation.core.DriverFactory;

public class Popup_Handling extends DriverFactory {
	public static final Logger logger = LogManager.getLogger("Test log in TMA Web mail");

	@Test
	public void alert() throws Exception {
		WebDriver driver = getDriver();
		driver.manage().window().maximize();
		driver.get("http://tdcuong:12345678x!X@sccm.tma.com.vn/CMApplicationCatalog/#/SoftwareCatalog");
		logger.info("Open TMA sccm");

		// Switch to alert popup
		Thread.sleep(10000);
		Alert popup_alert = driver.switchTo().alert();
		popup_alert.sendKeys("tdcuong");
		popup_alert.sendKeys("12345678x!X");

		popup_alert.accept();
		Thread.sleep(2000);

	}
}
