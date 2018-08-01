package automation.core;

import automation.core.DriverFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class TestReport extends TestListenerAdapter  {
    final String tmp_browser = System.getProperty("browser", "FIREFOX").toUpperCase();
    public static final Logger logger = LogManager.getLogger("Test Report");
    
    public static boolean createFile(File screenshot) {
        boolean fileCreated = false;

        if (screenshot.exists()) {
            fileCreated = true;
        } else {
            File parentDirectory = new File(screenshot.getParent());
            if (parentDirectory.exists() || parentDirectory.mkdirs()) {
                try {
                    fileCreated = screenshot.createNewFile();
                } catch (IOException errorCreatingScreenshot) {
                    errorCreatingScreenshot.printStackTrace();
                }
            }
        }

        return fileCreated;
    }
    
    public static void writeScreenshotToFile(WebDriver driver, File screenshot) {
        try {
            FileOutputStream screenshotStream = new FileOutputStream(screenshot);
            screenshotStream.write(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
            screenshotStream.close();
        } catch (IOException unableToWriteScreenshot) {
            System.err.println("Unable to write " + screenshot.getAbsolutePath());
            unableToWriteScreenshot.printStackTrace();
        }
    }
    
    public static String getTestClassName(ITestResult result) {
        String testName = result.getInstanceName().trim();
        String[] reqTestClassname = testName.split("\\.");
        int i = reqTestClassname.length - 1;
        return reqTestClassname[i];
    }

    public static String getTestName(ITestResult result) {
        String testName = result.getName().toString().trim();
        return testName;
    }
    
    @Override
    public void onTestFailure(ITestResult failingTest)  {
    	if (!tmp_browser.equals("API")) {
            WebDriver driver = null;
    		try {
    			driver = DriverFactory.getDriver();
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
            String screenshotDirectory = System.getProperty("reportDir") + File.separator + "screenshots";
            String browser = System.getProperty("browser", "Firefox");
            String browserVersion = System.getProperty("desiredBrowserVersion");
            String testClassName = getTestClassName(failingTest);
            String testMethodName = getTestName(failingTest);
            String screenshotAbsolutePath = screenshotDirectory + File.separator + testMethodName +  "_" + testClassName + "_" + browser + "_" + browserVersion +  ".png";
            File screenShotdir = new File(screenshotDirectory);
            if (!screenShotdir.exists()) {
                screenShotdir.mkdirs();
            }
            File screenshot = new File(screenshotAbsolutePath);
            if (createFile(screenshot)) {
                try {
                    writeScreenshotToFile(driver, screenshot);
                } catch (ClassCastException weNeedToAugmentOurDriverObject) {
                    writeScreenshotToFile(new Augmenter().augment(driver), screenshot);
                }
                logger.info("Written screenshot to " + screenshotAbsolutePath);
            } else {
                logger.error("Unable to create " + screenshotAbsolutePath);
            }
    	}
    }
}
