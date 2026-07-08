package com.secureguard.validator;

import java.util.List;
import java.util.regex.Matcher;

public interface ValueValidator {

    boolean validate(List<String> lines, int currentLine, Matcher matcher);

}