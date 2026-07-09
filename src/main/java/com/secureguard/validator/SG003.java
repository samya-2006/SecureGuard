package com.secureguard.validator;

import com.secureguard.util.ValueAnalyzer;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SG003 implements ValueValidator {

    private static final int CONTEXT_WINDOW = 8;

    // Word-bounded so we match the actual SQL keyword and not a substring
    // of an unrelated identifier - e.g. the old .contains("SELECT") /
    // .contains("UPDATE") / .contains("DELETE") checks also matched
    // "selectedItems", "updatedAt", "isDeleted", "INSERTED_ID", etc.,
    // which produced a steady stream of false-positive (and effectively
    // duplicate-looking) findings on lines that have nothing to do with
    // building a SQL string.
    private static final Pattern SQL_KEYWORD = Pattern.compile(
            "\\b(SELECT|INSERT|UPDATE|DELETE)\\b", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean validate(List<String> lines,
                            int currentLine,
                            Matcher matcher) {

        String current = lines.get(currentLine);

        // Case 1: the matched line itself is a direct SQL keyword glued to a
        // string via "+". This is unambiguous - flag immediately regardless
        // of whether it also happens to contain a traceable variable.
        if (isSqlConcatenation(current)) {
            return true;
        }

        // If this call is clearly operating on a PreparedStatement /
        // parameterized-query object (the recommended fix for this exact
        // rule), don't flag it just for calling execute*() - that would
        // penalize the correct pattern.
        if (isLikelyParameterizedQuery(lines, currentLine)) {
            return false;
        }

        // Case 2: an execute()/executeQuery()/query() style call - trace the
        // variable passed in to see how the query string was built.
        String variable = ValueAnalyzer.extractVariable(current);

        if (variable.isEmpty()) {
            // Matched a dangerous query-execution API but couldn't isolate an
            // argument (e.g. chained/wrapped call) - still worth flagging at
            // the detector's own (already lower) confidence level rather than
            // silently dropping it.
            return true;
        }

        String assignment = ValueAnalyzer.findAssignment(lines, variable);

        if (assignment.isEmpty()) {
            // Couldn't trace the variable locally - it's likely a method
            // parameter or built elsewhere. Flag it instead of assuming it's
            // safe.
            return true;
        }

        if (isSqlConcatenation(assignment)) {
            return true;
        }

        return ValueAnalyzer.containsConcatenation(assignment)
                || ValueAnalyzer.containsUserInput(assignment);
    }

    private boolean isSqlConcatenation(String text) {
        return SQL_KEYWORD.matcher(text).find() && text.contains("+");
    }

    /**
     * Looks a few lines above the current one for clear signs of a
     * parameterized/prepared query (PreparedStatement, "?" placeholders,
     * setString/setInt-style binding calls) - the standard fix for SQL
     * injection. If present, calling execute()/executeQuery() on that object
     * shouldn't be flagged.
     */
    private boolean isLikelyParameterizedQuery(List<String> lines, int currentLine) {
        int start = Math.max(0, currentLine - CONTEXT_WINDOW);

        for (int i = start; i <= currentLine; i++) {
            String lower = lines.get(i).toLowerCase();

            if (lower.contains("preparedstatement")
                    || lower.contains("prepare(")
                    || lower.contains("prepare_stmt")
                    || lower.contains(".setstring(")
                    || lower.contains(".setint(")
                    || lower.contains(".bindparam")
                    || lower.contains(".bindvalue")) {
                return true;
            }
        }

        return false;
    }
}