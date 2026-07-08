package com.secureguard.model;

public class Detector {

    private String name;
    private String regex;
    private String example;
    private String confidence;

    public Detector() {
    }

    public Detector(String name, String regex, String example, String confidence) {
        this.name = name;
        this.regex = regex;
        this.example = example;
        this.confidence = confidence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }
}