package eu.goodlike.validate.impl;

import com.google.common.collect.Lists;
import eu.goodlike.neat.Null;
import eu.goodlike.validate.ComparableValidator;

import java.util.List;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static eu.goodlike.validate.Validate.Collections.chars;
import static eu.goodlike.validate.Validate.Collections.codePoints;
import static eu.goodlike.validate.Validate.*;
import static java.util.stream.Collectors.toList;

/**
 * Validator implementation for String
 */
public final class StringValidator extends ComparableValidator<String, StringValidator> {

    /**
     * Adds a predicate which tests if the string being validated passes the given predicate as list of characters
     * @throws NullPointerException if predicate is null
     */
    public StringValidator passesAsChars(Predicate<? super List<Character>> predicate) {
        return passesAs(Lists::charactersOf, predicate);
    }

    /**
     * Adds a predicate which tests if the string being validated passes the given predicate as list of code points
     * @throws NullPointerException if predicate is null
     */
    public StringValidator passesAsCodePoints(Predicate<? super List<Integer>> predicate) {
        return passesAs(csq -> csq.codePoints().boxed().collect(toList()), predicate);
    }

    /**
     * Adds a predicate which tests if the string being validated is empty
     */
    public StringValidator isEmpty() {
        return registerCondition(String::isEmpty);
    }

    /**
     * Adds a predicate which tests if the string being validated has a specific amount of characters
     */
    public StringValidator hasChars(int amount) {
        return registerCondition(str -> str.length() == amount);
    }

    /**
     * Adds a predicate which tests if the string being validated has at least a specific amount of characters
     */
    public StringValidator hasAtLeastChars(int amount) {
        return registerCondition(str -> str.length() >= amount);
    }

    /**
     * Adds a predicate which tests if the string being validated has at most a specific amount of characters
     */
    public StringValidator hasAtMostChars(int amount) {
        return registerCondition(str -> str.length() <= amount);
    }

    /**
     * Adds a predicate which tests if the string being validated has less than a specific amount of characters
     */
    public StringValidator hasLessThanChars(int amount) {
        return registerCondition(str -> str.length() < amount);
    }

    /**
     * Adds a predicate which tests if the string being validated has more than a specific amount of characters
     */
    public StringValidator hasMoreThanChars(int amount) {
        return registerCondition(str -> str.length() > amount);
    }

    /**
     * Adds a predicate which tests if the string being validated has a specific amount of code points
     */
    public StringValidator hasCodePoints(int amount) {
        return passesAsCodePoints(codePoints().hasSizeOf(amount));
    }

    /**
     * Adds a predicate which tests if the string being validated has at least a specific amount of code points
     */
    public StringValidator hasAtLeastCodePoints(int amount) {
        return passesAsCodePoints(codePoints().hasSizeAtLeast(amount));
    }

    /**
     * Adds a predicate which tests if the string being validated has at most a specific amount of code points
     */
    public StringValidator hasAtMostCodePoints(int amount) {
        return passesAsCodePoints(codePoints().hasSizeAtMost(amount));
    }

    /**
     * Adds a predicate which tests if the string being validated has less than a specific amount of code points
     */
    public StringValidator hasLessThanCodePoints(int amount) {
        return passesAsCodePoints(codePoints().hasSizeSmallerThan(amount));
    }

    /**
     * Adds a predicate which tests if the string being validated has more than a specific amount of code points
     */
    public StringValidator hasMoreThanCodePoints(int amount) {
        return passesAsCodePoints(codePoints().hasSizeLargerThan(amount));
    }

    /**
     * Adds a predicate which tests if the string being validated contains a sub sequence of given string
     */
    public StringValidator contains(String subSequence) {
        return registerCondition(str -> str.contains(subSequence));
    }

    /**
     * Adds a predicate which tests if the string being validated contains only whitespace
     */
    public StringValidator isBlank() {
        return passesAsCodePoints(codePoints().allMatch(Character::isWhitespace));
    }

    /**
     * Refer to StingValidator::startsEndsOrContainsConsecutive
     */
    public StringValidator startsEndsOrContainsConsecutive(String pattern) {
        return registerCondition(str -> startsEndsOrContainsConsecutive(str, pattern));
    }

