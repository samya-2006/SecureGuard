package com.secureguard.validator;

public class ValidatorFactory {

    public static ValueValidator getValidator(String ruleId) {

        switch (ruleId) {

            case "SG001":
                return new SG001();

            case "SG002":
                return new SG002();

            case "SG003":
                return new SG003();

            case "SG004":
                return new SG004();

            case "SG005":
                return new SG005();

            case "SG006":
                return new SG006();

            case "SG007":
                return new SG007();

            case "SG008":
                return new SG008();

            case "SG009":
                return new SG009();

            case "SG010":
                return new SG010();

            default:
                return null;
        }
    }
}