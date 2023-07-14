package com.example.bdd.stokeOut;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        monochrome = true,//详细显示运行结果
        strict = true,//严格检查测试结果
        features = {"warehousing-management/src/test/resources/features/StokeOut.feature"},
        plugin = {"json:warehousing-management/target/cucumber-reports/StokeOut.json"}
)
public class OutStepRunner {
}