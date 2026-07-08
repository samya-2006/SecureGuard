package com.secureguard.validator;

import com.secureguard.util.ValueAnalyzer;

import java.util.List;
import java.util.regex.Matcher;

public class SG005 implements ValueValidator {

    @Override
    public boolean validate(List<String> lines,
                            int currentLine,
                            Matcher matcher) {

        String current = lines.get(currentLine);

        String variable = ValueAnalyzer.extractVariable(current);

        // If path is passed directly
        if (variable.isEmpty()) {

            String lower = current.toLowerCase();

            if (ValueAnalyzer.containsPathTraversal(lower)) {
                return true;
            }

            return ValueAnalyzer.containsUserInput(lower);

        }

        // Find where the path variable was assigned
        String assignment =
                ValueAnalyzer.findAssignment(lines, variable);

        if (assignment.isEmpty()) {
            return false;
        }

        assignment = assignment.toLowerCase();

        // ../  ..\
        if (ValueAnalyzer.containsPathTraversal(assignment)) {
            return true;
        }

        // request.getParameter(), scanner.nextLine(), etc.
        if (ValueAnalyzer.containsUserInput(assignment)) {
            return true;
        }

        // "uploads/" + filename
        if (ValueAnalyzer.containsConcatenation(assignment)) {
            return true;
        }

        return false;

    }

}