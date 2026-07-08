package com.secureguard.validator;

import com.secureguard.util.ValueAnalyzer;

import java.util.List;
import java.util.regex.Matcher;

public class SG004 implements ValueValidator {

    @Override
    public boolean validate(List<String> lines,
                            int currentLine,
                            Matcher matcher) {

        String current = lines.get(currentLine);

        String variable = ValueAnalyzer.extractVariable(current);

        // Direct command execution
        if (variable.isEmpty()) {

            String lower = current.toLowerCase();

            return ValueAnalyzer.containsCommandExecution(lower);

        }

        // Find where the command variable was assigned
        String assignment =
                ValueAnalyzer.findAssignment(lines, variable);

        if (assignment.isEmpty()) {
            return false;
        }

        assignment = assignment.toLowerCase();

        // User controlled command
        if (ValueAnalyzer.containsUserInput(assignment)) {
            return true;
        }

        // Command created using concatenation
        if (ValueAnalyzer.containsConcatenation(assignment)) {
            return true;
        }

        // Dangerous command execution
        if (ValueAnalyzer.containsCommandExecution(assignment)) {
            return true;
        }

        return false;

    }

}