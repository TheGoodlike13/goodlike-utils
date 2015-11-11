package eu.goodlike.v2.validate.impl;

import eu.goodlike.v2.validate.Validate;

import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static eu.goodlike.misc.Constants.NOT_NULL_NOT_BLANK;

/**
 * String validator implementation
 */
public final class StringValidator extends Validate<String, StringValidator> {

    /**
     * Adds a predicate which tests if the string being validated is empty
     */
    public StringValidator isEmpty() {
        return registerCondition(String::isEmpty);
    }

    /**
     * Adds a predicate which tests if the string being validated has no less than limited amount of characters
     */
    public StringValidator isNoSmallerThan(int limit) {
        return registerCondition(string -> string.length() >= limit);
    }

    /**
     * Adds a predicate which tests if the string being validated has no more than limited amount of characters
     */
    public StringValidator isNoLargerThan(int limit) {
        return registerCondition(string -> string.length() <= limit);
    }

    /**
     * Adds a predicate which tests if the string being validated has an exact amount of characters
     */
    public StringValidator isExactlyOfSize(int size) {
        return registerCondition(string -> string.length() == size);
    }

    /**
     * Adds a predicate which tests if the string being validated has no more than limited amount of code points
     */
    public StringValidator containsNoMoreUTFCharsThan(int limit) {
        return registerCondition(string -> string.chars().count() <= limit);
    }

    /**
     * Adds a predicate which tests if the string being validated contains only whitespace
     */
    public StringValidator isBlank() {
        return registerCondition(string -> string.chars().allMatch(Character::isWhitespace));
    }

    /**
     * Adds a predicate which tests if the string being validated is e-mail; this check is not conclusive, but should
     * get rid of most obvious mistakes
     */
    public StringValidator isEmail() {
        return registerCondition(StringValidator::isEmail);
    }

    /**
     * Adds a predicate which tests if the string being validated is a comma separated list of positive numbers
     */
    public StringValidator isCommaSeparatedListOfIntegers() {
        return registerCondition(StringValidator::isCommaSeparatedListOfIntegers);
    }

    /**
     * Adds a predicate which tests if the string being validated is a date of format YYYY-MM-DD; more specifically, if
     * it can be parsed using DateTimeFormatter.ISO_LOCAL_DATE
     */
    public StringValidator isDate() {
        return registerCondition(StringValidator::isDate);
    }

    /**
     * Adds a predicate which tests if the string being validated is an integer; more specifically, it tests if
     * Integer.parseInt() would pass
     */
    public StringValidator isInteger() {
        return registerCondition(StringValidator::isInteger);
    }

    /**
     * Adds a predicate which tests if the string being validated is an integer that also passes custom IntPredicate test;
     * more specifically, it tests if  Integer.parseInt() would pass, and then checks if resulting int would pass
     * given predicate
     */
    public StringValidator isInteger(IntPredicate custom) {
        return registerCondition(str -> StringValidator.isInteger(str) && custom.test(Integer.parseInt(str)));
    }

    /**
     * <pre>
     * ASSUMES string has been checked for null/blank
     *
     * Does some basic checking to filter out non e-mails; the following form is considered:
     *      PREFIX@DOMAIN
     * both PREFIX and DOMAIN
     *      1) cannot be empty
     *      2) cannot contain @
     *      3) cannot contain whitespace
     *      4) cannot start with a dot, end with a dot or contain consecutive dots
     * While some of these conditions are allowed (when using "" to wrap around the special characters), they are
     * too rare to bother (and usually not allowed by e-mail providers anyway)
     *
     * Consider a third party e-mail check if you need different functionality
     * </pre>
     */
    public static boolean isEmail(String email) {
        int index = email.indexOf('@');
        if (index < 0)
            return false;

        String prefix = email.substring(0, index);
        String domain = email.substring(index + 1);
        return !(prefix.isEmpty() || domain.isEmpty()
                || prefix.contains("@") || domain.contains("@")
                || prefix.chars().anyMatch(Character::isWhitespace)
                || domain.chars().anyMatch(Character::isWhitespace)
                || startsEndsOrContainsConsecutive(prefix, ".")
                || startsEndsOrContainsConsecutive(domain, "."));
    }

    /**
     * ASSUMES string has been checked for null/blank
     * @return true if given string starts, ends or contains consecutive given pattern, false otherwise
     */
    public static boolean startsEndsOrContainsConsecutive(String string, String pattern) {
        return string.startsWith(pattern) || string.endsWith(pattern) || string.contains(pattern + pattern);
    }

    /**
     * ASSUMES string has been checked for null/blank
     * @return true if string is a comma separated list of positive integers, false otherwise
     */
    public static boolean isCommaSeparatedListOfIntegers(String string) {
        return Stream.of(string.split(",")).allMatch(string().not().isBlank().isInteger(Int().isMoreThan(0)));
    }

    /**
     * <pre>
     * ASSUMES string has been checked for null/blank
     *
     * This checks if the String can be parsed by LocalDate using DateTimeFormatter.ISO_LOCAL_DATE
     * </pre>
     * @return true if string is a date of format YYYY-MM-DD, false otherwise
     */
    public static boolean isDate(String string) {
        Prefix prefix = Prefix.forString(string);
        if (!prefix.noPrefix())
            string = string.substring(1);

        int firstDash = string.indexOf("-");
        if (firstDash < 0)
            return false;

        String year = string.substring(0, firstDash);
        if (prefix.noPrefix() && year.length() != 4
            || prefix.isPositive() && year.length() < 5
            || prefix.isNegative() && year.length() < 4)
            return false;

        String remains = string.substring(firstDash + 1);
        int secondDash = remains.indexOf("-");
        if (secondDash < 0)
            return false;

        String month = remains.substring(0, secondDash);
        String day = remains.substring(secondDash + 1);
        return NOT_NULL_NOT_BLANK.isInteger(Int().passes(i -> i != 0 || !prefix.isNegative()).isBetween(0, 999999999)).test(year)
                && NOT_NULL_NOT_BLANK.isNoSmallerThan(2).isInteger(Int().isMonthOfYear()).test(month)
                && NOT_NULL_NOT_BLANK.isNoSmallerThan(2).isInteger(Int().isDayOfMonth(Integer.parseInt(year), Integer.parseInt(month))).test(day);
    }

    public static boolean isInteger(String string) {
        if (!Prefix.forString(string).noPrefix())
            string = string.substring(1);
        return string.chars().allMatch(codePoint().isSimpleDigit());
    }

    // CONSTRUCTORS

    public StringValidator() {
        this(null, null, null, false);
    }

    protected StringValidator(StringValidator outerValidator, Predicate<String> mainCondition, Predicate<String> accumulatedCondition, boolean notCondition) {
        super(outerValidator, mainCondition, accumulatedCondition, notCondition);
    }

    // PROTECTED

    @Override
    protected StringValidator thisValidator() {
        return this;
    }

    @Override
    protected StringValidator newValidator(StringValidator outerValidator, Predicate<String> mainCondition, Predicate<String> accumulatedCondition, boolean notCondition) {
        return new StringValidator(outerValidator, mainCondition, accumulatedCondition, notCondition);
    }

    private enum Prefix {
        PLUS, MINUS, NEITHER;

        private boolean isPositive() {
            return this == PLUS;
        }

        private boolean isNegative() {
            return this == MINUS;
        }

        private boolean noPrefix() {
            return this == NEITHER;
        }

        private static Prefix forString(String string) {
            return string.startsWith("+")
                    ? PLUS
                    : string.startsWith("-") ? MINUS : NEITHER;
        }
    }

}
