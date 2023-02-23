package cucumber.Options;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

	@RunWith(Cucumber.class)
	@CucumberOptions(
	  features = "src/test/java/features",
	  plugin = {"pretty",
			  "html:target/htmlReports/cucumber-report.html",
			  "json:target/jsonReports/cucumber-report.json"
			  },
	  glue =  "stepDefinitions"
	 // tags =  "@DeletePlace"
	)
public class TestRunner {}
