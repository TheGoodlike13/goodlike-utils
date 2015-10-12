package eu.goodlike.functional.some;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SomeTest {

    @Test
    public void tryIntSampler_shouldBeEqualToIntSamplerConstructor() {
        assertThat(Some.of(String::valueOf)).isEqualTo(new IntSampler<>(String::valueOf));
    }

    @Test
    public void tryLongSampler_shouldBeEqualToLongSamplerConstructor() {
        assertThat(Some.ofLong(String::valueOf)).isEqualTo(new LongSampler<>(String::valueOf));
    }

}
