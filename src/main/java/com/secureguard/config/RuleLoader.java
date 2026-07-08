package com.secureguard.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.secureguard.model.Rule;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RuleLoader {

    public List<Rule> loadRules() {

        try {

            ObjectMapper mapper = new ObjectMapper();

            List<Rule> rules = new ArrayList<>();

            ClassLoader classLoader = getClass().getClassLoader();

            for (int i = 1; i <= 10; i++) {

                String resource = String.format("rules/SG%03d.json", i);

                try (InputStream inputStream = classLoader.getResourceAsStream(resource)) {

                    if (inputStream == null) {
                        continue;
                    }

                    List<Rule> loaded =
                            mapper.readValue(inputStream, new TypeReference<List<Rule>>() {});

                    rules.addAll(loaded);

                }

            }

            return rules;

        } catch (Exception e) {

            throw new RuntimeException("Failed to load security rules.", e);

        }

    }

}