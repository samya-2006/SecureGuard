package com.secureguard.scanner;

import com.secureguard.model.Issue;
import com.secureguard.model.Language;
import com.secureguard.model.Rule;
import com.secureguard.model.Detector;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import com.secureguard.validator.ValidatorFactory;
import com.secureguard.validator.ValueValidator;
import java.util.regex.Matcher;

public class RuleEngine {

    // How many extra lines to fold into the current line when trying to catch
    // statements that a developer split across multiple lines (method chains,
    // wrapped arguments, etc). Increasing this catches more multi-line
    // vulnerabilities at the cost of a slightly larger scan window.
    private static final int MAX_JOIN_LOOKAHEAD = 4;

    // Detectors are cached per-language so we don't re-filter the whole rule
    // list for every single line of every file.
    private java.util.Map<Language, List<Rule>> rulesByLanguage = new java.util.HashMap<>();

    public List<Issue> scanFile(File file, List<Rule> rules) {
        LanguageDetector languageDetector = new LanguageDetector();
        Language detectedLanguage = languageDetector.detect(file);
        List<Issue> issues = new ArrayList<>();

        if (detectedLanguage == Language.UNKNOWN) {
            return issues;
        }

        List<Rule> applicableRules = rulesByLanguage.computeIfAbsent(detectedLanguage,
                lang -> {
                    List<Rule> filtered = new ArrayList<>();
                    for (Rule rule : rules) {
                        if (rule.getLanguage() == lang) {
                            filtered.add(rule);
                        }
                    }
                    return filtered;
                });

        if (applicableRules.isEmpty()) {
            return issues;
        }

        try {

            List<String> lines = Files.readAllLines(
                    file.toPath(),
                    java.nio.charset.StandardCharsets.UTF_8
            );

            boolean inBlockComment = false;

            for (int i = 0; i < lines.size(); i++) {

                String line = lines.get(i);
                String trimmed = line.trim();

                // Track multi-line block comments (/* ... */) properly instead of
                // only skipping lines that happen to start with * or /*.
                if (inBlockComment) {
                    int end = trimmed.indexOf("*/");
                    if (end == -1) {
                        continue;
                    }
                    inBlockComment = false;
                    trimmed = trimmed.substring(end + 2).trim();
                    if (trimmed.isEmpty()) {
                        continue;
                    }
                }

                if (trimmed.startsWith("//")
                        || trimmed.startsWith("#")) {
                    continue;
                }

                if (trimmed.startsWith("/*")) {
                    int end = trimmed.indexOf("*/", 2);
                    if (end == -1) {
                        inBlockComment = true;
                        continue;
                    }
                }

                if (trimmed.isEmpty()) {
                    continue;
                }

                // Build a "logical statement" window: the current line plus as
                // many following lines as needed until we see a statement
                // terminator, capped at MAX_JOIN_LOOKAHEAD. This lets the regex
                // detectors catch vulnerabilities that a developer wrote across
                // several lines (wrapped method calls, chained builders, etc.)
                // instead of only ever matching a single physical line.
                StringBuilder windowBuilder = new StringBuilder(line);
                int joined = 0;
                int cursor = i;
                while (!statementLikelyComplete(windowBuilder.toString())
                        && joined < MAX_JOIN_LOOKAHEAD
                        && cursor + 1 < lines.size()) {
                    cursor++;
                    joined++;
                    windowBuilder.append(' ').append(lines.get(cursor).trim());
                }
                String window = windowBuilder.toString();

                for (Rule rule : applicableRules) {

                    for (Detector detector : rule.getDetectors()) {

                        Pattern pattern = Pattern.compile(detector.getRegex());

                        // NOTE: we deliberately do NOT strip/mask string
                        // literals before matching here. Several rules
                        // depend on matching literal text *inside* quotes
                        // (SG002's `"*"` CORS wildcard, SG006's
                        // `"DES"`/`"RC4"` cipher names, SG010's
                        // `"http://..."` URLs, SG001's hardcoded secret
                        // values). Masking those out at the engine level
                        // would silently break all of those detectors.
                        // The SG009 self-match issue is instead handled
                        // inside SG009 itself, where it can distinguish
                        // "this is a real logger.debug( call" from "this
                        // is a string literal being compared against" -
                        // see SG009.java for details.
                        Matcher matcher = pattern.matcher(line);
                        boolean matchedOnLine = matcher.find();
                        boolean matchedOnWindowOnly = false;

                        if (!matchedOnLine && joined > 0) {
                            // Fall back to the joined multi-line window so
                            // statements split across lines are still caught.
                            Matcher windowMatcher = pattern.matcher(window);
                            if (windowMatcher.find()) {
                                matcher = windowMatcher;
                                matchedOnWindowOnly = true;
                            }
                        }

                        if (matchedOnLine || matchedOnWindowOnly) {

                            ValueValidator validator =
                                    ValidatorFactory.getValidator(rule.getRuleId());

                            if (validator != null
                                    && !validator.validate(lines, i, matcher)) {
                                continue;
                            }

                            String snippet = matchedOnWindowOnly
                                    ? truncate(window, 160)
                                    : line.trim();

                            issues.add(new Issue(
                                    rule,
                                    detector,
                                    file.getName(),
                                    i + 1,
                                    snippet
                            ));

                            break;
                        }
                    }
                }
            }

        } catch (IOException e) {
        // or your logging framework of choice
        System.err.println("Failed to scan file " + file.getName() + ": " + e.getMessage());
    }

        return dedupe(issues);
    }

