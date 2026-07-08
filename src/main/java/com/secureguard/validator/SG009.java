package com.secureguard.validator;

import java.util.List;
import java.util.regex.Matcher;

public class SG009 implements ValueValidator {

    @Override
    public boolean validate(List<String> lines,
                            int currentLine,
                            Matcher matcher) {

        String current = lines.get(currentLine).toLowerCase().trim();

        // Safe configurations
        if (current.matches(".*\\bdebug\\s*=\\s*false\\b.*"))
            return false;

        if (current.matches(".*\\btrace\\s*=\\s*false\\b.*"))
            return false;

        if (current.matches(".*\\bdisplay_errors\\s*=\\s*off\\b.*"))
            return false;

        // Safe logger configuration
        if (current.contains("logging.level.root")
                && !current.contains("debug"))
            return false;

        if (current.contains("logging.level.")
                && !current.contains("debug"))
            return false;

        // Everything else matched by the regex is considered vulnerable
        return true;
    }
}