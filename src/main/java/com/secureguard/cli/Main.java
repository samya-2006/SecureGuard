package com.secureguard.cli;

import com.secureguard.config.RuleLoader;
import com.secureguard.model.Issue;
import com.secureguard.model.Rule;
import com.secureguard.report.ConsoleReporter;
import com.secureguard.scanner.FileScanner;
import com.secureguard.scanner.RuleEngine;
import com.secureguard.report.HtmlReporter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String VERSION = "2.0.0";

    public static void main(String[] args) {

        if (args.length == 0) {
            printHelp();
            return;
        }

        String command = args[0];

        switch (command.toLowerCase()) {

            case "scan":
                handleScan(args);
                break;

            case "--help":
            case "-h":
            case "help":
                printHelp();
                break;

            case "--version":
            case "-v":
            case "version":
                printVersion();
                break;

            default:
                System.err.println(
                        "Unknown command: " + command
                );

                System.err.println(
                        "Run 'secureguard --help' for usage."
                );

                System.exit(1);
        }
    }


    private static void handleScan(String[] args) {

        if (args.length < 2) {

            System.err.println(
                    "Error: Scan path is required."
            );

            System.err.println(
                    "Usage: secureguard scan <path>"
            );

            return;
        }

        String scanPath = args[1];

        File target = new File(scanPath);

        if (!target.exists()) {

            System.err.println(
                    "Error: Path does not exist: "
                            + target.getAbsolutePath()
            );

            return;
        }

        try {

            RuleLoader ruleLoader = new RuleLoader();

            List<Rule> rules =
                    ruleLoader.loadRules();


            FileScanner fileScanner =
                    new FileScanner();

            List<File> files =
                    fileScanner.scanProject(target);


            RuleEngine ruleEngine =
                    new RuleEngine();

            List<Issue> allIssues =
                    new ArrayList<>();


            for (File file : files) {

                List<Issue> fileIssues =
                        ruleEngine.scanFile(
                                file,
                                rules
                        );

                allIssues.addAll(fileIssues);
            }

            ConsoleReporter consoleReporter =
                    new ConsoleReporter();


            HtmlReporter htmlReporter =
                    new HtmlReporter();


            consoleReporter.printReport(
                    allIssues,
                    files.size()
            );


            String reportPath =
                    System.getProperty("user.dir")
                            + File.separator
                            + "secureguard-report.html";


            htmlReporter.generate(
                    allIssues,
                    reportPath
            );


        } catch (Exception e) {

            System.err.println(
                    "SecureGuard scan failed: "
                            + e.getMessage()
            );

            e.printStackTrace();

            System.exit(1);
        }
    }


    private static void printHelp() {

        System.out.println();
        System.out.println("SecureGuard v" + VERSION);
        System.out.println(
                "CLI-Based Secure Development Assistant"
        );

        System.out.println();

        System.out.println("Usage:");
        System.out.println(
                "  secureguard scan <path>"
        );

        System.out.println(
                "  secureguard --help"
        );

        System.out.println(
                "  secureguard --version"
        );

        System.out.println();

        System.out.println("Examples:");

        System.out.println(
                "  secureguard scan ."
        );

        System.out.println(
                "  secureguard scan C:\\Projects\\Demo"
        );

        System.out.println();
    }


    private static void printVersion() {

        System.out.println(
                "SecureGuard v" + VERSION
        );
    }
}