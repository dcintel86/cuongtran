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
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {
	static DesiredCapabilities capabilities = new DesiredCapabilities();
	public static WebDriver driver = null;
	public static AndroidDriver driver_androidDriver = null;
	public static IOSDriver driver_iOSDriver = null;


	protected static SessionId sessionid = null;
	public String testName = null;
	static String browserType = System.getProperty("browser", "firefox").toLowerCase();
	static String remote = System.getProperty("remote", "false").toLowerCase();
	static String seleniumHub = System.getProperty("seleniumHub", "127.0.0.1").toLowerCase();
	static String version = System.getProperty("version", "any");
	static String proxyIP = System.getProperty("proxy", "na").toLowerCase();
	static String urlAppium = System.getProperty("urlAppium","127.0.0.1").toLowerCase();
	static String orientation = System.getProperty("orientation", "false").toLowerCase();
	static String deviceName = System.getProperty("deviceName", "any").toLowerCase();
	static String platformVersion = System.getProperty("platformVersion","any").toLowerCase();
	static String apkName = System.getProperty("apkName","any").toLowerCase();
	static String appPackage = System.getProperty("appPackage","any");
	static String appActivity = System.getProperty("appActivity","any");
	public static void setDriver(WebDriver dr) {
		driver = dr;
	}

	// Used to set driver to null for new browser
	public static void setEmptyDriver() {
		driver = null;
	}

	@SuppressWarnings("deprecation")
	public static WebDriver getDriver() throws Exception {

		if (remote.equals("true")) {
			
			if (!browserType.equals("safari")) {
				capabilities.setBrowserName(browserType);
				capabilities.setPlatform(Platform.WINDOWS);
				capabilities.setVersion(version);
			}

			
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
			case "safari":
				initSafari();
				break;

			default:
				initChrome();
				break;
			}
			
			//Connect to Appium or SeleniumGrid
			if (driver == null) {
				URL SeleniumGridURL = null;
				try {
					SeleniumGridURL = new URL(seleniumHub);
				} catch (MalformedURLException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				driver = new RemoteWebDriver(SeleniumGridURL, capabilities);
				sessionid = ((RemoteWebDriver) driver).getSessionId();

				
			}

		} else {

			switch (browserType) {
			case "chrome":
				if (driver == null) {
					WebDriverManager.chromedriver().setup();
					driver = new ChromeDriver();
				}
				break;
			case "ie":  //Uncheck "Enable Protected Mode" for both Internet and Restricted site in Security tab in Internet Options of IE
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
					if (!proxyIP.equals("na")) {
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
				if (driver == null) {
					if (proxyIP.equals("na")) {
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
	//For Android Driver
	public static AndroidDriver getDriver_Android() throws Exception {

		if (remote.equals("true")) {
			
			switch (browserType) {
			case "androidchrome":
				initAndroidChrome();
				break;
			case "androidapp":
				initAndroidApp();
				break;

			default:
				initAndroidChrome();
				break;
			}
			
			//Connect to Appium 
			if (driver_androidDriver == null) {
				if (browserType.equals("androidchrome")) {
					URL urlAppiumAndroid = new URL(urlAppium);
					driver_androidDriver = new AndroidDriver<>(urlAppiumAndroid, capabilities);
				}
				if (browserType.equals("androidapp")) {
					URL urlAppiumAndroid = new URL(urlAppium);
					driver_androidDriver = new AndroidDriver(urlAppiumAndroid, capabilities);
				}
			}

		}

		return  driver_androidDriver;

	}
	//Connect to iOSsafari
	public static IOSDriver getDriver_iOS() throws Exception {

		if (remote.equals("true")) {
	
			switch (browserType) {
			case "iossafari":
				initIos();
				break;

			default:
				initIos();
				break;
			}
			
			//Connect to Appium
			if (driver_iOSDriver == null) {
				if (browserType.equals("iossafari")) {
					URL urlAppiumIos = new URL (urlAppium);
					driver_iOSDriver = new IOSDriver(urlAppiumIos, capabilities);
					driver_iOSDriver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
				}

			}
		}
		return driver_iOSDriver;
	}


	@BeforeMethod
	public void handleTestMethodName(Method method) {
		testName = method.getName();
		if (remote.equals("true")) {
			System.setProperty("video.enabled", "false");
		} else
			System.setProperty("video.enabled", "true");
	}

	@AfterClass
	public void closeBrowser() throws IOException {

		if (driver != null) {
			driver.quit();
			driver = null;
		}
		if (driver_androidDriver != null) {
			driver_androidDriver.quit();
			driver_androidDriver = null;
		}
		if (driver_iOSDriver != null) {
			driver_iOSDriver.quit();
			driver_iOSDriver = null;
		}

		// ********************************FOR RECORDING REMOTE VIDEO IN SELENIUM GRID WITH selenium-video-node FOR WEB AUTOMATION****************************************************
		if (remote.equals("true") & !browserType.equals("androidchrome") & !browserType.equals("iossafari") & !browserType.equals("androidapp")) {
			// File (or directory) with old name
			File file = new File(System.getProperty("video.path") + "\\" + sessionid.toString() + ".webm");
			// File (or directory) with new name
			File file_new = new File(System.getProperty("video.path") + "\\" + testName + "_"
					+ new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ".webm");
			if (file_new.exists())
				file_new.delete();

			// Rename file (or directory)
			boolean success = file.renameTo(file_new);

			if (!success) {
				// File was not successfully renamed
				System.out.println("Video cannot be renamed");
			} else
				System.out.println("Video is stored at: " + System.getProperty("video.path") + "\\" + testName + "_"
						+ new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ".webm");

		}

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
	public static void initSafari() throws Exception {
		capabilities.setBrowserName("safari");
		capabilities.setPlatform(Platform.MAC);
	}
	public static void initAndroidChrome() throws Exception {
		switch (orientation) {
		case "true":
			capabilities.setCapability("orientation", "LANDSCAPE");
			break;
		default:
			capabilities.setCapability("orientation", "PORTRAIT");
			break;
		}

		capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "chrome");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "8.0.0");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "ANDROID");
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "any");
		capabilities.setCapability(MobileCapabilityType.BROWSER_VERSION, version);
	}
	public static void initAndroidApp() throws Exception {
		if (!apkName.equals("any")) {
			File app = new File("data" +File.separator+ "AndroidAPK", apkName);
			capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
		}
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "ANDROID");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
		capabilities.setCapability("appPackage", appPackage);
		capabilities.setCapability("appActivity", appActivity);

	}
	
	public static void initIos() throws Exception{
		String iosDeviceName = System.getProperty("iosDeviceName", "any");
		String iosVersion = System.getProperty("iosVersion", "any");
		String UDID = System.getProperty("UDID", "any");
		switch (orientation) {
		case "true":
			capabilities.setCapability("orientation", "LANDSCAPE");
			break;
		default:
			capabilities.setCapability("orientation", "PORTRAIT");
			break;
		}
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, iosVersion);
		capabilities.setCapability("udid", UDID);
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, iosDeviceName);
		capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "safari");
	}
	
}
