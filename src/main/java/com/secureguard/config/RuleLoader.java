package com.secureguard.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.secureguard.model.Rule;


import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.util.List;

public class RuleLoader {

    public  List<Rule> loadRules() {

        try {

            ObjectMapper mapper = new ObjectMapper();

            List<Rule> rules = new ArrayList<>();

            ClassLoader classLoader = getClass().getClassLoader();

            URL folderUrl = classLoader.getResource("rules");

            if (folderUrl == null) {
                throw new RuntimeException("Rules folder not found.");
            }

            File folder = new File(folderUrl.toURI());

            File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));

            if (files == null) {
                return rules;
            }

            for (File file : files) {

                List<Rule> loaded =
                        mapper.readValue(file, new TypeReference<List<Rule>>() {});

                rules.addAll(loaded);
            }

            return rules;

        } catch (Exception e) {

            throw new RuntimeException("Failed to load security rules.", e);

        }

    }

}