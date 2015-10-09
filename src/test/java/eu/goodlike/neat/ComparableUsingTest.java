package eu.goodlike.neat;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ComparableUsingTest {

    @Before
    public void setup() {
        zero = i -> Integer.compare(0, i);
    }

    @Test
    public void tryAtLeast0_shouldBeTrue() {
        assertThat(zero.isAtLeast(0)).isTrue();
    }

    @Test
    public void tryAtLeast1_shouldBeFalse() {
        assertThat(zero.isAtLeast(1)).isFalse();
    }

    @Test
    public void tryAtLeastMinus1_shouldBeTrue() {
        assertThat(zero.isAtLeast(-1)).isTrue();
    }

    @Test
    public void tryAtMost0_shouldBeTrue() {
        assertThat(zero.isAtMost(0)).isTrue();
    }

    @Test
    public void tryAtMost1_shouldBeTrue() {
        assertThat(zero.isAtMost(1)).isTrue();
    }

    @Test
    public void tryAtMostMinus1_shouldBeFalse() {
        assertThat(zero.isAtMost(-1)).isFalse();
    }

    @Test
    public void tryExactly0_shouldBeTrue() {
        assertThat(zero.isExactly(0)).isTrue();
    }

    @Test
    public void tryExactly1_shouldBeFalse() {
        assertThat(zero.isExactly(1)).isFalse();
    }

    @Test
    public void tryExactlyMinus1_shouldBeFalse() {
        assertThat(zero.isExactly(-1)).isFalse();
    }

    @Test
    public void tryLessThan0_shouldBeFalse() {
        assertThat(zero.isLessThan(0)).isFalse();
    }

    @Test
    public void tryLessThan1_shouldBeTrue() {
        assertThat(zero.isLessThan(1)).isTrue();
    }

    @Test
    public void tryLessThanMinus1_shouldBeFalse() {
        assertThat(zero.isLessThan(-1)).isFalse();
    }

    @Test
    public void tryMoreThan0_shouldBeFalse() {
        assertThat(zero.isMoreThan(0)).isFalse();
    }

    @Test
    public void tryMoreThan1_shouldBeFalse() {
        assertThat(zero.isMoreThan(1)).isFalse();
    }

    @Test
    public void tryMoreThanMinus1_shouldBeTrue() {
        assertThat(zero.isMoreThan(-1)).isTrue();
    }

    // PRIVATE

    private ComparableUsing<Integer> zero;

}
