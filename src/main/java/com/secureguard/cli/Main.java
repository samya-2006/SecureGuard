package com.secureguard.cli;

import com.secureguard.config.RuleLoader;
import com.secureguard.model.Issue;
import com.secureguard.model.Rule;
import com.secureguard.report.ConsoleReporter;
import com.secureguard.scanner.FileScanner;
import com.secureguard.scanner.RuleEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        if (args.length == 0) {
            printHelp();
            return;
        }

        switch (args[0].toLowerCase()) {

            case "scan":

                runScan(args);

                break;

            case "--help":
            case "-h":

                printHelp();

                break;

            case "--version":
            case "-v":

                System.out.println("SecureGuard v2.1.0");

                break;

            default:

                System.out.println("Unknown command: " + args[0]);
                System.out.println();
                printHelp();

        }

    }

    private static void runScan(String[] args) {

        System.out.println("""
                =========================================
                          SecureGuard v2.1.0
                =========================================
                Cross-Language Security Scanner

                Initializing...
                """);

        RuleLoader ruleLoader = new RuleLoader();
        List<Rule> rules = ruleLoader.loadRules();

        FileScanner fileScanner = new FileScanner();
        RuleEngine ruleEngine = new RuleEngine();

        File scanFolder;

        if (args.length >= 2) {
            scanFolder = new File(args[1]);
        } else {
            scanFolder = new File(".");
        }

        List<File> files = fileScanner.scanProject(scanFolder);

        List<Issue> allIssues = new ArrayList<>();

        for (File file : files) {
            allIssues.addAll(ruleEngine.scanFile(file, rules));
        }

        ConsoleReporter reporter = new ConsoleReporter();

        reporter.printReport(allIssues, files.size());
    }

    private static void printHelp() {

        System.out.println("""
                
                SecureGuard CLI
                
                Usage:
                
                  secureguard scan <path>
                
                Commands:
                
                  scan        Scan a project
                  --help      Show help
                  --version   Show version
                
                Examples:
                
                  secureguard scan .
                  secureguard scan src
                  secureguard scan C:\\Projects\\Demo
                
                """);

    }

}
