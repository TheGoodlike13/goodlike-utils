package eu.goodlike.tbr.validate.impl;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BooleanValidatorTest {

    private BooleanValidator validator;

    @Before
    public void setup() {
        validator = new BooleanValidator();
    }

    @Test
    public void tryTrueWithTrue_shouldBeTrue() {
        assertThat(validator.isTrue().test(true)).isTrue();
    }

    @Test
    public void tryTrueWithFalse_shouldBeFalse() {
        assertThat(validator.isTrue().test(false)).isFalse();
    }

    @Test
    public void tryFalseWithTrue_shouldBeFalse() {
        assertThat(validator.isFalse().test(true)).isFalse();
    }

    @Test
    public void tryFalseWithFalse_shouldBeTrue() {
        assertThat(validator.isFalse().test(false)).isTrue();
    }

}