    /**
     * Refer to StingValidator::isSimpleEmail
     */
    public StringValidator isSimpleEmail() {
        return registerCondition(StringValidator::isSimpleEmail);
    }

    /**
     * Refer to StingValidator::isInteger
     */
    public StringValidator isInteger() {
        return registerCondition(StringValidator::isInteger);
    }

    /**
     * Refer to StingValidator::isInt
     */
    public StringValidator isInt() {
        return registerCondition(StringValidator::isInt);
    }

    /**
     * Refer to StingValidator::isInt; also adds an additional check for the parsed int
     */
    public StringValidator isInt(IntPredicate customCheck) {
        Null.check(customCheck).ifAny("Cannot be null: customCheck");
        return registerConditions(StringValidator::isInt, str -> customCheck.test(Integer.parseInt(str)));
    }

    /**
     * Refer to StingValidator::isLong
     */
    public StringValidator isLong() {
        return registerCondition(StringValidator::isLong);
    }

    /**
     * Refer to StingValidator::isInt; also adds an additional check for the parsed int
     */
    public StringValidator isLong(LongPredicate customCheck) {
        Null.check(customCheck).ifAny("Cannot be null: customCheck");
        return registerConditions(StringValidator::isLong, str -> customCheck.test(Long.parseLong(str)));
    }

    /**
     * Refer to StingValidator::isCommaSeparatedListOfIntegers
     */
    public StringValidator isCommaSeparatedListOfIntegers() {
        return registerCondition(StringValidator::isCommaSeparatedListOfIntegers);
    }

    /**
     * Refer to StingValidator::isDate
     */
    public StringValidator isDate() {
        return registerCondition(StringValidator::isDate);
    }

    /**
     * ASSUMES string has been checked for null/blank
     * @return true if given string starts, ends or contains consecutive given pattern, false otherwise
     * @throws NullPointerException if string or pattern is null
     */
    public static boolean startsEndsOrContainsConsecutive(String string, String pattern) {
        Null.check(pattern).ifAny("Pattern cannot be null");
        return string.startsWith(pattern) || string.endsWith(pattern) || string.contains(pattern + pattern);
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
     * @return true if string is email (for most cases), false otherwise
     * @throws NullPointerException if string is null
     */
    public static boolean isSimpleEmail(String email) {
        int index = email.indexOf('@');
        return index >= 0
                && SUB_EMAIL_VALIDATOR.isValid(email.substring(0, index))
                && SUB_EMAIL_VALIDATOR.isValid(email.substring(index + 1));
    }

    /**
     * ASSUMES string has been checked for null/blank
     * @return true if string is made only of chars '0' to '9', with an optional prefix of '-' or '+'
     * @throws NullPointerException if string is null
     */
    public static boolean isInteger(String string) {
        if (Prefix.forString(string).anyPrefix())
            string = string.substring(1);
        return string.codePoints().allMatch(codePoint().isSimpleDigit());
    }

    /**
     * ASSUMES string has been checked for null/blank
     * @return true if string is made only of chars '0' to '9', with an optional prefix of '-' or '+' AND can be parsed as int
     * @throws NullPointerException if string is null
     */
    public static boolean isInt(String string) {
        return isInteger(string) && fitsIntoInt(string);
    }

    /**
     * ASSUMES string has been checked for null/blank
     * @return true if string is made only of chars '0' to '9', with an optional prefix of '-' or '+' AND can be parsed as long
     * @throws NullPointerException if string is null
     */
    public static boolean isLong(String string) {
        return isInteger(string) && fitsIntoLong(string);
    }

    /**
     * ASSUMES string has been checked for null/blank
     * @return true if string is a comma separated list of integers, false otherwise
     * @throws NullPointerException if string is null
     */
    public static boolean isCommaSeparatedListOfIntegers(String string) {
        return Stream.of(string.split(",")).allMatch(string().not().isBlank().isInteger());
    }

    /**
     * ASSUMES string has been checked for null/blank
     * @return true if string is a comma separated list of positive integers that pass given predicate, false otherwise
     * @throws NullPointerException if string or intPredicate is null
     */
    public static boolean isCommaSeparatedListOfIntegers(String string, IntPredicate intPredicate) {
        Null.check(intPredicate).ifAny("IntPredicate cannot be null");
        return Stream.of(string.split(",")).allMatch(string().not().isBlank().isInt(intPredicate));
    }

    /**
     * <pre>
     * ASSUMES string has been checked for null/blank
     *
     * This checks if the String can be parsed by LocalDate using DateTimeFormatter.ISO_LOCAL_DATE
     * </pre>
     * @return true if string is a date of format DateTimeFormatter.ISO_LOCAL_DATE, false otherwise
     */
    public static boolean isDate(String string) {
        Prefix prefix = Prefix.forString(string);
        if (prefix.anyPrefix())
            string = string.substring(1);

        int firstDash = string.indexOf("-");
        if (firstDash < 0)
            return false;

        String year = string.substring(0, firstDash);
        if (prefix.noPrefix() && year.length() != 4
                || prefix.isPositive() && year.length() < 5
                || prefix.isNegative() && (year.length() < 4 || ONLY_ZERO.test(year)))
            return false;

        String remains = string.substring(firstDash + 1);
        int secondDash = remains.indexOf("-");
        if (secondDash < 0)
            return false;

        String month = remains.substring(0, secondDash);
        String day = remains.substring(secondDash + 1);
        return YEAR_VALIDATOR.isValid(year)
                && MONTH_VALIDATOR.isValid(month)
                && dayValidator(year, month).isValid(day);
    }

    // CONSTRUCTORS

    public StringValidator() {
        super();
    }

    protected StringValidator(Predicate<String> mainCondition, Predicate<String> accumulatedCondition, boolean negateNext) {
        super(mainCondition, accumulatedCondition, negateNext);
    }

    // PROTECTED

    @Override
    protected StringValidator thisValidator() {
        return this;
    }

    @Override
    protected StringValidator newValidator(Predicate<String> mainCondition, Predicate<String> accumulatedCondition, boolean negateNext) {
        return new StringValidator(mainCondition, accumulatedCondition, negateNext);
    }

    // PRIVATE

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

        private boolean anyPrefix() {
            return !noPrefix();
        }

        private static Prefix forString(String string) {
            return string.startsWith("+")
                    ? PLUS
                    : string.startsWith("-") ? MINUS : NEITHER;
        }
    }

