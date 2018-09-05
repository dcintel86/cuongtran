package cucumber.Steps;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.Augmenter;

import com.automation.remarks.video.RecorderFactory;
import com.automation.remarks.video.enums.VideoSaveMode;
import com.automation.remarks.video.recorder.IVideoRecorder;
import com.automation.remarks.video.recorder.VideoRecorder;

import automation.core.DriverFactory4Cucumber;
import automation.core.TestReport;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import io.restassured.RestAssured;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

public class BaseCucumber extends DriverFactory4Cucumber {
	public static final Logger logger = LogManager.getLogger("Base Cucumber");
	private IVideoRecorder recorder;
	static String remote = System.getProperty("remote", "false").toLowerCase();
	static String scenarioName = null;
	static String browserType = System.getProperty("browser","chrome").toLowerCase();

	@Before
	public void beforeScenario(Scenario scenario) throws Exception {
		scenarioName = scenario.getName();
		System.setProperty("IsCucumber", "True");
		if (remote.equals("true")) {
			System.setProperty("video.enabled", "false");
		} else {
			// System.setProperty("video.enabled", "true");
			if (!browserType.equals("api")) {
				recorder = RecorderFactory.getRecorder(VideoRecorder.conf().recorderType());
				recorder.start();
			}
			
		}

	}

	@After
	public void afterScenario(Scenario scenario) throws Exception {
		if (!browserType.equals("api")) {
			String screenshotDirectory = System.getProperty("reportDir") + File.separator + "screenshots";
			String browser = System.getProperty("browser", "Chrome");
			String testMethodName = scenario.getName();
			String screenshotAbsolutePath = screenshotDirectory + File.separator + testMethodName + "_" + browser + ".png";
			File screenShotdir = new File(screenshotDirectory);
			File screenshot = new File(screenshotAbsolutePath);
			File video = stopRecording(testMethodName);

			if (scenario.getStatus().equals("failed")) {
				if (!screenShotdir.exists()) {
					screenShotdir.mkdirs();
				}
				if (TestReport.createFile(screenshot)) {

					try {
						TestReport.writeScreenshotToFile(driver, screenshot);
						logger.info("Written screenshot to " + screenshotAbsolutePath);
					} catch (ClassCastException weNeedToAugmentOurDriverObject) {
						// TODO: handle exception
						TestReport.writeScreenshotToFile(new Augmenter().augment(driver), screenshot);
						logger.info("Written screenshot to " + screenshotAbsolutePath);
					}
				} else {
					logger.error("Unable to create " + screenshotAbsolutePath);
				}
				doVideoProcessing(false, video);

			} else
				doVideoProcessing(true, video);

			driver.quit();
			driver = null;

			// ********************************FOR RECORDING REMOTE VIDEO IN SELENIUM GRID
			// WITH selenium-video-node<****************************************************
			if (remote.equals("true")) {
				try {
					// File (or directory) with old name
					File file = new File(System.getProperty("video.path") + "\\" + sessionid.toString() + ".webm");
					// File (or directory) with new name
					File file_new = new File(System.getProperty("video.path") + "\\" + scenarioName + "_"
							+ new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ".webm");
					// Rename file (or directory)
					try {
						boolean success = file.renameTo(file_new);
						if (!success) {
							// File was not successfully renamed
							System.out.println("Video cannot be renamed");
						} else
							System.out.println(
									"Video is stored at: " + System.getProperty("video.path") + "\\" + scenarioName + "_"
											+ new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ".webm");
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
		
	}

	private File stopRecording(String filename) {
		if (recorder != null) {
			return recorder.stopAndSave(filename);
		} else {
			return null;
		}

	}

	public static String doVideoProcessing(boolean successfulTest, File video) {
		String filePath = formatVideoFilePath(video);
		if (!successfulTest || VideoRecorder.conf().saveMode().equals(VideoSaveMode.ALL)) {
			return filePath;
		} else if (video != null) {
			video.delete();
			logger.info("Recording video has been removed since TC passed");
		}
		return "";
	}

	private static String formatVideoFilePath(File video) {
		if (video == null) {
			return "";
		} else {
			return video.getAbsolutePath();
		}
	}
}
