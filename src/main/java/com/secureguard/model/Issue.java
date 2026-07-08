package com.secureguard.model;

public class Issue {

    private Rule rule;
    private Detector detector;
    private String fileName;
    private int lineNumber;
    private String matchedCode;

    public Issue(Rule rule, Detector detector, String fileName , int lineNumber , String matchedCode) {
        this.rule = rule;
        this.detector = detector;
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.matchedCode = matchedCode;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public Detector getDetector() {
        return detector;
    }

    public void setDetector(Detector detector) {
        this.detector = detector;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getLineNumber() { return lineNumber; }

    public void setLineNumber(int lineNumber) { this.lineNumber = lineNumber; }

    public String getMatchedCode() { return matchedCode; }

    public void setMatchedCode(String matchedCode) { this.matchedCode = matchedCode;}
}
