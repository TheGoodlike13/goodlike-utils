package eu.goodlike.tbr.validate.impl;

import com.google.common.primitives.Ints;
import eu.goodlike.neat.Null;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import java.util.Collection;
import java.util.function.*;

/**
 * <pre>
 * Primitive int validator implementation; also can be used to validate code points (which are ints)
 *
 * Primitive and boxed versions are separate, because IntPredicate is not compatible with Predicate of Integer; more
 * specifically, their methods or() and and() require those two different kind of predicates; as a result, there is no
 * way to decide which is which by just looking at the lambda expressions
 * </pre>
 */
public final class IntValidator implements IntPredicate {

    /**
     * Adds a custom predicate for validating integers
     */
    public IntValidator passes(IntPredicate customPredicate) {
        return registerCondition(customPredicate);
    }

    /**
     * Adds a predicate which tests if the integer being validated is contained by given collection
     * @throws NullPointerException if collection is null
     */
    public IntValidator isContainedIn(Collection<Integer> values) {
        Null.check(values).ifAny("Values collection cannot be null");
        return registerCondition(values::contains);
    }

    /**
     * Adds a predicate which tests if the integer being validated is contained by given array
     * @throws NullPointerException if array is null
     */
    public IntValidator isContainedIn(int... values) {
        Null.checkAlone(values).ifAny("Values array cannot be null");
        return isContainedIn(Ints.asList(values));
    }

    /**
     * Does nothing, only useful for readability
     * @throws IllegalStateException if and() is used before any condition, i.e. Int().and()...
     */
    public IntValidator and() {
        if (!hasAccumulatedAnyConditions())
            throw new IllegalStateException("There must be at least a single condition before every and()");

        return this;
    }

    /**
     * Accumulates all predicates before this or() that haven't been accumulated previously using && operator, then
     * adds it to the previously accumulated condition (if such exists) using || operator
     * @throws IllegalStateException if or() is used before any condition, i.e. Int().or()...
     */
    public IntValidator or() {
        if (!hasAccumulatedAnyConditions())
            throw new IllegalStateException("There must be at least a single condition before every or()");

        return new IntValidator(outerValidator, mainCondition(), null, false);
    }

    /**
     * <pre>
     * Sets the next registered condition to be negated
     *
     * Registered conditions are basically every method call that performs a boolean test, including brackets
     * </pre>
     */
    public IntValidator not() {
        return new IntValidator(outerValidator, mainCondition, accumulatedCondition, !notCondition);
    }

    /**
     * <pre>
     * Simulates opening brackets which allows for
     *      p1 && (p2 || p3)
     * which would be interpreted as
     *      (p1 && p2) || p3
     * without brackets
     * </pre>
     */
    public IntValidator openBracket() {
        return new IntValidator(this, null, null, false);
    }

    /**
     * <pre>
     * Simulates closing brackets which allows for
     *      p1 && (p2 || p3)
     * which would be interpreted as
     *      (p1 && p2) || p3
     * without brackets
     *
     * You can skip calling closeBracket() before terminating the validator, it will be called automatically
     * </pre>
     * @throws IllegalStateException if closeBracket() is called before openBracket()
     * @throws IllegalStateException if there are no conditions between openBracket() and closeBracket()
     */
    public IntValidator closeBracket() {
        if (!hasOuterValidator())
            throw new IllegalStateException("You must use openBracket() before using closeBracket()");

        return outerValidator.registerCondition(collapseCondition());
    }

    /**
     * Closes all brackets, if any, and evaluates constructed predicate for given integer
     * @return true if integer passes the predicate test, false otherwise
     * @throws IllegalStateException if there are no conditions at all, or when closing brackets
     */
    @Override
    public boolean test(int integer) {
        return hasOuterValidator()
                ? closeBracket().test(integer)
                : collapseCondition().test(integer);
    }

    /**
     * @return true if integer does not pass the predicate test, false otherwise
     * @throws IllegalStateException if there are no conditions at all, or when closing brackets
     */
    public boolean isInvalid(int integer) {
        return !test(integer);
    }

    /**
     * @return validator actor, which allows specifying an action if the object is invalid
     */
    public final IntValidationActor ifInvalid(int object) {
        return IntValidationActor.of(object, this);
    }

    /**
     * Executes an arbitrary action if and only if the given integer is NOT valid
     * @throws NullPointerException if invalidAction is null
     * @throws IllegalStateException if there are no conditions at all, or when closing brackets
     */
    public IntValidator ifInvalid(int integer, Runnable invalidAction) {
        Null.check(invalidAction).ifAny("Action cannot be null");
        if (isInvalid(integer))
            invalidAction.run();
        return this;
    }

