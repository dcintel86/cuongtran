package webAutomation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import com.automation.remarks.video.annotations.Video;

import automation.core.DriverFactory;

public class WebDriverNavigate extends DriverFactory{
	
	public static final Logger logger = LogManager.getLogger("Web Navigation");

	@Video
	@Test
	public void WebNavigate() throws InterruptedException {
		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		driver.navigate().to("https://www.google.com/");
		
		WebElement searchBox = driver.findElement(By.name("q"));
		searchBox.sendKeys("Selenium WebDriver");
		searchBox.submit();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='rso']")));
		WebElement searchBox1 = driver.findElement(By.xpath("//input[@name='q' and @type='text']"));
		searchBox1.clear();
		searchBox1.sendKeys("Packt Publishing");
		searchBox1.submit();
			
		driver.navigate().back();
		Thread.sleep(2000);
		driver.navigate().forward();
		Thread.sleep(2000);
		driver.navigate().refresh();
		Thread.sleep(2000);
		
		driver.quit();
		
	}

}
