package eu.goodlike.misc;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ROUND_UNNECESSARY;
import static org.assertj.core.api.Assertions.assertThat;

public class ScalelessTest {

    @Before
    public void setup() {
        big1 = ONE;
    }

    @Test
    public void tryTwoEqualBigDecimals_shouldBeEqual() {
        assertThat(Scaleless.bigDecimal(big1)).isEqualTo(Scaleless.bigDecimal(big1));
    }

    @Test
    public void tryTwoNotEqualBigDecimals_shouldNotBeEqual() {
        BigDecimal big2 = big1.add(ONE);
        assertThat(Scaleless.bigDecimal(big1)).isNotEqualTo(Scaleless.bigDecimal(big2));
    }

    @Test
    public void tryTwoEqualWhenSameScaleBigDecimals_shouldBeEqual() {
        BigDecimal big2 = big1.setScale(big1.scale() + 1, ROUND_UNNECESSARY);
        assertThat(Scaleless.bigDecimal(big1)).isEqualTo(Scaleless.bigDecimal(big2));
    }

    @Test
    public void tryTwoEqualBigDecimals_shouldGiveSameHashCode() {
        assertThat(Scaleless.bigDecimal(big1).hashCode()).isEqualTo(Scaleless.bigDecimal(big1).hashCode());
    }

    @Test
    public void tryTwoEqualWhenSameScaleBigDecimals_shouldGiveSameHashCode() {
        BigDecimal big2 = big1.setScale(big1.scale() + 1, ROUND_UNNECESSARY);
        assertThat(Scaleless.bigDecimal(big1).hashCode()).isEqualTo(Scaleless.bigDecimal(big2).hashCode());
    }

    // PRIVATE

    private BigDecimal big1;

}
