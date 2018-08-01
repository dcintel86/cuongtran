package cucumber.Steps;

import java.io.File;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;

import com.automation.remarks.video.recorder.VideoRecorder;
import com.automation.remarks.video.RecorderFactory;
import com.automation.remarks.video.enums.VideoSaveMode;
import com.automation.remarks.video.recorder.IVideoRecorder;

import automation.core.DriverFactory;
import automation.core.TestReport;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class BaseCucumber extends DriverFactory{
	public static final Logger logger = LogManager.getLogger("Base Cucumber");
	private IVideoRecorder recorder;
	
	@Before
	public void beforeScenario() throws Exception{
		System.setProperty("IsCucumber", "True");
		recorder = RecorderFactory.getRecorder(VideoRecorder.conf().recorderType());
		recorder.start();
	}
	
	@After
	public void afterScenario (Scenario scenario) throws Exception{
		Boolean result = scenario.isFailed();
		String screenshotDirectory = System.getProperty("reportDir") + File.separator + "screenshots";
		String browser = System.getProperty("browser", "Chrome");
		String testMethodName = scenario.getName();
		String screenshotAbsolutePath = screenshotDirectory + File.separator + testMethodName + "_" + browser + ".png";
		File screenShotdir = new File(screenshotDirectory);
		File screenshot = new File(screenshotAbsolutePath);
        File video = stopRecording(testMethodName);

		if (result) {
			WebDriver driver = null;
			try {
				driver = DriverFactory.getDriver();
			} catch ( Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			if (!screenShotdir.exists()) {
				screenShotdir.mkdirs();
			}
			if (TestReport.createFile(screenshot)) {
				try {
					TestReport.writeScreenshotToFile(driver, screenshot);
				} catch (ClassCastException weNeedToAugmentOurDriverObject) {
					// TODO: handle exception
					TestReport.writeScreenshotToFile(new Augmenter().augment(driver), screenshot);
	                logger.info("Written screenshot to " + screenshotAbsolutePath);
				}
			} else {
                logger.error("Unable to create " + screenshotAbsolutePath);

			}
            video = stopRecording(testMethodName);
            doVideoProcessing(false, stopRecording(testMethodName));

		} else
			{ doVideoProcessing(true, stopRecording(testMethodName));}

		driver.quit();
		//setEmptyDriver();
	}
	
    private File stopRecording(String filename) {
        if (recorder != null) {
            return recorder.stopAndSave(filename);
        }else{
            return null;
        }

    }
    public static String doVideoProcessing(boolean successfulTest, File video) {
        String filePath = formatVideoFilePath(video);
        if (!successfulTest || VideoRecorder.conf().saveMode().equals(VideoSaveMode.ALL)) {
            return filePath;
        } else if (video != null) {
            video.delete();
            logger.info("No video on success test");
        }
        return "";
    }
    private static String formatVideoFilePath(File video) {
        if (video == null) {
            return "";
        }else {
            return video.getAbsolutePath();
        }
    }
}
