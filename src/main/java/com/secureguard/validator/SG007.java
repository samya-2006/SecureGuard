package com.secureguard.validator;

import com.secureguard.util.ValueAnalyzer;

import java.util.List;
import java.util.regex.Matcher;

public class SG007 implements ValueValidator {

    @Override
    public boolean validate(List<String> lines,
                            int currentLine,
                            Matcher matcher) {

        String current = lines.get(currentLine);

        // If SecureRandom is used, ignore
        if (current.contains("SecureRandom")
                || current.contains("random_int")
                || current.contains("random_bytes")
                || current.contains("crypto.randomBytes")
                || current.contains("crypto.getRandomValues")
                || current.contains("RandomNumberGenerator")
                || current.contains("secrets.")) {

            return false;
        }

        // Direct insecure RNG
        if (ValueAnalyzer.isWeakRandom(current)) {
            return true;
        }

        // Variable-based RNG
        String variable = ValueAnalyzer.extractVariable(current);

        if (variable.isEmpty()) {
            return false;
        }

        String assignment =
                ValueAnalyzer.findAssignment(lines, variable);

        if (assignment.isEmpty()) {
            return false;
        }

        return ValueAnalyzer.isWeakRandom(assignment);

    }

}