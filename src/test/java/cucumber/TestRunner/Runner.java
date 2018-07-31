package cucumber.TestRunner;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

public class Runner {
	@CucumberOptions (features = "src/test/java/cucumber/Features/MyTest.feature", glue ="cucumber.Steps" , tags = {"@Reset,@Reset1"})
	/*
	 * example of running multiple scenario or suit(feature) tags =
	 * {"@org,@site,@site2"}
	 */
	public class Test extends AbstractTestNGCucumberTests{}
}
