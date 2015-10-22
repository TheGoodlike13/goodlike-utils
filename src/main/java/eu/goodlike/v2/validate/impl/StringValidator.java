package eu.goodlike.v2.validate.impl;

import eu.goodlike.v2.validate.Validate;

import java.util.function.Predicate;

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
     * Adds a predicate which tests if the string being validated has no more than limited amount of characters
     */
    public StringValidator isNoLargerThan(int limit) {
        return registerCondition(string -> string.length() <= limit);
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
     * @return true if string is a comma separated list of non-negative integers, false otherwise
     */
    public static boolean isCommaSeparatedListOfIntegers(String string) {
        return !startsEndsOrContainsConsecutive(string, ",")
                && string.chars().allMatch(codePoint().isDigit().or().isEqual('-'));
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

}
