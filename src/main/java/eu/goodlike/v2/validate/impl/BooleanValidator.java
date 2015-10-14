package eu.goodlike.v2.validate.impl;

import eu.goodlike.v2.validate.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * boolean validator implementation
 */
public final class BooleanValidator extends Validate<Boolean, BooleanValidator> {

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
        this(null, null, new ArrayList<>(), false);
    }

    protected BooleanValidator(BooleanValidator outerValidator, Predicate<Boolean> condition, List<Predicate<Boolean>> subConditions, boolean notCondition) {
        super(outerValidator, condition, subConditions, notCondition);
    }

    // PROTECTED

    @Override
    protected BooleanValidator thisValidator() {
        return this;
    }

    @Override
    protected BooleanValidator newValidator(BooleanValidator outerValidator, Predicate<Boolean> condition, List<Predicate<Boolean>> subConditions, boolean notCondition) {
        return new BooleanValidator(outerValidator, condition, subConditions, notCondition);
    }

}
