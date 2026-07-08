package com.secureguard.validator;

import java.util.List;
import java.util.regex.Matcher;

public class SG008 implements ValueValidator {

    @Override
    public boolean validate(List<String> lines,
                            int currentLine,
                            Matcher matcher) {

        // Search entire file for secure parser configuration
        for (String line : lines) {

            String lower = line.toLowerCase();

            // Java
            if (lower.contains("disallow-doctype-decl"))
                return false;

            if (lower.contains("external-general-entities")
                    && lower.contains("false"))
                return false;

            if (lower.contains("external-parameter-entities")
                    && lower.contains("false"))
                return false;

            if (lower.contains("secure_processing"))
                return false;

            // .NET
            if (lower.contains("dtdprocessing.prohibit"))
                return false;

            if (lower.contains("xmlresolver = null"))
                return false;

            // Python
            if (lower.contains("defusedxml"))
                return false;

            // PHP
            if (lower.contains("libxml_nonet"))
                return false;

        }

        // Parser exists but no protection found
        return true;

    }

}