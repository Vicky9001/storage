package com.example.bdd.outBound;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Cucumber.class)
@CucumberOptions(
        monochrome = true,//详细显示运行结果
        strict = true,//严格检查测试结果
//        features = {"src/test/resources/features/OutBound.feature"},
//        plugin = {"json:target/cucumber-reports/OutBound.json"}
        features = {"external-service/src/test/resources/features/OutBound.feature"},
        plugin = {"json:external-service/target/cucumber-reports/OutBound.json"}
)
public class OutBoundStepRunner {
}