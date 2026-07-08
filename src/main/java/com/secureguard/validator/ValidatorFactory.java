package com.secureguard.validator;

public class ValidatorFactory {

    public static ValueValidator getValidator(String ruleId) {

        switch (ruleId) {

            case "SG001":
                return new SG001();

            case "SG003":
                return new SG003();

            case "SG010":
                return new SG010();

            default:
                return null;
        }
    }
}