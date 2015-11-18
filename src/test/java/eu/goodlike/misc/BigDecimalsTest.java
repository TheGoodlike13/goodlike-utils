package eu.goodlike.misc;

import org.junit.Test;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ROUND_UNNECESSARY;
import static org.assertj.core.api.Assertions.assertThat;

public class BigDecimalsTest {

    private final BigDecimal big1 = ONE;

    @Test
    public void tryTwoSameBigDecimals_shouldBeEqual() {
        assertThat(BigDecimals.equalsIgnoreScale(big1, big1)).isTrue();
    }

    @Test
    public void tryTwoNotEqualBigDecimals_shouldNotBeEqual() {
        BigDecimal big2 = big1.add(ONE);
        assertThat(BigDecimals.equalsIgnoreScale(big1, big2)).isFalse();
    }

    @Test
    public void tryTwoEqualWhenSameScaleBigDecimals_shouldBeEqual() {
        BigDecimal big2 = big1.setScale(big1.scale() + 1, ROUND_UNNECESSARY);
        assertThat(BigDecimals.equalsIgnoreScale(big1, big2)).isTrue();
    }

    @Test
    public void tryTwoSameBigDecimals_shouldGiveSameHashCode() {
        assertThat(BigDecimals.hashCode(big1)).isEqualTo(BigDecimals.hashCode(big1));
    }

    @Test
    public void tryTwoEqualWhenSameScaleBigDecimals_shouldGiveSameHashCode() {
        BigDecimal big2 = big1.setScale(big1.scale() + 1, ROUND_UNNECESSARY);
        assertThat(BigDecimals.hashCode(big1)).isEqualTo(BigDecimals.hashCode(big2));
    }

}
