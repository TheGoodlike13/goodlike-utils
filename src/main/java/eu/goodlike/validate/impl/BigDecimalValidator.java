package eu.goodlike.validate.impl;

import eu.goodlike.validate.ComparableValidator;

import java.math.BigDecimal;
import java.util.function.Predicate;

/**
 * Validator implementation for BigDecimal
 */
public final class BigDecimalValidator extends ComparableValidator<BigDecimal, BigDecimalValidator> {

    /**
     * Adds a predicate which tests if the BigDecimal is equal to another using their comparison (which ignores scale,
     * unlike equals())
     */
    public BigDecimalValidator isEqualIgnoreScale(BigDecimal other) {
        return isEqualComparably(other);
    }

    /**
     * Adds a predicate which tests if the BigDecimal being validated is positive
     */
    public BigDecimalValidator isPositive() {
        return registerCondition(dec -> dec.signum() == 1);
    }

    /**
     * Adds a predicate which tests if the BigDecimal being validated is zero
     */
    public BigDecimalValidator isZero() {
        return registerCondition(dec -> dec.signum() == 0);
    }

    /**
     * Adds a predicate which tests if the BigDecimal being validated is negative
     */
    public BigDecimalValidator isNegative() {
        return registerCondition(dec -> dec.signum() == -1);
    }

    // CONSTRUCTORS

    public BigDecimalValidator() {
        super();
    }

    protected BigDecimalValidator(Predicate<BigDecimal> mainCondition, Predicate<BigDecimal> accumulatedCondition, boolean negateNext) {
        super(mainCondition, accumulatedCondition, negateNext);
    }

    // PROTECTED

    @Override
    protected BigDecimalValidator thisValidator() {
        return this;
    }

    @Override
    protected BigDecimalValidator newValidator(Predicate<BigDecimal> mainCondition, Predicate<BigDecimal> accumulatedCondition, boolean negateNext) {
        return new BigDecimalValidator(mainCondition, accumulatedCondition, negateNext);
    }

}
