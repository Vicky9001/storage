package com.example.bdd.stokeIn;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        monochrome = true,//详细显示运行结果
        strict = true,//严格检查测试结果
//        features = {"warehousing-management/src/test/resources/features/StokeIn.feature"},
//        plugin = {"json:warehousing-management/target/cucumber-reports/StokeIn.json"}
        features = {"src/test/resources/features/StokeIn.feature"},
        plugin = {"json:target/cucumber-reports/StokeIn.json"}
)
public class StokeInStepRunner {
}
