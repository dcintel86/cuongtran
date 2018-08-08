package automation.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory4Cucumber {
	static DesiredCapabilities capabilities = new DesiredCapabilities();
	public static WebDriver driver = null;
	protected static SessionId sessionid = null;
	public String testName = null;
	static String browserType = System.getProperty("browser", "firefox").toLowerCase();
	static String remote = System.getProperty("remote", "false").toLowerCase();
	static String seleniumHub = System.getProperty("seleniumHub", "none").toLowerCase();
	static String version = System.getProperty("version", "any").toLowerCase();

	public static WebDriver getDriver() throws Exception {
		driver = null;
		if (remote.equals("true")) {
			URL SeleniumGridURL = null;
			try {
				SeleniumGridURL = new URL(seleniumHub);
			} catch (MalformedURLException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			capabilities.setBrowserName(browserType);
			capabilities.setPlatform(Platform.WINDOWS);
			capabilities.setVersion(version);
			switch (browserType) {
			case "chrome":
				initChrome();
				break;
			case "firefox":
				initFirefox();
				break;
			case "ie":
				initIE();
				break;
			case "edge":
				initEdge();
				break;

			default:
				initChrome();
				break;
			}
			// capabilities.setVersion(version);
			if (driver == null)
				driver = new RemoteWebDriver(SeleniumGridURL, capabilities);
			sessionid = ((RemoteWebDriver) driver).getSessionId();

		} else {
			switch (browserType) {
			case "chrome":
				if (driver == null) {
					WebDriverManager.chromedriver().setup();
					driver = new ChromeDriver();
				}
				break;
			case "ie":
				if (driver == null) {
					WebDriverManager.iedriver().arch32().setup();
					driver = new InternetExplorerDriver();
				}
				break;
			case "edge":
				if (driver == null) {
					WebDriverManager.edgedriver().version("4.15063").setup();
					driver = new EdgeDriver();
				}
				break;
			case "firefox":
				if (driver == null) {
					WebDriverManager.firefoxdriver().arch64().setup();
					driver = new FirefoxDriver();
				}
				break;

			default:
				if (driver == null) {
					WebDriverManager.firefoxdriver().arch64().setup();
					driver = new FirefoxDriver();
				}
				break;
			}
		}

		return driver;

	}

	// Init Browser
	public static void initChrome() {
		capabilities.setCapability("chrome.switches", Arrays.asList("--no-default-browser-check"));
		capabilities.setCapability("chrome.switches", Arrays.asList("--disable-extensions"));
		HashMap<String, String> chromePreferences = new HashMap<String, String>();
		chromePreferences.put("profile.password_manager_enabled", "false");
		capabilities.setCapability("chrome.prefs", chromePreferences);
	}

	public static void initFirefox() throws Exception {
		ProfilesIni profilesIni = new ProfilesIni();
		FirefoxProfile profile = profilesIni.getProfile("John");
		capabilities.setCapability(FirefoxDriver.PROFILE, profile);
	}

	public static void initIE() throws Exception {
		capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
		capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		capabilities.setCapability("requireWindowFocus", true);
		capabilities.setCapability(InternetExplorerDriver.NATIVE_EVENTS, true);
		capabilities.setBrowserName("internet explorer");
	}

	public static void initEdge() throws Exception {
		capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
		capabilities.setCapability("requireWindowFocus", true);
		capabilities.setBrowserName("MicrosoftEdge");
	}

}
