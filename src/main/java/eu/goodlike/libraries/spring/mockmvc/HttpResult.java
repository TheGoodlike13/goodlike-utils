package eu.goodlike.libraries.spring.mockmvc;

/**
 * <pre>
 * Enumerates possible scenarios for a MockMvc request
 *
 * OK is intended to be used when a body is expected, which verifies if it is of JSON type;
 * OK_NO_BODY skips the body type verification
 * BAD skips all verification and leaves it up to the user
 * </pre>
 */
public enum HttpResult {

    OK,               // returned OK with a JSON body
    OK_NO_BODY,     // returned OK without body
    BAD              // returned error

}
