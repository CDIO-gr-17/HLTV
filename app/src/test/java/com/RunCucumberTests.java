package com;


import org.junit.runner.RunWith;

import io.cucumber.junit.CucumberOptions;

@RunWith(io.cucumber.junit.Cucumber.class)
@CucumberOptions(features = "src/test/java/com/cucumber/features", plugin = {"pretty", "html:target/cucumber-reports.html"})
public class RunCucumberTests {
}
