package eu.goodlike.misc;

/**
 * <pre>
 * Contains various constants, without association to any particular subject
 *
 * It was created to avoid multiple Utils classes just holding a single constant field
 * </pre>
 */
public final class Constants {

    /**
     * Default RestController "produces" and "consumes" value
     */
    public static final String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";

    /**
     * Default separator, used by MYSQL when doing group_concat()
     */
    public static final String MYSQL_GROUP_CONCAT_SEPARATOR = ",";

    /**
     * <pre>
     * Default value I choose to use for VARCHAR table fields when there is no good reason to reduce/increase it
     *
     * This value arises from utf8mb4 char size and primary key limit, which becomes exceeded at around 190 chars;
     * Since I sometimes had to use two VARCHAR columns, 90 felt better than 95, thus 180 seems better than 190 where
     * I only need 1 VARCHAR field
     * </pre>
     */
    public static final int DEFAULT_VARCHAR_FIELD_SIZE = 180;

    /**
     * Default value for "page" parameter in RestController methods
     */
    public static final int DEFAULT_PAGE = 0;

    /**
     * Default value for "per_page" parameter in RestController methods
     */
    public static final int DEFAULT_PER_PAGE = 25;

    // PRIVATE

    private Constants() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
