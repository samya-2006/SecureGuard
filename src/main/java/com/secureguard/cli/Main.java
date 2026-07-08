package com.secureguard.cli;
import com.secureguard.report.ConsoleReporter;
import com.secureguard.config.RuleLoader;
import com.secureguard.model.Rule;
import com.secureguard.model.Issue;
import com.secureguard.scanner.FileScanner;
import com.secureguard.scanner.RuleEngine;

import java.io.File;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println("""
                =========================================
                          SecureGuard v0.1.0
                =========================================
                Cross-Language Security Scanner
                
                Initializing...
                """);

        RuleLoader ruleLoader = new RuleLoader();
        List<Rule> rules = ruleLoader.loadRules();
        FileScanner fileScanner = new FileScanner();
        RuleEngine ruleEngine = new RuleEngine();

        File demoFolder = new File("src/main/resources/demo");

        List<File> files = fileScanner.scanProject(demoFolder);
        int filesScanned = files.size();
        List<Issue> allIssues = new java.util.ArrayList<>();

        for (File file : files) {

            List<Issue> issues = ruleEngine.scanFile(file, rules);

            allIssues.addAll(issues);
        }

        ConsoleReporter reporter = new ConsoleReporter();
        reporter.printReport(allIssues, filesScanned);
    }
}

