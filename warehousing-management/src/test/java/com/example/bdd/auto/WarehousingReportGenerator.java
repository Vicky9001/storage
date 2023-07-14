package com.example.bdd.auto;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.Reportable;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class WarehousingReportGenerator {
    public static void main(String[] args) {

        // 启动该微服务下所有testRunner类
        Result result = JUnitCore.runClasses(WarehousingTestSuite.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());

        // 生成html报告
        File reportOutputDirectory = new File("warehousing-management/target");
        List<String> jsonFiles = new ArrayList<>();
        jsonFiles.add("warehousing-management/target/cucumber-reports/Check.json");
        jsonFiles.add("warehousing-management/target/cucumber-reports/StokeIn.json");
        jsonFiles.add("warehousing-management/target/cucumber-reports/StokeOut.json");

        String buildNumber = "1";
        String projectName = "WarehousingReport";

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