    /**
     * Executes an action using the object if and only if the given integer is NOT valid
     * @throws NullPointerException if invalidConsumer is null
     * @throws IllegalStateException if there are no conditions at all, or when closing brackets
     */
    public IntValidator ifInvalid(int integer, IntConsumer invalidConsumer) {
        Null.check(invalidConsumer).ifAny("Consumer cannot be null");
        if (isInvalid(integer))
            invalidConsumer.accept(integer);
        return this;
    }

    /**
     * Throws an arbitrary exception if and only if the given integer is NOT valid
     * @throws NullPointerException if exceptionSupplier is null
     * @throws IllegalStateException if there are no conditions at all, or when closing brackets
     */
    public <X extends Throwable> IntValidator ifInvalidThrow(int integer, Supplier<? extends X> exceptionSupplier) throws X {
        Null.check(exceptionSupplier).ifAny("Exception supplier cannot be null");
        if (isInvalid(integer))
            throw exceptionSupplier.get();
        return this;
    }

    /**
     * Throws an exception using the object if and only if the given integer is NOT valid
     * @throws NullPointerException if exceptionSupplier is null
     * @throws IllegalStateException if there are no conditions at all, or when closing brackets
     */
    public <X extends Throwable> IntValidator ifInvalidThrow(int integer, IntFunction<? extends X> exceptionSupplier) throws X {
        Null.check(exceptionSupplier).ifAny("Exception supplier cannot be null");
        if (isInvalid(integer))
            throw exceptionSupplier.apply(integer);
        return this;
    }

    /**
     * Adds a predicate which tests if the integer being validated is larger than some amount
     */
    public IntValidator isMoreThan(int amount) {
        return registerCondition(i -> i > amount);
    }

    /**
     * Adds a predicate which tests if the integer being validated is smaller than some amount
     */
    public IntValidator isLessThan(int amount) {
        return registerCondition(i -> i < amount);
    }

    /**
     * Adds a predicate which tests if the integer being validated is larger or equal to some amount
     */
    public IntValidator isAtLeast(int amount) {
        return registerCondition(i -> i >= amount);
    }

    /**
     * Adds a predicate which tests if the integer being validated is smaller or equal to some amount
     */
    public IntValidator isAtMost(int amount) {
        return registerCondition(i -> i <= amount);
    }

    /**
     * Adds a predicate which tests if the integer being validated is equal to some amount
     */
    public IntValidator isEqual(int amount) {
        return registerCondition(i -> i == amount);
    }

