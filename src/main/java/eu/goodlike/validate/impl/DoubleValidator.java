package eu.goodlike.validate.impl;

import eu.goodlike.validate.ComparableValidator;

import java.util.function.Predicate;

/**
 * Validator implementation for Double (boxed)
 */
public final class DoubleValidator extends ComparableValidator<Double, DoubleValidator> {

    /**
     * Adds a predicate which tests if the double can describe a latitude
     */
    public final DoubleValidator isLatitude() {
        return isBetween(-90.0, 90.0);
    }

    /**
     * Adds a predicate which tests if the double can describe a longitude
     */
    public final DoubleValidator isLongitude() {
        return isBetween(-180.0, 180.0);
    }

    // CONSTRUCTORS

    public DoubleValidator() {
        super();
    }

    protected DoubleValidator(Predicate<Double> mainCondition, Predicate<Double> accumulatedCondition, boolean negateNext) {
        super(mainCondition, accumulatedCondition, negateNext);
    }

    // PROTECTED

    @Override
    protected DoubleValidator thisValidator() {
        return null;
    }

    @Override
    protected DoubleValidator newValidator(Predicate<Double> mainCondition, Predicate<Double> accumulatedCondition, boolean negateNext) {
        return new DoubleValidator(mainCondition, accumulatedCondition, negateNext);
    }

}
