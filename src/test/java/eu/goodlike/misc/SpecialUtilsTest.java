package eu.goodlike.misc;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ROUND_UNNECESSARY;
import static org.assertj.core.api.Assertions.assertThat;

public class SpecialUtilsTest {

    private final BigDecimal big1 = ONE;

    @Test
    public void tryTwoEqualBigDecimals_shouldBeEqual() {
        assertThat(SpecialUtils.equalsJavaMathBigDecimal(big1, big1)).isTrue();
    }

    @Test
    public void tryTwoNotEqualBigDecimals_shouldNotBeEqual() {
        BigDecimal big2 = big1.add(ONE);
        assertThat(SpecialUtils.equalsJavaMathBigDecimal(big1, big2)).isFalse();
    }

    @Test
    public void tryTwoEqualWhenSameScaleBigDecimals_shouldBeEqual() {
        BigDecimal big2 = big1.setScale(big1.scale() + 1, ROUND_UNNECESSARY);
        assertThat(SpecialUtils.equalsJavaMathBigDecimal(big1, big2)).isTrue();
    }

    @Test
    public void tryTwoEqualWhenSameScaleBigDecimals_shouldGiveSameHashCode() {
        BigDecimal big2 = big1.setScale(big1.scale() + 1, ROUND_UNNECESSARY);
        assertThat(SpecialUtils.bigDecimalCustomHashCode(big1)).isEqualTo(SpecialUtils.bigDecimalCustomHashCode(big2));
    }

    @Test
    public void tryTwoEqualIntegers_shouldReturn0() {
        assertThat(SpecialUtils.compareNullableIntegers(1, 1)).isEqualTo(0);
    }

    @Test
    public void tryTwoNullIntegers_shouldReturn0() {
        assertThat(SpecialUtils.compareNullableIntegers(null, null)).isEqualTo(0);
    }

    @Test
    public void tryFirstThenSecondInteger_shouldReturnMinus1() {
        assertThat(SpecialUtils.compareNullableIntegers(1, 2)).isEqualTo(-1);
    }

    @Test
    public void trySecondThenFirstInteger_shouldReturn1() {
        assertThat(SpecialUtils.compareNullableIntegers(2, 1)).isEqualTo(1);
    }

    @Test
    public void tryNullThenInteger_shouldReturn1() {
        assertThat(SpecialUtils.compareNullableIntegers(null, 1)).isEqualTo(1);
    }

    @Test
    public void tryIntegerThenNull_shouldReturnMinus1() {
        assertThat(SpecialUtils.compareNullableIntegers(1, null)).isEqualTo(-1);
    }

    @Test
    public void tryNull_shouldEncodeIntoNullString() {
        assertThat(SpecialUtils.urlEncode(null)).isEqualTo("null");
    }

    @Test
    public void tryStringWithInvalidUrlCharacters_shouldEncodeIntoUrlEncodeWithDefaultCharset() throws UnsupportedEncodingException {
        String string = "anyString?#&/&#?";
        assertThat(SpecialUtils.urlEncode(string)).isEqualTo(URLEncoder.encode(string, Constants.DEFAULT_CHARSET));
    }

    @Test
    public void tryEqualsOnTwoDifferentScaleEqualBigDecimalsUsingScaleless_shouldBeEqual() {
        BigDecimal big2 = big1.setScale(big1.scale() + 1, ROUND_UNNECESSARY);
        assertThat(SpecialUtils.equals(big1, big2, Scaleless::bigDecimal)).isTrue();
    }

    @Test
    public void tryEqualsOnTwoDifferentStringBuildersFromSameString_shouldBeEqual() {
        String any = "test";
        assertThat(SpecialUtils.equals(new StringBuilder(any), new StringBuilder(any), StringBuilder::toString)).isTrue();
    }

}