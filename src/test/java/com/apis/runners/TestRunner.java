package com.apis.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.apis",
        tags = "@NO_UI",
        plugin = {
                "pretty",
                "json:target/cucumber-reports/cucumber.json" // 生成JSON报告，供Maven识别
        }
)
public class TestRunner {
}