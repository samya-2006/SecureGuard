package com.secureguard.model;
import java.util.List;
public class Rule {

    private String ruleId;
    private String name;
    private String severity;
    private List<Detector> detectors;
    private String description;
    private String recommendation;
    private Language language;

    public Rule() {
    }

    public Rule(String ruleId, String name, String severity,
                List<Detector> detectors, String description,
                String recommendation, Language language) {

        this.ruleId = ruleId;
        this.name = name;
        this.severity = severity;
        this.detectors = detectors;
        this.description = description;
        this.recommendation = recommendation;
        this.language = language;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public List<Detector> getDetectors() {
        return detectors;
    }

    public void setDetectors(List<Detector> detectors) {
        this.detectors = detectors;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
}