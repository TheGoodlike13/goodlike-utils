package eu.goodlike.validate.impl;

import eu.goodlike.validate.ComparableValidator;

import java.util.function.Predicate;

/**
 * Validator implementation for Boolean (boxed)
 */
public final class BooleanValidator extends ComparableValidator<Boolean, BooleanValidator> {

    /**
     * Adds a predicate which tests if the boolean being validated is true
     */
    public BooleanValidator isTrue() {
        return registerCondition(bool -> bool);
    }

    /**
     * Adds a predicate which tests if the boolean being validated is false
     */
    public BooleanValidator isFalse() {
        return registerCondition(bool -> !bool);
    }

    // CONSTRUCTORS

    public BooleanValidator() {
        super();
    }

    protected BooleanValidator(Predicate<Boolean> mainCondition, Predicate<Boolean> accumulatedCondition, boolean negateNext) {
        super(mainCondition, accumulatedCondition, negateNext);
    }

    // PROTECTED

    @Override
    protected BooleanValidator thisValidator() {
        return this;
    }

    @Override
    protected BooleanValidator newValidator(Predicate<Boolean> mainCondition, Predicate<Boolean> accumulatedCondition, boolean negateNext) {
        return new BooleanValidator(mainCondition, accumulatedCondition, negateNext);
    }

}