    private static final StringValidator SUB_EMAIL_VALIDATOR = string()
            .not().isBlank()
            .not().contains("@")
            .not().startsEndsOrContainsConsecutive(".");

    private static final StringValidator ONLY_ZERO = string().passesAsChars(chars().allMatch(c -> c == '0'));

    private static final StringValidator YEAR_VALIDATOR = string().not().isBlank()
            .isInt(aPrimInt().isBetween(0, 999999999));

    private static final StringValidator DATE_PART_VALIDATOR = string().not().isBlank()
            .hasAtLeastChars(2);

    private static final StringValidator MONTH_VALIDATOR = DATE_PART_VALIDATOR
            .isInt(aPrimInt().isMonthOfYear());

    private static StringValidator dayValidator(String year, String month) {
        return DATE_PART_VALIDATOR.isInt(aPrimInt().isDayOfMonth(Integer.parseInt(year), Integer.parseInt(month)));
    }

    private static final String MIN_INT = String.valueOf(Integer.MIN_VALUE).substring(1);
    private static final String MAX_INT = String.valueOf(Integer.MAX_VALUE);

    private static final String MIN_LONG = String.valueOf(Long.MIN_VALUE).substring(1);
    private static final String MAX_LONG = String.valueOf(Long.MAX_VALUE);

    private static boolean fitsIntoInt(String string) {
        return fitsInto(string, MIN_INT, MAX_INT);
    }

    private static boolean fitsIntoLong(String string) {
        return fitsInto(string, MIN_LONG, MAX_LONG);
    }

    private static boolean fitsInto(String string, String min, String max) {
        Prefix prefix = Prefix.forString(string);
        if (prefix.isNegative()) {
            string = string.substring(1);
            return string.length() < min.length()
                    || string.length() == min.length() && string.compareTo(min) <= 0;
        }

        if (prefix.isPositive())
            string = string.substring(1);

        return string.length() < max.length()
                || string.length() == max.length() && string.compareTo(max) <= 0;
    }

}
