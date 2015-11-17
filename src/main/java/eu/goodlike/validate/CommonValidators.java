package eu.goodlike.validate;

import eu.goodlike.tbr.validate.impl.IntegerValidator;
import eu.goodlike.tbr.validate.impl.LongIntegerValidator;
import eu.goodlike.tbr.validate.impl.StringValidator;

import static eu.goodlike.misc.Constants.DEFAULT_VARCHAR_FIELD_SIZE;
import static eu.goodlike.tbr.validate.Validate.*;

/**
 * Hosts common validators that appear in my work (TO BE UPDATED!)
 */
public final class CommonValidators {

    /**
     * Validator to check if a string is null or blank
     */
    public static final StringValidator NOT_NULL_NOT_BLANK = string().not().isNull().not().isBlank();

    /**
     * Validator to check if a string is null or blank
     */
    public static final StringValidator NULL_OR_NOT_BLANK = string().isNull().or().not().isBlank();

    public static final StringValidator OPTIONAL_JSON_STRING =
            string().isNull().or().not().isBlank().isNoLargerThan(DEFAULT_VARCHAR_FIELD_SIZE);

    public static final StringValidator OPTIONAL_JSON_STRING_BLANK_ABLE =
            string().isNull().or().isNoLargerThan(DEFAULT_VARCHAR_FIELD_SIZE);

    public static final StringValidator MANDATORY_JSON_STRING = NOT_NULL_NOT_BLANK
            .isNoLargerThan(DEFAULT_VARCHAR_FIELD_SIZE);

    public static final StringValidator MANDATORY_JSON_STRING_BLANK_ABLE =
            string().not().isNull().isNoLargerThan(DEFAULT_VARCHAR_FIELD_SIZE);

    public static final IntegerValidator OPTIONAL_JSON_ID = integer().isNull().or().isAtLeast(1);

    public static final IntegerValidator MANDATORY_JSON_ID = integer().not().isNull().isAtLeast(1);

    public static final LongIntegerValidator OPTIONAL_JSON_LONG_ID = longInt().isNull().or().isAtLeast(1);

    public static final LongIntegerValidator MANDATORY_JSON_LONG_ID = longInt().not().isNull().isAtLeast(1);

    public static final LongIntegerValidator OPTIONAL_TIMESTAMP = longInt().isNull().or().isAtLeast(0);

    public static final LongIntegerValidator MANDATORY_TIMESTAMP = longInt().not().isNull().isAtLeast(0);

    // PRIVATE

    private CommonValidators() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
