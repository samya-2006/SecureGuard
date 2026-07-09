package com.secureguard.validator;

import com.secureguard.util.ValueAnalyzer;

import java.util.List;
import java.util.regex.Matcher;

public class SG009 implements ValueValidator {

    @Override
    public boolean validate(List<String> lines,
                            int currentLine,
                            Matcher matcher) {

        String raw = lines.get(currentLine).trim();

        // Ignore comments
        if (raw.startsWith("//")
                || raw.startsWith("/*")
                || raw.startsWith("*")
                || raw.startsWith("*/")) {
            return false;
        }

        // The old approach only masked "call-shaped" indicators
        // (logger.debug(, printStackTrace()) and left the "assignment-
        // shaped" ones (debug = true, trace = true) reading the raw line
        // on the assumption those quotes were always a real config value.
        // That assumption broke on SG009's OWN source: lines like
        //     current.contains("debug = true")
        // are quoting "debug = true" as a comparison argument, not
        // assigning it - but the raw-line check couldn't tell the
        // difference and flagged them anyway.
        //
        // The actual distinguishing signal isn't "call-shaped vs.
        // assignment-shaped indicator", it's "is this quoted text sitting
        // inside a string-comparison call (.contains(...), .equals(...),
        // etc.) or not". So every indicator below is checked against a
        // single line where ONLY the arguments of comparison-style calls
        // are masked - genuine assignments like NODE_ENV = 'development'
        // or Cipher.getInstance("DES")-style literal API arguments are
        // completely untouched, since those quotes are the real value,
        // not incidental comparison text.
        String current = ValueAnalyzer.maskComparisonStringArguments(raw).toLowerCase();

        // Ignore normal console output
        if (current.contains("system.out.print")
                || current.contains("system.err.print")
                || current.contains("printf(")
                || current.contains("console.writeline(")
                || current.contains("console.write(")
                || current.contains("fmt.print")
                || current.contains("std::cout")
                || current.contains("print(")
                || current.contains("console.log(")) {
            return false;
        }

        // Safe configurations
        if (current.matches(".*\\bdebug\\s*=\\s*false\\b.*"))
            return false;

        if (current.matches(".*\\btrace\\s*=\\s*false\\b.*"))
            return false;

        if (current.matches(".*\\bdisplay_errors\\s*=\\s*off\\b.*"))
            return false;

        if (current.contains("logging.level.root")
                && !current.contains("debug"))
            return false;

        if (current.contains("logging.level.")
                && !current.contains("debug"))
            return false;

        // Real debug indicators.
        if (current.contains("logger.debug(")
                || current.contains("log.debug(")
                || current.contains(".debug(")
                || current.contains("console.debug(")
                || current.contains("e.printstacktrace(")
                || current.contains(".printstacktrace(")
                || current.contains("debug = true")
                || current.contains("debug=true")
                || current.contains("debug: true")
                || current.contains("debug:true")
                || current.contains("logging.level.root=debug")
                || current.contains("logging.level.root = debug")
                || current.contains("logging.level=debug")
                || current.contains("trace = true")
                || current.contains("trace=true")
                || current.contains("display_errors = on")
                || current.contains("display_errors=on")) {
            return true;
        }

        return false;
    }
}