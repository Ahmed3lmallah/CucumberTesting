package org.example.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:Feature",
        plugin = {"json:target/cucumber-reports/cucumber.json", "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "junit:target/cucumber-reports/cucumber.xml"})
public class CucumberIntegrationTest {
}