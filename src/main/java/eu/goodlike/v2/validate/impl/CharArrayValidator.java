package eu.goodlike.v2.validate.impl;

import com.google.common.primitives.Chars;
import eu.goodlike.functional.Action;
import eu.goodlike.neat.Null;
import eu.goodlike.v2.validate.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * char[] validator implementation
 */
public final class CharArrayValidator extends Validate<char[], CharArrayValidator> {

    /**
     * Adds a predicate which tests if the char array being validated is empty
     */
    public CharArrayValidator isEmpty() {
        return registerCondition(array -> array.length == 0);
    }

    /**
     * Adds a predicate which tests if the char array being validated fits into a limited amount of characters
     */
    public CharArrayValidator isNoLargerThan(int limit) {
        return registerCondition(array -> array.length <= limit);
    }

    /**
     * Adds a predicate which checks if every char in the array passes the predicate
     */
    public CharArrayValidator allMatch(Predicate<Character> elementPredicate) {
        Null.check(elementPredicate).ifAny("Predicate cannot be null");
        return registerCondition(chars -> Chars.asList(chars).stream().allMatch(elementPredicate));
    }

    /**
     * Adds a predicate which checks if any char in the array passes the predicate
     */
    public CharArrayValidator anyMatch(Predicate<Character> elementPredicate) {
        Null.check(elementPredicate).ifAny("Predicate cannot be null");
        return registerCondition(chars -> Chars.asList(chars).stream().anyMatch(elementPredicate));
    }

    /**
     * <pre>
     * Adds a validation check for every element, with a custom action for every invalid element
     *
     * The action will be executed for every invalid element, not just the first one found
     * </pre>
     */
    public <V extends Validate<Character, V>> CharArrayValidator forEachIf(V elementValidator, Action customAction) {
        Null.check(elementValidator, customAction).ifAny("Predicate and action cannot be null");
        return registerCondition(chars -> forEach(chars, elementValidator, customAction));
    }

    /**
     * <pre>
     * Adds a validation check for every element, with a custom consumer for every invalid element
     *
     * The consumer will be executed for every invalid element, not just the first one found
     * </pre>
     */
    public <V extends Validate<Character, V>> CharArrayValidator forEachIf(V elementValidator, Consumer<Character> customConsumer) {
        Null.check(elementValidator, customConsumer).ifAny("Predicate and consumer cannot be null");
        return registerCondition(chars -> forEach(chars, elementValidator, customConsumer));
    }

    /**
     * <pre>
     * Adds a validation check for every element, with a custom exception thrown if any of them is invalid
     *
     * The exception will be thrown just for the first invalid element
     * </pre>
     */
    public <V extends Validate<Character, V>, X extends RuntimeException> CharArrayValidator forEachThrowIf(V elementValidator, Supplier<X> customException) {
        Null.check(elementValidator, customException).ifAny("Predicate and exception supplier cannot be null");
        return registerCondition(chars -> forEach(chars, elementValidator, customException));
    }

    /**
     * <pre>
     * Adds a validation check for every element, with a custom exception thrown if any of them is invalid
     *
     * The exception will be thrown just for the first invalid element
     * </pre>
     */
    public <V extends Validate<Character, V>, X extends RuntimeException> CharArrayValidator forEachThrowIf(V elementValidator, Function<Character, X> customException) {
        Null.check(elementValidator, customException).ifAny("Predicate and exception supplier cannot be null");
        return registerCondition(chars -> forEach(chars, elementValidator, customException));
    }

    // CONSTRUCTORS

    public CharArrayValidator() {
        this(null, null, new ArrayList<>(), false);
    }

    protected CharArrayValidator(CharArrayValidator outerValidator, Predicate<char[]> condition, List<Predicate<char[]>> subConditions, boolean notCondition) {
        super(outerValidator, condition, subConditions, notCondition);
    }

    // PROTECTED

    @Override
    protected CharArrayValidator thisValidator() {
        return this;
    }

    @Override
    protected CharArrayValidator newValidator(CharArrayValidator outerValidator, Predicate<char[]> condition, List<Predicate<char[]>> subConditions, boolean notCondition) {
        return new CharArrayValidator(outerValidator, condition, subConditions, notCondition);
    }

    // PRIVATE

    private static <V extends Validate<Character, V>> boolean forEach(char[] chars, V elementValidator, Action customAction) {
        boolean result = true;
        for (char c : chars) {
            elementValidator.ifInvalid(c, customAction);
            result = false;
        }
        return result;
    }

    private static <T, V extends Validate<Character, V>> boolean forEach(char[] chars, V elementValidator, Consumer<Character> customConsumer) {
        boolean result = true;
        for (char c : chars) {
            elementValidator.ifInvalid(c, customConsumer);
            result = false;
        }
        return result;
    }

    private static <V extends Validate<Character, V>, X extends RuntimeException> boolean forEach(char[] chars, V elementValidator, Supplier<X> customException) {
        for (char c : chars)
            elementValidator.ifInvalidThrow(c, customException);

        return true;
    }

    private static <V extends Validate<Character, V>, X extends RuntimeException> boolean forEach(char[] chars, V elementValidator, Function<Character, X> customException) {
        for (char c : chars)
            elementValidator.ifInvalidThrow(c, customException);

        return true;
    }

}
