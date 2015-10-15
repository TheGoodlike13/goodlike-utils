package eu.goodlike.v2.validate.impl;

import com.google.common.primitives.Ints;
import eu.goodlike.functional.Action;
import eu.goodlike.neat.Null;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
     */
    public IntValidator and() {
        return this;
    }

    /**
     * Accumulates all predicates before this or() that haven't been accumulated previously using && operator, then
     * adds it to the previously accumulated condition (if such exists) using || operator
     * @throws IllegalStateException if or() is used before any condition, i.e. Long().or()...
     */
    public IntValidator or() {
        if (subConditions.isEmpty())
            throw new IllegalStateException("There must be at least a single condition before every or()");

        return new IntValidator(outerValidator, mainCondition(), new ArrayList<>(), false);
    }

    /**
     * <pre>
     * Sets the next registered condition to be negated
     *
     * Registered conditions are basically every method call that performs a boolean test, including brackets
     * </pre>
     */
    public IntValidator not() {
        return new IntValidator(outerValidator, condition, subConditions, !notCondition);
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
        return new IntValidator(this, null, new ArrayList<>(), false);
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
     * Executes an arbitrary action if and only if the given integer is NOT valid
     * @throws NullPointerException if invalidAction is null
     * @throws IllegalStateException if there are no conditions at all, or when closing brackets
     */
    public void ifInvalid(int integer, Action invalidAction) {
        Null.check(invalidAction).ifAny("Action cannot be null");
        if (isInvalid(integer))
            invalidAction.doIt();
    }

    /**
     * Executes an action using the object if and only if the given integer is NOT valid
     * @throws NullPointerException if invalidConsumer is null
     * @throws IllegalStateException if there are no conditions at all, or when closing brackets
     */
    public void ifInvalid(int integer, IntConsumer invalidConsumer) {
        Null.check(invalidConsumer).ifAny("Consumer cannot be null");
        if (isInvalid(integer))
            invalidConsumer.accept(integer);
    }

    /**
     * Throws an arbitrary exception if and only if the given integer is NOT valid
     * @throws NullPointerException if exceptionSupplier is null
     * @throws IllegalStateException if there are no conditions at all, or when closing brackets
     */
    public <X extends Throwable> void ifInvalidThrow(int integer, Supplier<? extends X> exceptionSupplier) throws X {
        Null.check(exceptionSupplier).ifAny("Exception supplier cannot be null");
        if (isInvalid(integer))
            throw exceptionSupplier.get();
    }

    /**
     * Throws an exception using the object if and only if the given integer is NOT valid
     * @throws NullPointerException if exceptionSupplier is null
     * @throws IllegalStateException if there are no conditions at all, or when closing brackets
     */
    public <X extends Throwable> void ifInvalidThrow(int integer, IntFunction<? extends X> exceptionSupplier) throws X {
        Null.check(exceptionSupplier).ifAny("Exception supplier cannot be null");
        if (isInvalid(integer))
            throw exceptionSupplier.apply(integer);
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
        this(null, null, new ArrayList<>(), false);
    }

    private IntValidator(IntValidator outerValidator, IntPredicate condition, List<IntPredicate> subConditions, boolean notCondition) {
        this.outerValidator = outerValidator;
        this.condition = condition;
        this.subConditions = subConditions;
        this.notCondition = notCondition;
    }

    // PRIVATE

    private final IntValidator outerValidator;
    private final IntPredicate condition;
    private final List<IntPredicate> subConditions;
    private final boolean notCondition;

    /**
     * @return true if this validator is used for bracket simulation, false otherwise
     */
    private boolean hasOuterValidator() {
        return outerValidator != null;
    }

    /**
     * Adds a predicate to subCondition list, negating if not() was called before this method
     */
    private IntValidator registerCondition(IntPredicate predicate) {
        List<IntPredicate> subConditions = new ArrayList<>(this.subConditions);
        subConditions.add(notCondition ? predicate.negate() : predicate);
        return new IntValidator(outerValidator, condition, subConditions, false);
    }

    private IntPredicate collapseCondition() {
        IntPredicate condition = subConditions.isEmpty() ? this.condition : mainCondition();

        if (condition == null)
            throw new IllegalStateException("You must have at least one condition total, or between openBracket() and closeBracket()");

        return condition;
    }

    private IntPredicate mainCondition() {
        return this.condition == null ? accumulatedCondition() : this.condition.or(accumulatedCondition());
    }

    private IntPredicate accumulatedCondition() {
        return subConditions.stream().reduce(IntPredicate::and)
                .orElseThrow(() -> new IllegalStateException("Cannot accumulate an empty list"));
    }

}
