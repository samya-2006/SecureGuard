package com.secureguard.validator;

import com.secureguard.util.ValueAnalyzer;

import java.util.List;
import java.util.regex.Matcher;

public class SG010 implements ValueValidator {

    @Override
    public boolean validate(List<String> lines,
                            int currentLine,
                            Matcher matcher) {

        String current = lines.get(currentLine).trim();

        // Case 1: Direct hardcoded HTTP URL
        if (ValueAnalyzer.isHTTP(current)) {
            return !ValueAnalyzer.isLocalhost(current);
        }

        // Case 2: Variable passed to URL/API
        String variable = ValueAnalyzer.extractVariable(current);

        if (variable.isEmpty()) {
            return false;
        }

        String assignment =
                ValueAnalyzer.findAssignment(lines, variable);

        if (assignment.isEmpty()) {
            return false;
        }

        return ValueAnalyzer.isHTTP(assignment)
                && !ValueAnalyzer.isLocalhost(assignment);
    }
}