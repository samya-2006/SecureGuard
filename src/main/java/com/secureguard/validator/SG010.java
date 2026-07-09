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

        // Case 1: Direct hardcoded HTTP URL on this line.
        if (ValueAnalyzer.isHTTP(current)) {
            return !ValueAnalyzer.isLocalhost(current);
        }

        // Case 2: A generic HTTP-client class/API usage (e.g.
        // HttpURLConnection, http.client) with no literal URL on the same
        // line. We can't prove the target is HTTPS, so flag it at whatever
        // confidence the detector itself already assigned (typically MEDIUM).
        String variable = ValueAnalyzer.extractVariable(current);

        if (variable.isEmpty()) {
            return true;
        }

        String assignment =
                ValueAnalyzer.findAssignment(lines, variable);

        if (assignment.isEmpty()) {
            return true;
        }

        if (ValueAnalyzer.isLocalhost(assignment)) {
            return false;
        }

        return true;
    }
}
