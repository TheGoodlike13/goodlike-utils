package eu.goodlike.functional.impl.some;

import eu.goodlike.functional.Some;
import org.junit.Test;

import java.util.Arrays;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class IntSamplerTest {

    private final IntSampler<String> toString = Some.of(String::valueOf);

    @Test
    public void trySampling_shouldReturnSample() {
        assertThat(toString.sample(1)).isEqualTo("1");
    }

    @Test
    public void tryGetting_shouldReturnFrom0ToGivenMinus1() {
        assertThat(toString.zeroTo(5)).isEqualTo(Arrays.asList("0", "1", "2", "3", "4"));
    }

    @Test
    public void tryFetching_shouldReturnFrom1ToGiven() {
        assertThat(toString.oneUpTo(5)).isEqualTo(Arrays.asList("1", "2", "3", "4", "5"));
    }

    @Test
    public void tryRange_shouldReturnRange() {
        assertThat(toString.range(2, 5)).isEqualTo(Arrays.asList("2", "3", "4"));
    }

    @Test
    public void tryRangeClosed_shouldReturnRangeClosed() {
        assertThat(toString.rangeClosed(2, 5)).isEqualTo(Arrays.asList("2", "3", "4", "5"));
    }

    @Test
    public void tryWith_shouldReturnGivenValues() {
        assertThat(toString.with(1, 3, 5)).isEqualTo(Arrays.asList("1", "3", "5"));
    }

    @Test
    public void tryWithCollection_shouldReturnGivenValues() {
        assertThat(toString.with(Arrays.asList(1, 3, 5))).isEqualTo(Arrays.asList("1", "3", "5"));
    }

    @Test
    public void tryGettingStream_shouldReturnFrom0ToGivenMinus1() {
        assertThat(toString.zeroToStream(5).collect(toList())).isEqualTo(Arrays.asList("0", "1", "2", "3", "4"));
    }

    @Test
    public void tryFetchingStream_shouldReturnFrom1ToGiven() {
        assertThat(toString.oneUpToStream(5).collect(toList())).isEqualTo(Arrays.asList("1", "2", "3", "4", "5"));
    }

    @Test
    public void tryRangeStream_shouldReturnRange() {
        assertThat(toString.rangeStream(2, 5).collect(toList())).isEqualTo(Arrays.asList("2", "3", "4"));
    }

    @Test
    public void tryRangeClosedStream_shouldReturnRangeClosed() {
        assertThat(toString.rangeStreamClosed(2, 5).collect(toList())).isEqualTo(Arrays.asList("2", "3", "4", "5"));
    }

    @Test
    public void tryStream_shouldReturnGivenValues() {
        assertThat(toString.stream(1, 3, 5).collect(toList())).isEqualTo(Arrays.asList("1", "3", "5"));
    }

    @Test
    public void tryStreamCollection_shouldReturnGivenValues() {
        assertThat(toString.stream(Arrays.asList(1, 3, 5)).collect(toList())).isEqualTo(Arrays.asList("1", "3", "5"));
    }

}
