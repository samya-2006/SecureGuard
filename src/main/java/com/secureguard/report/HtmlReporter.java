package com.secureguard.report;

import com.secureguard.model.Issue;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class HtmlReporter {

    public void generate(List<Issue> issues, String outputFile) {

        int critical = 0;
        int high = 0;
        int medium = 0;
        int low = 0;

        for (Issue issue : issues) {

            String severity = issue.getRule().getSeverity();

            if (severity == null) {
                continue;
            }

            switch (severity.toUpperCase()) {

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
        }


        StringBuilder html = new StringBuilder();


        html.append("""
                <!DOCTYPE html>
                <html lang="en">

                <head>

                <meta charset="UTF-8">

                <meta name="viewport"
                      content="width=device-width, initial-scale=1.0">

                <title>SecureGuard Scan Report</title>


                <style>

                :root {

                    --bg: #02040f;

                    --panel: #050817;

                    --panel-light: #080c1d;

                    --border: #202744;

                    --text: #f6f7ff;

                    --muted: #9ca6c4;


                    --pink: #ff3d9d;

                    --pink-light: #ff72bb;

                    --blue: #4285ff;

                    --purple: #b55cff;

                    --cyan: #3ee8f5;

                    --green: #58ef7d;


                    --critical: #ff3158;

                    --high: #ff4b5f;

                    --medium: #ff9d3d;

                    --low: #58ef7d;

                }


                * {

                    box-sizing: border-box;

                }


                html {

                    scroll-behavior: smooth;

                }


                body {

                    margin: 0;

                    min-height: 100vh;


                    background:

                        radial-gradient(
                            circle at 8% 0%,
                            rgba(255, 61, 157, 0.08),
                            transparent 25%
                        ),

                        radial-gradient(
                            circle at 100% 45%,
                            rgba(66, 133, 255, 0.04),
                            transparent 28%
                        ),

                        #02040f;


                    color: var(--text);


                    font-family:

                        "Segoe UI",

                        Arial,

                        sans-serif;

                }


                body::before {

                    content: "";

                    position: fixed;

                    inset: 0;

                    pointer-events: none;


                    background-image:

                        linear-gradient(
                            rgba(255, 255, 255, 0.008) 1px,
                            transparent 1px
                        ),

                        linear-gradient(
                            90deg,
                            rgba(255, 255, 255, 0.008) 1px,
                            transparent 1px
                        );


                    background-size: 45px 45px;

                }


                .container {

                    position: relative;

                    z-index: 1;


                    width: calc(100% - 64px);

                    max-width: 1600px;


                    margin: auto;


                    padding:

                        28px 0

                        60px;

                }


                /* ========================================
                   HERO
                   ======================================== */


                .hero {

                    position: relative;

                    overflow: hidden;


                    border:

                        1px solid

                        rgba(255, 61, 157, 0.72);


                    border-radius: 12px;


                    padding:

                        25px 32px

                        20px;


                    margin-bottom: 26px;


                    background:

                        linear-gradient(
                            120deg,
                            rgba(255, 61, 157, 0.025),
                            transparent 42%
                        ),

                        linear-gradient(
                            135deg,
                            #050817,
                            #030512
                        );


                    box-shadow:

                        0 0 22px

                        rgba(255, 61, 157, 0.08),

                        inset 0 0 35px

                        rgba(255, 61, 157, 0.018);

                }


                .hero::before {

                    content: "";

                    position: absolute;


                    width: 470px;

                    height: 470px;


                    right: -130px;

                    top: -220px;


                    border-radius: 50%;


                    border:

                        1px solid

                        rgba(255, 61, 157, 0.16);


                    box-shadow:

                        0 0 0 30px

                        rgba(255, 61, 157, 0.025),

                        0 0 0 65px

                        rgba(255, 61, 157, 0.018),

                        0 0 50px

                        rgba(255, 61, 157, 0.12);

                }


                .hero::after {

                    content: "";

                    position: absolute;


                    right: 55px;

                    top: 48px;


                    width: 280px;

                    height: 100px;


                    opacity: 0.25;


                    background:

                        linear-gradient(
                            90deg,
                            transparent 0 15%,
                            var(--pink) 15% 15.5%,
                            transparent 15.5% 28%,
                            var(--pink) 28% 28.5%,
                            transparent 28.5%
                        );


                    clip-path:

                        polygon(
                            0 20%,
                            20% 20%,
                            25% 40%,
                            50% 40%,
                            55% 65%,
                            80% 65%,
                            85% 85%,
                            100% 85%,
                            100% 90%,
                            82% 90%,
                            77% 70%,
                            52% 70%,
                            47% 45%,
                            22% 45%,
                            17% 25%,
                            0 25%
                        );

                }


                .brand {

                    position: relative;

                    z-index: 2;


                    display: flex;

                    align-items: center;


                    gap: 22px;

                }


                .shield {

                    position: relative;


                    display: flex;

                    align-items: center;

                    justify-content: center;


                    width: 70px;

                    height: 76px;


                    color: var(--pink-light);


                    font-size: 31px;

                    font-weight: 800;


                    border:

                        5px solid

                        var(--pink-light);


                    border-radius:

                        45% 45%

                        55% 55%;


                    box-shadow:

                        0 0 8px var(--pink),

                        0 0 24px

                        rgba(255, 61, 157, 0.65),

                        inset 0 0 15px

                        rgba(255, 61, 157, 0.15);


                    transform:

                        scaleX(0.82);

                }


                .shield span {

                    transform:

                        scaleX(1.22);

                }


                .brand-text h1 {

                    margin: 0;


                    font-family:

                        Consolas,

                        "Courier New",

                        monospace;


                    font-size: 37px;


                    letter-spacing: 1px;


                    color: #f8f8ff;


                    text-shadow:

                        0 0 15px

                        rgba(255, 255, 255, 0.08);

                }


                .scan-status {

                    display: flex;

                    align-items: center;


                    gap: 11px;


                    margin-top: 10px;


                    color: var(--muted);


                    font-family:

                        Consolas,

                        monospace;


                    font-size: 16px;

                }


                .status-dot {

                    width: 12px;

                    height: 12px;


                    border-radius: 50%;


                    background: var(--green);


                    box-shadow:

                        0 0 10px

                        rgba(88, 239, 125, 0.8);

                }


                /* ========================================
                   SUMMARY
                   ======================================== */


                .summary-panel {

                    position: relative;

                    z-index: 2;


                    width: 80%;


                    margin-top: 25px;


                    padding:

                        15px 18px

                        17px;


                    border:

                        1px solid

                        rgba(255, 61, 157, 0.22);


                    border-radius: 9px;


                    background:

                        rgba(3, 5, 17, 0.75);

                }


                .summary-title {

                    margin-bottom: 14px;


                    color: var(--pink);


                    font-family:

                        Consolas,

                        monospace;


                    font-size: 20px;

                    font-weight: 800;


                    text-shadow:

                        0 0 10px

                        rgba(255, 61, 157, 0.3);

                }


                .summary-grid {

                    display: grid;


                    grid-template-columns:

                        repeat(
                            4,
                            minmax(0, 1fr)
                        );


                    gap: 20px;

                }


                .summary-card {

                    position: relative;


                    min-height: 80px;


                    display: flex;

                    align-items: center;


                    gap: 18px;


                    padding: 15px 20px;


                    border:

                        1px solid

                        #1f294d;


                    border-radius: 9px;


                    background:

                        linear-gradient(
                            135deg,
                            rgba(8, 12, 30, 0.96),
                            rgba(3, 6, 18, 0.96)
                        );


                    overflow: hidden;

                }


                .summary-card::after {

                    content: "";

                    position: absolute;


                    width: 80px;

                    height: 80px;


                    right: -45px;

                    bottom: -45px;


                    border-radius: 50%;


                    background: currentColor;


                    opacity: 0.05;


                    filter: blur(12px);

                }


                .summary-card.issues {

                    color: var(--critical);

                }


                .summary-card.files {

                    color: var(--blue);

                }


                .summary-card.rules {

                    color: var(--purple);

                }


                .summary-card.status-card {

                    color: var(--cyan);

                }


                .summary-icon {

                    width: 43px;


                    text-align: center;


                    color: currentColor;


                    font-family:

                        Consolas,

                        monospace;


                    font-size: 29px;


                    font-weight: bold;


                    text-shadow:

                        0 0 10px

                        currentColor;

                }


                .summary-number {

                    color: currentColor;


                    font-family:

                        Consolas,

                        monospace;


                    font-size: 27px;

                    font-weight: bold;


                    text-shadow:

                        0 0 8px

                        currentColor;

                }


                .summary-label {

                    margin-top: 5px;


                    color: var(--muted);


                    font-family:

                        Consolas,

                        monospace;


                    font-size: 14px;

                }

/* ========================================
   FILE GROUP
   ======================================== */


.file-group {

    margin-bottom: 38px;

}


.file-header {

    position: relative;


    display: flex;

    align-items: center;


    gap: 18px;


    margin-bottom: 18px;


    padding: 18px 24px;


    border:

        1px solid

        rgba(255, 61, 157, 0.5);


    border-left:

        5px solid

        var(--pink);


    border-radius: 9px;


    background:

        linear-gradient(
            90deg,
            rgba(255, 61, 157, 0.09),
            rgba(5, 8, 23, 0.96) 30%
        );


    box-shadow:

        0 0 18px

        rgba(255, 61, 157, 0.07);

}


.file-icon {

    display: flex;


    align-items: center;

    justify-content: center;


    width: 48px;

    height: 48px;


    flex-shrink: 0;


    border:

        1px solid

        var(--pink);


    border-radius: 8px;


    color: var(--pink);


    background:

        rgba(255, 61, 157, 0.06);


    font-family:

        Consolas,

        monospace;


    font-size: 18px;

    font-weight: bold;


    text-shadow:

        0 0 9px

        rgba(255, 61, 157, 0.7);


    box-shadow:

        0 0 12px

        rgba(255, 61, 157, 0.12);

}


.file-header-content {

    min-width: 0;

}


.file-name {

    color: #ffffff;


    font-family:

        Consolas,

        "Courier New",

        monospace;


    font-size: 23px;

    font-weight: bold;


    word-break: break-word;

}


.file-finding-count {

    margin-top: 5px;


    color: var(--pink-light);


    font-family:

        Consolas,

        monospace;


    font-size: 13px;

}


.file-findings {

    position: relative;


    padding-left: 22px;

}


.file-findings::before {

    content: "";


    position: absolute;


    top: 0;

    bottom: 25px;

    left: 5px;


    width: 1px;


    background:

        linear-gradient(
            var(--pink),
            rgba(255, 61, 157, 0.05)
        );


    box-shadow:

        0 0 7px

        rgba(255, 61, 157, 0.3);

}
                /* ========================================
                   FINDING
                   ======================================== */


                .finding {

                    --severity-color:

                        var(--medium);


                    position: relative;


                    margin-bottom: 25px;


                    padding: 25px 30px;


                    border:

                        1px solid

                        color-mix(
                            in srgb,
                            var(--severity-color) 55%,
                            #17203d
                        );


                    border-left:

                        7px solid

                        var(--severity-color);


                    border-radius: 11px;


                    background:

                        radial-gradient(
                            circle at 0 20%,
                            color-mix(
                                in srgb,
                                var(--severity-color) 8%,
                                transparent
                            ),
                            transparent 25%
                        ),

                        linear-gradient(
                            135deg,
                            #050817,
                            #030512
                        );


                    box-shadow:

                        0 0 18px

                        color-mix(
                            in srgb,
                            var(--severity-color) 8%,
                            transparent
                        );

                }


                .finding.critical {

                    --severity-color:

                        var(--critical);

                }


                .finding.high {

                    --severity-color:

                        var(--high);

                }


                .finding.medium {

                    --severity-color:

                        var(--medium);

                }


                .finding.low {

                    --severity-color:

                        var(--low);

                }


                .finding-header {

                    display: flex;


                    align-items: center;

                    justify-content: space-between;


                    gap: 25px;


                    padding-bottom: 19px;


                    border-bottom:

                        1px solid

                        #18203a;

                }


                .finding-title {

                    display: flex;

                    align-items: center;


                    gap: 14px;


                    margin: 0;


                    color:

                        var(--severity-color);


                    font-family:

                        Consolas,

                        monospace;


                    font-size: 27px;


                    text-shadow:

                        0 0 10px

                        color-mix(
                            in srgb,
                            var(--severity-color) 25%,
                            transparent
                        );

                }


                .warning-icon {

                    display: inline-flex;


                    align-items: center;

                    justify-content: center;


                    width: 34px;

                    height: 34px;


                    border:

                        2px solid

                        var(--severity-color);


                    border-radius: 50%;


                    font-size: 21px;

                }


                .severity-area {

                    display: flex;

                    align-items: center;


                    gap: 18px;

                }


                .severity-badge {

                    padding:

                        7px 14px;


                    border:

                        1px solid

                        var(--severity-color);


                    border-radius: 6px;


                    color:

                        var(--severity-color);


                    background:

                        color-mix(
                            in srgb,
                            var(--severity-color) 7%,
                            transparent
                        );


                    font-family:

                        Consolas,

                        monospace;


                    font-size: 15px;

                    font-weight: bold;


                    box-shadow:

                        0 0 12px

                        color-mix(
                            in srgb,
                            var(--severity-color) 15%,
                            transparent
                        );

                }


                .threat-bars {

                    display: flex;


                    gap: 5px;


                    padding:

                        8px 10px;


                    border:

                        1px solid

                        #1e294b;


                    border-radius: 7px;


                    background: #050819;

                }


                .threat-bar {

                    width: 10px;

                    height: 17px;


                    border-radius: 2px;


                    background: #141b33;

                }


                .threat-bar.active {

                    background:

                        var(--severity-color);


                    box-shadow:

                        0 0 8px

                        var(--severity-color);

                }


                /* ========================================
                   FINDING BODY
                   ======================================== */


                .finding-body {

                    display: grid;


                    grid-template-columns:

                        minmax(320px, 0.7fr)

                        minmax(0, 1.4fr);


                    gap: 28px;


                    padding-top: 25px;

                }


                .metadata {

                    padding-right: 28px;


                    border-right:

                        1px solid

                        #1c2441;

                }


                .meta-row {

                    display: grid;


                    grid-template-columns:

                        170px 1fr;


                    align-items: center;


                    min-height: 48px;


                    border-bottom:

                        1px dashed

                        #17203a;

                }


                .meta-label {

                    color: var(--muted);


                    font-family:

                        Consolas,

                        monospace;


                    font-size: 15px;

                }


                .meta-label::before {

                    content: "◇";


                    display: inline-block;


                    width: 30px;


                    color: var(--pink);


                    font-size: 18px;


                    text-shadow:

                        0 0 8px

                        rgba(255, 61, 157, 0.5);

                }


                .meta-value {

                    color: #f3f4ff;


                    font-family:

                        Consolas,

                        monospace;


                    font-size: 15px;


                    word-break: break-word;

                }


                .meta-value.severity {

                    color:

                        var(--severity-color);


                    font-weight: bold;

                }


                /* ========================================
                   CODE PANEL
                   ======================================== */


                .code-section {

                    min-width: 0;

                }


                .code-heading {

                    margin-bottom: 13px;


                    color: var(--muted);


                    font-family:

                        Consolas,

                        monospace;


                    font-size: 14px;

                }


                .code-panel {

                    min-height: 175px;


                    display: flex;

                    align-items: flex-start;


                    padding: 35px 32px;


                    border:

                        1px solid

                        #8a8fa5;


                    border-radius: 10px;


                    background:

                        radial-gradient(
                            circle at 100% 100%,
                            rgba(255, 61, 157, 0.025),
                            transparent 35%
                        ),

                        #020511;


                    color: #f8f8ff;


                    font-family:

                        Consolas,

                        "Courier New",

                        monospace;


                    font-size: 18px;


                    line-height: 1.7;


                    white-space: pre-wrap;


                    word-break: break-word;


                    box-shadow:

                        inset 0 0 30px

                        rgba(0, 0, 0, 0.55);

                }


                .code-panel .keyword {

                    color: var(--pink-light);

                }


                /* ========================================
                   RECOMMENDATION
                   ======================================== */


                .recommendation {

                    position: relative;


                    display: flex;


                    gap: 20px;


                    align-items: flex-start;


                    margin-top: 18px;


                    padding: 22px 28px;


                    border:

                        1px solid

                        rgba(88, 239, 125, 0.34);


                    border-left:

                        6px solid

                        var(--green);


                    border-radius: 8px;


                    background:

                        linear-gradient(
                            90deg,
                            rgba(88, 239, 125, 0.08),
                            rgba(3, 16, 18, 0.86)
                        );


                    box-shadow:

                        inset 0 0 25px

                        rgba(88, 239, 125, 0.018);

                }


                .recommendation-icon {

                    display: flex;


                    align-items: center;

                    justify-content: center;


                    width: 38px;

                    height: 43px;


                    flex-shrink: 0;


                    border:

                        3px solid

                        var(--green);


                    border-radius:

                        45% 45%

                        55% 55%;


                    color: var(--green);


                    font-size: 18px;

                    font-weight: bold;


                    box-shadow:

                        0 0 12px

                        rgba(88, 239, 125, 0.35);

                }


                .recommendation-title {

                    margin-bottom: 7px;


                    color: var(--green);


                    font-family:

                        Consolas,

                        monospace;


                    font-size: 17px;

                    font-weight: bold;

                }


                .recommendation-text {

                    color: #f0fff4;


                    font-size: 16px;


                    line-height: 1.6;

                }


                /* ========================================
                   NO ISSUES
                   ======================================== */


                .no-issues {

                    padding: 35px;


                    border:

                        1px solid

                        rgba(88, 239, 125, 0.4);


                    border-left:

                        7px solid

                        var(--green);


                    border-radius: 10px;


                    background:

                        rgba(88, 239, 125, 0.05);

                }


                .no-issues h2 {

                    color: var(--green);


                    font-family:

                        Consolas,

                        monospace;

                }


                /* ========================================
                   FOOTER
                   ======================================== */


                .footer {

                    margin-top: 45px;


                    padding-top: 20px;


                    border-top:

                        1px solid

                        #1a213a;


                    text-align: center;


                    color: #707b9a;


                    font-family:

                        Consolas,

                        monospace;


                    font-size: 13px;

                }


                .footer-brand {

                    color: var(--pink);

                }


                /* ========================================
                   RESPONSIVE
                   ======================================== */


                @media (max-width: 1100px) {

                    .summary-panel {

                        width: 100%;

                    }


                    .summary-grid {

                        grid-template-columns:

                            repeat(2, 1fr);

                    }


                    .finding-body {

                        grid-template-columns: 1fr;

                    }


                    .metadata {

                        border-right: none;


                        border-bottom:

                            1px solid

                            #1c2441;


                        padding-right: 0;

                        padding-bottom: 20px;

                    }

                }


                @media (max-width: 650px) {

                    .container {

                        width: calc(100% - 28px);

                    }


                    .hero {

                        padding: 22px 18px;

                    }


                    .brand-text h1 {

                        font-size: 25px;

                    }


                    .shield {

                        width: 52px;

                        height: 58px;

                    }


                    .summary-grid {

                        grid-template-columns: 1fr;

                    }


                    .finding {

                        padding: 22px 18px;

                    }


                    .finding-header {

                        flex-direction: column;

                        align-items: flex-start;

                    }


                    .finding-body {

                        grid-template-columns: 1fr;

                    }


                    .meta-row {

                        grid-template-columns: 1fr;


                        padding: 12px 0;

                    }


                    .code-panel {

                        padding: 25px 20px;

                    }

                }


                </style>

                </head>


                <body>


                <main class="container">


                <section class="hero">


                    <div class="brand">


                        <div class="shield">

                            <span>✓</span>

                        </div>


                        <div class="brand-text">


                            <h1>

                                SecureGuard Scan Report

                            </h1>


                            <div class="scan-status">


                                <span class="status-dot"></span>


                                Scan completed successfully


                            </div>


                        </div>


                    </div>


                    <div class="summary-panel">


                        <div class="summary-title">

                            Scan Summary

                        </div>


                        <div class="summary-grid">
                """);


        appendSummaryCard(
                html,
                "issues",
                "⚠",
                issues.size(),
                "Issues Detected"
        );


        appendSummaryCard(
                html,
                "files",
                "▤",
                issues.stream()
                        .map(Issue::getFileName)
                        .distinct()
                        .count(),
                "Affected Files"
        );


        appendSummaryCard(
                html,
                "rules",
                "</>",
                issues.stream()
                        .map(issue ->
                                issue.getRule().getRuleId()
                        )
                        .distinct()
                        .count(),
                "Rules Triggered"
        );


        appendSummaryCard(
                html,
                "status-card",
                "✓",
                issues.isEmpty()
                        ? 0
                        : critical + high + medium + low,
                "Findings Processed"
        );


        html.append("""
                        </div>

                    </div>


                </section>
                """);


        if (issues.isEmpty()) {


            html.append("""
                    <section class="no-issues">

                        <h2>
                            ✓ No Security Issues Detected
                        </h2>

                        <p>
                            SecureGuard did not detect known
                            security misconfigurations in the
                            scanned source files.
                        </p>

                    </section>
                    """);


        } else {

            Map<String, List<Issue>> issuesByFile =
                    groupIssuesByFile(issues);


            for (Map.Entry<String, List<Issue>> fileEntry
                    : issuesByFile.entrySet()) {


                String fileName =
                        fileEntry.getKey();


                List<Issue> fileIssues =
                        fileEntry.getValue();


                html.append(
                        "<section class=\"file-group\">"
                );


                html.append(
                        "<div class=\"file-header\">"
                );


                html.append(
                        "<div class=\"file-icon\">&lt;/&gt;</div>"
                );


                html.append(
                        "<div class=\"file-header-content\">"
                );


                html.append(
                        "<div class=\"file-name\">"
                                + escapeHtml(fileName)
                                + "</div>"
                );


                html.append(
                        "<div class=\"file-finding-count\">"
                                + fileIssues.size()
                                + (
                                fileIssues.size() == 1
                                        ? " finding"
                                        : " findings"
                        )
                                + " detected"
                                + "</div>"
                );


                html.append("</div>");


                html.append("</div>");


                html.append(
                        "<div class=\"file-findings\">"
                );


                for (Issue issue : fileIssues) {


                    String severity =
                            issue.getRule()
                                    .getSeverity();


                    String severityClass =
                            severity == null
                                    ? "low"
                                    : severity.toLowerCase();


                    int threatLevel =
                            getThreatLevel(severity);


                    html.append(
                            "<section class=\"finding "
                                    + escapeHtml(severityClass)
                                    + "\">"
                    );


                    html.append(
                            "<div class=\"finding-header\">"
                    );


                    html.append(
                            "<h2 class=\"finding-title\">"
                    );


                    html.append(
                            "<span class=\"warning-icon\">!</span>"
                    );


                    html.append(
                            escapeHtml(
                                    issue.getRule()
                                            .getRuleId()
                            )
                    );


                    html.append(" - ");


                    html.append(
                            escapeHtml(
                                    issue.getRule()
                                            .getName()
                            )
                    );


                    html.append("</h2>");


                    html.append(
                            "<div class=\"severity-area\">"
                    );


                    html.append(
                            "<div class=\"severity-badge\">"
                                    + escapeHtml(severity)
                                    + "</div>"
                    );


                    appendThreatBars(
                            html,
                            threatLevel
                    );


                    html.append("</div>");


                    html.append("</div>");


                    html.append(
                            "<div class=\"finding-body\">"
                    );


                    html.append(
                            "<div class=\"metadata\">"
                    );


                    appendMeta(
                            html,
                            "Severity",
                            severity,
                            true
                    );


                    appendMeta(
                            html,
                            "Language",
                            String.valueOf(
                                    issue.getRule()
                                            .getLanguage()
                            ),
                            false
                    );


                    appendMeta(
                            html,
                            "Detector",
                            issue.getDetector()
                                    .getName(),
                            false
                    );


                    appendMeta(
                            html,
                            "Line",
                            String.valueOf(
                                    issue.getLineNumber()
                            ),
                            false
                    );


                    if (
                            issue.getDetector()
                                    .getConfidence() != null
                    ) {


                        appendMeta(
                                html,
                                "Confidence",
                                issue.getDetector()
                                        .getConfidence(),
                                false
                        );

                    }


                    html.append("</div>");


                    html.append(
                            "<div class=\"code-section\">"
                    );


                    html.append(
                            "<div class=\"code-heading\">" +
                                    "Matched Code" +
                                    "</div>"
                    );


                    html.append(
                            "<div class=\"code-panel\">"
                    );


                    html.append(
                            highlightCode(
                                    issue.getMatchedCode()
                            )
                    );


                    html.append("</div>");


                    html.append("</div>");


                    html.append("</div>");


                    html.append(
                            "<div class=\"recommendation\">"
                    );


                    html.append(
                            "<div class=\"recommendation-icon\">" +
                                    "✓" +
                                    "</div>"
                    );


                    html.append("<div>");


                    html.append(
                            "<div class=\"recommendation-title\">" +
                                    "Recommendation" +
                                    "</div>"
                    );


                    html.append(
                            "<div class=\"recommendation-text\">"
                    );


                    html.append(
                            escapeHtml(
                                    issue.getRule()
                                            .getRecommendation()
                            )
                    );


                    html.append("</div>");


                    html.append("</div>");


                    html.append("</div>");


                    html.append("</section>");

                }


                html.append("</div>");


                html.append("</section>");

            }

        }


        html.append("""
                <footer class="footer">

                    Generated by

                    <span class="footer-brand">

                        SecureGuard

                    </span>

                    · Developer-Focused Static Application
                    Security Testing

                </footer>


                </main>


                </body>

                </html>
                """);


        writeReport(
                html,
                outputFile
        );

    }
    private Map<String, List<Issue>> groupIssuesByFile(
            List<Issue> issues
    ) {

        Map<String, List<Issue>> groupedIssues =
                new LinkedHashMap<>();


        List<Issue> sortedIssues =
                new ArrayList<>(issues);


        sortedIssues.sort(
                (first, second) -> {

                    int fileComparison =
                            first.getFileName()
                                    .compareToIgnoreCase(
                                            second.getFileName()
                                    );


                    if (fileComparison != 0) {

                        return fileComparison;

                    }


                    return Integer.compare(
                            first.getLineNumber(),
                            second.getLineNumber()
                    );

                }
        );


        for (Issue issue : sortedIssues) {


            groupedIssues
                    .computeIfAbsent(
                            issue.getFileName(),
                            fileName ->
                                    new ArrayList<>()
                    )
                    .add(issue);

        }


        return groupedIssues;

    }

    private void appendSummaryCard(
            StringBuilder html,
            String cssClass,
            String icon,
            long value,
            String label
    ) {


        html.append(
                "<div class=\"summary-card "
                        + escapeHtml(cssClass)
                        + "\">"
        );


        html.append(
                "<div class=\"summary-icon\">"
                        + icon
                        + "</div>"
        );


        html.append("<div>");


        html.append(
                "<div class=\"summary-number\">"
                        + value
                        + "</div>"
        );


        html.append(
                "<div class=\"summary-label\">"
                        + escapeHtml(label)
                        + "</div>"
        );


        html.append("</div>");


        html.append("</div>");

    }


    private void appendMeta(
            StringBuilder html,
            String label,
            String value,
            boolean severity
    ) {


        html.append(
                "<div class=\"meta-row\">"
        );


        html.append(
                "<div class=\"meta-label\">"
                        + escapeHtml(label)
                        + "</div>"
        );


        html.append(
                "<div class=\"meta-value"
                        + (
                        severity
                                ? " severity"
                                : ""
                )
                        + "\">"
        );


        html.append(
                escapeHtml(value)
        );


        html.append("</div>");


        html.append("</div>");

    }


    private void appendThreatBars(
            StringBuilder html,
            int threatLevel
    ) {


        html.append(
                "<div class=\"threat-bars\">"
        );


        for (
                int i = 1;
                i <= 7;
                i++
        ) {


            if (i <= threatLevel) {


                html.append(
                        "<span class=\"threat-bar active\"></span>"
                );


            } else {


                html.append(
                        "<span class=\"threat-bar\"></span>"
                );

            }

        }


        html.append("</div>");

    }


    private int getThreatLevel(
            String severity
    ) {


        if (severity == null) {

            return 1;

        }


        switch (
                severity.toUpperCase()
        ) {


            case "CRITICAL":

                return 7;


            case "HIGH":

                return 5;


            case "MEDIUM":

                return 3;


            case "LOW":

                return 1;


            default:

                return 1;

        }

    }


    private String highlightCode(
            String code
    ) {


        String escaped =
                escapeHtml(code);


        escaped =
                escaped.replace(
                        "new ",
                        "<span class=\"keyword\">new</span> "
                );


        escaped =
                escaped.replace(
                        "File ",
                        "<span class=\"keyword\">File</span> "
                );


        escaped =
                escaped.replace(
                        "String ",
                        "<span class=\"keyword\">String</span> "
                );


        return escaped;

    }


    private void writeReport(
            StringBuilder html,
            String outputFile
    ) {

        try {

            Path reportPath =
                    Path.of(outputFile)
                            .toAbsolutePath()
                            .normalize();


            /*
             * Delete the previous HTML report.
             */
            Files.deleteIfExists(reportPath);


            /*
             * Generate the latest HTML report.
             */
            Files.writeString(
                    reportPath,
                    html.toString(),
                    StandardCharsets.UTF_8
            );


            System.out.println(
                    "[+] HTML report generated successfully."
            );


            System.out.println(
                    "[+] Report location: "
                            + reportPath
            );


            /*
             * Automatically open the HTML report
             * in the default browser.
             */
            openReport(reportPath);


        } catch (IOException e) {

            System.err.println(
                    "[-] Failed to generate HTML report."
            );


            System.err.println(
                    "[-] Reason: "
                            + e.getMessage()
            );


            e.printStackTrace();

        }

    }
    private void openReport(Path reportPath) {

        try {

            if (!java.awt.Desktop.isDesktopSupported()) {

                System.out.println(
                        "[!] Automatic browser opening is not supported."
                );

                return;
            }


            java.awt.Desktop desktop =
                    java.awt.Desktop.getDesktop();


            if (!desktop.isSupported(
                    java.awt.Desktop.Action.BROWSE
            )) {

                System.out.println(
                        "[!] Browser opening is not supported."
                );

                return;
            }


            desktop.browse(
                    reportPath.toUri()
            );


            System.out.println(
                    "[+] Opening HTML report in browser..."
            );


        } catch (Exception e) {

            System.out.println(
                    "[!] Could not automatically open HTML report."
            );


            System.out.println(
                    "[!] Open manually: "
                            + reportPath
            );

        }

    }
    private String escapeHtml(
            String value
    ) {


        if (value == null) {

            return "";

        }


        return value

                .replace("&", "&amp;")

                .replace("<", "&lt;")

                .replace(">", "&gt;")

                .replace("\"", "&quot;")

                .replace("'", "&#39;");

    }

}