    /**
     * Adds a predicate which tests if the integer being validated is between some two numbers, both inclusive
     */
    public IntValidator isBetween(int lowBound, int highBound) {
        return registerCondition(i -> i >= lowBound && i <= highBound);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the integer can describe a day of the month
     *
     * This is equivalent to isBetween(1, 31)
     *
     * While certain months do not have days 29 to 31, it is assumed that this is handled somewhere else
     * entirely; the purpose of this is simply to validate the input
     * </pre>
     */
    public IntValidator isDayOfMonth() {
        return isBetween(1, 31);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the integer can describe an hour of day
     *
     * This is equivalent to isBetween(0, 23)
     * </pre>
     */
    public IntValidator isHourOfDay() {
        return isBetween(0, 23);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the integer can describe a minute of hour
     *
     * This is equivalent to isBetween(0, 59)
     * </pre>
     */
    public IntValidator isMinuteOfHour() {
        return isBetween(0, 59);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the integer can describe a month of year
     *
     * This is equivalent to isBetween(1, 12)
     * </pre>
     */
    public IntValidator isMonthOfYear() {
        return isBetween(1, 12);
    }

    /**
     * Adds a predicate which tests if the integer is a valid day at a certain year and month; it is assumed
     * year and month values are already validated
     */
    public IntValidator isDayOfMonth(int year, int month) {
        ValueRange dayRange = ChronoField.DAY_OF_MONTH.rangeRefinedBy(LocalDate.of(year, month, 1));
        return registerCondition(dayRange::isValidIntValue);
    }

    /**
     * This method treats the integer as a code point; it checks if the integer is representing a digit between 0 and 9
     */
    public IntValidator isSimpleDigit() {
        return registerCondition(i -> i >= '0' && i <= '9');
    }

    /**
     * This method treats the integer as a code point; please refer to equivalent Character method for documentation
     */
    public IntValidator isWhitespace() {
        return registerCondition(Character::isWhitespace);
    }

    /**
     * This method treats the integer as a code point; please refer to equivalent Character method for documentation
     */
    public IntValidator isDigit() {
        return registerCondition(Character::isDigit);
    }

    /**
     * This method treats the integer as a code point; please refer to equivalent Character method for documentation
     */
    public IntValidator isAlphabetic() {
        return registerCondition(Character::isAlphabetic);
    }

    /**
     * This method treats the integer as a code point; please refer to equivalent Character method for documentation
     */
    public IntValidator isBmpCodePoint() {
        return registerCondition(Character::isBmpCodePoint);
    }

    /**
     * This method treats the integer as a code point; please refer to equivalent Character method for documentation
     */
    public IntValidator isDefined() {
        return registerCondition(Character::isDefined);
    }

    /**
     * This method treats the integer as a code point; please refer to equivalent Character method for documentation
     */
    public IntValidator isIdentifierIgnorable() {
        return registerCondition(Character::isIdentifierIgnorable);
    }

    /**
     * This method treats the integer as a code point; please refer to equivalent Character method for documentation
     */
    public IntValidator isIdeographic() {
        return registerCondition(Character::isIdeographic);
    }

    /**
     * This method treats the integer as a code point; please refer to equivalent Character method for documentation
     */
    public IntValidator isISOControl() {
        return registerCondition(Character::isISOControl);
    }

    /**
     * This method treats the integer as a code point; please refer to equivalent Character method for documentation
     */
    public IntValidator isJavaIdentifierPart() {
        return registerCondition(Character::isJavaIdentifierPart);
    }

    /**
     * This method treats the integer as a code point; please refer to equivalent Character method for documentation
     */
    public IntValidator isJavaIdentifierStart() {
        return registerCondition(Character::isJavaIdentifierStart);
    }

    /**
     * This method treats the integer as a code point; please refer to equivalent Character method for documentation
     */
    public IntValidator isLetter() {
        return registerCondition(Character::isLetter);
    }

    /**
     * This method treats the integer as a code point; please refer to equivalent Character method for documentation
     */
    public IntValidator isLetterOrDigit() {
        return registerCondition(Character::isLetterOrDigit);
    }

    /**
     * This method treats the integer as a code point; please refer to equivalent Character method for documentation
     */
    public IntValidator isLowerCase() {
        return registerCondition(Character::isLowerCase);
    }

    /**
     * This method treats the integer as a code point; please refer to equivalent Character method for documentation
     */
    public IntValidator isMirrored() {
        return registerCondition(Character::isMirrored);
    }

    /**
     * This method treats the integer as a code point; please refer to equivalent Character method for documentation
     */
    public IntValidator isSpaceChar() {
        return registerCondition(Character::isSpaceChar);
    }

    /**
     * This method treats the integer as a code point; please refer to equivalent Character method for documentation
     */
    public IntValidator isSupplementaryCodePoint() {
        return registerCondition(Character::isSupplementaryCodePoint);
    }

    /**
     * This method treats the integer as a code point; please refer to equivalent Character method for documentation
     */
    public IntValidator isTitleCase() {
        return registerCondition(Character::isTitleCase);
    }

    /**
     * This method treats the integer as a code point; please refer to equivalent Character method for documentation
     */
    public IntValidator isUnicodeIdentifierPart() {
        return registerCondition(Character::isUnicodeIdentifierPart);
    }

    /**
     * This method treats the integer as a code point; please refer to equivalent Character method for documentation
     */
    public IntValidator isUnicodeIdentifierStart() {
        return registerCondition(Character::isUnicodeIdentifierStart);
    }

    /**
     * This method treats the integer as a code point; please refer to equivalent Character method for documentation
     */
    public IntValidator isUpperCase() {
        return registerCondition(Character::isUpperCase);
    }

    /**
     * This method treats the integer as a code point; please refer to equivalent Character method for documentation
     */
    public IntValidator isValidCodePoint() {
        return registerCondition(Character::isValidCodePoint);
    }

    /**
     * Adds a predicate which checks if the integer being validated is equal to given character's code point
     */
    public IntValidator isCodePointOf(char c) {
        return isEqual(c);
    }

    /**
     * Adds a predicate which checks if the integer being validated is equal to given characters' code point
     * @throws IllegalArgumentException if given chars do not represent a code point
     */
    public IntValidator isCodePointOf(char high, char low) {
        if (!Character.isSurrogatePair(high, low))
            throw new IllegalArgumentException("Given chars do not represent a code point: " + high + low);

        return isEqual(Character.toCodePoint(high, low));
    }

    /**
     * Adds a predicate which checks if the integer being validated is equal to given character's code point
     * @throws NullPointerException if singleUnicodeChar is null
     * @throws IllegalArgumentException if given string has more than one code point
     */
    public IntValidator isCodePointOf(String singleUnicodeChar) {
        Null.check(singleUnicodeChar).ifAny("Unicode char cannot be null");
        if (singleUnicodeChar.chars().count() > 1)
            throw new IllegalArgumentException("The given string can only contain a single unicode char");

        return isEqual(Character.codePointAt(singleUnicodeChar, 0));
    }

    /**
     * @return this validator as just a Predicate
     */
    public Predicate<Integer> asPredicate() {
        return this::test;
    }

    /**
     * @return this validator as just a IntPredicate
     */
    public IntPredicate asIntPredicate() {
        return this;
    }

    // CONSTRUCTORS

    public IntValidator() {
        this(null, null, null, false);
    }

    private IntValidator(IntValidator outerValidator, IntPredicate mainCondition, IntPredicate accumulatedCondition, boolean notCondition) {
        this.outerValidator = outerValidator;
        this.mainCondition = mainCondition;
        this.accumulatedCondition = accumulatedCondition;
        this.notCondition = notCondition;
    }

    // PRIVATE

    private final IntValidator outerValidator;
    private final IntPredicate mainCondition;
    private final IntPredicate accumulatedCondition;
    private final boolean notCondition;

    /**
     * <pre>
     * Adds a predicate to subCondition list, negating if not() was called before this method
     *
     * DO NOT use this method more than once per method call, as this will cause not() to malfunction
     * </pre>
     */
    private IntValidator registerCondition(IntPredicate predicate) {
        Null.check(predicate).ifAny("Registered predicate cannot be null");
        if (notCondition)
            predicate = predicate.negate();
        return new IntValidator(outerValidator, mainCondition,
                accumulatedCondition == null ? predicate : accumulatedCondition.and(predicate), false);
    }

    /**
     * @return true if this validator is used for bracket simulation, false otherwise
     */
    private boolean hasOuterValidator() {
        return outerValidator != null;
    }

    private boolean hasAccumulatedAnyConditions() {
        return accumulatedCondition != null;
    }

    private IntPredicate collapseCondition() {
        IntPredicate condition = hasAccumulatedAnyConditions() ? mainCondition() : mainCondition;

        if (condition == null)
            throw new IllegalStateException("You must have at least one condition total, or between openBracket() and closeBracket()");

        return condition;
    }

    private IntPredicate mainCondition() {
        return mainCondition == null ? accumulatedCondition : mainCondition.or(accumulatedCondition);
    }

    public static final class IntValidationActor {

        /**
         * Executes an arbitrary action if and only if an invalid value is passed
         * @throws NullPointerException if someAction is null
         */
        public IntValidator thenDo(Runnable someAction) {
            return validator.ifInvalid(value, someAction);
        }

        /**
         * Consumes the value if and only if an invalid value is passed
         * @throws NullPointerException if valueConsumer is null
         */
        public IntValidator thenDo(IntConsumer valueConsumer) {
            return validator.ifInvalid(value, valueConsumer);
        }

        /**
         * Throws an arbitrary exception if and only if an invalid value is passed
         * @throws NullPointerException if exceptionSupplier is null
         */
        public <X extends Throwable> IntValidator thenThrow(Supplier<X> exceptionSupplier) throws X {
            return validator.ifInvalidThrow(value, exceptionSupplier);
        }

        /**
         * Throws an exception using the value if and only if an invalid value is passed
         * @throws NullPointerException if exceptionFunction is null
         */
        public <X extends Throwable> IntValidator thenThrow(IntFunction<X> exceptionFunction) throws X {
            return validator.ifInvalidThrow(value, exceptionFunction);
        }

        // CONSTRUCTORS

        public static IntValidationActor of(int value, IntValidator validator) {
            Null.check(validator).ifAny("Validator cannot be null");
            return new IntValidationActor(value, validator);
        }

        private IntValidationActor(int value, IntValidator validator) {
            this.value = value;
            this.validator = validator;
        }

        // PRIVATE

        private final int value;
        private final IntValidator validator;

    }

}
