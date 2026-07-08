package com.secureguard.util;

import java.util.List;

public class ValueAnalyzer {

    /**
     * Extracts variable from statements like:
     * executeQuery(sql)
     * new URL(url)
     * Runtime.exec(cmd)
     */
    public static String extractVariable(String line) {

        int start = line.indexOf('(');
        int end = line.indexOf(')');

        if (start == -1 || end == -1 || start >= end) {
            return "";
        }

        return line.substring(start + 1, end).trim();
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

        return assignment.contains("+");
    }

    public static boolean containsUserInput(String assignment) {

        String lower = assignment.toLowerCase();

        return lower.contains("request")
                || lower.contains("scanner")
                || lower.contains("bufferedreader")
                || lower.contains("console")
                || lower.contains("input")
                || lower.contains("args[")
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

        return lower.contains("des")
                || lower.contains("md5")
                || lower.contains("sha1")
                || lower.contains("rc4");
    }

    public static boolean isWeakRandom(String value) {

        String lower = value.toLowerCase();

        return lower.contains("random")
                || lower.contains("math.random")
                || lower.contains("rand(");
    }

    public static boolean containsPathTraversal(String value) {

        return value.contains("../")
                || value.contains("..\\");
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