package com.example.bdd.auto;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.Reportable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExternalReportGenerator {
    public static void main(String[] args) {

        // 启动该微服务下所有testRunner类
        Result result = JUnitCore.runClasses(ExternalTestSuite.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());

        // 生成html报告
        File reportOutputDirectory = new File("external-service/target");
        List<String> jsonFiles = new ArrayList<>();
        jsonFiles.add("external-service/target/cucumber-reports/Purchase.json");
        jsonFiles.add("external-service/target/cucumber-reports/OutBound.json");

        String buildNumber = "1";
        String projectName = "ExternalReport";

        Configuration configuration = new Configuration(reportOutputDirectory, projectName);
        // optional configuration - check javadoc
        configuration.setBuildNumber(buildNumber);
        // additional metadata presented on main page
        configuration.addClassifications("Platform", "Windows");
        configuration.addClassifications("Browser", "Firefox");
        configuration.addClassifications("Branch", "release/1.0");

        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
        Reportable res = reportBuilder.generateReports();

        // validate result to decide what to do if report has failed
        if (res == null) {
            System.out.println("Cucumber report generation failed!");
            // Handle failure scenario here
        } else {
            System.out.println("Cucumber report generation succeeded!");
            // Handle success scenario here
        }
    }
}
