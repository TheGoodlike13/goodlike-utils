package eu.goodlike.v2.validate.impl;

import eu.goodlike.misc.Scaleless;
import eu.goodlike.misc.SpecialUtils;
import eu.goodlike.v2.validate.Validate;

import java.math.BigDecimal;
import java.util.function.Predicate;

/**
 * BigDecimal validator implementation
 */
public final class BigDecimalValidator extends Validate<BigDecimal, BigDecimalValidator> {

    /**
     * Adds a predicate which tests if the BigDecimal being validated is equal to some other bigDecimal;
     * this comparison ignores scale
     */
    @Override
    public BigDecimalValidator isEqual(BigDecimal other) {
        return registerCondition(dec -> SpecialUtils.equals(dec, other, Scaleless::bigDecimal));
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

    // CONSTRUCTORS

    public BigDecimalValidator() {
        this(null, null, null, false);
    }

    protected BigDecimalValidator(BigDecimalValidator outerValidator, Predicate<BigDecimal> mainCondition, Predicate<BigDecimal> accumulatedCondition, boolean notCondition) {
        super(outerValidator, mainCondition, accumulatedCondition, notCondition);
    }

    // PROTECTED

    @Override
    protected BigDecimalValidator thisValidator() {
        return this;
    }

    @Override
    protected BigDecimalValidator newValidator(BigDecimalValidator outerValidator, Predicate<BigDecimal> mainCondition, Predicate<BigDecimal> accumulatedCondition, boolean notCondition) {
        return new BigDecimalValidator(outerValidator, mainCondition, accumulatedCondition, notCondition);
    }

}
