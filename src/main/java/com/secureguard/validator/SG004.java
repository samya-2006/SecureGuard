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
        String lowerCurrent = current.toLowerCase();

        // Hardcoded, fully-literal commands with no dynamic input at all are
        // low risk - e.g. exec("ls -la"). Only skip when the ENTIRE argument
        // list is a single quoted literal with no concatenation.
        if (isFullyHardcodedCall(current)) {
            return false;
        }

        String variable = ValueAnalyzer.extractVariable(current);

        if (variable.isEmpty()) {
            // Couldn't isolate a clean argument (chained/wrapped call syntax)
            // but we already know this line invokes a process-spawning API.
            return true;
        }

        String assignment = ValueAnalyzer.findAssignment(lines, variable);

        if (assignment.isEmpty()) {
            // Command variable is a method parameter or built elsewhere -
            // can't prove it's safe, so flag it.
            return true;
        }

        assignment = assignment.toLowerCase();

        if (ValueAnalyzer.containsUserInput(assignment)) {
            return true;
        }

        if (ValueAnalyzer.containsConcatenation(assignment) || assignment.contains("+")) {
            return true;
        }

        if (ValueAnalyzer.containsCommandExecution(assignment)) {
            return true;
        }

        // Assignment traced but doesn't look dynamic (e.g. a plain string
        // literal) - low risk.
        return false;
    }

    private boolean isFullyHardcodedCall(String line) {
        String trimmed = line.trim();
        int open = trimmed.indexOf('(');
        int close = trimmed.lastIndexOf(')');
        if (open == -1 || close == -1 || open >= close) {
            return false;
        }
        String args = trimmed.substring(open + 1, close).trim();
        if (args.isEmpty()) {
            return true;
        }
        boolean isQuoted = (args.startsWith("\"") && args.endsWith("\""))
                || (args.startsWith("'") && args.endsWith("'"));
        return isQuoted && !args.contains("+");
    }
}
