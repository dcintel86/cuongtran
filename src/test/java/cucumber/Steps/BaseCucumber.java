package cucumber.Steps;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	static String remote = System.getProperty("remote", "false").toLowerCase();
	static String scenarioName = null;

	
	@Before
	public void beforeScenario(Scenario scenario) throws Exception{
		scenarioName = scenario.getName(); 

		System.setProperty("IsCucumber", "True");
		 if (remote.equals("true")) {
	        	System.setProperty("video.enabled", "false");
			}else {
				//System.setProperty("video.enabled", "true");
				recorder = RecorderFactory.getRecorder(VideoRecorder.conf().recorderType());
				recorder.start();
			}

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
		//********************************FOR RECORDING REMOTE VIDEO IN SELENIUM GRID WITH selenium-video-node<****************************************************
		if (remote.equals("true")) {
			// File (or directory) with old name
			File file = new File(System.getProperty("video.path")+ "\\" +sessionid.toString()+".webm");
			// File (or directory) with new name
			File file_new = new File(System.getProperty("video.path")+ "\\" +scenarioName+"_"+new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+".webm");
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
