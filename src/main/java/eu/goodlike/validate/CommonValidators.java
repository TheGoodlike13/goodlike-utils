package eu.goodlike.validate;

import eu.goodlike.tbr.validate.impl.StringValidator;

import static eu.goodlike.tbr.validate.Validate.string;

public final class CommonValidators {

    /**
     * Validator to check if a string is null or blank
     */
    public static final StringValidator NOT_NULL_NOT_BLANK = string().not().isNull().not().isBlank();

    /**
     * Validator to check if a string is null or blank
     */
    public static final StringValidator NULL_OR_NOT_BLANK = string().isNull().or().not().isBlank();

    // PRIVATE

    private CommonValidators() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
