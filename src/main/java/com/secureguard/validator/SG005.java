package com.secureguard.validator;

import com.secureguard.util.ValueAnalyzer;

import java.util.List;
import java.util.regex.Matcher;

public class SG005 implements ValueValidator {

    @Override
    public boolean validate(List<String> lines,
                            int currentLine,
                            Matcher matcher) {

        String currentRaw = lines.get(currentLine);
        String current = currentRaw.toLowerCase();

        // Extract what's inside new File(...), Paths.get(...), etc.
        String value = ValueAnalyzer.extractVariable(current);

        if (value.isBlank()) {
            return false;
        }

        value = value.trim();

        // Safe literals - fully hardcoded filenames with no dynamic part.
        if (value.equals("\".\"")
                || value.equals("\"..\"")
                || value.equals("\"config.properties\"")
                || value.equals("\"application.properties\"")) {
            return false;
        }

        // Command-line arguments are expected in CLI applications by design.
        if (value.startsWith("args[") || value.startsWith("argv[")) {
            return false;
        }

        // Direct user-controlled input or a literal traversal payload.
        if (ValueAnalyzer.containsUserInput(value)) {
            return true;
        }

        if (ValueAnalyzer.containsPathTraversal(value)) {
            return true;
        }

        // A pure hardcoded string literal (no variables, no concatenation)
        // has no dynamic component and can't be traversed by an attacker.
        if (isPureStringLiteral(value)) {
            return false;
        }

        // Anything else - a bare variable/parameter, a field access, a
        // concatenation, or a nested call - carries an unproven dynamic
        // component. Try to trace it for extra context, but flag it either
        // way since file APIs taking non-literal paths are the whole point
        // of this rule.
        if (value.matches("[a-zA-Z_][a-zA-Z0-9_.]*")) {

            String assignment = ValueAnalyzer.findAssignment(lines, value);

            if (!assignment.isBlank()) {
                assignment = assignment.toLowerCase();

                if (ValueAnalyzer.containsPathTraversal(assignment)) {
                    return true;
                }
                if (ValueAnalyzer.containsUserInput(assignment)) {
                    return true;
                }
                if (isPureStringLiteral(assignment.trim())) {
                    return false;
                }
            }
        }

        // Dynamic/unresolved value with no proof of safety - flag it.
        return true;
    }

    private boolean isPureStringLiteral(String value) {
        String v = value.trim();
        if (v.endsWith(";")) {
            v = v.substring(0, v.length() - 1).trim();
        }
        boolean isQuoted = (v.startsWith("\"") && v.endsWith("\""))
                || (v.startsWith("'") && v.endsWith("'"));
        return isQuoted && !v.contains("+");
    }
}
