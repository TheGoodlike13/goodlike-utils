package eu.goodlike.v2.validate.impl;

import eu.goodlike.v2.validate.Validate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * BigDecimal validator implementation
 */
public final class BigDecimalValidator extends Validate<BigDecimal, BigDecimalValidator> {

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

    // CONSTRUCTORS

    public BigDecimalValidator() {
        this(null, null, new ArrayList<>(), false);
    }

    protected BigDecimalValidator(BigDecimalValidator outerValidator, Predicate<BigDecimal> condition, List<Predicate<BigDecimal>> subConditions, boolean notCondition) {
        super(outerValidator, condition, subConditions, notCondition);
    }

    // PROTECTED

    @Override
    protected BigDecimalValidator thisValidator() {
        return this;
    }

    @Override
    protected BigDecimalValidator newValidator(BigDecimalValidator outerValidator, Predicate<BigDecimal> condition, List<Predicate<BigDecimal>> subConditions, boolean notCondition) {
        return new BigDecimalValidator(outerValidator, condition, subConditions, notCondition);
    }

}
