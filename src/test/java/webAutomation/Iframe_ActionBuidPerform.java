package webAutomation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.automation.remarks.testng.VideoListener;
import com.automation.remarks.video.annotations.Video;

import automation.core.DriverFactory;


	@Listeners(VideoListener.class)
	public class Iframe_ActionBuidPerform extends DriverFactory {

		public static final Logger logger = LogManager.getLogger("Session1");

		@Video
		@Test
		public void ActionBuildPerform() throws Exception {
			// Print put: "Hello this is the first test case"
			System.out.println("This TC is about handling with Iframe and multiple actions");

			// Open browser, chrome/firefox depends on Maven configuration file
			WebDriver driver = getDriver();

			// Maximum browser
			driver.manage().window().maximize();

			// Navigate browser to open website: "https://jqueryui.com/selectable/#display-grid"
			driver.get("https://jqueryui.com/selectable/#display-grid");
			driver.switchTo().frame(0);
				
				WebElement one = driver.findElement(By.xpath("//li[text()=1]"));
				WebElement three = driver.findElement(By.xpath("//li[text()=3]"));
				WebElement five = driver.findElement(By.xpath("//li[text()=5]"));
				
				//Press 3 buttons in parallel
				Actions builder = new Actions(driver);
				builder.keyDown(Keys.CONTROL).click(one).click(three).click(five).keyUp(Keys.CONTROL);
				Thread.sleep(1000);
				builder.click(three).pause(500).click(five).pause(500).click(one).pause(500).build().perform();

				
				//Generate composite action
				Action compositeAction = builder.build();
				
				//Perform the composite action
				compositeAction.perform();
				
				Thread.sleep(2000);
		
		}
	}