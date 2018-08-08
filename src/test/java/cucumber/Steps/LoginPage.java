package cucumber.Steps;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

	private WebDriver driver;

	// public void LoginPage() {}

	public LoginPage(WebDriver driver1) {
		this.driver = driver1;
		PageFactory.initElements(driver1, this);
	}

	// Find Elements
	@FindBy(id = "username")
	private WebElement username;
	@FindBy(id = "password")
	private WebElement password;
	// @FindBy (name="uid") private WebElement username;
	// @FindBy (name ="password") private WebElement password;

	// Methods

	public LoginPage LoginWith(String user, String pass) {
		username.sendKeys(user);
		password.sendKeys(pass);
		password.submit();
		return this;

	}
}
