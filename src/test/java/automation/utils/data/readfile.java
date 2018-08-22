package automation.utils.data;

import java.io.File;

import org.json.JSONObject;
import org.testng.annotations.DataProvider;
import automation.utils.data.CommonData;

public class readfile {
	
	public static String createURL(String URL, String path, String parameters, JSONObject json){
		String workingURL = URL + path;
		if (!parameters.isEmpty()){
			workingURL = workingURL + "?";
			String[] par = parameters.split("\n");
			for (int i = 0; i < par.length; i++) {
				if (i == 0) { workingURL = workingURL + par[i];}
				else {workingURL = workingURL + "&" + par[i];}
			}
		}
		workingURL = handleVariables(workingURL, json);
		System.out.println(workingURL);
		return workingURL;
	}
	
	public  static  String[] createValidation(String validation){
		String[] val = validation.split("\n");
		return val;
	}
	
	public  static  String[] createJSONPaths(String Paths){
		String[] path_tmp = Paths.split("\n");
		return path_tmp;
	}
	

	public static String[] createJSONPath(String JSONPath){
		String[] Path_tmp = JSONPath.split("[.]");
		return Path_tmp;
	}
	
	public static String[] handleJSONArrayPath(String JSONPath){
		String Path_tmp = JSONPath.replace("]", ".");
		Path_tmp = Path_tmp.replace("[", ".");
		String[] Path = Path_tmp.split("[.]");
		return Path;
	}
	
	public static String handleVariables(String body, JSONObject json ){
		String[] tmp = null;
		while (body.contains("$")) {
			tmp = body.split("[$]",3);
			body = body.replaceFirst(tmp[1], json.getString(tmp[1])).replaceFirst("[$]", "");
			body = body.replaceFirst("[$]", "");		
		}
		return body;
	}
	
	public static String[] createHeader(String Header, JSONObject json){
		String header_tmp = handleVariables(Header, json);
		header_tmp = header_tmp.replaceAll("\n", ":");
		String[] Header_tmp = header_tmp.split("[:]");
		return Header_tmp;
	}
	
	
    @DataProvider(name="userData", parallel = false)
    public Object[][] loginData() {
        String workingDir = System.getProperty("user.dir");
        Object[][] arrayObject = CommonData.getExcelData(workingDir + File.separator + "data" + File.separator + "Rest1.xls","Sheet1");
        return arrayObject;
    }

}
