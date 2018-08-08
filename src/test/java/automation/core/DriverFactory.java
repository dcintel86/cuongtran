package automation.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;


import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {
	static DesiredCapabilities capabilities = new DesiredCapabilities();
	public static WebDriver driver = null;
	protected static SessionId sessionid = null;
	public String testName = null;
	static String browserType = System.getProperty("browser", "firefox").toLowerCase();
	static String remote = System.getProperty("remote", "false").toLowerCase();
	static String seleniumHub = System.getProperty("seleniumHub", "none").toLowerCase();
	static String version = System.getProperty("version","any").toLowerCase();
	static String proxyIP = System.getProperty("proxy","10.10.10.10").toLowerCase();
	
	public static void setDriver(WebDriver dr) {
		driver = dr;
	}

	//Used to set driver to null for new browser
	public static void setEmptyDriver() {
		driver = null;
	}
	
	@SuppressWarnings("deprecation")
	public static WebDriver getDriver() throws Exception {


		if (remote.equals("true")) {
			URL SeleniumGridURL = null;
			try {
				SeleniumGridURL = new URL (seleniumHub);
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
			sessionid = ((RemoteWebDriver)driver).getSessionId();

			
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
				if (driver == null ) {
					if (!proxyIP.equals("na") ) {
						org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
						proxy.setHttpProxy(proxyIP + ":" + 8080);
						DesiredCapabilities dc = DesiredCapabilities.firefox();
						dc.setCapability(CapabilityType.PROXY, proxy);
						WebDriverManager.firefoxdriver().arch64().setup();	
						driver = new FirefoxDriver(dc);
					} else {
						WebDriverManager.firefoxdriver().arch64().setup();	
						driver = new FirefoxDriver();

					}

				}
				break;

			default:
				if (driver == null ) {
					if (proxyIP.equals("na") ) {
						org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
						proxy.setHttpProxy(proxyIP + ":" + 8080);
						DesiredCapabilities dc = DesiredCapabilities.firefox();
						dc.setCapability(CapabilityType.PROXY, proxy);
						WebDriverManager.firefoxdriver().arch64().setup();	
						driver = new FirefoxDriver(dc);
					} else {
						WebDriverManager.firefoxdriver().arch64().setup();	
						driver = new FirefoxDriver();
					}
				}
				break;
			}
		}

		return driver;

	}

	@BeforeMethod
	   public void handleTestMethodName(Method method){
        testName = method.getName(); 
        if (remote.equals("true")) {
        	System.setProperty("video.enabled", "false");
		}else
			System.setProperty("video.enabled", "true");
    }
	
	@AfterClass
	public void closeBrowser() throws IOException {
		

		//********************************FOR RECORDING REMOTE VIDEO IN SELENIUM GRID WITH selenium-video-node<****************************************************
				if (remote.equals("true")) {
					// File (or directory) with old name
					File file = new File(System.getProperty("video.path")+ "\\" +sessionid.toString()+".webm");
		System.out.println(file);

					// File (or directory) with new name
					File file_new = new File(System.getProperty("video.path")+ "\\" +testName+"_"+new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+".webm");
		System.out.println(file_new);
					if (file_new.exists())
						file_new.delete();

					// Rename file (or directory)
					boolean success = file.renameTo(file_new);

					if (!success) {
					   // File was not successfully renamed
						System.out.println("Video cannot be renamed");
					}else
						System.out.println("Video is stored at: "+ System.getProperty("video.path")+"\\"+testName+"_"+new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+".webm");
					
				}
				if (driver != null) {
					driver.quit();
					driver = null;
				}
		}
		
		
	
	//Init Browser
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
