package com.secureguard.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValueAnalyzer {

    /**
     * Extracts the argument list from statements like:
     * executeQuery(sql)
     * new URL(url)
     * Runtime.exec(cmd)
     * new File(getBaseDir(), "sub/" + userInput)
     *
     * This walks from the first "(" to its properly *balanced* matching
     * ")", ignoring parentheses that appear inside string literals. The
     * previous implementation used indexOf('(') / indexOf(')') on the raw
     * text, which grabbed the closing paren of the FIRST nested call
     * instead of the outer one - e.g. for
     * "new File(getBaseDir(), \"sub/\" + userInput)" it returned just
     * "getBaseDir(" instead of the full argument list. That truncation
     * made downstream validators (SG003, SG005, ...) analyze the wrong
     * substring whenever an argument itself contained a call or a
     * parenthesized string.
     */
    public static String extractVariable(String line) {

        int start = line.indexOf('(');
        if (start == -1) {
            return "";
        }

        int depth = 0;
        boolean inString = false;
        char stringChar = 0;

        for (int i = start; i < line.length(); i++) {
            char c = line.charAt(i);

            if (inString) {
                if (c == stringChar && line.charAt(i - 1) != '\\') {
                    inString = false;
                }
                continue;
            }

            if (c == '"' || c == '\'') {
                inString = true;
                stringChar = c;
                continue;
            }

            if (c == '(') {
                depth++;
            } else if (c == ')') {
                depth--;
                if (depth == 0) {
                    return line.substring(start + 1, i).trim();
                }
            }
        }

        // Unbalanced (statement continues on another line) - nothing safe
        // to return here; callers already handle "" as "couldn't isolate
        // an argument".
        return "";
    }

    /**
     * Returns a copy of {@code text} with the same length in which every
     * character *inside* a string literal has been replaced with '#',
     * while the surrounding code and the quote characters themselves are
     * left untouched. Used by the engine (and validators that want extra
     * safety) to avoid matching code-shaped text that only appears inside
     * a quoted string, e.g. a log message or comparison like:
     * if (line.contains("logger.debug(")) { ... }
     * Without this, that string payload gets mistaken for an actual
     * logger.debug( call.
     *
     * Deliberately NOT used by rules that need to look INSIDE string
     * literals on purpose (e.g. SG003's SQL-keyword check, which is
     * looking for "SELECT ... " + var, where the keyword genuinely lives
     * inside the string).
     */
    public static String maskStringLiterals(String text) {

        StringBuilder result = new StringBuilder(text.length());
        boolean inString = false;
        char stringChar = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (inString) {
                if (c == stringChar && text.charAt(i - 1) != '\\') {
                    inString = false;
                    result.append(c);
                } else {
                    result.append(c == '\\' ? '\\' : '#');
                }
                continue;
            }

            if (c == '"' || c == '\'') {
                inString = true;
                stringChar = c;
                result.append(c);
                continue;
            }

            result.append(c);
        }

        return result.toString();
    }

    // Method names where a quoted argument is being COMPARED against, not
    // executed or assigned. A string literal sitting inside one of these
    // calls describes what the code is checking for, not what the code
    // does - e.g. `msg.contains("logger.debug(")` never calls
    // logger.debug(), and `line.contains("debug = true")` never sets
    // debug = true. Matching detector text against such literals verbatim
    // is exactly how a scanner/validator source file ends up tripping its
    // own rules on its own comparison strings.
    private static final Pattern COMPARISON_CALL = Pattern.compile(
            "\\b(contains|equals|equalsIgnoreCase|matches|indexOf|startsWith|endsWith|compareTo)\\s*\\(");

    /**
     * Like {@link #maskStringLiterals(String)}, but scoped: only the
     * quoted arguments of string-comparison calls (.contains(...),
     * .equals(...), .matches(...), etc.) are masked. Everything else -
     * including genuine assignments like NODE_ENV = 'development' or
     * literal API arguments like Cipher.getInstance("DES") - is left
     * exactly as written, since those quotes ARE the dangerous value,
     * not incidental text being compared against.
     */
    public static String maskComparisonStringArguments(String text) {

        List<int[]> regions = findComparisonCallArgRegions(text);
        if (regions.isEmpty()) {
            return text;
        }

        String fullyMasked = maskStringLiterals(text);
        char[] chars = text.toCharArray();

        for (int[] region : regions) {
            for (int i = region[0]; i < region[1] && i < chars.length; i++) {
                chars[i] = fullyMasked.charAt(i);
            }
        }

        return new String(chars);
    }

    private static List<int[]> findComparisonCallArgRegions(String text) {
        List<int[]> regions = new ArrayList<>();
        Matcher m = COMPARISON_CALL.matcher(text);

        while (m.find()) {
            int openParen = m.end() - 1;
            int close = matchParen(text, openParen);
            if (close != -1) {
                regions.add(new int[]{openParen + 1, close});
            }
        }

        return regions;
    }

    /**
     * Given the index of an opening '(', returns the index of its
     * balanced matching ')', ignoring parens inside string literals.
     * Returns -1 if unbalanced (statement continues elsewhere).
     */
    private static int matchParen(String text, int openIdx) {
        int depth = 0;
        boolean inString = false;
        char stringChar = 0;

        for (int i = openIdx; i < text.length(); i++) {
            char c = text.charAt(i);

            if (inString) {
                if (c == stringChar && text.charAt(i - 1) != '\\') {
                    inString = false;
                }
                continue;
            }

            if (c == '"' || c == '\'') {
                inString = true;
                stringChar = c;
                continue;
            }

            if (c == '(') {
                depth++;
            } else if (c == ')') {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     * Finds the complete assignment of a variable.
     * Supports:
     * String sql =
     * final String sql =
     * var sql =
     * sql =
     * Multi-line assignments
     */
    public static String findAssignment(List<String> lines, String variable) {

        if (variable == null || variable.isBlank()) {
            return "";
        }

        for (int i = 0; i < lines.size(); i++) {

            String line = lines.get(i).trim();

            if (isAssignment(line, variable)) {

                StringBuilder builder = new StringBuilder(line);

                while (!builder.toString().contains(";")
                        && i + 1 < lines.size()) {

                    i++;
                    builder.append(" ")
                            .append(lines.get(i).trim());
                }

                return builder.toString();
            }
        }

        return "";
    }

    private static boolean isAssignment(String line, String variable) {

        line = line.replaceAll("\\s+", " ");

        return line.matches(".*\\b" + variable + "\\b\\s*=.*");
    }

    /* ---------- Shared Checks ---------- */

    public static boolean containsConcatenation(String assignment) {

        String lower = assignment.toLowerCase();

        if (!assignment.contains("+")) {
            return false;
        }

        return containsUserInput(lower)
                || containsPathTraversal(lower);
    }

    public static boolean containsUserInput(String assignment) {

        String lower = assignment.toLowerCase();

        return lower.contains("request")
                || lower.contains("scanner")
                || lower.contains("bufferedreader")
                || lower.contains("console")
                || lower.contains("getparameter")
                || lower.contains("readline")
                || lower.contains("nextline")
                || lower.contains("next()");
    }

    public static boolean isEnvironmentLookup(String value) {

        String lower = value.toLowerCase();

        return lower.contains("system.getenv")
                || lower.contains("config.get")
                || lower.contains("properties.getproperty")
                || lower.contains("environment.getproperty")
                || lower.contains("system.getproperty");
    }

    public static boolean isLocalhost(String value) {

        String lower = value.toLowerCase();

        return lower.contains("localhost")
                || lower.contains("127.0.0.1")
                || lower.contains("0.0.0.0");
    }

    public static boolean isHTTP(String value) {

        return value.toLowerCase().contains("http://");
    }

    public static boolean isWeakCipher(String value) {

        String lower = value.toLowerCase();

        return lower.contains("md2")
                || lower.contains("md4")
                || lower.contains("md5")
                || lower.contains("sha1")
                || lower.contains("sha-1")
                || lower.contains("des")
                || lower.contains("3des")
                || lower.contains("tripledes")
                || lower.contains("desede")
                || lower.contains("rc2")
                || lower.contains("rc4");
    }

    public static boolean isWeakRandom(String value) {

        String lower = value.toLowerCase();

        return lower.contains("random")
                || lower.contains("math.random")
                || lower.contains("rand(");
    }

    public static boolean containsPathTraversal(String value) {

        String lower = value.toLowerCase();

        return lower.contains("../")
                || lower.contains("..\\")
                || lower.contains("%2e%2e")
                || lower.contains("%252e")
                || lower.contains("/etc/passwd")
                || lower.contains("c:\\windows");
    }
    public static boolean containsCommandExecution(String value) {

        String lower = value.toLowerCase();

        return lower.contains("runtime.exec")
                || lower.contains("processbuilder")
                || lower.contains("os.system")
                || lower.contains("exec(")
                || lower.contains("system(");
    }

}