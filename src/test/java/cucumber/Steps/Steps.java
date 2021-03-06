package cucumber.Steps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import automation.core.DriverFactory;
import automation.core.DriverFactory4Cucumber;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class Steps extends DriverFactory {

	WebDriver driver;

	@Given("^Open the Firefox and lauch the application$")
	public void open_the_Firefox_and_launch_the_application() {
		System.out.println("This Step open the Firefox and launch the application.");

	}

	@Given("^Open browser and launch the application$")
	public void open_browser() throws Exception {
		try {
			if (driver == null) {
				driver = DriverFactory4Cucumber.getDriver();
				setDriver(driver);
				driver.get("https://webmail.tma.com.vn/");
				driver.manage().window().fullscreen();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}

	}

	@When("^Enter the Username and Password$")
	public void enter_the_Username_and_Password() throws Throwable {
		System.out.println("This step enter the Username and Password on the login page.");
	}

	@When("^Enter user: \"([^\"]*)\" and password: \"([^\"]*)\"$")
	public void enter_user_password(String user, String pass) {
		LoginPage loginpage = new LoginPage(driver);
		loginpage.LoginWith(user, pass);
	}

	@Then("^Reset the credential$")
	public void Reset_the_credential() {
		System.out.println("This step click on the Reset button.");
	}

	@Then("^Click credential$")
	public void Reset_credential() throws InterruptedException {
		// driver.findElement(By.name("btnLogin")).click();
	}

	@Then("^Verify failure result contain text: \"([^\"]*)\"$")
	public void verify_failure_result(String contain_text) {
		Boolean result = (driver.findElement(By.xpath("//*[@id=\"ZLoginErrorPanel\"]/table/tbody/tr/td[2]")).getText()
				.contains(contain_text));
		Assert.assertTrue(result);
		System.out.println("Verified TC");

	}

}
