package restAPI;

import static com.jayway.restassured.RestAssured.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

import automation.utils.data.CommonData;
import automation.utils.data.readfile;

public class RestAPI {
	public static final Logger logger = LogManager.getLogger("API Test");
	static String workingDir = System.getProperty("user.dir");
	static String browser = System.getProperty("browser").toUpperCase();
	static DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	static Date date = new Date();
	static String destFile = workingDir + File.separator + "logs" + File.separator + browser + "_" + dateFormat.format(date);
	@BeforeTest
	public static void test123(){
		System.out.println("CREATE A NEW FOLDER: " + destFile);
		new File(destFile).mkdirs();
	}
	@Test(dataProvider="userData")
	public static void API(String TCID, String URL, String Path, String Method, String Header, String Parameters,String Body, String Status, String Validations, String JSONPath) throws IOException, InterruptedException{
		logger.info("*****************Start TC: " + TCID + "*********************************");
		//handle log Name
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		Date date = new Date();
		String logName = System.getProperty("user.dir") + File.separator + "logs" + File.separator + "logName.txt";
		File log_name = new File(logName);
		try {
			FileWriter file = new FileWriter(log_name,false);
			file.write(destFile + File.separator + "ResultTC_" + TCID);
			file.close();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		//Huy 05-12
		try {
			logger.info("METHOD: " + Method);
			//Read JSON file
			JSONObject obj = new JSONObject();
			String workingDir = System.getProperty("user.dir");
			//Create JSON file if it doesn't exist
			String pathFile = workingDir + File.separator + "data" + File.separator + "Excel" + "file1.txt";
			Path path = Paths.get(pathFile);
	        try {
	            Files.createFile(path);
	        } catch (FileAlreadyExistsException e) {}
			BufferedReader br = new BufferedReader(new FileReader(pathFile));
			String content = br.readLine();
			br.close();
			try {
				JSONObject obj_tmp = new JSONObject(content);
				obj = obj_tmp;
			} catch (Exception e) {
				obj = obj;
			}
			//
			//HMAC
			//test HMAC
			String hmac = null;
			try {
				String data = readfile.handleVariables(Body, obj);
				//System.out.println("DATAAAAAAAAAAAAAAAAAAAAA: " + data);
				if (data != "") data = data;
				else data = "{}";
				hmac = calculateRFC2104HMAC(data, "643a9b6f85db57ca01f17778411d0333");
				//System.out.println("HMACCCCCCCCCCCCCCCCCCCCCCCCCCCC: " + hmac);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//Handle Body
			if (!Body.isEmpty()){
				logger.info("BODY IS: " + readfile.handleVariables(Body, obj));
			}	
			//handle Header
			RequestSpecBuilder builder = new RequestSpecBuilder();
			builder.addHeader("Content-Type", "application/json; charset=UTF-8");
			if (!Header.isEmpty()){
				String[] header_tmp = readfile.createHeader(Header, obj);
				for (int i = 0; i < header_tmp.length; i = i + 2) {
					builder.addHeader(header_tmp[i], header_tmp[i+1]);
					logger.info(header_tmp[i] + ":" + header_tmp[i+1]);
				}
			}
			builder.addHeader("X-Hmac", hmac);
			RequestSpecification responseSpec = builder.build();
			String workingURL = readfile.createURL(URL, Path, Parameters, obj);
			logger.info("URL: " + workingURL);
			String val[] = readfile.createValidation(Validations);
			Response resp = null;
			if (Method != null){
				if (Method.matches("GET")){
					resp = 	given().
								spec(responseSpec).
							when().
								get(workingURL);
				}
				else if (Method.matches("POST")){
					resp = 	given().
							spec(responseSpec).
							body(readfile.handleVariables(Body, obj)).
						when().
							post(workingURL);
				}
				else if (Method.matches("DEL")){
					resp = 	given().
							spec(responseSpec).	
							body(readfile.handleVariables(Body, obj)).
						when().
							delete(workingURL);
				}
				else if (Method.matches("PUT")){
					resp = 	given().
							spec(responseSpec).		
							body(readfile.handleVariables(Body, obj)).
						when().
							put(workingURL);
				}
				//Convert resp to String
				String respInString = resp.asString();
				logger.info("RETURN: " + respInString);
				
				
				// Verify status code
				logger.info(resp.getStatusCode());
				if (!String.valueOf(resp.getStatusCode()).matches(Status)){
					logger.error(" The expected return code is: " + Status + " but the actual return code is: " + String.valueOf(resp.getStatusCode()));
				}
				Assert.assertTrue(String.valueOf(resp.getStatusCode()).matches(Status));
				//Convert resp to JSON
				ArrayList<String> JSONResultInString = new ArrayList<String>();
				ArrayList<String> varNames = new ArrayList<String>();
				JSONObject workingJSON = null;
				String resultJSON = null;
				double resultJSON_number = 0;
				//Handle JSON Array
				if (respInString.startsWith("[")) {
					JSONArray jsonResponse = new JSONArray(respInString);
					if (!JSONPath.isEmpty()) {
						String[]JSONPaths = readfile.createJSONPaths(JSONPath);
						for (int j = 0; j < JSONPaths.length; j++) {	
							String[] Path_tmp = JSONPaths[j].split("[=]");
							varNames.add(Path_tmp[0]);
							String[] JSONPath_tmp = readfile.createJSONPath(Path_tmp[1]);
							//Huy Nov 30
							try {
								workingJSON = jsonResponse.getJSONObject(Integer.parseInt(JSONPath_tmp[0]));
							} catch (Exception e) {
								String resp_tmp = "{\"resp\":" + respInString + "}";
								workingJSON = new JSONObject(resp_tmp);
							}
							//workingJSON = jsonResponse.getJSONObject(Integer.parseInt(JSONPath_tmp[0]));
							//Huy Nov 30
							for (int i = 1; i < JSONPath_tmp.length - 1; i++) {
								if (JSONPath_tmp[i].contains("[")){
									String[] PathArray_tmp = readfile.handleJSONArrayPath(JSONPath_tmp[i]);
									JSONArray resultJSON_A = workingJSON.getJSONArray(PathArray_tmp[0]);
									workingJSON = resultJSON_A.getJSONObject(Integer.parseInt(PathArray_tmp[1]));
								}
								else workingJSON = workingJSON.getJSONObject(JSONPath_tmp[i]);
							}
							// Handle final path
							if (!JSONPath_tmp[JSONPath_tmp.length - 1].contains("[")){
								try {
									resultJSON = workingJSON.getString(JSONPath_tmp[JSONPath_tmp.length - 1]);
									JSONResultInString.add(resultJSON);
								} catch (Exception e) {
									resultJSON_number = workingJSON.getDouble(JSONPath_tmp[JSONPath_tmp.length - 1]);
									JSONResultInString.add(Double.toString(resultJSON_number));
								}
							}
							else {
								String[] PathArray_tmp = readfile.handleJSONArrayPath(JSONPath_tmp[JSONPath_tmp.length - 1]);
								try {
									JSONArray resultJSON_A = workingJSON.getJSONArray(PathArray_tmp[0]);
									JSONResultInString.add(resultJSON_A.getString(Integer.parseInt(PathArray_tmp[1])));
								} catch (Exception e) {
									JSONArray resultJSON_A = workingJSON.getJSONArray(PathArray_tmp[0]);
									JSONResultInString.add(Double.toString(resultJSON_A.getDouble(Integer.parseInt(PathArray_tmp[1]))));
								}
							}
						}
					}
				}
				else {
					JSONObject jsonResponse = new JSONObject(respInString);	
					if (!JSONPath.isEmpty()) {
						String[]JSONPaths = readfile.createJSONPaths(JSONPath);
						for (int j = 0; j < JSONPaths.length; j++) {
							
							String[] Path_tmp = JSONPaths[j].split("=");
							varNames.add(Path_tmp[0]);
							String[] JSONPath_tmp = readfile.createJSONPath(Path_tmp[1]);
/*							try {
								workingJSON = jsonResponse.getJSONObject(JSONPath_tmp[0]);
							} catch (Exception e) {
								workingJSON = jsonResponse;
							}*/
							workingJSON = jsonResponse;
							for (int i = 0; i < JSONPath_tmp.length - 1; i++) {
								if (JSONPath_tmp[i].contains("[")){
									String[] PathArray_tmp = readfile.handleJSONArrayPath(JSONPath_tmp[i]);
									JSONArray resultJSON_A = workingJSON.getJSONArray(PathArray_tmp[0]);
									workingJSON = resultJSON_A.getJSONObject(Integer.parseInt(PathArray_tmp[1]));
								}
								else workingJSON = workingJSON.getJSONObject(JSONPath_tmp[i]);
							}
							// Handle final path
							if (!JSONPath_tmp[JSONPath_tmp.length - 1].contains("[")){
								try {
									resultJSON = workingJSON.getString(JSONPath_tmp[JSONPath_tmp.length - 1]);
									JSONResultInString.add(resultJSON);
								} catch (Exception e) {
									resultJSON_number = workingJSON.getDouble(JSONPath_tmp[JSONPath_tmp.length - 1]);
									JSONResultInString.add(Double.toString(resultJSON_number));
								}
							}
							else {
								String[] PathArray_tmp = readfile.handleJSONArrayPath(JSONPath_tmp[JSONPath_tmp.length - 1]);
								try {
									JSONArray resultJSON_A = workingJSON.getJSONArray(PathArray_tmp[0]);
									JSONResultInString.add(resultJSON_A.getString(Integer.parseInt(PathArray_tmp[1])));
								} catch (Exception e) {
									JSONArray resultJSON_A = workingJSON.getJSONArray(PathArray_tmp[0]);
									JSONResultInString.add(Double.toString(resultJSON_A.getDouble(Integer.parseInt(PathArray_tmp[1]))));
								}
							}
						}	
					}
				}
				//Validate
				for (int i = 0; i < val.length; i++) {
					if (!respInString.contains(val[i])) {
						logger.error("Response doesn't contain : " + val[i]);
					}
					Assert.assertTrue(respInString.contains(val[i]));
					logger.info("Response contains : " + val[i]);
				} 
				
				//Create JSON file
				for (int i = 0; i < JSONResultInString.size(); i++) {
					obj.put(varNames.get(i), JSONResultInString.get(i));
					logger.info("Added " + varNames.get(i) + " : " + JSONResultInString.get(i));
				}
				
				// Write JSON file
				try (FileWriter file = new FileWriter(pathFile)) {
					file.append(obj.toString());
				}
			}
			else logger.info("unknown method");	
		} catch (Exception e) {
			Boolean testIsAvaiable = false;
			logger.error("Test case is not runable");
			logger.error(e.getMessage());
			FileWriter file = new FileWriter(log_name,true);
			file.write("_UNABLE_TO_RUN");
			file.close();
			Assert.assertTrue(testIsAvaiable);
		}

		Thread.sleep(1000);
	}
	@AfterSuite
	public void cleanUp() {	
		String workingDir = System.getProperty("user.dir");
		try {
			File f = new File(workingDir + File.separator + "data" + File.separator + "file1.txt");
			f.delete();
			logger.info("Delete file");
		} catch (Exception e) {
			logger.info("Unable to delete file");
		}
	}
    @DataProvider(name="userData", parallel = false)
    public Object[][] loginData() {
        String workingDir = System.getProperty("user.dir");
        Object[][] arrayObject = CommonData.getExcelData(workingDir + File.separator + "data" + File.separator +"Excel" +File.separator + System.getProperty("dataFile", "RestAPI.xls") ,System.getProperty("dataSheet", "Sheet4"));// change file name and sheet name
        return arrayObject;
    }
    
    //Huy - HMAC
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	private static String toHexString(byte[] bytes) {
		Formatter formatter = new Formatter();
		
		for (byte b : bytes) {
			formatter.format("%02x", b);
		}

		return formatter.toString();
	}

	public static String calculateRFC2104HMAC(String data, String key)
		throws SignatureException, NoSuchAlgorithmException, InvalidKeyException
	{
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
		Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
		mac.init(signingKey);
		return toHexString(mac.doFinal(data.getBytes()));
	}
	
}

