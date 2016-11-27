package eu.goodlike.validate;

import eu.goodlike.validate.impl.IntValidator;
import eu.goodlike.validate.impl.LongValidator;
import eu.goodlike.validate.impl.StringValidator;

import static eu.goodlike.misc.Constants.DEFAULT_VARCHAR_FIELD_SIZE;
import static eu.goodlike.validate.Validate.*;

/**
 * Common validators that appear in my work
 */
public final class CommonValidators {

    /**
     * Checks if String is not blank (useful when null has already been checked for)
     */
    public static final StringValidator NOT_BLANK = string().not().isBlank();

    /**
     * Checks if String is not null and not blank
     */
    public static final StringValidator NOT_NULL_NOT_BLANK = string().not().isNull().not().isBlank();

    /**
     * Checks if String is null, otherwise not blank
     */
    public static final StringValidator NULL_OR_NOT_BLANK = string().isNull().or().not().isBlank();

    /**
     * Checks if String is null, otherwise not blank and fits into DEFAULT_VARCHAR_FIELD_SIZE
     */
    public static final StringValidator OPTIONAL_JSON_STRING =
            string().isNull().or().not().isBlank().hasAtMostCodePoints(DEFAULT_VARCHAR_FIELD_SIZE);

    /**
     * Checks if String is null, otherwise fits into DEFAULT_VARCHAR_FIELD_SIZE
     */
    public static final StringValidator OPTIONAL_JSON_STRING_BLANK_ABLE =
            string().isNull().or().hasAtMostCodePoints(DEFAULT_VARCHAR_FIELD_SIZE);

    /**
     * Checks if String is not null, not blank and fits into DEFAULT_VARCHAR_FIELD_SIZE
     */
    public static final StringValidator MANDATORY_JSON_STRING = NOT_NULL_NOT_BLANK
            .hasAtMostCodePoints(DEFAULT_VARCHAR_FIELD_SIZE);

    /**
     * Checks if String is not null and fits into DEFAULT_VARCHAR_FIELD_SIZE
     */
    public static final StringValidator MANDATORY_JSON_STRING_BLANK_ABLE =
            string().not().isNull().hasAtMostCodePoints(DEFAULT_VARCHAR_FIELD_SIZE);

    /**
     * Checks if Integer is null, otherwise 1 or more
     */
    public static final IntValidator OPTIONAL_JSON_ID = anInt().isNull().or().isAtLeast(1);

    /**
     * Checks if Integer is not null and 1 or more
     */
    public static final IntValidator MANDATORY_JSON_ID = anInt().not().isNull().isAtLeast(1);

    /**
     * Checks if Long is null, otherwise 1 or more
     */
    public static final LongValidator OPTIONAL_JSON_LONG_ID = aLong().isNull().or().isAtLeast(1L);

    /**
     * Checks if Long is not null and 1 or more
     */
    public static final LongValidator MANDATORY_JSON_LONG_ID = aLong().not().isNull().isAtLeast(1L);

    /**
     * Checks if Long is null, otherwise a valid timestamp (>= 0)
     */
    public static final LongValidator OPTIONAL_TIMESTAMP = aLong().isNull().or().isAtLeast(0L);

    /**
     * Checks if Long is not null and a valid timestamp (>= 0)
     */
    public static final LongValidator MANDATORY_TIMESTAMP = aLong().not().isNull().isAtLeast(0L);

    // PRIVATE

    private CommonValidators() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
