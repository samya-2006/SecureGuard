package com.secureguard.validator;

import com.secureguard.util.ValueAnalyzer;

import java.util.List;
import java.util.regex.Matcher;

public class SG006 implements ValueValidator {

    @Override
    public boolean validate(List<String> lines,
                            int currentLine,
                            Matcher matcher) {

        String current = lines.get(currentLine);

        // Direct weak algorithm usage
        if (ValueAnalyzer.isWeakCipher(current)) {
            return true;
        }

        // Variable passed to crypto API
        String variable = ValueAnalyzer.extractVariable(current);

        if (variable.isEmpty()) {
            return false;
        }

        String assignment =
                ValueAnalyzer.findAssignment(lines, variable);

        if (assignment.isEmpty()) {
            return false;
        }

        return ValueAnalyzer.isWeakCipher(assignment);

    }

}