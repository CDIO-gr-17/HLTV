package com;


import org.junit.runner.RunWith;

import io.cucumber.android.runner.CucumberAndroidJUnitRunner;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/androidTest/java/com/cucumberAndroid/features",
        glue = {"/src/androidTest/java/com/cucumberAndroid/steps"}, plugin = {"pretty", "html:target/cucumber-reports.html"})
public class RunCucumberAndroidTests extends CucumberAndroidJUnitRunner {


}
