package com.example.bdd.purchase;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        monochrome = true,//详细显示运行结果
        strict = true,//严格检查测试结果
        //上面的路径单独运行测试时生效，下面的是统一打印报告时生效
        features = {"src/test/resources/features/Purchase.feature"},
        plugin = {"json:target/cucumber-reports/Purchase.json"}
//        features = {"external-service/src/test/resources/features/Purchase.feature"},
//        plugin = {"json:external-service/target/cucumber-reports/Purchase.json"}
)
public class PurchaseStepRunner {
}