    /**
     * Heuristic: is this line/window likely a complete statement already?
     * We don't do full parsing, just look for common statement/line
     * terminators across the supported languages so we know when to stop
     * pulling in more lines.
     */
    private boolean statementLikelyComplete(String text) {
        String t = text.trim();
        if (t.isEmpty()) {
            return true;
        }

        // Unbalanced brackets/parens/braces mean the statement clearly
        // continues on the next line, regardless of language.
        if (!bracketsBalanced(t)) {
            return false;
        }

        char last = t.charAt(t.length() - 1);

        if (last == ';' || last == '{' || last == '}') {
            return true;
        }

        // Trailing continuation markers (concatenation, line-continuation
        // backslash, dangling assignment, boolean operators) mean more is
        // coming even though brackets are balanced so far.
        if (last == '\\' || last == '+' || last == ',' || last == '='
                || last == '&' || last == '|') {
            return false;
        }

        return true;
    }

    /**
     * Rough bracket-balance check that ignores anything inside string
     * literals, so we don't get confused by "(" appearing inside a message.
     */
    private boolean bracketsBalanced(String text) {
        // Deliberately NOT tracking { } here: an opening brace at the end of
        // a line is a normal block start (method/if/for/...), not a sign
        // that a single statement continues. We only care about ( ) and
        // [ ] because those are what typically get wrapped across lines
        // (long method calls, argument lists, array/collection literals).
        int round = 0;
        int square = 0;
        boolean inString = false;
        char stringChar = 0;

        for (int idx = 0; idx < text.length(); idx++) {
            char c = text.charAt(idx);

            if (inString) {
                if (c == stringChar && (idx == 0 || text.charAt(idx - 1) != '\\')) {
                    inString = false;
                }
                continue;
            }

            if (c == '"' || c == '\'') {
                inString = true;
                stringChar = c;
                continue;
            }

            switch (c) {
                case '(': round++; break;
                case ')': round--; break;
                case '[': square++; break;
                case ']': square--; break;
                default: break;
            }
        }

        return round <= 0 && square <= 0;
    }

    private String truncate(String text, int maxLen) {
        if (text.length() <= maxLen) {
            return text;
        }
        return text.substring(0, maxLen) + "...";
    }

    /**
     * Because we now match against sliding multi-line windows, the same
     * underlying vulnerability can occasionally be picked up from two
     * neighbouring starting lines. Collapse those into a single reported
     * issue, keeping the earliest line number.
     */
    private List<Issue> dedupe(List<Issue> issues) {
        List<Issue> result = new ArrayList<>();

        for (Issue issue : issues) {
            boolean duplicate = false;

            for (Issue existing : result) {
                if (existing.getFileName().equals(issue.getFileName())
                        && existing.getRule().getRuleId().equals(issue.getRule().getRuleId())
                        && existing.getDetector().getName().equals(issue.getDetector().getName())
                        && Math.abs(existing.getLineNumber() - issue.getLineNumber()) <= 2) {
                    duplicate = true;
                    break;
                }
            }

            if (!duplicate) {
                result.add(issue);
            }
        }

        return result;
    }
}