package com.secureguard.validator;

import com.secureguard.util.ValueAnalyzer;

import java.util.List;
import java.util.regex.Matcher;

public class SG002 implements ValueValidator {

    @Override
    public boolean validate(List<String> lines,
                            int currentLine,
                            Matcher matcher) {

        String current = lines.get(currentLine).trim();

        // Direct wildcard origin
        if (current.contains("\"*\"")
                || current.contains("'*'")
                || current.contains(": *")
                || current.contains("[\"*\"]")) {

            return true;
        }

        // Variable-based origin
        String variable = ValueAnalyzer.extractVariable(current);

        if (variable.isEmpty()) {
            return false;
        }

        String assignment =
                ValueAnalyzer.findAssignment(lines, variable);

        if (assignment.isEmpty()) {
            return false;
        }

        assignment = assignment.trim();

        return assignment.contains("\"*\"")
                || assignment.contains("'*'")
                || assignment.contains(": *")
                || assignment.contains("[\"*\"]");

    }

}