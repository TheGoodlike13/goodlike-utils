package eu.goodlike.misc;

import org.junit.Test;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ROUND_UNNECESSARY;
import static org.assertj.core.api.Assertions.assertThat;

public class ScalelessTest {

    @Test
    public void tryTwoEqualBigDecimals_shouldBeEqual() {
        BigDecimal big = ONE;
        assertThat(Scaleless.bigDecimal(big)).isEqualTo(Scaleless.bigDecimal(big));
    }

    @Test
    public void tryTwoNotEqualBigDecimals_shouldNotBeEqual() {
        BigDecimal big1 = ONE;
        BigDecimal big2 = ONE.add(ONE);
        assertThat(Scaleless.bigDecimal(big1)).isNotEqualTo(Scaleless.bigDecimal(big2));
    }

    @Test
    public void tryTwoEqualWhenSameScaleBigDecimals_shouldBeEqual() {
        BigDecimal big1 = ONE;
        BigDecimal big2 = ONE.setScale(big1.scale() + 1, ROUND_UNNECESSARY);
        assertThat(Scaleless.bigDecimal(big1)).isEqualTo(Scaleless.bigDecimal(big2));
    }

    @Test
    public void tryTwoEqualBigDecimals_shouldGiveSameHashCode() {
        BigDecimal big = ONE;
        assertThat(Scaleless.bigDecimal(big).hashCode()).isEqualTo(Scaleless.bigDecimal(big).hashCode());
    }

    @Test
    public void tryTwoEqualWhenSameScaleBigDecimals_shouldGiveSameHashCode() {
        BigDecimal big1 = ONE;
        BigDecimal big2 = ONE.setScale(big1.scale() + 1, ROUND_UNNECESSARY);
        assertThat(Scaleless.bigDecimal(big1).hashCode()).isEqualTo(Scaleless.bigDecimal(big2).hashCode());
    }

}
