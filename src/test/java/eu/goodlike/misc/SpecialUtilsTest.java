package eu.goodlike.misc;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecialUtilsTest {

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
    public void tryEqualsOnTwoDifferentStringBuildersFromSameString_shouldBeEqual() {
        String any = "test";
        assertThat(SpecialUtils.equals(new StringBuilder(any), new StringBuilder(any), StringBuilder::toString)).isTrue();
    }

}
