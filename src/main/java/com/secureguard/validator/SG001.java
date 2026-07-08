package com.secureguard.validator;

import java.util.regex.Matcher;
import java.util.List;
public class SG001 implements ValueValidator {

    @Override
    public boolean validate(List<String> lines, int currentLine, Matcher matcher) {
        String line = lines.get(currentLine);
        String matchedText = matcher.group();

        String[] parts = matchedText.split("=", 2);

        if (parts.length < 2) {
            return false;
        }

        String value = parts[1].trim();

        value = value.replace("\"", "").replace("'", "");

        // Ignore empty values
        if (value.isEmpty()) {
            return false;
        }
        // Ignore null values
        if (value.equalsIgnoreCase("null")) {
            return false;
        }
        // Ignore environment variables
        if (value.startsWith("System.getenv(")) {
            return false;
        }
        // Ignore configuration lookups
        if (value.startsWith("config.get(")) {
            return false;
        }

        if (value.startsWith("properties.getProperty(")) {
            return false;
        }

        if (value.startsWith("environment.getProperty(")) {
            return false;
        }

        if (value.startsWith("System.getProperty(")) {
            return false;
        }
        // Ignore placeholder values
        String lower = value.toLowerCase();

        if (lower.equals("your_api_key") ||
                lower.equals("your_password") ||
                lower.equals("your_secret") ||
                lower.equals("change_me") ||
                lower.equals("replace_me") ||
                lower.equals("example") ||
                lower.equals("sample")) {
            return false;
        }
        return true;
    }
}