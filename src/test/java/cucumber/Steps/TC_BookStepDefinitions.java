package cucumber.Steps;

import static io.restassured.RestAssured.given;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.json.Json;
import org.testng.Assert;


import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.JSONParser;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;


public class TC_BookStepDefinitions {

	private Response response;
	private RequestSpecification request;
	private String ENDPOINT_GET_BOOK_BY_ISBN = "https://www.googleapis.com/books/v1/volumes";
	private Headers allHeaders;
	
	//#################GET METHOD#################
	
	@Given("a book exists with an isbn of \"([^\"]*)\"$")
	public void a_book_exists_with_isbn(String isbn) {
		request = given().param("q", "isbn:" + isbn);
		request.given().log().all();
		
	}

	@When("a user retrieves the book by isbn")
	public void a_user_retrieves_the_book_by_isbn() {
		response = request.when().get(ENDPOINT_GET_BOOK_BY_ISBN);
	}

	@Then("verify status code is \"([^\"]*)\"$")
	public void verify_status_code(int statusCode) {
		Assert.assertEquals(response.statusCode(), statusCode);
		System.out.println("Verified status code passed:  " + statusCode );

	}
	
	@Then ("^get response all header$")
	public void get_allheader_response() {
		allHeaders = response.getHeaders();
		for (Header header : allHeaders) {
			System.out.println("KEY: " + header.getName() + "			VALUE: " + header.getValue());
		}
	}
	@Then ("^verify the header \"([^\"]*)\" is \"([^\"]*)\"$")
	public void verify_header(String header, String value) {
		String expectedheader=response.getHeader(header);
		Assert.assertEquals(expectedheader, value);
		System.out.println("Value of "+ expectedheader + " header is: " + value);		
	}
	@Then ("^get response body$")
	public void get_response_body() {
		ResponseBody responseBody = response.getBody();
		System.out.println("Response Body is: " + responseBody.asString());
	}
	@Then ("^verify response contains \"([^\"]*)\"$")
	public void check_response_contain(String expectedvalue) {
		Assert.assertTrue(response.body().asString().contains(expectedvalue),"Verified passed");
		System.out.println("Verified body passed when contains " + expectedvalue );
	}
	@Then ("^verify response \"([^\"]*)\" contains \"([^\"]*)\"$")
	public void check_response_author_contain(String actualvalue, String expectedvalue) {
		System.out.println("Author: " + response.jsonPath().get(actualvalue));
		Assert.assertTrue(response.jsonPath().get(actualvalue).equals(expectedvalue));
		System.out.println(actualvalue +" contains value: " + expectedvalue );
	}
	
	
	//#################POST METHOD#################


	@Given ("^Insert URI: \"([^\"]*)\" and location for json file: \"([^\"]*)\"$")
	public void insert_body_content(String uri, String jsonfilelocation) throws IOException {
		System.out.println("=============================================================");
		System.out.println("API URL is: " + uri);

		//Building request by using requestSpecBuilder
		RequestSpecBuilder builder = new RequestSpecBuilder();
		//Setting content type as application/json
		builder.setContentType("application/json; charset=UTF-8");
		//Read String from file
		String contentBody = new String (Files.readAllBytes(Paths.get(jsonfilelocation)));
		//Set API's body
		builder.setBody(contentBody);
		RequestSpecification requestSpec = builder.build();
		//Making post request with authentication or leave blank if you dont have credentials like: basic ("","")
		response = given().spec(requestSpec).when().post(uri);
		System.out.println("Response: " + response.body().prettyPrint());		

		
	}
	@Then ("^Verify the response with key: \"([^\"]*)\" and its value: \"([^\"]*)\"$")
	public void verify_response(String key, String value) {
		Assert.assertEquals(response.jsonPath().get(key), value);
		System.out.println("Response contains " + key + ": "+ value);
	}
	
}