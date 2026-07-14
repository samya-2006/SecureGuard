package com.secureguard.report;

import com.secureguard.model.Issue;

import java.util.List;
import com.secureguard.report.HtmlReporter;
public class ConsoleReporter {

    public void printReport(List<Issue> issues,
                            int filesScanned) {

        System.out.println(
                "[DEBUG] HTML reporter finished"
        );

        System.out.println("\n====================================");
        System.out.println("        SecureGuard Scan Report");
        System.out.println("====================================");

        if (issues.isEmpty()) {
            System.out.println("✓ No security issues found.");
            return;
        }
        int critical = 0;
        int high = 0;
        int medium = 0;
        int low = 0;
        String currentFile = "";
        issues.sort((a, b) -> a.getFileName().compareTo(b.getFileName()));
        for (Issue issue : issues) {
            if (!issue.getFileName().equals(currentFile)) {

                currentFile = issue.getFileName();

                System.out.println();
                System.out.println("========================================");
                System.out.println("File : " + "\u001B[38;2;255;20;147m" +currentFile+ "\u001B[0m");
                System.out.println("========================================");
            }

            System.out.println("Rule ID        : " + issue.getRule().getRuleId());
            System.out.println("Rule           : " + issue.getRule().getName());
            System.out.println("Severity       : " + issue.getRule().getSeverity());
            System.out.println("Recommendation : " + issue.getRule().getRecommendation());
            switch (issue.getRule().getSeverity().toUpperCase()) {

                case "CRITICAL":
                    critical++;
                    break;

                case "HIGH":
                    high++;
                    break;

                case "MEDIUM":
                    medium++;
                    break;

                case "LOW":
                    low++;
                    break;
            }
            if (issue.getDetector().getExample() != null) {
                System.out.print("Error (Line " + issue.getLineNumber() + "): ");
                System.out.println(issue.getMatchedCode());
            }

            if (issue.getDetector().getConfidence() != null) {
                System.out.println("Confidence     : " + issue.getDetector().getConfidence());
            }

            System.out.println("----------------------------------------");
        }
        System.out.println("\n");

        System.out.println("Critical      : " + critical);
        System.out.println("High          : " + high);
        System.out.println("Medium        : " + medium);
        System.out.println("Low           : " + low);

        System.out.println("----------------------------------------");
        System.out.println("Files Scanned : " + filesScanned);
        System.out.println("Total Issues  : " + issues.size());
        System.out.println("========================================");
        System.out.print("Scan completed successfully.");
    }

}