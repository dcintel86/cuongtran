package cucumber.Steps;

import static io.restassured.RestAssured.given;

import org.openqa.selenium.json.Json;
import org.testng.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
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

	@Then("the status code is \"([^\"]*)\"$")
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
	@Then ("^the header \"([^\"]*)\" is \"([^\"]*)\"$")
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
	@Then ("^Check response contains \"([^\"]*)\"$")
	public void check_response_contain(String expectedvalue) {
		Assert.assertTrue(response.body().asString().contains(expectedvalue),"Verified passed");
		System.out.println("Verified body passed when contains " + expectedvalue );
	}
	@Then ("^Check response \"([^\"]*)\" contains \"([^\"]*)\"$")
	public void check_response_author_contain(String actualvalue, String expectedvalue) {
		System.out.println("Author: " + response.jsonPath().get(actualvalue));
		Assert.assertTrue(response.jsonPath().get(actualvalue).equals(expectedvalue));
		System.out.println(actualvalue +" contains value: " + expectedvalue );
	}
	
	//#################POST METHOD#################
	
	
}