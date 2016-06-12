package eu.goodlike.validate.impl;

import eu.goodlike.validate.Validate;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static java.math.BigDecimal.*;
import static org.assertj.core.api.Assertions.assertThat;

public class BigDecimalValidatorTest {

    private BigDecimalValidator validator;

    @Before
    public void setup() {
        validator = Validate.bigDecimal();
    }

    @Test
    public void tryEqualsOfSameBigDecimal_shouldBeTrue() {
        assertThat(validator.isEqual(ONE).test(ONE)).isTrue();
    }

    @Test
    public void tryEqualsIgnoreScaleOfSameBigDecimalDifferentScale_shouldBeTrue() {
        BigDecimal oneScaled = ONE.setScale(4, ROUND_UNNECESSARY);
        assertThat(validator.isEqualIgnoreScale(ONE).test(oneScaled)).isTrue();
    }

    @Test
    public void tryEqualsDifferentBigDecimals_shouldBeFalse() {
        assertThat(validator.isEqual(ONE).test(ZERO)).isFalse();
    }

    @Test
    public void tryPositiveWithPositive_shouldBeTrue() {
        assertThat(validator.isPositive().test(ONE)).isTrue();
    }

    @Test
    public void tryPositiveWithZero_shouldBeFalse() {
        assertThat(validator.isPositive().test(ZERO)).isFalse();
    }

    @Test
    public void tryPositiveWithNegative_shouldBeFalse() {
        assertThat(validator.isPositive().test(ONE.negate())).isFalse();
    }

    @Test
    public void tryZeroWithZero_shouldBeTrue() {
        assertThat(validator.isZero().test(ZERO)).isTrue();
    }

    @Test
    public void tryZeroWithNotZero_shouldBeFalse() {
        assertThat(validator.isZero().test(ONE)).isFalse();
    }

}
