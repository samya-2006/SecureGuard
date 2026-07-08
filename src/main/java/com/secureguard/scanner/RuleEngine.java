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

    public List<Issue> scanFile(File file, List<Rule> rules) {
        LanguageDetector languageDetector = new LanguageDetector();
        Language detectedLanguage = languageDetector.detect(file);
        List<Issue> issues = new ArrayList<>();

        try {

            List<String> lines = Files.readAllLines(file.toPath());

            for (int i = 0; i < lines.size(); i++) {

                String line = lines.get(i);

                for (Rule rule : rules) {
                    if (rule.getLanguage() != detectedLanguage) {
                        continue;
                    }

                    for (Detector detector : rule.getDetectors()) {

                        Pattern pattern = Pattern.compile(detector.getRegex());

                        Matcher matcher = pattern.matcher(line);

                        if (matcher.find()) {

                            ValueValidator validator =
                                    ValidatorFactory.getValidator(rule.getRuleId());

                            if (validator != null && !validator.validate(lines, i, matcher)) {
                                continue;
                            }

                            issues.add(new Issue(
                                    rule,
                                    detector,
                                    file.getName(),
                                    i + 1,
                                    line.trim()
                            ));

                            break;
                        }
                    }
                }
            }

            } catch(IOException e){

                e.printStackTrace();

            }

            return issues;

        }
    }
