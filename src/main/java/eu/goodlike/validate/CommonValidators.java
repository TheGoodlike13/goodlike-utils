package eu.goodlike.validate;

import eu.goodlike.validate.impl.IntObjValidator;
import eu.goodlike.validate.impl.LongObjValidator;
import eu.goodlike.validate.impl.StringValidator;

import static eu.goodlike.misc.Constants.DEFAULT_VARCHAR_FIELD_SIZE;
import static eu.goodlike.validate.Validate.Collections.codePoints;
import static eu.goodlike.validate.Validate.*;

/**
 * Common validators that appear in my work
 */
public final class CommonValidators {

    /**
     * Checks if String is not null and not blank
     */
    public static final StringValidator NOT_NULL_NOT_BLANK = str().not().isNull().not().isBlank();

    /**
     * Checks if String is null, otherwise not blank
     */
    public static final StringValidator NULL_OR_NOT_BLANK = str().isNull().or().not().isBlank();

    /**
     * Checks if String is null, otherwise not blank and fits into DEFAULT_VARCHAR_FIELD_SIZE
     */
    public static final StringValidator OPTIONAL_JSON_STRING =
            str().isNull().or().not().isBlank().passesAsCodePoints(codePoints().isAtMostSizeOf(DEFAULT_VARCHAR_FIELD_SIZE));

    /**
     * Checks if String is null, otherwise fits into DEFAULT_VARCHAR_FIELD_SIZE
     */
    public static final StringValidator OPTIONAL_JSON_STRING_BLANK_ABLE =
            str().isNull().or().passesAsCodePoints(codePoints().isAtMostSizeOf(DEFAULT_VARCHAR_FIELD_SIZE));

    /**
     * Checks if String is not null, not blank and fits into DEFAULT_VARCHAR_FIELD_SIZE
     */
    public static final StringValidator MANDATORY_JSON_STRING = NOT_NULL_NOT_BLANK
            .passesAsCodePoints(codePoints().isAtMostSizeOf(DEFAULT_VARCHAR_FIELD_SIZE));

    /**
     * Checks if String is not null and fits into DEFAULT_VARCHAR_FIELD_SIZE
     */
    public static final StringValidator MANDATORY_JSON_STRING_BLANK_ABLE =
            str().not().isNull().passesAsCodePoints(codePoints().isAtMostSizeOf(DEFAULT_VARCHAR_FIELD_SIZE));

    /**
     * Checks if Integer is null, otherwise 1 or more
     */
    public static final IntObjValidator OPTIONAL_JSON_ID = oInt().isNull().or().isAtLeast(1);

    /**
     * Checks if Integer is not null and 1 or more
     */
    public static final IntObjValidator MANDATORY_JSON_ID = oInt().not().isNull().isAtLeast(1);

    /**
     * Checks if Long is null, otherwise 1 or more
     */
    public static final LongObjValidator OPTIONAL_JSON_LONG_ID = oLong().isNull().or().isAtLeast(1L);

    /**
     * Checks if Long is not null and 1 or more
     */
    public static final LongObjValidator MANDATORY_JSON_LONG_ID = oLong().not().isNull().isAtLeast(1L);

    /**
     * Checks if Long is null, otherwise a valid timestamp (>= 0)
     */
    public static final LongObjValidator OPTIONAL_TIMESTAMP = oLong().isNull().or().isAtLeast(0L);

    /**
     * Checks if Long is not null and a valid timestamp (>= 0)
     */
    public static final LongObjValidator MANDATORY_TIMESTAMP = oLong().not().isNull().isAtLeast(0L);

    // PRIVATE

    private CommonValidators() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
