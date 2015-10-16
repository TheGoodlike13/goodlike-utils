package eu.goodlike.libraries.jool;

import org.junit.Test;

import java.util.stream.Stream;

import static eu.goodlike.libraries.jool.Sequence.seq;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class SequenceTest {

    @Test
    public void trySequenceOf_shouldBeSeqOf() {
        assertThat(Sequence.of(1, 2, 3).toList()).isEqualTo(Stream.of(1, 2, 3).collect(toList()));
    }

    @Test
    public void trySequenceSeq_shouldBeSeqOf() {
        assertThat(seq(1, 2, 3).toList()).isEqualTo(Stream.of(1, 2, 3).collect(toList()));
    }

}
