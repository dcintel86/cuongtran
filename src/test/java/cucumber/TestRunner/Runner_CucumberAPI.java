package cucumber.TestRunner;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

public class Runner_CucumberAPI {
	@CucumberOptions(features = "src/test/java/cucumber/Features/CucumberAPI.feature", glue = "cucumber.Steps", tags = {
			"@feature" },
			/*
			 * example of running multiple scenario or suit(feature) tags =
			 * {"@org,@site,@site2"}
			 */
			plugin = { "pretty:target/cucumber/google-pretty.txt", "html:target/cucumber/google",
					"json:target/cucumber/google.json", "junit:target/cucumber/google-results.xml" }, monochrome = true)
	public class Test extends AbstractTestNGCucumberTests {
	}

}
