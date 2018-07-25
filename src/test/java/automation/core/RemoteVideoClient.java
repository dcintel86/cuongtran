package automation.core;

import com.automation.remarks.video.recorder.VideoRecorder;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static automation.core.RestUtils.sendRecordingRequest;

/**
 * Created by sepi on 16.12.16.
 */
public class RemoteVideoClient {

    private String servletUrl;

    public RemoteVideoClient(String nodeUrl) {
        //String servletPath = "/extra/Video";
    	String servletPath = "";
        this.servletUrl = nodeUrl + servletPath;
System.out.println(servletUrl);              
        
    }

    public void videoStart() {
        String folder_url = encodeFilePath(new File(VideoRecorder.conf().folder()));
System.out.println(folder_url);      
        String url = servletUrl + "/start?&folder=" + folder_url;
System.out.println(url);              
        sendRecordingRequest(url);
    }

    public void videoStop(String testName, boolean isSuccess) {
        String url = servletUrl + "/stop?result=" + isSuccess + "&name=" + testName;
        sendRecordingRequest(url);
    }

    private String encodeFilePath(File file) {
        URL videoFolder = null;
        try {
            videoFolder = file.toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return videoFolder.toString().replace("file:", "");
    }

}
